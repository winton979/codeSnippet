package com.pricl.frame.sms.topic;

import com.pricl.frame.sms.TemplateCodeEnum;

/**
 * 提车通知主题
 * 
 * @author winton
 *
 */
public class GetCarNotifyTopic extends BaseTopic {

	public GetCarNotifyTopic(String phoneNumber, String orderNumber) {
		super(phoneNumber, TemplateCodeEnum.GET_CAR.getCode(), SmsReceiverParamsCreater.INSTANCE.getCarNotifySmsReceivers(orderNumber));
	}

}
