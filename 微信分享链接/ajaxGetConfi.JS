  /*以下调用微信接口*/ 
   $(function(){
   	//通过config接口注入权限验证配置
   		$.ajax({
   			type:"GET",
   			url:"",
   			data:{"reqLink":window.location.href}
   		}).done(function(result){
   			//console.log(result.model.config.appid);
   			wx.config({
   				debug:false,
   				appId:result.model.config.appid,
   				timestamp:result.model.config.timestamp,
   				nonceStr:result.model.config.noncestr,
   				signature:result.model.config.signature,
   				jsApiList:result.model.config.jsApiList
   			});
   		});
   		
   	//通过ready接口处理成功验证
   		wx.ready(function(){
   			var protocol = window.location.protocol;
   			alert(protocol+'//'+window.location.host);
			// 全局配置
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
   			//分享到朋友圈
   			wx.onMenuShareTimeline({
   				title:'瓜姐专栏123',
   				link:window.location.href,
   				imgUrl:'img/pic_car.jpg',
   				success:function(){
   					alert('您已成功分享瓜姐百科');
   				},
   				cancel:function(){
   					alert('分享失败');
   				}
   			});
			// 注入配置
   			wx.onMenuShareAppMessage(share_config.share);//分享给好友
	        wx.onMenuShareTimeline(share_config.share);//分享给好友
			wx.onMenuShareQQ(share_config.share);//分享qq
			wx.onMenuShareQZone(share_config.share);//分享qq空间
   			
   		});
   })
