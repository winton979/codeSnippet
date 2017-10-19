package com.wechat.controller;

/**
 * 用于微信分享链接连接生成图文块
 * 
 * 如果用jsp方式获取，直接从request获取当前连接，
 * 
 * @author winton
 *
 */
@Controller
@RequestMapping(value = "wxad")
public class WeiXinShare {
	
	// github的wx-tools https://github.com/antgan/wx-tools
	private static IService iService = new WxService();

	@GetMapping(value = "getWxShareConfig")
	@ResponseBody
	public R getWxShareConfig(Model m, String reqLink) {
		List<String> jsApiList = new ArrayList<>();
		// 需要用到的分享渠道
		jsApiList.add("onMenuShareAppMessage");
		jsApiList.add("onMenuShareTimeline");
		jsApiList.add("onMenuShareQQ");
		jsApiList.add("onMenuShareQZone");

		WxJsapiConfig config = null;
		try {
			// reqLink地址需要完整的，wx每次分享后都会有queryString需要参数需要加上。否则二次分享会导致图文块失效，变回纯链接
			// 如果是jsp页面可以考虑采用下面的代码，去掉reqLink参数，采用req获取拼接
			//
			// StringBuffer reqLink = req.getRequestURL();
			// if (StringUtils.isNoneEmpty(req.getQueryString())) {
			// 	reqLink.append("?" + req.getQueryString());
			// }
			//
			
			config = iService.createJsapiConfig(reqLink.toString(), jsApiList);
			config.setAppid(WxConfig.getInstance().getAppId());
		} catch (WxErrorException e) {
			e.printStackTrace();
			return R.error("获取失败，系统异常");
		}
		return R.ok(config);
	}

}
