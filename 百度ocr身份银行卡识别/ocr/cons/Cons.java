package com.pricl.biz.ocr.cons;

import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.xiaoleilu.hutool.date.DateUtil;

/**
 * 由于一个key只能一天请求500次，所以再此类做了调整。如果超出了自动更换其他key。如果都超出了，那么就如果当前时间大于明天早上的时候，重新设置
 * Created by winton
 */
public class Cons {

	private Cons() {
		throw new RuntimeException();
	}

	public static volatile String APP_KEY = "";
	public static volatile String APP_SECRET = "";
	public static volatile String APP_ID = "";
	
	public static final List<AppAuth> AUTHLIST;
	
	private static int index = 0;
	
	private static Date resetAuthTime = DateUtil.beginOfDay(DateUtil.tomorrow());
	
	private static boolean continueChage = true;
	
	static {
		AppAuth appAuth1 = new AppAuth("6fUiawTHngtKUAZxTYNGEBdy","fIuRPIZ2Ttz8s7CcdM566r0NpvajHgBV","10499952");
		AppAuth appAuth2 = new AppAuth("ZXYXpC3kyxKjQPZFh5YYLEZa","ILCy2HyBCjLxyhgvTjaOOXO8QU4HetT6","9993854");
		AUTHLIST = Lists.newArrayList(appAuth1,appAuth2);
		
		setFirst();
	}
	
	/**
	 * test
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(APP_KEY);
		for (int i = 0; i<5; i++ ) {
			changeAuth();
			System.out.println(APP_KEY);
			System.out.println(index);
			System.out.println(resetAuthTime);
			System.out.println(continueChage);
			System.out.println("------------------");
		}
	}
	
	public static void changeAuth() {
		if (index+1 >= AUTHLIST.size() && continueChage) {
			setEmpty();
			resetAuthTime = DateUtil.beginOfDay(DateUtil.tomorrow());
			continueChage = false;
			return ;
		} 
		
		if (DateUtil.date().after(resetAuthTime)) {
			continueChage = true;
			index = 0;
			resetAuthTime = DateUtil.beginOfDay(DateUtil.tomorrow());
			setFirst();
			return ;
		}
		
		if (continueChage) {
			setNext();
		}
	}
	
	public static class AppAuth {
		String appKey;
		String appSecret;
		String appId;
		
		public AppAuth(String appKey, String appSecret, String appId) {
			super();
			this.appKey = appKey;
			this.appSecret = appSecret;
			this.appId = appId;
		}
		
		public AppAuth() {
			
		}

		public String getAppKey() {
			return appKey;
		}

		public void setAppKey(String appKey) {
			this.appKey = appKey;
		}

		public String getAppSecret() {
			return appSecret;
		}

		public void setAppSecret(String appSecret) {
			this.appSecret = appSecret;
		}

		public String getAppId() {
			return appId;
		}

		public void setAppId(String appId) {
			this.appId = appId;
		}

		@Override
		public String toString() {
			return "AppAuth [appKey=" + appKey + ", appSecret=" + appSecret + ", appId=" + appId + "]";
		}

	}
	
	/**
	 * 设置为第一个
	 */
	private static void setFirst() {
		set(AUTHLIST.get(0));
	}
	
	/**
	 * 设置为空
	 */
	private static void setEmpty() {
		set(new AppAuth("","",""));
	}
	
	/**
	 * 设置为下一个
	 */
	private static void setNext() {
		index++;
		set(AUTHLIST.get(index));
	}
	
	/**
	 * 设置 
	 */
	private static void set(AppAuth appAuth) {
		APP_ID = appAuth.appId;
		APP_KEY = appAuth.appKey;
		APP_SECRET = appAuth.appSecret;
	}

}
