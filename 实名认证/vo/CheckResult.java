package com.wanshun.common.idcard.vo;

/**
 * @author yangwendong 2018-3-21
 */
public class CheckResult {

	public boolean checkSuccess;
	
	public String checkResultRemark;
	
	public String checkDataJson;

	public boolean isCheckSuccess() {
		return checkSuccess;
	}

	public void setCheckSuccess(boolean checkSuccess) {
		this.checkSuccess = checkSuccess;
	}

	public String getCheckResultRemark() {
		return checkResultRemark;
	}

	public void setCheckResultRemark(String checkResultRemark) {
		this.checkResultRemark = checkResultRemark;
	}

	public String getCheckDataJson() {
		return checkDataJson;
	}

	public void setCheckDataJson(String checkDataJson) {
		this.checkDataJson = checkDataJson;
	}

	@Override
	public String toString() {
		return "CheckResult [checkSuccess=" + checkSuccess + ", checkResultRemark=" + checkResultRemark
				+ ", checkDataJson=" + checkDataJson + "]";
	}
	
	
	
}
