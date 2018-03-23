package com.wanshun.common.idcard.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wanshun.common.idcard.vo.CheckParams;
import com.wanshun.common.idcard.vo.CheckResult;

/**
 * @author yangwendong 2018-3-21
 */
public class XinyanIdCardHandler extends IdCardHandler {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public CheckResult dispose(CheckParams checkParams) {
		CheckResult check = super.idCardAuthentication.check(checkParams);
		logger.info(check.toString());
		if (check.isCheckSuccess()) {
			logger.info("新颜校验通过");
			return check;
		} else {
			logger.info("新颜校验不通过，给下一个");
			if (super.nextHandler != null) {
				return super.nextHandler.dispose(checkParams);
			}
		}
		return null;
	}


}
