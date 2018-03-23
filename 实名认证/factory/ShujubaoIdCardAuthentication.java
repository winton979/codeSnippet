package com.wanshun.common.idcard.factory;

import java.util.HashMap;
import java.util.Map;

import com.wanshun.common.idcard.config.IdCardConfig;
import com.wanshun.common.idcard.factory.IdCardAuthenticationFactory.Type;
import com.wanshun.common.idcard.vo.CheckParams;
import com.wanshun.common.idcard.vo.CheckResult;
import com.wanshun.common.utils.JsonUtil;
 
import cn.hutool.http.HttpUtil;

/**
 * @author yangwendong 2018-3-21
 */
public class ShujubaoIdCardAuthentication implements IdCardAuthentication {

	private CheckResult execut(CheckParams checkParams) {
		
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json;charset=UTF-8");
		Map<String, Object> params = new HashMap<>();
		params.put("key", IdCardConfig.SHUJUBAOKEY);
		params.put("name", checkParams.getName());
		params.put("idcard", checkParams.getIdcard());
		
		CheckResult  cr = new CheckResult();
		while (true) {
			
			String result = HttpUtil.post(IdCardConfig.SHUJUBAOURL, params);
			@SuppressWarnings("unchecked")
			Map<String,Object> fromJson = JsonUtil.fromJson(result, Map.class);
			cr.setCheckDataJson(result);
			
			// 开始校验结果，默认成功
			cr.setCheckSuccess(true);
			cr.setCheckResultRemark("实名认证通过");
			
			if (!"10000".equals(fromJson.get("code"))) { // 后期统一错误码配置
				cr.setCheckSuccess(false);
				cr.setCheckResultRemark("查询失败");
				break;
			}
			
			@SuppressWarnings("unchecked")
			Map<String,Object> resultMap = JsonUtil.fromJson(fromJson.get("data").toString(),Map.class);
			if (!"1.0".equals(resultMap.get("result").toString())) { // 注意此处对比
				cr.setCheckSuccess(false);
				cr.setCheckResultRemark("实名认证不通过");
				break;
			}
			
			break;
		}
		
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
		IdCardAuthentication ica = IdCardAuthenticationFactory.createAuthentication(Type.SHU_JU_BAO);
		
		CheckParams checkParams = new CheckParams();
		checkParams.setIdcard("441424199406111373");
		checkParams.setName("杨文东");
		CheckResult check = ica.check(checkParams );
		System.out.println(check);
	}

}
