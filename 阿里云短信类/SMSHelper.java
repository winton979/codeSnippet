package com.pricl.frame.sms;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.io.DefaultSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudTopic;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.model.BatchSmsAttributes;
import com.aliyun.mns.model.MessageAttributes;
import com.aliyun.mns.model.RawTopicMessage;
import com.aliyun.mns.model.TopicMessage;
import com.pricl.frame.core.R;
import com.pricl.frame.core.web.Helper;


/**
 * <p>
 * <strong>该短信类只适用于阿里云短信平台的旧版短信接口。新版的参照文档</strong>
 * <strong>另外，如果用于较高的安全信的话，验证码应该绑定sessionId</strong>
 * </p>
 * 阿里短信服务
 * @author winton 2017年10月16日
 *
 */
public class SMSHelper {

	public static final String ACCESS_ID = "";
	
	public static final String ACCESS_KEY = "";
	
	public static final String ENDPOINT = "";

	public static final String SIGN_NAME = "";
	
	public static final String TEMPLATE_CODE = "";

	public static final int EXPIRE_TIME = 180; // 验证码过期时间 单位s

	public static final Integer NUMERIC_COUNT = 4; // 验证码位数

	public static final String REDIS_KEY_PREFIX = "sms_code"; // redis存储短信的前缀
	
	// 用springMVC配置注入
	@SuppressWarnings("rawtypes")
	RedisTemplate redisTemplate;
	
	public R sendSMS(String phoneNumber) {
		Matcher m = isPhoneNunber(phoneNumber);
		if (!m.matches()) {
			return R.result(-1, "不是有效的手机号码", "");
		}
		String numeric = send(phoneNumber);

		// 存入redis缓存
		redisTemplate.opsForValue().set(REDIS_KEY_PREFIX + phoneNumber, numeric);
		redisTemplate.expire(REDIS_KEY_PREFIX + phoneNumber, 180L, TimeUnit.SECONDS);
		return R.result(1, "发送成功", "");
	}
	
	/**
	 * 验证码验证
	 * @param phoneNumber
	 * @param code
	 * @return
	 */
	public R checkCode(String phoneNumber, String code) {
		
		Matcher m = isPhoneNunber(phoneNumber);
		if (!m.matches()) {
			return R.result(-1, "不是有效的手机号码", "");
		}
		String value = (String) redisTemplate.opsForValue().get(REDIS_KEY_PREFIX + phoneNumber);
		if (StringUtils.isEmpty(value)) {
			return R.result(-1, "验证码无效或过期", "");
		}
		
		if (!StringUtils.equals(code, value)) {
			return R.result(-1, "验证码不正确", "");
		}
		return R.result(1, "验证成功", "");
	}
	
	
	public RedisTemplate getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
	/**
	 * @param phoneNumber
	 * @return
	 */
	private Matcher isPhoneNunber(String phoneNumber) {
		Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
		Matcher m = p.matcher(phoneNumber);
		return m;
	}

	/**
	 * ali code
	 * 
	 * @param phoneNumber
	 * @return
	 */
	private String send(String phoneNumber) {
		// 随机生成code
		String numeric = RandomStringUtils.randomNumeric(NUMERIC_COUNT);
		/**
		 * Step 1. 获取主题引用
		 */
		CloudAccount account = new CloudAccount(ACCESS_ID, ACCESS_KEY, ENDPOINT);
		MNSClient client = account.getMNSClient();
		CloudTopic topic = client.getTopicRef("sms.topic-cn-shenzhen");
		/**
		 * Step 2. 设置SMS消息体（必须）
		 *
		 * 注：目前暂时不支持消息内容为空，需要指定消息内容，不为空即可。
		 */
		RawTopicMessage msg = new RawTopicMessage();
		msg.setMessageBody("sms-message");
		/**
		 * Step 3. 生成SMS消息属性
		 */
		MessageAttributes messageAttributes = new MessageAttributes();
		BatchSmsAttributes batchSmsAttributes = new BatchSmsAttributes();
		// 3.1 设置发送短信的签名（SMSSignName）
		batchSmsAttributes.setFreeSignName(SIGN_NAME);
		// 3.2 设置发送短信使用的模板（SMSTempateCode）
		batchSmsAttributes.setTemplateCode(TEMPLATE_CODE);
		// 3.3 设置发送短信所使用的模板中参数对应的值（在短信模板中定义的，没有可以不用设置）
		BatchSmsAttributes.SmsReceiverParams smsReceiverParams = new BatchSmsAttributes.SmsReceiverParams();
		smsReceiverParams.setParam("code", numeric);
		// 3.4 增加接收短信的号码
		batchSmsAttributes.addSmsReceiver(phoneNumber, smsReceiverParams);
		messageAttributes.setBatchSmsAttributes(batchSmsAttributes);
		try {
			/**
			 * Step 4. 发布SMS消息
			 */
			TopicMessage ret = topic.publishMessage(msg, messageAttributes);
			System.out.println("MessageId: " + ret.getMessageId());
			System.out.println("MessageMD5: " + ret.getMessageBodyMD5());
		/*} catch (ServiceException se) {
			System.out.println(se.getErrorCode() + se.getRequestId());
			System.out.println(se.getMessage());
			se.printStackTrace();*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		client.close();
		return numeric;
	}
	
}
