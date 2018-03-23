package com.wanshun.common.idcard;

import com.wanshun.common.exception.BusinessException;
import com.wanshun.common.idcard.authentication.IdCardHandler;
import com.wanshun.common.idcard.authentication.ShujubaoIdCardHandler;
import com.wanshun.common.idcard.authentication.XinyanIdCardHandler;
import com.wanshun.common.idcard.factory.IdCardAuthenticationFactory;
import com.wanshun.common.idcard.factory.IdCardAuthenticationFactory.Type;
import com.wanshun.common.idcard.vo.CheckParams;
import com.wanshun.common.idcard.vo.CheckResult;

public class IdCardUtil {
	
	private IdCardUtil() {
		throw new BusinessException();
	}
	
	public static CheckResult doCheck(CheckParams checkParams) {
		
		IdCardHandler xinyan = new XinyanIdCardHandler();
		IdCardHandler shujubao = new ShujubaoIdCardHandler();
		
		xinyan.setIdCardAuthentication(IdCardAuthenticationFactory.createAuthentication(Type.XIN_YAN));
		shujubao.setIdCardAuthentication(IdCardAuthenticationFactory.createAuthentication(Type.SHU_JU_BAO));
		
		xinyan.setNextHandler(shujubao); // 设置下一级别处理
		return xinyan.dispose(checkParams);
	}
	
}
