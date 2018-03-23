package com.wanshun.common.idcard.vo;

import java.io.Serializable;

/**
 * @author yangwendong 2018-3-21
 */
public class CheckParams implements Serializable {
	
	/** 
	* @Fields serialVersionUID 
	*/ 
	private static final long serialVersionUID = 1L;

	/**
	 * 姓名
	 */
	private String name;
	
	/**
	 * 身份证
	 */
	private String idcard;
	
	public CheckParams () {
		
	}
	
	public CheckParams(String name, String idcard) {
		super();
		this.name = name;
		this.idcard = idcard;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	@Override
	public String toString() {
		return "CheckParams [name=" + name + ", idcard=" + idcard + "]";
	}
	
	
	
}
