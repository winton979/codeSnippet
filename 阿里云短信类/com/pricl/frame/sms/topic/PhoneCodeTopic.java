package com.pricl.frame.sms.topic;

import com.pricl.frame.sms.TemplateCodeEnum;

/**
 * 验证码获取主题
 * @author winton
 *
 */
public class PhoneCodeTopic extends BaseTopic {

	public PhoneCodeTopic(String phoneNumber,String code) {
		super(phoneNumber, TemplateCodeEnum.PHONE_CODE.getCode(),SmsReceiverParamsCreater.INSTANCE.phoneCodeSmsReceivers(code));
	}

}
