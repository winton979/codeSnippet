package com.wanshun.common.idcard.factory;

/**
 * @author yangwendong 2018-3-21
 */
public class IdCardAuthenticationFactory {
	
	public static IdCardAuthentication createAuthentication(Type type) {
		if (Type.XIN_YAN == type) {
			return new XinyanIdCardAuthentication();
		} 
		if (Type.SHU_JU_BAO == type) {
			return new ShujubaoIdCardAuthentication();
		}
		return null;
	}
	
	public enum Type {
		
		XIN_YAN("xinyan"),SHU_JU_BAO("shujubao");
		
		Type (String name) {
			this.name = name();
		}
		
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
	}
	
	public static void main(String[] args) {
		new IdCardAuthenticationFactory().createAuthentication(Type.SHU_JU_BAO);
	}
}
