<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<head
	<title>title</title>
</head>

<body>
	
</body>

<script type="text/javascript">
	wx.config({
		debug : false,
		appId : "${wxconfig.appid}",
		timestamp : "${wxconfig.timestamp}",
		nonceStr : "${wxconfig.noncestr}",
		signature : "${wxconfig.signature}",
		jsApiList : [
		// 所有要调用的 API 都要加到这个列表中
		'onMenuShareAppMessage','onMenuShareTimeline','onMenuShareQQ','onMenuShareQZone']
	});
	window.share_config = {
		     "share": {
		        "imgUrl": "",//分享图，默认当相对路径处理，所以使用绝对路径的的话，“http://”协议前缀必须在。 图片大小一定要注意：300*300px,不然就出问题了，坑人的
		        "desc" : "",//摘要,如果分享到朋友圈的话，不显示摘要。
		        "title" : '',//分享卡片标题
		        "link": window.location.href,//分享出去后的链接，这里可以将链接设置为另一个页面。
		        "success":function(){//分享成功后的回调函数
		        },
		        'cancel': function () { 
		            // 用户取消分享后执行的回调函数
		        }
		    }
		};  
    wx.ready(function () {
	    wx.onMenuShareAppMessage(share_config.share);//分享给好友
	    wx.onMenuShareTimeline(share_config.share);//分享给好友
	    wx.onMenuShareQQ(share_config.share);//分享qq
	    wx.onMenuShareQZone(share_config.share);//分享qq空间
	});

</script>

</html>