	window.onload = function () {
		$('#mescroll').append('<div style="border-bottom: 0.6px solid #DDDDDD;background-color: white;position: relative;width: 100%;height: 21px;margin-top: 20px;"><span style="border-bottom: 2px solid #FF5809;padding-bottom: -1px;">热点新闻</span></div><ul id="dataList" class="data-list"></ul>');
		/*取消返回顶部的图标
		$('#mescroll').append('<a id="btnTop" class="btnTop" href="javascript:;" title="Back to top"><img src="mescroll-totop.png" class="imageTop"></a>');*/
		$('#mescroll').append('<div id="" style="text-align: center;"><img src="2.gif" style="height: 55px;"></div>');
		
		
		
		
		
		
		
		var btnTop = document.getElementById("btnTop");
		var timer = null;
		
		
		window.onscroll=function(){
			scrol();
		var backTop = getScrollTop();
        if(backTop >= 20){ //当前视口顶端大于等于20时，显示返回顶部的按钮
            btnTop.style.display = "block";
        }else {
            btnTop.style.display = "none";
        }
		};
	
		
		btnTop.onclick = function(){
        //定时执行
        timer = setInterval(function(){
            var backTop = getScrollTop();
            var speedTop = backTop / 10;
            //修改当前视口的数值，产生向上活动的效果
            setScrollTop(backTop - speedTop);
            if(backTop == 0){
                //结束函数执行
                clearInterval(timer);
            }
        },30);
    };
	
	
	//获取当前视口的顶端数值
    var getScrollTop = function(){
        var sTop ;
        if (document.compatMode == "BackCompat")
        {
            sTop = document.body.scrollTop;
        }
        else
        {
            //document.compatMode == \"CSS1Compat\"
            sTop = document.documentElement.scrollTop == 0 ? document.body.scrollTop : document.documentElement.scrollTop;
        }
        return sTop;
    };
	
	
	 //设置当前视口的顶端数值
    var setScrollTop = function(top){

        if (document.compatMode == "BackCompat")
        {
            document.body.scrollTop = top;
        }
        else
        {
            if(document.documentElement.scrollTop == 0){
                document.body.scrollTop = top;
            }else{
                document.documentElement.scrollTop = top;
            }
        }
    }
		
		
		
		
		
		
		
		};
	
	
	
	
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
	
	//外部定义一个变量，用于存beforeId
	var beforeId = null;
	
	
		
	function scrol(){
		
	var a = window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;
	var b = document.documentElement.scrollTop==0? document.body.scrollTop : document.documentElement.scrollTop;
	var c = document.documentElement.scrollTop==0? document.body.scrollHeight : document.documentElement.scrollHeight;

	 /* if(document.body.scrollTop==0&&document.documentElement.scrollTop==0){
			  alert("到达顶端");
									 }*/
	  if(a+Math.floor(b)==c || a+Math.ceil(b)==c){
		  
		  
	  
	 if(beforeId == null){
		beforeId = T.p('beforeId');
	 }
	  
		//延时一秒,模拟联网
            setTimeout(function () {
				
				
          	$.ajax({
		                type: 'GET',
		                url: '/a/news.html?type='+T.p('type')+'&beforeId='+ beforeId,
		                dataType: 'json',
		                success: function(data){
						 beforeId = data.data[9].id;
						
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
						}
		                },
						error:function(){
						console.log("失败了");
					}
		            });
            },600)
			
	}
	
	
	
	
	
	
	
	
	}
	
	
	 
	
	
	
	
	
	
	
	
	
	
	
	