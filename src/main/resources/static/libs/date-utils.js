/**
 * Created by ITMX on 2017/12/2.
 */
(function ($) {
    /**
     *对Date的扩展，将 Date 转化为指定格式的String
     *月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
     *年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
     *例子：
     *(new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
     *(new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
     */
    Date.prototype.format = function (fmt) {
        var o = {
            "M+": this.getMonth() + 1, //月份
            "d+": this.getDate(), //日
            "h+": this.getHours(), //小时
            "m+": this.getMinutes(), //分
            "s+": this.getSeconds(), //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    };

    //dates为数字类型，0代表今日,-1代表昨日，1代表明日，返回yyyy-mm-dd格式字符串，dates不传默认代表今日。
    $.getDate = function(dates) {
        var dd = new Date(new Date().setHours(0, 0, 0, 0));
        var n = dates || 0;
        dd.setDate(dd.getDate() + n);
        return dd.format("yyyy-MM-dd");
    };

    //type为字符串类型，有两种选择，"s"代表开始,"e"代表结束，dates为数字类型，不传或0代表本周，-1代表上周，1代表下周
    $.getMonday = function(type, dates) {
        var now = new Date();
        var nowTime = now.getTime();
        var day = now.getDay();
        var longTime = 24 * 60 * 60 * 1000;
        var n = longTime * 7 * (dates || 0);
        if (type == "s") {
            var dd = nowTime - (day + 49) * longTime + n;
        }
        if (type == "e") {
            var dd = nowTime + (day) * longTime + n;
        }
        dd = new Date(dd);
        return dd.format("yyyy-MM-dd");
    };

    //type为字符串类型，有两种选择，"s"代表开始,"e"代表结束，months为数字类型，不传或0代表本月，-1代表上月，1代表下月
    $.getMonth = function(type, months) {
        var d = new Date();
        var year = d.getFullYear();
        var month = d.getMonth() + 1;
        if (Math.abs(months) > 12) {
            months = months % 12;
        }
        if (months != 0) {
            if (month + months > 12) {
                year++;
                month = (month + months) % 12;
            } else if (month + months < 1) {
                year--;
                month = 12 + month + months;
            } else {
                month = month + months;
            }
        }
        month = month < 10 ? "0" + month: month;
        var date = d.getDate();
        var firstday = year + "-" + month + "-" + "01";
        var lastday = "";
        if (month == "01" || month == "03" || month == "05" || month == "07" || month == "08" || month == "10" || month == "12") {
            lastday = year + "-" + month + "-" + 31;
        } else if (month == "02") {
            if ((year % 4 == 0 && year % 100 != 0) || (year % 100 == 0 && year % 400 == 0)) {
                lastday = year + "-" + month + "-" + 29;
            } else {
                lastday = year + "-" + month + "-" + 28;
            }
        } else {
            lastday = year + "-" + month + "-" + 30;
        }
        var day = "";
        if (type == "s") {
            day = firstday;
        } else {
            day = lastday;
        }
        return day;
    };

    //type为字符串类型，有两种选择，"s"代表开始,"e"代表结束，dates为数字类型，不传或0代表今年，-1代表去年，1代表明年
    $.getYear = function(type, dates) {
        var dd = new Date();
        var n = dates || 0;
        var year = dd.getFullYear() + Number(n);
        if (type == "s") {
            var day = year + "-01-01";
        }
        if (type == "e") {
            var day = year + "-12-31";
        }
        if (!type) {
            var day = year + "-01-01/" + year + "-12-31";
        }
        return day;
    };
})(window.dateUtils = {});