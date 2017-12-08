package com.pricl.frame.sms.topic;

import com.pricl.frame.sms.TemplateCodeEnum;

/**
 * 提车通知主题
 * 
 * @author winton
 *
 */
public class PaySuccessNotifyTopic extends BaseTopic {

	public PaySuccessNotifyTopic(String phoneNumber, String orderNumber) {
		super(phoneNumber, TemplateCodeEnum.GET_CAR.getCode(), SmsReceiverParamsCreater.INSTANCE.paySuccessNotifySmsReceivers(orderNumber));
	}


}
