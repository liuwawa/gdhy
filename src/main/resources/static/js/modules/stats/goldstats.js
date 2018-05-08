$(function (){
    initStatsDataByDay("DAY",fastCondition("nearlyseven"),"gold_stats_day");
    initStatsDataByDay("HOUR",fastCondition("HOUR"),"gold_stats_hour");
    initStatsDataByDay("MONTH",fastCondition("MONTH"),"gold_stats_month");
    // initStatsDataByDay("YEAR",fastCondition("YEAR"),"gold_stats_year");
});

function initStatsDataByDay(type,dateRange,idName){
    var myChart = echarts.init(document.getElementById(idName));
    $.ajax({
        type: "POST",
        url: baseURL + "stats/goldstats/range",
        data: {
			type:type,
            dateRange:dateRange
		},
        success: function(r){
            if(r.code === 200){
            	var xAxisData=[];
            	var list=r.list;
                var totalData=[];
				var compairsonData=[];
				var title;
				var legendData=['总计数量','同比增长'];
            	for(var i=0;i<list.length;i++){
            		if(type=="HOUR"){
                        xAxisData.push(list[i].thanDay.split(" ")[0]+" "+list[i].thanHour+"时");
					}else if(type=="MONTH"){
                        xAxisData.push(list[i].thanYear+" "+list[i].thanMonth+"月");
                    }else if(type=="YEAR"){
                        xAxisData.push(list[i].thanYear)+"年";
                    } else{
                        xAxisData.push(list[i].thanDay.split(" ")[0]);
                    }
                    totalData.push(list[i].total);//总计数量
                    compairsonData.push(list[i].compairson);//同比增长
				}
                var seriesData=[{
                        name:'总计数量',
                        type:'line',
                        data:totalData
                    },{
                        name:'同比增长',
                        type:'line',
                        data:compairsonData
                    }];
                title="金币领取情况"+(type=="HOUR"?"(按时)":(type=="MONTH"?"(按月)":(type=="YEAR"?"(按年)":"(按天)")));
                initEcharts(title,myChart,legendData,xAxisData,seriesData)
            }else{
                alert(r.msg);
            }
        }
    });

}
var vm = new Vue({
	el:'#rrapp',
	data:{
		q: {},
	},
	methods: {
    }
});

laydate.render({
    // type:'datetime',
    format: 'yyyy-MM-dd',
    elem: '#gold_stats_day_date'
    ,range: true
    ,trigger: 'click'
    ,done: function (value, date, endDate) {
        initStatsDataByDay("DAY",value,"gold_stats_day");
    }
});
laydate.render({
    type:'datetime',
    elem: '#gold_stats_hour_date'
    ,range: true
    ,trigger: 'click'
    ,done: function (value, date, endDate) {
        initStatsDataByDay("HOUR",value,"gold_stats_hour");
    }
});
laydate.render({
    type:'month',
    elem: '#gold_stats_month_date'
    ,range: true
    ,trigger: 'click'
    ,done: function (value, date, endDate) {
        initStatsDataByDay("MONTH",date.year+"-"+date.month +"-01 - "+endDate.year+"-"+endDate.month +"-01","gold_stats_month");
    }
});

// laydate.render({
//     type:'year',
//     elem: '#gold_stats_year_date'
//     ,range: true
//     ,trigger: 'click'
//     ,done: function (value, date, endDate) {
//         initStatsDataByDay("YEAR",value,"gold_stats_year");
//     }
// });