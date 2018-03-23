package com.wanshun.common.idcard.factory;

import com.wanshun.common.idcard.vo.CheckParams;
import com.wanshun.common.idcard.vo.CheckResult;

/**
 * @author yangwendong 2018-3-21
 */
public interface IdCardAuthentication {
	
	CheckResult check(CheckParams checkParams);
	
}
