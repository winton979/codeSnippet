#### 阿里云短信旧版
该短信类只适用于阿里云短信平台的旧版短信接口。新版的参照文档

另外，如果用于较高的安全信的话，验证码应该绑定sessionId

applicationContext.xml添加
<bean class="com.pricl.frame.sms.SMSHelper">
	<property name="redisTemplate" ref="redisTemplate" />
</bean>