$(function () {
      alert(666);
    });

    new Vue({
        el:'#app',
        data: {
            shopListArr: [],
            totalMoney: '',
			commentList: [],
			nowdate: '',
			aid: ''

        },
        //组件已经加载完成，请求网络数据，业务处理
        mounted(){
			this.getAid();
            this.getLocalData(this.aid);
			
        },
        //过滤
        filters: {
            //格式化金钱
            dateFormat(date){
				
				var k = this.getNowDate(date);
				
               
            }
        },
        methods: {
            //1.请求网络数据，
            getLocalData(aid){
                this.$http.get("http://localhost:9080/renren-fast/app/comment/findCommAndFabulous?aid="+aid).then(response => {
                    
					var res = response.data;
					var nowdate = new Date();
					
					if(res){
						this.commentList = res.commentAndFabulous.commentList;
						this.totalMoney = res.commentAndFabulous.commentTotal;
						console.log(this.commentList[0].aid);
					}
					
					for(var i = 0 ; i < this.commentList.length; i++){
						
						var d =  new Date(this.commentList[i].commentdate);
						
						var diff = nowdate.getTime() - d.getTime();
						
						if((diff / (1000 * 60 * 60 * 24)) >=2){
							
							
							var date = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate()
							
							this.commentList[i].commentdate = date;
							
						}else if((diff / (1000 * 60 * 60 * 24))>=1 && (diff / (1000 * 60 * 60 * 24))<2){
							
							this.commentList[i].commentdate = "一天前";
			
						}else{
			  
							this.commentList[i].commentdate = "刚刚";
						}
						
						console.log(this.commentList[i].commentdate);
					}
					
					
					

            },response => {
                    alert(666);
                });
            },
			getAid(){
				
				var id = window.location.search;
				var id1 = id.substring(5,id.length);
				alert(id1);
				this.aid = id1;
			}
        }
    });