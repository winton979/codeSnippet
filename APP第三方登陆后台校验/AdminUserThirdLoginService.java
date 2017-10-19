/**
 *<p><strong>第三方登录service</strong>Biz Service</p>
 *@author winton 2017-10-19
 */
@Service
public class AdminUserThirdLoginService {
	/**
	 * wx-tool service，因为系统中用到了微信的tool包有便捷的http get和post请求，就没有用httpclient
	 */
	private IService iService = new WxService();
	
	/**
	 * 第三方service
	 */
	@Autowired
	AdminUserThirdLoginDao dao;

	/**
	 * 检查第三方token的有效期
	 * @return R code为200通过，400则失效
	 */
	public R checkAccessToken(String type, String token, String uid) {
		// 先判断token和数据库的token是否一致。
		AdminUserThirdLoginCriteria atlc = new AdminUserThirdLoginCriteria();
		atlc.setSAccesstokenEqualTo(token);
		atlc.setSUidEqualTo(uid);
		List<AdminUserThirdLogin> atl = dao.selectByEntity(atlc);
		if (atl.size() == 1) {
			return R.ok(200,"","登录成功");
		}
		// 该token没有存在数据库则根据type去查找对应的第三方接口
		if (ThirdPartyType.WX == Integer.parseInt((type))) {
			try {
				WxUser wechatuser = WxUser.fromJson(iService.post("https://api.weixin.qq.com/sns/userinfo", String.format("access_token=%s&openid=%s", token,uid)));
				if (wechatuser != null) {
					return R.ok(200,"","微信验证成功");
				}
			} catch (IOException | WxErrorException e) {
				e.printStackTrace();
				return R.ok(400,"","授权不通过");
			}
		}
		if (ThirdPartyType.WB == Integer.parseInt((type))) {
			Map<String,String> param = Maps.newHashMap(ImmutableMap.of("access_token", token, "uid", uid));
			try {
				String resultJson = iService.get("https://api.weibo.com/2/users/show.json", param);
				Map<String,String> resultObject = (Map<String, String>) JSON.parse(resultJson);
				if (resultObject.containsKey("error")) {
					return R.ok(400,"","微博授权验证不通过");
				} else {
					return R.ok(200,"","微博登录成功");
				}
			} catch (Exception e) {
				e.printStackTrace();
				return R.ok(400,"","微博授权不通过");
			}
		}
		if (ThirdPartyType.QQ == Integer.parseInt((type))) {
			Map<String,String> param = Maps.newHashMap(ImmutableMap.of("access_token", token, "openid", uid, "oauth_consumer_key", CertifyConstant.OAUTH_CONSUMER_KEY));
			try {
				String resultJson = iService.get("https://graph.qq.com/user/get_user_info", param);
				Map<String,String> resultObject = (Map<String, String>) JSON.parse(resultJson);
				if (resultObject.containsKey("ret")) {
					return R.ok(400,"","QQ授权验证不通过");
				} else {
					return R.ok(200,"","QQ登录成功");
				}
			} catch (Exception e) {
				e.printStackTrace();
				return R.ok(400,"","QQ授权不通过");
			}
		}
		return R.ok(400,"","type异常");
	}
}
