
	
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
		
	
		//创建vue对象
		var vm = new Vue({
			el: "#commentlist",
			data: {
				mescroll: null,
				pdlist: [],
				p: {
					bid: null
				},
				msg: 'ceshi'
				
			},
			mounted: function() {
				//创建MeScroll对象,down可以不用配置,因为内部已默认开启下拉刷新,重置列表数据为第一页
				//解析: 下拉回调默认调用mescroll.resetUpScroll(); 而resetUpScroll会将page.num=1,再执行up.callback,从而实现刷新列表数据为第一页;
				var self = this;
				self.mescroll = new MeScroll("mescroll", { //请至少在vue的mounted生命周期初始化mescroll,以确保您配置的id能够被找到
					up: {
						callback: self.upCallback, //上拉回调
						//以下参数可删除,不配置
						isBounce: false, //此处禁止ios回弹,解析(务必认真阅读,特别是最后一点): http://www.mescroll.com/qa.html#q10
						toTop:{ //配置回到顶部按钮
							src : "mescroll-totop.png", //默认滚动到1000px显示,可配置offset修改
						},
						empty:{ //配置列表无任何数据的提示
							warpId:"dataList",
							icon : "mescroll-empty.png" , 
						},
						
					},
					down: {
					use: false
				}}
				);
					
				
				
				
			},
			
			methods: {
				//上拉回调 page = {num:1, size:10}; num:当前页 ,默认从1开始; size:每页数据条数,默认10
				upCallback: function(page) {
				
					
					//联网加载数据
					var self = this;
					getListDataFromNet(page.num, page.size, function(curPageData) {
						//curPageData=[]; //打开本行注释,可演示列表无任何数据empty的配置
						
						//如果是第一页需手动制空列表 (代替clearId和clearEmptyId的配置)
						if(page.num == 1) self.pdlist = [];
						
						//更新列表数据
						self.pdlist = self.pdlist.concat(curPageData);
						
						self.mescroll.endSuccess(curPageData.length);
					
					}, function() {
						//联网失败的回调,隐藏下拉刷新和上拉加载的状态;
						self.mescroll.endErr();
					});
				},
			},
		});
		
		/*联网加载列表数据
		 请忽略getListDataFromNet的逻辑,这里仅仅是在本地模拟分页数据,本地演示用
		 实际项目以您服务器接口返回的数据为准,无需本地处理分页.
		 * */
		function getListDataFromNet(pageNum,pageSize,successCallback,errorCallback) {
		
		
			var aid = T.p('aid');
				
				
		
			
			//延时一秒,模拟联网
            setTimeout(function () {
					
          	$.ajax({
		                type: 'POST',
		                url: 'http://192.168.1.202:8080/api/comment/findCommAndFabulous',
						data: {'aid':aid},
		                dataType: 'json',
		                success: function(data){
						
						console.log(data.commentAndFabulous.commentList);
						
					var nowdate = new Date();
						
						for(var i = 0 ; i < data.commentAndFabulous.commentList; i++){

                    var d =  new Date(data.commentAndFabulous.commentList[i].commentdate);

                    var diff = nowdate.getTime() - d.getTime();

                    if((diff / (1000 * 60 * 60 * 24)) >= 1){


                        var date = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate()

                        data.commentAndFabulous.commentList[i].commentdate = date;

                    }else if((diff / (1000 * 60 * 60 * 24)) >= 0.2 && (diff / (1000 * 60 * 60 * 24)) < 1)  {
						data.commentAndFabulous.commentList[i].commentdate = "一天内";
					}
					else{

                        data.commentAndFabulous.commentList[i].commentdate = "刚刚";
                    }


                }
		                successCallback(data.commentAndFabulous.commentList);
		                },
						error:function(){
					}
		            });
            },700)
		}