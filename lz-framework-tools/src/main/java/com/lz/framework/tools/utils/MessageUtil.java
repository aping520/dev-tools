package com.lz.framework.tools.utils;

import com.lz.framework.tools.dto.MessageDto;
import com.lz.framework.tools.enums.ReturnCodeEnum;

/**
 * 组装返回报文体
 *
 */
public class MessageUtil {
	
	public static MessageDto buildDto(ReturnCodeEnum returnCode){
		MessageDto result = new MessageDto();
		result.setCode(returnCode.getValue());
		return result;
	}
	
	public static MessageDto buildDto(ReturnCodeEnum returnCode, String message){
		MessageDto result = new MessageDto();
		result.setCode(returnCode.getValue());
		result.setMessage(message);
		return result;
	}
	
	public static MessageDto buildDto(ReturnCodeEnum returnCode, String message, Object data){
		MessageDto result = new MessageDto();
		result.setCode(returnCode.getValue());
		result.setMessage(message);
		result.setData(data);
		return result;
	}

	public static MessageDto buildDto(String returnCode){
		MessageDto result = new MessageDto();
		result.setCode(returnCode);
		return result;
	}
	
	public static MessageDto buildDto(String returnCode, String message){
		MessageDto result = new MessageDto();
		result.setCode(returnCode);
		result.setMessage(message);
		return result;
	}
	
	public static MessageDto buildDto(String returnCode, String message, Object data){
		MessageDto result = new MessageDto();
		result.setCode(returnCode);
		result.setMessage(message);
		result.setData(data);
		return result;
	}
}
