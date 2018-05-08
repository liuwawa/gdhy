
	
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
		
		
		Vue.component('my1', {
			
			template: '<div><li class="li1" style="padding-bottom: 12px;margin-top: 10px;" v-show="pd.images.length > 2"><a :href="pd.url" target="_parent" ><div class="titleClass"><span>{{pd.title}}</span></div><div class="img1"><img :src="pd.images[0]" alt="图1" class="pic"><img :src="pd.images[1]" alt="图1" class="pic"><img :src="pd.images[2]" alt="图1" class="pic"></div><div class="beizhu"><span><p style="font-color: #99999">{{pd.source}}</p><p>{{pd.updateTime}}</p></span></div></a></li><li class="li2" v-show="pd.images.length <= 1"><a :href="pd.url" target="_parent" style="margin-bottom: 10px;"><div class="left"><img :src="pd.images[0]" alt="图1"></div><div class="right"><span>{{pd.title}}</span><span><p>{{pd.source}}</p><p>{{pd.updateTime}}</p></span></div></a></li></div>',
			props: ['pd']
			
		});
		
		
		

	
		//创建vue对象
		var vm = new Vue({
			el: "#dataList",
			data: {
				mescroll: null,
				pdlist: [],
				p: {
					bid: null
				},
				msg: 'ceshi'
				
			},
			 components: {
                'user': {
                    template:"<li>{{user.name}}  {{user.email}}</li>",
                    props:["user"]
                }
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
		
		
			if(vm.p.bid == null){
				vm.p.bid = T.p('beforeId');
				
				}
		
			
			//延时一秒,模拟联网
            setTimeout(function () {
					console.log(vm.p.bid+"----");
			
          	$.ajax({
		                type: 'GET',
		                url: '/a/news.html?type='+T.p('type')+'&beforeId='+vm.p.bid+'&num='+pageSize,
		                dataType: 'json',
		                success: function(data){
						vm.p.bid = data.data[9].id;
						console.log(vm.p.bid);
						
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
						
		                successCallback(data.data);
		                },
						error:function(){
					}
		            });
            },500)
		}