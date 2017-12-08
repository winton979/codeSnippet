package com.pricl.frame.sms;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.redis.core.RedisTemplate;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudTopic;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.model.BatchSmsAttributes;
import com.aliyun.mns.model.MessageAttributes;
import com.aliyun.mns.model.RawTopicMessage;
import com.aliyun.mns.model.TopicMessage;
import com.pricl.frame.core.E;
import com.pricl.frame.core.R;
import com.pricl.frame.sms.topic.BaseTopic;
import com.pricl.frame.sms.topic.GetCarNotifyTopic;
import com.pricl.frame.sms.topic.PaySuccessNotifyTopic;
import com.pricl.frame.sms.topic.PhoneCodeTopic;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * 阿里短信服务
 * 
 * @author winton 2017年10月16日
 *
 */
public class SMSHelper extends SMSBase {
	
	public static final String ACCESS_ID = "";

	public static final String ACCESS_KEY = "N";

	public static final String ENDPOINT = "";

	public static final String CHIGUA_SIGN_NAME = "";

	public static final int EXPIRE_TIME = 180; // 验证码过期时间 单位s

	public static final Integer NUMERIC_COUNT = 4; // 验证码位数

	public static final String REDIS_KEY_PREFIX = "sms_code"; // redis存储短信的前缀

	public static final String CHECK_COUNT_REDIS_KEY_PREFIX = "sms_code_check_count"; // redis手机号校验次数前缀

	@SuppressWarnings("rawtypes")
	RedisTemplate redisTemplate;

	/**
	 * 发送验证码
	 */
	@Override
	public R<?> sendPhoneCode(String phoneNumber) {
		return createPhoneCode(phoneNumber);
	}

	/**
	 * 发送提车信息
	 */
	@Override
	public R<?> sendGetCarNotify(BaseTopic topic) {
		return createGetCarNotify(topic);
	}
	
	/**
	 * 发送支付成功消息
	 */
	@Override
	public R<?> sendPaySuccessNotify(BaseTopic topic) {
		return createPaySuccessNotify(topic);
	}

	/**
	 * 验证码验证
	 * 
	 * @param phoneNumber
	 * @param code
	 * @return
	 */
	@Override
	public R<?> checkCode(String phoneNumber, String code) {
		// TODO 校验次数处理
		return checkResult(phoneNumber, code);
	}

	/**
	 * ali code
	 * 
	 * @param phoneNumber
	 * @return
	 */
	private void send(BaseTopic baseTopic) {
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
		batchSmsAttributes.setFreeSignName(CHIGUA_SIGN_NAME);
		// 3.2 设置发送短信使用的模板（SMSTempateCode）
		batchSmsAttributes.setTemplateCode(baseTopic.getTemplateCode());
		// 3.3 设置发送短信所使用的模板中参数对应的值（在短信模板中定义的，没有可以不用设置）
		/*
		 * BatchSmsAttributes.SmsReceiverParams smsReceiverParams = new
		 * BatchSmsAttributes.SmsReceiverParams();
		 * smsReceiverParams.setParam("code", numeric);
		 */
		// 3.4 增加接收短信的号码
		batchSmsAttributes.addSmsReceiver(baseTopic.getPhoneNumber(), baseTopic.getSmsReceiverParams());
		messageAttributes.setBatchSmsAttributes(batchSmsAttributes);
		try {
			/**
			 * Step 4. 发布SMS消息
			 */
			TopicMessage ret = topic.publishMessage(msg, messageAttributes);
			System.out.println("MessageId: " + ret.getMessageId());
			System.out.println("MessageMD5: " + ret.getMessageBodyMD5());
		} catch (ServiceException se) {
			System.out.println(se.getErrorCode() + se.getRequestId());
			System.out.println(se.getMessage());
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		client.close();
	}

	@SuppressWarnings("unchecked")
	private R<?> createPhoneCode(String phoneNumber) {
		Matcher m = isPhoneNunber(phoneNumber);
		if (!m.matches()) {
			return R.result(-1, "不是有效的手机号码", "");
		}

		// 随机生成code
		String numeric = RandomStringUtils.randomNumeric(NUMERIC_COUNT);
		send(new PhoneCodeTopic(phoneNumber, numeric));

		// 存入redis缓存
		redisTemplate.opsForValue().set(REDIS_KEY_PREFIX + phoneNumber, numeric);
		redisTemplate.expire(REDIS_KEY_PREFIX + phoneNumber, EXPIRE_TIME, TimeUnit.SECONDS);
		return R.result(1, "发送成功", "");
	}

	private R<?> checkResult(String phoneNumber, String code) {
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
	
	private R<?> createGetCarNotify(BaseTopic topic) {
		if (!(topic instanceof GetCarNotifyTopic)) {
			throw new RuntimeException("错误的类型");
		}
		GetCarNotifyTopic getCarNotifyTopic = (GetCarNotifyTopic) topic;

		Matcher m = isPhoneNunber(getCarNotifyTopic.getPhoneNumber());
		if (!m.matches()) {
			return R.result(-1, "不是有效的手机号码", "");
		}
		send(getCarNotifyTopic);

		return R.result(1, "发送成功", "");
	}
	
	private R<?> createPaySuccessNotify(BaseTopic topic) {
		if (!(topic instanceof PaySuccessNotifyTopic)) {
			throw new RuntimeException("错误的类型");
		}
		PaySuccessNotifyTopic paySuccessNotifyTopic = (PaySuccessNotifyTopic) topic;
		
		Matcher m = isPhoneNunber(paySuccessNotifyTopic.getPhoneNumber());
		if (!m.matches()) {
			return R.result(-1, "不是有效的手机号码", "");
		}
		send(paySuccessNotifyTopic);
		return R.result(1, "发送成功", "");
	}

	public void setRedisTemplate(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

}
