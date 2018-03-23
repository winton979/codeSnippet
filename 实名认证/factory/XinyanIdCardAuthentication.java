package com.wanshun.common.idcard.factory;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wanshun.common.idcard.config.IdCardConfig;
import com.wanshun.common.idcard.factory.IdCardAuthenticationFactory.Type;
import com.wanshun.common.idcard.vo.CheckParams;
import com.wanshun.common.idcard.vo.CheckResult;
import com.wanshun.common.idcard.xinyan.RsaCodingUtil;
import com.wanshun.common.utils.JsonUtil;

import cn.hutool.http.HttpUtil;

/**
 * 新颜实名认证接口
 * @author yangwendong 2018-3-21
 */
public class XinyanIdCardAuthentication implements IdCardAuthentication {

	private Logger log = LoggerFactory.getLogger(getClass());

	private CheckResult execut(CheckParams checkParams) {
		log.info(checkParams.toString());

		String trans_id = "" + System.currentTimeMillis();// 商户订单号
		String trade_date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());// 订单日期

		/** 组装参数 **/
		Map<Object, Object> MapData = new HashMap<Object, Object>();
		MapData.put("member_id", IdCardConfig.XYMEMBERID);
		MapData.put("terminal_id", IdCardConfig.XYTERMINALID);
		MapData.put("trans_id", trans_id);
		MapData.put("trade_date", trade_date);
		MapData.put("id_card", checkParams.getIdcard());
		MapData.put("id_holder", checkParams.getName());
		MapData.put("industry_type", "A1");// 根据自己的业务类型传入参数
		MapData.put("is_photo", "noPhoto");
		log.info("====请求明文:" + MapData);

		String base64str = null;
		try {
			base64str = new String(new Base64().encode(JsonUtil.toJson(MapData).getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		log.info("====base64 编码:" + base64str);

		/** rsa加密 **/
		String pfxpath = IdCardConfig.XYPFXPATH;// 商户私钥

		String data_content = RsaCodingUtil.encryptByPriPfxFile(base64str, pfxpath, IdCardConfig.XYPFXPWD);// 加密数据

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("member_id", IdCardConfig.XYMEMBERID);
		params.put("terminal_id", IdCardConfig.XYTERMINALID);
		params.put("data_type", "json");
		params.put("data_content", data_content);
		String PostString = HttpUtil.post(IdCardConfig.XYURL, params);

		@SuppressWarnings("unchecked")
		Map<String, Object> postResultMap = JsonUtil.fromJson(PostString, Map.class);
		
		CheckResult cr = new CheckResult();
		cr.setCheckSuccess(true);
		cr.setCheckDataJson(PostString);
		cr.setCheckResultRemark("实名认证成功"); // 默认正确
		
		while (true) {
			
			if (!"true".equals(postResultMap.get("success").toString())) {
				cr.setCheckResultRemark("查询接口失败");
				cr.setCheckSuccess(false);
				break;
			}
			
			@SuppressWarnings("unchecked")
			Map<String,Object> dataMap = JsonUtil.fromJson(postResultMap.get("data").toString(),Map.class);
			if (!"0.0".equals(dataMap.get("code").toString())) { // 注意此处的值对比
				cr.setCheckResultRemark("实名认证不通过");
				cr.setCheckSuccess(false);
				break;
			}
			
			break;
		}
		
		log.info(PostString);
		return cr;
	}

	@Override
	public CheckResult check(CheckParams checkParams) {
		return execut(checkParams);
	}

	/**
	 * test
	 */
	public static void main(String[] args) {
		IdCardAuthentication ica = IdCardAuthenticationFactory.createAuthentication(Type.XIN_YAN);

		CheckParams checkParams = new CheckParams();
		checkParams.setIdcard("441424199406111373");
		checkParams.setName("杨文东");
		CheckResult check = ica.check(checkParams);
		System.out.println(check);
	}

}
