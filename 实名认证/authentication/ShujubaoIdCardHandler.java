package com.wanshun.common.idcard.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wanshun.common.idcard.vo.CheckParams;
import com.wanshun.common.idcard.vo.CheckResult;

/**
 * @author yangwendong 2018-3-21
 */
public class ShujubaoIdCardHandler extends IdCardHandler {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public CheckResult dispose(CheckParams checkParams) {
		CheckResult check = super.idCardAuthentication.check(checkParams);
		logger.info(check.toString());
		if (check.isCheckSuccess()) {
			logger.info("数据宝校验通过");
			return check;
		} else if (super.nextHandler != null) {
			logger.info("数据宝校验不通过，给下一个");
			return super.nextHandler.dispose(checkParams);
		} else {
			return check;
		}
	}


}
