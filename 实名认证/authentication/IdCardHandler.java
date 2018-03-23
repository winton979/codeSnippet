package com.wanshun.common.idcard.authentication;

import com.wanshun.common.idcard.factory.IdCardAuthentication;
import com.wanshun.common.idcard.vo.CheckParams;
import com.wanshun.common.idcard.vo.CheckResult;

/**
 * @author yangwendong 2018-3-21
 */
public abstract class IdCardHandler {
	
	protected IdCardHandler nextHandler = null;
	
	protected IdCardAuthentication idCardAuthentication;

	public IdCardHandler getNextHandler() {
		return nextHandler;
	}

	public void setNextHandler(IdCardHandler nextHandler) {
		this.nextHandler = nextHandler;
	}

	public void setIdCardAuthentication(IdCardAuthentication idCardAuthentication) {
		this.idCardAuthentication = idCardAuthentication;
	}
	
	public abstract CheckResult dispose(CheckParams checkParams);
}