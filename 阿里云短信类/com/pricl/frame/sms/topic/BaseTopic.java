package com.pricl.frame.sms.topic;

import com.aliyun.mns.model.BatchSmsAttributes;
import com.aliyun.mns.model.BatchSmsAttributes.SmsReceiverParams;

/**
 * 主题继承该类后请在子类的构造方法后初始化smsReceiveParams；
 * @author winton
 *
 */
public abstract class BaseTopic {

	private String phoneNumber;

	private String templateCode;

	private BatchSmsAttributes.SmsReceiverParams smsReceiverParams;

	protected BaseTopic(String phoneNumber, String templateCode, SmsReceiverParams smsReceiverParams) {
		super();
		this.phoneNumber = phoneNumber;
		this.templateCode = templateCode;
		this.smsReceiverParams = smsReceiverParams;
		
		
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public BatchSmsAttributes.SmsReceiverParams getSmsReceiverParams() {
		return smsReceiverParams;
	}

	public void setSmsReceiverParams(BatchSmsAttributes.SmsReceiverParams smsReceiverParams) {
		this.smsReceiverParams = smsReceiverParams;
	}
	
}
