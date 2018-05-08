    $(function () {
    function GetQueryString(name)
    {
        var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if(r!=null)return  unescape(r[2]); return null;
    }

    var user_id=GetQueryString("id");
    var vm = new Vue({
        el:'#rrapp',
        data:{
            showList: true,
            title: null,
            user: {},
            withdrawalsRecord:{},
            taskrecord:{},
            applog:{},
            lastLocation: "未获取",
            userGold: {},
            mapInfo:{},
            mymaster:{},
            withdrawalsSum: "获取中",
            withdrawalsTodaySum:"获取中",
            inviteTotal:"获取中",
            todayInviteCount:"获取中",
            lastAppLog:{},
            equipmentInfo:{},
            taskInfo:{
                read:{},
                video:{},
                sign:{},
                time:{},
                invitation:{},
                readings:{},
                other:{}
            }
        },
        created: function () {
            //获取用户信息
            $.get(baseURL + "admin/user/info/"+user_id ,function(r){
                if(r.user.birthday){
                    var data=r.user.birthday.split(" ");//格式化生日
                    r.user.birthday=data[0].toString();
                }
                if(!r.user.avatar){
                    r.user.avatar='/img/avatar.png'
                }

                if(!r.user.weixinPayAvatar){
                    r.user.weixinPayAvatar='/img/avatar.png'
                }
                vm.user = r.user;
            });
            //获取提现记录
            $.ajax({
                type: "GET",
                url: baseURL + "admin/withdrawals/list",
                contentType: "application/json",
                data: {
                    user_id:user_id,
                    limit:10,
                    page:1,
                    order:'asc',
                    sidx:'',
                    offset:0
                },
                success: function(r){
                    if(r.code === OK){
                        vm.withdrawalsRecord=r.page.list;
                    }
                }
            });


            $.ajax({
                type: "GET",
                url: baseURL + "admin/taskrecord/info",
                contentType: "application/x-www-form-urlencoded;",
                data: {
                    'id':user_id
                },
                traditional: true,
                success: function (r) {
                    if (r.code === OK) {
                       vm.taskInfo.read= r.data.read;
                       vm.taskInfo.video=r.data.video;
                       vm.taskInfo.sign=r.data.sign;
                       vm.taskInfo.time=r.data.time;
                       vm.taskInfo.invitation=r.data.invitation;
                       vm.taskInfo.readings=r.data.readings;
                       vm.taskInfo.other=r.data.other;
                    }
                }
            });

            $.ajax({
                type: "GET",
                url: baseURL + "admin/applog/list",
                contentType: "application/json",
                data: {
                    user:user_id,
                    limit:1,
                    page:1,
                    order:'desc',
                    sidx:'create_time',
                    offset:0
                },
                success: function(r){
                    if(r.code === OK){
                        vm.lastAppLog=r.page.list[0];
                        vm.getLastLocation(vm.lastAppLog.ip);
                    }
                }
            });

            //获取金币情况
            $.ajax({
                type: "GET",
                url: baseURL + "admin/gold/info/" + user_id,
                contentType: "application/json",
                data: {},
                success: function(r){
                    if(r.code === OK){
                        vm.userGold = r.gold;
                    }
                }
            });
            //获取累计提现
            $.ajax({
                type: "GET",
                url: baseURL + "admin/withdrawals/sumByUser/" + user_id,
                contentType: "application/json",
                data: {},
                success: function(r){
                    if(r.code === OK){
                        vm.withdrawalsSum = r.sum / 100 + "元";
                        vm.withdrawalsTodaySum = r.todaysum / 100 + "元";
                    }
                }
            });
            //获取设备信息
            $.ajax({
                type: "GET",
                url: baseURL + "admin/utils/getDeviceInfoByUserId",
                contentType: "application/json",
                data: {
                    "userId":user_id
                },
                success: function(r){
                    if(r.code === OK && r.devInfo){
                        //赋值前，在上面vue中先定义
                        vm.mapInfo = r.mapInfo;
                        if(!vm.mapInfo[0].Root){
                            vm.mapInfo.root=vm.mapInfo[0].SPLIT_FAIL_1.indexOf("Root=true");
                        }
                    }
                }
            });

            //获取我的师傅信息
            $.ajax({
                type: "POST",
                url: baseURL + "invite/inviterecord/mymaster",
                contentType: "application/x-www-form-urlencoded",
                data: {apprentice:user_id},
                success: function(r){
                    if(r.code === OK){
                        vm.mymaster = r.mymaster;
                        vm.todayInviteCount = r.todayInviteCount;
                        vm.inviteTotal = r.inviteTotal;
                    }
                }
            });

            SummaryOneDay();
        },
        methods: {
            deviceInfo:function (index) {
                vm.equipmentInfo=vm.mapInfo[index];
                layer.open({
                    type:1,
                    area: ['400px', '600px'],
                    title:'设备信息',
                    content:$("#mapInfo")
                });
            },
            getLastLocation: function (ip) {
                $.ajax({
                    url: baseURL + "admin/utils/ipToAddress?ip=" + ip,
                    type: "get",
                    success: function (data) {
                        if(data.code == OK){
                            if(data.type==2){
                                if(data.data.code==0){

                                    vm.lastLocation =data.data.data.country+","+data.data.data.region+","+data.data.data.city+","+
                                        (data.data.data.county=="XX"?"":data.data.data.county+",")+data.data.data.isp
                                }else{
                                    vm.lastLocation="获取失败"
                                }
                            }else{
                                if(data.data.showapi_res_code == 0){
                                    vm.lastLocation = data.data.showapi_res_body.country + "," +
                                        data.data.showapi_res_body.region + "," +
                                        data.data.showapi_res_body.city + "," +
                                        data.data.showapi_res_body.county + "," +
                                        data.data.showapi_res_body.isp;
                                }else{
                                    vm.lastLocation = data.data.showapi_res_error;
                                }
                            }
                        }else {
                            vm.lastLocation = "未获取";
                        }
                    }
                });
            },
            reload: function (event) {
                vm.showList = true;
            }
        }
    });
        $("#withdrawalsJqGrid").jqGrid({
            url:  baseURL + 'admin/withdrawals/list',
            datatype: "json",
            colModel: [
                { label: 'ID', name: 'id', index: 'id', width: 90, key: true },
                { label: '金额', name: 'rmb', index: 'rmb', width: 45,formatter: function(value, options, row){
                    return value/100;
                } },
                { label: '状态', name: 'status', index: 'status', width: 50 ,formatter: function(value, options, row){
                    if(value == "1"){
                        return '<span class="label label-primary">处理中</span>';
                    }else if(value == "2"){
                        return '<span class="label label-success">已完成</span>';
                    }else if(value == "-1"){
                        return '<span class="label label-danger">已关闭</span>';
                    }
                }},
                { label: '渠道', name: 'channel', index: 'channel', width: 50 ,formatter: function(value, options, row){
                    if('alipay'==value){
                        return '支付宝';
                    } else if('weixinPay' == value){
                        return '微信';
                    }
                    return value;
                }},
                { label: '账户', name: 'userAccount', index: 'user_account', width: 80 },
                { label: '姓名', name: 'name', index: 'name', width: 50 },
                { label: '手机号', name: 'phone', index: 'phone', width: 60 },
                { label: '剩余金币', name: 'banlance', index: 'banlance', width: 85 },
                { label: '商户支付单号', name: 'payNo', index: 'pay_no', width: 100 },
                { label: '下单', name: 'createTime', index: 'create_time', width: 50, formatter: function(value, options, row){
                    return value?value.substring(5,16):'';
                }},
                { label: '完成', name: 'finishTime', index: 'finish_time', width: 50, formatter: function(value, options, row){
                    return value?value.substring(5,16):'';
                }},
                { label: '关闭', name: 'closeTime', index: 'close_time', width: 56, formatter: function(value, options, row){
                    return value?value.substring(5,16):'';
                }},
            ],
            viewrecords: true,
            height: 270,
            rowNum: 10,
            rowList : [10,20,50],
            rownumbers: false,
            rownumWidth: 25,
            autowidth:true,
            multiselect: false,
            pager: "#withdrawalsJqGridPager",
            jsonReader : {
                root: "page.list",
                page: "page.currPage",
                total: "page.totalPage",
                records: "page.totalCount"
            },
            prmNames : {
                page:"page",
                rows:"limit",
                order: "order"
            },
            postData:{
                "user_id":user_id
            },
            gridComplete:function(){
                //隐藏grid底部滚动条
                $("#withdrawalsJqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }).closest(".ui-jqgrid-bdiv").css({ 'overflow-y' : 'scroll' });
            }
        });

        $("#apprenticeJqGrid").jqGrid({
            url: baseURL + 'invite/inviterecord/list',
            datatype: "json",
            colModel: [
                { label: '徒弟ID', name: 'apprentice', index: 'apprentice', width: 72, key: true  ,formatter: function(value, options, row){
                    return '<a href="javaScript:parent.showUserDetail(' + value + ')">' + value + '</a>';
                }},
                { label: '奖励规则', name: 'rewardRule', index: 'reward_rule', width: 72 },
                { label: '收益', name: 'profit', index: 'profit', width: 72 },
                { label: '阅读提成', name: 'readProfit', index: 'read_profit', width: 72 },
                { label: '已发放', name: 'gave', index: 'gave', width: 72 },
                { label: '待发放', name: 'waitGive', index: 'wait_give', width: 72 },
                { label: '下次奖励', name: 'nextReward', index: 'next_reward', width: 72 },
                { label: '上次奖励', name: 'lastRewardDate', index: 'last_reward_date', width: 72 },
                { label: '邀请码', name: 'inviteCode', index: 'invite_code', width: 72 },
            ],
            viewrecords: true,
            height: 270,
            rowNum: 10,
            rowList : [10,20,50],
            rownumbers: false,
            rownumWidth: 25,
            autowidth:true,
            multiselect: false,
            pager: "#apprenticeJqGridPager",
            jsonReader : {
                root: "page.list",
                page: "page.currPage",
                total: "page.totalPage",
                records: "page.totalCount"
            },
            prmNames : {
                page:"page",
                rows:"limit",
                order: "order"
            },postData:{
                "master":user_id,
            },
            gridComplete:function(){
                //隐藏grid底部滚动条
                $("#apprenticeJqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }).closest(".ui-jqgrid-bdiv").css({ 'overflow-y' : 'scroll' });
            }
        });

        function echartsInit(seriesData){
            echarts.init(document.getElementById("taskEcharts")).setOption({
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data:["金币"]
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
                    name:"时段",
                    data: ['0','1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','19','20','21','22','23']
                },
                yAxis: {
                    type: 'value',
                    name:"金币数"
                },
                series:[{
                    name:'金币',
                    type:'line',
                    data:seriesData
                }]
            });
        }


    // (function ($) {
    //     $('.tab ul.tabs').addClass('active').find('> li:eq(0)').addClass('current');
    //
    //     $('.tab ul.tabs li a').click(function (g) {
    //         var tab = $(this).closest('.tab'),
    //             index = $(this).closest('li').index();
    //
    //         tab.find('ul.tabs > li').removeClass('current');
    //         $(this).closest('li').addClass('current');
    //
    //         tab.find('.tab_content').find('div.tabs_item').not('div.tabs_item:eq(' + index + ')').slideUp();
    //         tab.find('.tab_content').find('div.tabs_item:eq(' + index + ')').slideDown();
    //
    //         g.preventDefault();
    //     });
    // })(jQuery);

        function SummaryOneDay(time) {
            //获取任务24小时汇总
            $.ajax({
                type: "GET",
                url: baseURL + "admin/taskrecord/SummaryOneDay",
                contentType: "application/x-www-form-urlencoded",
                data: {
                    "user_id":user_id,
                    "time":time
                },
                success: function(r){
                    if(r.code === OK){
                        echartsInit(r.list);
                    }
                }
            });
        }

    laydate.render({
        elem: '#time'
        ,trigger: 'click'
        ,btns: ['confirm','now']
        ,format: 'yyyy-MM-dd HH:mm:ss'
        ,done: function (value, date, endDate) {
            SummaryOneDay(value);
        }
    });
});
