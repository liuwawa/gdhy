
function initEcharts(title,myChart,legendData,xAxisData,seriesData){
    // title='金币领取情况'+title;
    option = {
        title: {
            text: title,
            left:100
        },
        tooltip: {
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


function fastCondition(type) {
    switch (type){
        case "yesterday"://昨天
            return dateUtils.getDate(-1) + " - " + dateUtils.getDate(-1);
        case "today"://today
            return  dateUtils.getDate(0) + " - " + dateUtils.getDate(0);
        case "nearlyseven"://七天
            return dateUtils.getDate(-7)+" 00:00:00" + " - " + dateUtils.getDate(-1)+" 00:00:00";
        case "lastweek"://上周
            return  dateUtils.getMonday("s",-1) + " - " + dateUtils.getMonday("e",-1);
        case "thismonth"://本月
            return  dateUtils.getMonth("s",0) + " - " + dateUtils.getMonth("e",0);
        case "lastmonth"://上月
            return dateUtils.getMonth("s",-1) + " - " + dateUtils.getMonth("e",-1);
        case "HOUR":
            var date = new Date(new Date().setHours(new Date().getHours()-7));
            date.setMinutes(0);
            return date.format("yyyy-MM-dd hh:mm:ss")+
                " - "+new Date().format("yyyy-MM-dd hh:mm:ss");

        case "MONTH":
            var MonthDate=new Date(new Date().setMonth(new Date().getMonth()-7));
            MonthDate.setDate(1);
            MonthDate.setHours(0);
            MonthDate.setMinutes(0);
            MonthDate.setSeconds(0);

            var MonthDate2=new Date();
            MonthDate2.setDate(1);
            MonthDate2.setHours(0);
            MonthDate2.setMinutes(0);
            MonthDate2.setSeconds(0);
            return MonthDate.format("yyyy-MM-dd")+" - "+
                MonthDate2.format("yyyy-MM-dd");
        case "YEAR":
            var now=new Date().format("yyyy-MM-dd hh:mm:ss");
            var yearDate=new Date();
            yearDate.setYear(now.split("-")[0]-7);
            yearDate.setDate(1);
            yearDate.setMonth(0);
            yearDate.setHours(0);
            yearDate.setMinutes(0);
            yearDate.setSeconds(0);
            return yearDate.format("yyyy-MM-dd hh:mm:ss")+" - "+now;
        default:
            return;
    }
}
