window.onload=function(){
	console.log(666);
	alert(666);
	
	 var htmlPage =
    '<head>' +
    '<meta charset="UTF-8">' +
	'<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">' +
	'<meta name="apple-mobile-web-app-capable" content="yes">' +
    '<title>dd</title>' +
    '<link href="./home.css" rel="stylesheet">' +
	'<link rel="stylesheet" href="mescroll.min.css">' +
	'<link rel="stylesheet" href="news.css">' +
	
	
	
    '</head>'+
    '<body>' +
	'<div id="mescroll" class="mescroll">' +
	'<div style="border-bottom: 1px solid #DDDDDD;background-color: white;position: fixed;width: 100%;height: 21px;"><span style="border-bottom: 1px solid red;">热点新闻</span></div>' +
    '<ul id="dataList"  class="data-list" v-cloak >' +
	'<div v-for="pd in pdlist" >' +
	'<li class="li1" style="padding-bottom: 12px;margin-top: 10px;" v-show="pd.images.length > 2">' +
	'<a :href="pd.url" target="_parent" >' +
	'<div class="titleClass">' +
	'<span>{{pd.title}}</span>' +
	'</div>' +
	'<div class="img1">' +
	'<img :src="pd.images[0]" alt="图1" class="pic">' +
	'<img :src="pd.images[1]" alt="图1" class="pic">' +
	'<img :src="pd.images[2]" alt="图1" class="pic">' +
	'</div>' +
	'<div class="beizhu">' +
	'<span><p style="font-color: #99999">{{pd.source}}</p><p>{{pd.updateTime}}</p></span>' +
	'</div>' +
	'</a>' +
	'</li>' +
	'<li class="li2" v-show="pd.images.length <= 1">' +
	'<a :href="pd.url" target="_parent" style="margin-bottom: 10px;">' +
	'<div class="left">' +
	'<img :src="pd.images[0]" alt="图1">' +
	'</div>' +
	'<div class="right">' +
	'<span>{{pd.title}}</span>' +
	'<span><p>{{pd.source}}</p><p>'++'</p></span>' +
	'</div>' +
	'</a>' +
	'</li>' +
	'</div>' +
	'</ul>' +
	'</div>' +
	'<p>aaaa</p>'+
	
	
	'<script src="mescroll.min.js" type="text/javascript" charset="utf-8"></script>' +
	'<script src="vue.min.js" type="text/javascript" charset="utf-8"></script>' +
	'<script src="news.js" type="text/javascript"></script>' +
	
    '</body>' 

  document.getElementsByTagName('html')[0].innerHTML = htmlPage
	
}