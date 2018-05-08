$(function () {
    initStatsDataByDay("DAY",fastCondition("nearlyseven"),"user_stats_day");
    initStatsDataByDay("HOUR",fastCondition("HOUR"),"user_stats_hour");
    initStatsDataByDay("MONTH",fastCondition("MONTH"),"user_stats_month");
    // initStatsDataByDay("YEAR",fastCondition("YEAR"),"user_stats_year");
});

function initStatsDataByDay(type,dateRange,idName){
    var myChart = echarts.init(document.getElementById(idName));
    $.ajax({
        type: "POST",
        url: baseURL + "stats/userstats/range",
        data: {
			type:type,
            dateRange:dateRange
		},
        success: function(r){
            if(r.code === 200){
            	var xAxisData=[];
            	var list=r.list;
                var totalData=[];
				var increaseData=[];
				var compairsonData=[];
				var title;
				var legendData=['总计数量','增长数量','同比增长','邀请码注册'];
				var invitationsNum=[];
            	for(var i=0;i<list.length;i++){
            		if(type=="HOUR"){
                        xAxisData.push(list[i].thanDay.split(" ")[0]+" "+list[i].thanHour+"时");
					}else if(type=="MONTH"){
                        xAxisData.push(list[i].thanYear+"年 "+list[i].thanMonth+"月");
                    } else if(type=="YEAR"){
                        xAxisData.push(list[i].thanYear)+"年";
                    }else{
                        xAxisData.push(list[i].thanDay.split(" ")[0]);
                    }
                    invitationsNum.push(list[i].invitationsNum);
                    totalData.push(list[i].total);//总计数量
                    increaseData.push(list[i].increase);//增长数量
                    compairsonData.push(list[i].compairson);//同比增长
				}
                var seriesData=[{
                        name:'总计数量',
                        type:'line',
                        data:totalData
                    },
                    {
                        name:'增长数量',
                        type:'line',
                        data:increaseData
                    },
                    {
                        name:'同比增长',
                        type:'line',
                        data:compairsonData
                    },{
                        name:'邀请码注册',
                        type:'line',
                        data:invitationsNum
                    }];

                title="用户增长情况"+(type=="HOUR"?"(按时)":(type=="MONTH"?"(按月)":(type=="YEAR"?"(按年)":"(按天)")));
                initEcharts(title,myChart,legendData,xAxisData,seriesData);
            }else{
                alert(r.msg);
            }
        }
    });

}

laydate.render({
    // type:'datetime',
    elem: '#stats_user_date_datetime'
    ,range: true
    ,format: 'yyyy-MM-dd'
    ,trigger: 'click'
    ,done: function (value, date, endDate) {
        initStatsDataByDay("DAY",value,"user_stats_day");
    }
});
laydate.render({
    type:'datetime',
    elem: '#stats_user_hour_datetime'
    ,range: true
    ,trigger: 'click'
    ,done: function (value, date, endDate) {
        initStatsDataByDay("HOUR",value,"user_stats_hour");
    }
});
laydate.render({
    type:'month',
    elem: '#stats_user_month_datetime'
    ,range: true
    // , format: 'yyyy-MM-dd'
    ,trigger: 'click'
    ,done: function (value, date, endDate) {
        initStatsDataByDay("MONTH",date.year+"-"+date.month +"-01 - "+endDate.year+"-"+endDate.month +"-01","user_stats_month");
    }
});
// laydate.render({
//     type:'year',
//     elem: '#stats_user_year_datetime'
//     ,range: true
//     ,trigger: 'click'
//     ,done: function (value, date, endDate) {
//         initStatsDataByDay("YEAR", value,"user_stats_year");
//     }
// });