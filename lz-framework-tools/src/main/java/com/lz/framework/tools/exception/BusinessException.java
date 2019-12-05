package com.lz.framework.tools.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lz.framework.tools.enums.ReturnCodeEnum;

/**
 * 
 * @author admin
 *
 */
public class BusinessException extends RuntimeException {

	/**
	 * 自定义业务异常
	 */
	private static final long serialVersionUID = 8184566062442592256L;

	/**
	 * 代码
	 */
	String code;
	
	/**
	 * 失败原因
	 */
	String message;

	/**
	 * 返回结果
	 */
	ReturnCodeEnum returnCode;

	static final Logger logger = LoggerFactory.getLogger(BusinessException.class);

	public BusinessException() {
		super();
	}

	public BusinessException(ReturnCodeEnum returnCode) {
		super(returnCode.getName());
		this.returnCode = returnCode;
		this.code = returnCode.getValue();
		logger.info("异常代码：{}，异常原因：{}", returnCode.getValue(), returnCode.getName());
	}

	public BusinessException(ReturnCodeEnum returnCode, String message) {
		super(message);
		this.message = message;
		this.returnCode = returnCode;
		this.code = returnCode.getValue();
		logger.info("异常代码：{}，异常原因：{}，异常备注：{}", returnCode.getValue(), returnCode.getName(), message);
	}

	public BusinessException(String code, String message) {
		super(message);
		this.code = code;
		this.message = message;
		logger.info("异常代码：{}，异常原因：{}，异常备注：{}", returnCode.getValue(), returnCode.getName(), message);
	}

	public BusinessException(ReturnCodeEnum returnCode, String message, Throwable cause) {
		super(message, cause);
		this.message = message;
		this.returnCode = returnCode;
		this.code = returnCode.getValue();
		logger.info("异常代码：{}，异常原因：{}，异常备注：{}", returnCode.getValue(), returnCode.getName(), message, cause);
	}

	public BusinessException(Throwable cause) {
		super(cause);
		logger.info("捕捉到的业务异常", cause);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
		logger.info(message, cause);
	}

	public BusinessException(String message) {
		super(message);
		logger.info(message);
	}

	public ReturnCodeEnum getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(ReturnCodeEnum returnCode) {
		this.returnCode = returnCode;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
