
	document.write('<div class="follow"><span class="follow-font">猜你关注</span></div>');

	//同步广告位
	document.write('<div class="ws-zl-dybanner357"></div><script>var ws_dy="zl-dybanner357";var ws_width =320;var ws_height=100;</script><script src="//nads.wuaiso.com/newswap/wap/js/newsasyunion.js"></script><div class="ws-zl-dybanner357"></div><script>var ws_dy="zl-dybanner357";var ws_width =320;var ws_height=100;</script><script src="//nads.wuaiso.com/newswap/wap/js/newsasyunion.js"></script>');

	document.write('<div id="mescroll" class="mescroll"></div>');
	
	
	
	document.write('<link rel="stylesheet" href="news.css?_r=0411">');
	
	/*
	<script src="news_zj.js" type="text/javascript" charset="utf-8"></script>
	
	*/
		
		
	
		$(function(){
	
		var T = {};
		/**
		 * 获取url上的请求参数
		 * 使用示例 location.href = http://localhost/index.html?id=123
		 * T.p('id') --> 123;
		 * @param name
		 * @returns {null}
		 */
		T.p = function(name) {
			var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
			var r = window.location.search.substr(1).match(reg);
			if(r!=null)return  decodeURI(r[2]); return null;
		};
		
		
		
		$('#mescroll').append('<div style="border-bottom: 0.1px solid #DDDDDD;background-color: white;position: relative;width: auto;height: 21px;margin-top: 10px;margin-left: 6px;"><span style="border-bottom: 2px solid #FF5809;padding-bottom: -1px;">热点推荐</span></div><ul id="dataList" class="data-list"></ul>');
		
		
		 var count = 0;
	$(window).scroll(function(){
　　var scrollTop = $(this).scrollTop();
　　var scrollHeight = $(document).height();
　　var windowHeight = $(this).height();
	
　　if(scrollTop + windowHeight == scrollHeight){
　　　　
		

		if(count == 0){
		//延时一秒,模拟联网
         //延时一秒,模拟联网
		 count = count+1;
		 
            setTimeout(function () {   
		
		
				$.ajax({
		                type: 'GET',
		                url: '/a/news.html?type='+T.p('type')+'&beforeId='+ T.p('id'),
		                dataType: 'json',
		                success: function(data){
						
					
					//时间格式转换
					var nowdate = new Date();
						
					for(var i = 0 ; i < data.data.length; i++){

                    var d =  new Date(data.data[i].updateTime);

                    var diff = nowdate.getTime() - d.getTime();

                    if((diff / (1000 * 60 * 60 * 24)) >= 1){


                    var date = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate()

                    data.data[i].updateTime = date;

                    }else if((diff / (1000 * 60 * 60 * 24)) >= 0.2 && (diff / (1000 * 60 * 60 * 24)) < 1)  {
						data.data[i].updateTime = "一天内";
					}
					else{

                        data.data[i].updateTime = "刚刚";
                    }


                }	
						var datalist = data.data;
						for(var i = 0 ; i < datalist.length; i++){

							if(datalist[i].images.length > 2){
							$('#dataList').append('<li class="li1" style="padding-bottom: 12px;margin-top: 10px;" "><a href='+datalist[i].url+' target="_parent" ><div class="titleClass"><span>'+datalist[i].title+'</span></div><div class="img1"><img src='+datalist[i].images[0]+' alt="图1" class="pic"><img src='+datalist[i].images[1]+' alt="图1" class="pic"><img src='+datalist[i].images[2]+' alt="图1" class="pic"></div><div class="beizhu"><span><p style="font-color: #99999">'+datalist[i].source+'</p><p>'+datalist[i].updateTime+'</p></span></div></a></li>');
							}else{
							$('#dataList').append('<li class="li2" ><a href='+datalist[i].url+' target="_parent" style="margin-bottom: 10px;"><div class="left"><img src='+datalist[i].images[0]+' alt="图1"></div><div class="right"><span>'+datalist[i].title+'</span><span><p>'+datalist[i].source+'</p><p>'+datalist[i].updateTime+'</p></span></div></a></li>');
							}

                                                        if(i>0 && (i+1)%2 == 0){
                                                                $('#dataList').append('<div class="ws-zl-dybanner371"></div><script>var ws_dy="zl-dybanner371";var ws_width =320;var ws_height=100;</script><script src="//nads.wuaiso.com/newswap/wap/js/newsasyunion.js"></script>');
                                                        }
						}
						setTimeout(function(){var i = 0;var lastAd = [];do {lastAd = $("#dataList > .ws-" + ws_dy + ":last > iframe");$("#dataList > .ws-" + ws_dy).eq(i++).append(lastAd[0]);}while (lastAd.length>1);lastAd = null;},1000);
						//setTimeout(function(){$("#dataList > .ws-zl-dybanner371").eq(0).append($("#dataList > .ws-zl-dybanner371:last > iframe")[0]);$("#dataList > .ws-zl-dybanner371").eq(1).append($("#dataList > .ws-zl-dybanner371:last > iframe")[0]);},1000);
		                },
						error:function(){
						console.log("失败了");
					}
		            });
		
		
		
			},400)
			
			}    //------

　　}
});
		
		
		
		
		
		
	});	
	