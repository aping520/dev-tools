package com.lz.framework.tools.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;

import com.lz.framework.tools.enums.ReturnCodeEnum;
import com.lz.framework.tools.exception.BusinessException;

public class Base64Util {
	
	private Base64Util() {
		
	}

	public static String toBase64(String path) {
        byte[] data = null;
        try(InputStream in = new FileInputStream(path)) {
            data = new byte[in.available()];
            in.read(data);
        } catch (IOException e) {
            throw new BusinessException(ReturnCodeEnum.UNVALID, "图片base64编码失败", e);
        } 
        
        return Base64.getEncoder().encodeToString(data);
    }

    public static void toFile(String base64, String path) {
        if (StringUtil.isEmpty(base64)) {
            return;
        }
        try(OutputStream out = new FileOutputStream(path)) {
            byte[] b = Base64.getDecoder().decode(base64);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            out.write(b);
            out.flush();
        } catch (Exception e) {
            throw new BusinessException(ReturnCodeEnum.UNVALID, "base64编码文件失败", e);
        } 
    }

}