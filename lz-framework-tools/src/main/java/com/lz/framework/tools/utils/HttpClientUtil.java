package com.lz.framework.tools.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.activation.MimetypesFileTypeMap;
import javax.net.ssl.SSLContext;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lz.framework.tools.constants.Constants;

public class HttpClientUtil {

    private static PoolingHttpClientConnectionManager connMgr;
    private static RequestConfig requestConfig;
    private static final int MAX_TIMEOUT = 7000;

    private final static Logger log = LoggerFactory.getLogger(HttpClientUtil.class);

    static {
        // 设置连接池
        connMgr = new PoolingHttpClientConnectionManager();
        // 设置连接池大小
        connMgr.setMaxTotal(100);
        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());
        connMgr.setValidateAfterInactivity(30000);

        RequestConfig.Builder configBuilder = RequestConfig.custom();
        // 设置连接超时
        configBuilder.setConnectTimeout(MAX_TIMEOUT);
        // 设置读取超时
        configBuilder.setSocketTimeout(MAX_TIMEOUT);
        // 设置从连接池获取连接实例的超时
        configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
        requestConfig = configBuilder.build();
    }

    /**
     * 发送 GET 请求（HTTP），不带输入数据
     * 
     * @param url
     * @return
     */
    public static String doGet(String url) {
        return doGet(url, new HashMap<String, Object>());
    }

    /**
     * 发送 GET 请求（HTTP），K-V形式
     * 
     * @param url
     * @param params
     * @return
     */
    public static String doGet(String url, Map<String, Object> params) {
        String apiUrl = url;
        StringBuilder param = new StringBuilder();
        int i = 0;
        for (String key : params.keySet()) {
            if (i == 0) {
                param.append("?");
            } else {
                param.append("&");
            }
            param.append(key).append("=").append(params.get(key));
            i++;
        }
        apiUrl += param;
        String result = null;
        HttpClient httpclient = HttpClientBuilder.create().build();
        try {
            HttpGet httpPost = new HttpGet(apiUrl);
            HttpResponse response = httpclient.execute(httpPost);

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                result = IOUtils.toString(instream, Constants.CHARSET.UTF_8);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String doGetSSL(String apiUri) {
        return doGet(apiUri, new HashMap<>());
    }

    /**
     * 
     * @param apiUrl
     * @param json
     * @return
     */
    public static String doGetSSL(String apiUrl, Map<String, String> requestHeaderMap) {
        final SSLConnectionSocketFactory sslsf;
        try {
            sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault(), NoopHostnameVerifier.INSTANCE);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        final Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", new PlainConnectionSocketFactory()).register("https", sslsf).build();

        final PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
        cm.setMaxTotal(100);
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).setConnectionManager(cm)
                .build();

        HttpGet httpGet = new HttpGet(apiUrl);
        CloseableHttpResponse response = null;
        String httpStr = null;

        try {
            httpGet.setConfig(requestConfig);
            // 设置请求头
            if (requestHeaderMap != null) {
                for (Map.Entry<String, String> entry : requestHeaderMap.entrySet()) {
                    httpGet.setHeader(entry.getKey(), entry.getValue());
                }
            }

            response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                log.error(response.toString());
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return null;
            }
            httpStr = EntityUtils.toString(entity, Constants.CHARSET.UTF_8);
        } catch (Exception e) {
            log.error("doGetSSL request error:{}", e);
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;
    }

    /**
     * 
     * @param apiUrl
     * @param json
     * @return
     */
    public static String doGetByHeader(String apiUrl, Map<String, String> requestHeaderMap) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(apiUrl);
        HttpResponse response = null;
        String httpStr = null;

        try {
            httpGet.setConfig(requestConfig);
            // 设置请求头
            if (requestHeaderMap != null) {
                for (Map.Entry<String, String> entry : requestHeaderMap.entrySet()) {
                    httpGet.setHeader(entry.getKey(), entry.getValue());
                }
            }

            response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                log.error(response.toString());
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return null;
            }
            httpStr = EntityUtils.toString(entity, Constants.CHARSET.UTF_8);
        } catch (Exception e) {
            log.error("doGetByHeader request error:{}", e);
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;
    }

    /**
     * 发送 POST 请求（HTTP），不带输入数据
     * 
     * @param apiUrl
     * @return
     */
    public static String doPost(String apiUrl) {
        return doPost(apiUrl, new HashMap<String, Object>());
    }

    /**
     * 发送 POST 请求（HTTP），K-V形式
     * 
     * @param apiUrl
     *            API接口URL
     * @param params
     *            参数map
     * @return
     */
    public static String doPost(String apiUrl, Map<String, Object> params) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        String httpStr = null;
        HttpPost httpPost = new HttpPost(apiUrl);
        HttpResponse response = null;

        try {
            httpPost.setConfig(requestConfig);
            List<NameValuePair> pairList = new ArrayList<NameValuePair>(params.size());
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
                pairList.add(pair);
            }
            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName(Constants.CHARSET.UTF_8)));
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            httpStr = EntityUtils.toString(entity, Constants.CHARSET.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;
    }

    /**
     * 发送 POST 请求（HTTP），JSON形式
     * 
     * @param apiUrl
     * @param json
     *            json对象
     * @return
     */
    public static String doPost(String apiUrl, String json) {
        String httpStr = null;
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;

        try (CloseableHttpClient httpClient = HttpClients.createDefault();){
            httpPost.setConfig(requestConfig);
            StringEntity stringEntity = new StringEntity(json, Constants.CHARSET.UTF_8);// 解决中文乱码问题
            stringEntity.setContentEncoding(Constants.CHARSET.UTF_8);
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            httpStr = EntityUtils.toString(entity, Constants.CHARSET.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;
    }

    public static String doPostSSL(String apiUrl, String data) {
        return doPostSSL(apiUrl, data, null);
    }

    /**
     * 发送 SSL POST 请求（HTTPS），K-V形式
     * 
     * @param apiUrl
     *            API接口URL
     * @param params
     *            参数map
     * @return
     */
    public static String doPostSSL(String apiUrl, Map<String, Object> params, Map<String, String> requestHeaderMap) {
        final SSLConnectionSocketFactory sslsf;
        try {
            sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault(), NoopHostnameVerifier.INSTANCE);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        final Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", new PlainConnectionSocketFactory()).register("https", sslsf).build();

        final PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
        cm.setMaxTotal(100);
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).setConnectionManager(cm)
                .build();

        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;
        String httpStr = null;

        try {
            httpPost.setConfig(requestConfig);
            List<NameValuePair> pairList = new ArrayList<NameValuePair>(params.size());
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
                pairList.add(pair);
            }
            // 设置请求头
            if (requestHeaderMap != null) {
                for (Map.Entry<String, String> entry : requestHeaderMap.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }

            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName(Constants.CHARSET.UTF_8)));
            response = httpClient.execute(httpPost);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                log.error(response.toString());
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return null;
            }
            
            httpStr = EntityUtils.toString(entity, Constants.CHARSET.UTF_8);
        } catch (Exception e) {
            log.error("doPostSSL Map params request error:{}", e);
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;
    }

    /**
     * 发送 SSL POST 请求（HTTPS），JSON形式
     * 
     * @param apiUrl
     *            API接口URL
     * @param json
     *            JSON对象
     * @return
     */
    public static String doPostSSL(String apiUrl, Object json, Map<String, String> requestHeaderMap) {
        final SSLConnectionSocketFactory sslsf;
        try {
            sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault(), NoopHostnameVerifier.INSTANCE);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        final Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", new PlainConnectionSocketFactory()).register("https", sslsf).build();

        final PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
        cm.setMaxTotal(100);
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).setConnectionManager(cm)
                .build();

        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;
        String httpStr = null;

        try {
            httpPost.setConfig(requestConfig);
            // 设置请求头
            if (requestHeaderMap != null) {
                for (Map.Entry<String, String> entry : requestHeaderMap.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }
            StringEntity stringEntity = new StringEntity(json.toString(), Constants.CHARSET.UTF_8);// 解决中文乱码问题
            stringEntity.setContentEncoding(Constants.CHARSET.UTF_8);
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                log.error(response.toString());
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return null;
            }
            httpStr = EntityUtils.toString(entity, Constants.CHARSET.UTF_8);
        } catch (Exception e) {
            log.error("doPostSSL Json params request error:{}", e);
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;
    }

    /**
     * 发送 POST 请求（HTTP），JSON形式
     * 
     * @param apiUrl
     *            API接口URL
     * @param json
     *            JSON对象
     * @return
     */
    public static String doPost(String apiUrl, Object json, Map<String, String> requestHeaderMap) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(apiUrl);
        HttpResponse response = null;
        String httpStr = null;

        try {
            httpPost.setConfig(requestConfig);
            // 设置请求头
            if (requestHeaderMap != null) {
                for (Map.Entry<String, String> entry : requestHeaderMap.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }
            StringEntity stringEntity = new StringEntity(json.toString(), Constants.CHARSET.UTF_8);// 解决中文乱码问题
            stringEntity.setContentEncoding(Constants.CHARSET.UTF_8);
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                log.error(response.toString());
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return null;
            }
            httpStr = EntityUtils.toString(entity, Constants.CHARSET.UTF_8);
        } catch (Exception e) {
            log.error("doPost Json params request error:{}", e);
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;
    }

    /**
     * 表单上传
     * 
     * @param urlStr
     * @param formFields
     * @param localFile
     * @return
     * @throws Exception
     */
    public static String formUpload(String urlStr, Map<String, Object> formFields, String localFile) throws Exception {
        String res = "";
        HttpURLConnection conn = null;
        String boundary = "9431149156168";
        File file = new File(localFile);
        try (DataInputStream in = new DataInputStream(new FileInputStream(file));){
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            OutputStream out = new DataOutputStream(conn.getOutputStream());

            // text
            if (formFields != null) {
                StringBuilder strBuf = new StringBuilder();
                Iterator<Entry<String, Object>> iter = formFields.entrySet().iterator();
                int i = 0;

                while (iter.hasNext()) {
                    Entry<String, Object> entry = iter.next();
                    String inputName = entry.getKey();
                    String inputValue = String.valueOf(entry.getValue());

                    if (inputValue == null) {
                        continue;
                    }
                    if (i == 0) {
                        strBuf.append("--").append(boundary).append("\r\n");
                        strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
                        strBuf.append(inputValue);
                    } else {
                        strBuf.append("\r\n").append("--").append(boundary).append("\r\n");
                        strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
                        strBuf.append(inputValue);
                    }
                    i++;
                }
                out.write(strBuf.toString().getBytes());
            }

            String filename = file.getName();
            String contentType = new MimetypesFileTypeMap().getContentType(file);
            if (contentType == null || contentType.equals("")) {
                contentType = "application/octet-stream";
            }

            StringBuilder strBuf = new StringBuilder();
            strBuf.append("\r\n").append("--").append(boundary).append("\r\n");
            strBuf.append("Content-Disposition: form-data; name=\"file\"; " + "filename=\"" + filename + "\"\r\n");
            strBuf.append("Content-Type: " + contentType + "\r\n\r\n");
            out.write(strBuf.toString().getBytes());

            
            int bytes = 0;
            byte[] bufferOut = new byte[1024];
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }

            byte[] endData = ("\r\n--" + boundary + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();

            // 读取返回数据
            strBuf = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuf.append(line).append("\n");
            }
            res = strBuf.toString();
            reader.close();
            reader = null;
        } catch (Exception e) {
            System.err.println("Send post request exception: " + e);
            throw e;
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return res;
    }

    /**
     * 下载文件
     * 
     * @param urlPath
     * @param localFile
     * @return
     */
    public static File doGetSteam(String urlPath, String localFile) {
        HttpURLConnection conn = null;
        BufferedInputStream bin = null;
        File file = new File(localFile);
        try (OutputStream out = new FileOutputStream(file);){
            // 统一资源
            URL url = new URL(urlPath);
            // 连接类的父类，抽象类
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            // 设置字符编码
            conn.setRequestProperty("Charset", Constants.CHARSET.UTF_8);
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            // 打开到此 URL 引用的资源的通信链接（如果尚未建立这样的连接）。
            conn.connect();

            bin = new BufferedInputStream(conn.getInputStream());
            int size = 0;
            byte[] buf = new byte[1024];
            while ((size = bin.read(buf)) != -1) {
                out.write(buf, 0, size);
            }
            out.flush();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bin != null) {
                    bin.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return file;
    }
    

    /**
     * 下载文件
     * 
     * @param urlPath
     * @param localFile
     * @return
     */
    public static File doPostFileSteam(String urlPath, Map<String, Object> paramMap, String localFile) {
        HttpURLConnection conn = null;
        BufferedInputStream bin = null;
        File file = new File(localFile);;
        try (OutputStream out = new FileOutputStream(file);){
        	 StringBuilder params=new StringBuilder();
             for(Entry<String, Object> entry : paramMap.entrySet()){
                 params.append(entry.getKey());
                 params.append("=");
                 params.append(entry.getValue().toString());
                 params.append("&");
             }
             if(params.length()>0){
                 params.deleteCharAt(params.lastIndexOf("&"));
             }
             URL url = new URL(urlPath+(params.length()>0 ? "?"+params.toString() : ""));
            // 连接类的父类，抽象类
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            // 设置字符编码
            conn.setRequestProperty("Charset", Constants.CHARSET.UTF_8);
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            // 打开到此 URL 引用的资源的通信链接（如果尚未建立这样的连接）。
            conn.connect();

            bin = new BufferedInputStream(conn.getInputStream());
            
            int size = 0;
            byte[] buf = new byte[1024];
            while ((size = bin.read(buf)) != -1) {
                out.write(buf, 0, size);
            }
            out.flush();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bin != null) {
                    bin.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return file;
    }
    

    /**
     * 下载文件
     * 
     * @param urlPath
     * @param localFile
     * @return
     */
    public static String doPostByteSteam(String urlPath, Map<String, Object> paramMap) {
    	byte[] data = null;
        HttpURLConnection conn = null;
        BufferedInputStream bin = null;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();){
        	 StringBuilder params=new StringBuilder();
             for(Entry<String, Object> entry : paramMap.entrySet()){
                 params.append(entry.getKey());
                 params.append("=");
                 params.append(entry.getValue().toString());
                 params.append("&");
             }
             if(params.length()>0){
                 params.deleteCharAt(params.lastIndexOf("&"));
             }
             URL url = new URL(urlPath+(params.length()>0 ? "?"+params.toString() : ""));
            // 连接类的父类，抽象类
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            // 设置字符编码
            conn.setRequestProperty("Charset", Constants.CHARSET.UTF_8);
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            // 打开到此 URL 引用的资源的通信链接（如果尚未建立这样的连接）。
            conn.connect();

            bin = new BufferedInputStream(conn.getInputStream());
            int size = 0;
            byte[] buf = new byte[1024];
            while ((size = bin.read(buf)) != -1) {
                out.write(buf, 0, size);
            }
            data = out.toByteArray();
            out.flush();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bin != null) {
                    bin.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return new String(Base64.encodeBase64(data));
    }

}
