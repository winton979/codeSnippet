package com.pricl.frame.sms;

public enum TemplateCodeEnum {

	PHONE_CODE("短信验证码", "SMS_65925331"), GET_CAR("提车", "SMS_115205064"), PAY_SUCCESS("支付成功","SMS_115050102");

	private TemplateCodeEnum(String describe, String code) {
		this.describe = describe;
		this.code = code;
	}

	private String describe;

	private String code;

	public String getDescribe() {
		return describe;
	}

	public String getCode() {
		return code;
	}

}
