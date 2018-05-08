$(function () {
    initStatsDataByDay("DAY",fastCondition("nearlyseven"),"task_stats_day");
    initStatsDataByDay("MONTH",fastCondition("MONTH"),"task_stats_month");
    initStatsDataByDay("YEAR",fastCondition("YEAR"),"task_stats_year");
    // initStatsDataByDay("HOUR",fastCondition("HOUR"),"task_stats_hour");
});


function initStatsDataByDay(type,dateRange,idName){
    var myChart = echarts.init(document.getElementById(idName));
    $.ajax({
        type: "POST",
        url: baseURL + "stats/taskstats/range",
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
				var legendData=[];

				var temp={};
            	for(var i=0;i<list.length;i++){
                    if(type=="HOUR"){
                        xAxisData.push(list[i].thanDay.split(" ")[0]+" "+list[i].thanHour+"时");
                    }else if(type=="MONTH"){
                        xAxisData.push(list[i].thanYear+" "+list[i].thanMonth+"月");
                    }else if(type=="YEAR"){
                        xAxisData.push(list[i].thanYear)+"年";
                    }else{
                        xAxisData.push(list[i].thanDay.split(" ")[0]);
                    }

                    legendData.push("任务："+list[i].taskId);
                    compairsonData.push(list[i].compairson);//同比增长
                    totalData.push(list[i].total);//总计数量

                    try {
                        temp["任务："+list[i].taskId].push(list[i].total);
                    }catch (e){
                        console.error(e);
                        temp["任务："+list[i].taskId]=[list[i].total];
                    }





            	}

            	var seriesData=[];

                for (var i=0;i<legendData.length;i++){
                    seriesData.push({
                        name:legendData[i],
                        type:'line',
                        data:temp[legendData[i]]
                        });
                }
                title="用户增长情况"+(type=="HOUR"?"(按时)":(type=="MONTH"?"(按月)":(type=="YEAR"?"(按年)":"(按天)")));
                initEcharts(title,myChart,legendData,xAxisData,seriesData);

            }else{
                alert(r.msg);
            }
        }
    });
}

function initEcharts(title,myChart,legendData,xAxisData,seriesData,data){
    option = {
        title: {
            text: title,
            left:100
        },
        tooltip: {
            formatter:function (data) {
                
            },
            trigger: 'axis'
        },
        legend: {
            data:legendData
        },
        grid: {
            // left: '3%',
            // right: '4%',
            // bottom: '3%',
            containLabel: true
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: xAxisData
        },
        yAxis: {
            type: 'value'
        },
        series:seriesData
    };
    myChart.setOption(option);
}

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
// laydate.render({
//     type:'datetime',
//     elem: '#task_stats_hour_date'
//     ,range: true
//     ,trigger: 'click'
//     ,done: function (value, date, endDate) {
//         initStatsDataByDay("HOUR",value,"task_stats_hour");
//     }
// });
laydate.render({
    type:'month',
    elem: '#task_stats_month_date'
    ,range: true
    ,trigger: 'click'
    ,done: function (value, date, endDate) {
        initStatsDataByDay("MONTH",date.year+"-"+date.month +"-01 - "+endDate.year+"-"+endDate.month +"-01","task_stats_month");
    }
});

laydate.render({
    type:'year',
    elem: '#task_stats_year_date'
    ,range: true
    ,trigger: 'click'
    ,done: function (value, date, endDate){
        initStatsDataByDay("YEAR",value,"task_stats_year");
    }
});

