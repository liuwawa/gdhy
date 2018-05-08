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
	
	window.onload = function () {
		$('#mescroll').append('<div style="border-bottom: 0.6px solid #DDDDDD;background-color: white;position: relative;width: 100%;height: 21px;margin-top: 20px;"><span style="border-bottom: 2px solid #FF5809;padding-bottom: -1px;">热点推荐</span></div><ul id="dataList" class="data-list"></ul>');
		
		
		
	
		//异步请求数据显示页面
		$.ajax({
		                type: 'GET',
		                url: '/a/news.html?type='+T.p('type')+'&beforeId='+ T.p('beforeId'),
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
							$('#dataList').append('<li class="li1" ><a href='+datalist[i].url+' target="_parent" ><div class="titleClass"><span>'+datalist[i].title+'</span></div><div class="img1"><img src='+datalist[i].images[0]+' alt="图1" class="pic"><img src='+datalist[i].images[1]+' alt="图1" class="pic"><img src='+datalist[i].images[2]+' alt="图1" class="pic"></div><div class="beizhu"><span><p style="font-color: #99999">'+datalist[i].source+'</p><p>'+datalist[i].updateTime+'</p></span></div></a></li>');
							}else{
							$('#dataList').append('<li class="li2" ><a href='+datalist[i].url+' target="_parent" style="margin-bottom: 10px;"><div class="left"><img src='+datalist[i].images[0]+' alt="图1"></div><div class="right"><span>'+datalist[i].title+'</span><span><p>'+datalist[i].source+'</p><p>'+datalist[i].updateTime+'</p></span></div></a></li>');
							}
							
							
						}
		                },
						error:function(){
						console.log("失败了");
					}
		            });
		};
	

	
	
	 
	
	
	
	
	
	
	
	
	
	
	
	