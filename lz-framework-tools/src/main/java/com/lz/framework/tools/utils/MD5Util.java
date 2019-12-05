package com.lz.framework.tools.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lz.framework.tools.constants.Constants;
import com.lz.framework.tools.enums.ReturnCodeEnum;
import com.lz.framework.tools.exception.BusinessException;

public class MD5Util {

	static Logger logger = LoggerFactory.getLogger(MD5Util.class);
	
	public static String md5Encode(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			logger.info("MD5字符串失败", e);
			return "";
		}

		try {
			byte[] byteArray = inStr.getBytes(Constants.CHARSET.UTF_8);
			byte[] md5Bytes = md5.digest(byteArray);
			StringBuilder hexValue = new StringBuilder();
			for (int i = 0; i < md5Bytes.length; i++) {
				int val = ((int) md5Bytes[i]) & 0xff;
				if (val < 16) {
					hexValue.append("0");
				}
				hexValue.append(Integer.toHexString(val));
			}
			return hexValue.toString();
		} catch (UnsupportedEncodingException e) {
			logger.error("MD5加密字符串时发生异常,", e);
			throw new BusinessException(ReturnCodeEnum.ERROR, "MD5加密字符串时发生异常,", e);
		}
	}

    public static String md5File(File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file);){
            MessageDigest MD5 = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[8192];
            int length;
            while ((length = fileInputStream.read(buffer)) != -1) {
                MD5.update(buffer, 0, length);
            }
            return new String(Hex.encodeHex(MD5.digest()));
        } catch (Exception e) {
            logger.info("MD5文件失败", e);
            return null;
        } 
    }
}
