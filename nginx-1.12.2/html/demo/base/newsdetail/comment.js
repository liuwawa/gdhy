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
    if(r!=null)return  unescape(r[2]); return null;
};





		Vue.component('comm', {
			
				template: '<div><li><div class="box-top"><div class="user-msg"><div class="head-portrait"><img v-if="comment.headImg" :src="comment.headImg"><img v-else src="https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=3248848127,3443564377&fm=58"></div><div class="msg"><p class="name">{{comment.uname}}</p><p class="time">{{comment.commentdate}}</p></div></div><div class="praise"><a class="icon Hui-iconfont">&#xe697;</a><span>888</span></div></div><div class="box-bottom"><span class="comments">{{comment.comment}}</span></div></li></div>',
				props: ['comment']
			
			});



var vm = new Vue({
    http: {
        headers: {'Content-Type': 'application/x-www-form-urlencoded'}
    },
    el:'#commentList',
    data: {
        totalmoney: '',
        commentlist: [],
        nowdate: '',
        msg: '10',
		cmt: ''

    },
    //组件已经加载完成，请求网络数据，业务处理
    mounted(){
        this.getLocalData();
    },
    //过滤

    methods: {
        //1.请求网络数据，
        getLocalData(){
			
			var aid = T.p('aid');
			
            this.$http.post("http://192.168.1.202:8080/api/comment/findCommAndFabulous","aid=" + T.p('aid')+"&size=3").then(response => {

                var res = response.data;
                var nowdate = new Date();

                if(res){
                    this.commentlist = res.commentAndFabulous.commentList;
                    this.totalmoney = res.commentAndFabulous.commentTotal;
                }


                for(var i = 0 ; i < this.commentlist.length; i++){

                    var d =  new Date(this.commentlist[i].commentdate);

                    var diff = nowdate.getTime() - d.getTime();

                    if((diff / (1000 * 60 * 60 * 24)) >=2){


                        var date = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate()

                        this.commentlist[i].commentdate = date;

                    }else if((diff / (1000 * 60 * 60 * 24))>=1 && (diff / (1000 * 60 * 60 * 24))<2){

                        this.commentlist[i].commentdate = "一天前";

                    }else{

                        this.commentlist[i].commentdate = "刚刚";
                    }


                }


            },response => {

            });
        }
    }
});


