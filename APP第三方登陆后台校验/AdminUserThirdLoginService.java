/**
 *<p><strong>第三方登录service</strong>Biz Service</p>
 *@author winton 2017-10-19
 */
@Service
public class AdminUserThirdLoginService {
	private R checkAccessToken(int type, String token, String uid) {
 
+		if (ThirdPartyType.WX == type) {
 
+			Map<String, Object> param = Maps.newHashMap(ImmutableMap.of("access_token", token, "openid", uid));
 
+			String resultJson = HttpUtil.post("https://api.weixin.qq.com/sns/userinfo",param);
 
+			JSONObject jsonObj = JSONUtil.parseObj(resultJson);
 
+			if (jsonObj.containsKey("errcode")) {
 
+				return R.result(-1, "微信授权验证不通过", "");
 
+			} else {
 
+				return R.result(1, "微信授权通过", "");
 
+			}
 
+		}
 
+		if (ThirdPartyType.WB == type) {
 
+			Map<String, Object> param = Maps.newHashMap(ImmutableMap.of("access_token", token, "uid", uid));
 
+			String resultJson = HttpUtil.get("https://api.weibo.com/2/users/show.json", param);
 
+			JSONObject jsonObj = JSONUtil.parseObj(resultJson);
 
+			if (jsonObj.get("error") != null) {
 
+				return R.result(-1, "微博授权验证不通过", "");
 
+			} else {
 
+				return R.result(1, "微博登录成功", "");
 
+			}
 
+		}
 
+		if (ThirdPartyType.QQ == type) {
 
+			Map<String, Object> param = Maps.newHashMap(ImmutableMap.of("access_token", token, "openid", uid, "oauth_consumer_key", TokenConstant.OAUTH_CONSUMER_KEY));
 
+			String resultJson = HttpUtil.get("https://graph.qq.com/user/get_user_info", param);
 
+			JSONObject jsonObj = JSONUtil.parseObj(resultJson);
 
+			if (!"0".equals(String.valueOf(jsonObj.get("ret")))) {
 
+				return R.result(-1, "QQ授权验证不通过", "");
 
+			} else {
 
+				return R.result(1, "QQ登录成功", "");
 
+			}
 
+		}
 
+		return R.result(-1, "type异常", "");
 
+	}
}
