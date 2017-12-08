package com.pricl.frame.sms.topic;

import com.aliyun.mns.model.BatchSmsAttributes;
import com.aliyun.mns.model.BatchSmsAttributes.SmsReceiverParams;

public enum SmsReceiverParamsCreater {
	
	INSTANCE;
	
	public SmsReceiverParams phoneCodeSmsReceivers(String code) {
		BatchSmsAttributes.SmsReceiverParams smsReceiverParams = new BatchSmsAttributes.SmsReceiverParams();
		smsReceiverParams.setParam("code", code);
		return smsReceiverParams;
	}
	
	public SmsReceiverParams getCarNotifySmsReceivers(String orderNumber) {
		BatchSmsAttributes.SmsReceiverParams smsReceiverParams = new BatchSmsAttributes.SmsReceiverParams();
		smsReceiverParams.setParam("orderNumber", orderNumber);
		return smsReceiverParams;
	}
	
	public SmsReceiverParams paySuccessNotifySmsReceivers(String orderNumber) {
		BatchSmsAttributes.SmsReceiverParams smsReceiverParams = new BatchSmsAttributes.SmsReceiverParams();
		smsReceiverParams.setParam("orderNumber", orderNumber);
		return smsReceiverParams;
	}
	
	
}
