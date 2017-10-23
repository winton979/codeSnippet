package com.pricl.frame.cache;

import 

/**
 * redis 配置
 *
 */
public class MyRedisConfig {
	private static final String configFile = "/config.properties";
	private static MyRedisConfig config = null;
	
	//配置文件读取项
	private volatile String host;
	private volatile String port;
	private volatile String password;
	private volatile String database;
	
	private volatile String maxIdle;
	private volatile String maxWaitMillis;
	private volatile String testOnBorrow;

	public MyRedisConfig() {
		//写读配置文件代码
		Properties p = new Properties();
		InputStream inStream = this.getClass().getResourceAsStream(configFile);
		if(inStream == null){
			try {
				throw new Exception("找不到文件");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			p.load(inStream);
			this.host = p.getProperty("redis.host");
			if (StringUtils.isNotBlank(this.host)) {
				this.host = this.host.trim();
			}
			this.port = p.getProperty("redis.port");
			if (StringUtils.isNotBlank(this.port)) {
				this.port = this.port.trim();
			}
			this.password = p.getProperty("redis.password");
			if (StringUtils.isNotBlank(this.port)) {
				this.password = this.password.trim();
			}
			this.database = p.getProperty("redis.database");
			if (StringUtils.isNotBlank(this.database)) {
				this.database = this.database.trim();
			}
			this.maxIdle = p.getProperty("redis.pool.maxIdle");
			if (StringUtils.isNotBlank(this.maxIdle)) {
				this.maxIdle = this.maxIdle.trim();
			}
			this.maxWaitMillis = p.getProperty("redis.pool.maxWait");
			if (StringUtils.isNotBlank(this.maxWaitMillis)) {
				this.maxWaitMillis = this.maxWaitMillis.trim();
			}
			this.testOnBorrow = p.getProperty("redis.pool.testOnBorrow");
			if (StringUtils.isNotBlank(this.testOnBorrow)) {
				this.testOnBorrow = this.testOnBorrow.trim();
			}
			inStream.close();
		} catch (IOException e) {
			try {
				throw new Exception("load config.properties error,class根目录下找不到config.properties文件");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		System.out.println("load config.properties success");
	}
	

	/**
	 * 同步获取/加载单例
	 * @return
	 */
	public static synchronized MyRedisConfig getInstance(){
		if(config == null){
			config = new MyRedisConfig();
		}
		return config;
	}

	public static MyRedisConfig getConfig() {
		return config;
	}

	public static void setConfig(MyRedisConfig config) {
		MyRedisConfig.config = config;
	}

	// getter and setter

}
