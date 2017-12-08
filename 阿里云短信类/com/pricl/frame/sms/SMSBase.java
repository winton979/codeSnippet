package com.pricl.frame.sms;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pricl.frame.core.R;
import com.pricl.frame.sms.topic.BaseTopic;

/**
 * 短信基类
 * @author winton
 *
 */
public abstract class SMSBase {

	/**
	 * 发送验证码
	 * @param phoneNumber
	 * @return
	 */
	abstract R sendPhoneCode(String phoneNumber);
	
	/**
	 * 发送提车通知
	 * @param topic
	 * @return
	 */
	abstract R sendGetCarNotify(BaseTopic topic);

	/**
	 * 验证验证码
	 * @param phoneNumber
	 * @param code
	 * @return
	 */
	abstract R checkCode(String phoneNumber, String code);
	
	/**
	 * 支付成功消息
	 * @param topic
	 * @return
	 */
	abstract R<?> sendPaySuccessNotify(BaseTopic topic);
	
	/**
	 * 判断是否手机号
	 * @param phoneNumber
	 * @return
	 */
	protected Matcher isPhoneNunber(String phoneNumber) {
		Pattern p = Pattern.compile("^[1][3,4,5,7,8,9][0-9]{9}$");
		Matcher m = p.matcher(phoneNumber);
		return m;
	}


}
