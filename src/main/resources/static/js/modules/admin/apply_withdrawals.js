$(function () {

    $("#jqGrid").jqGrid({
        url: baseURL + 'admin/withdrawals/list',
        datatype: "json",
        postData:{'status':1},
        colModel: [
            { label: 'ID', name: 'id', index: 'id', width: 60, key: true },
            { label: '用户ID', name: 'userId', index: 'user_id', width: 30 ,formatter: function (value, options, row) {
                return '<a href="javascript:showUserDetail(' + value + ')">' + value + '</a>';
            }},
            { label: '金币', name: 'gold', index: 'gold', width: 35 },
            { label: '金额', name: 'rmb', index: 'rmb', width: 25 ,formatter: function(value, options, row){
                return value / 100;
            }},
            { label: '累计提现', name: 'totalRmb', index: 'total_rmb', width: 25 ,formatter: function(value, options, row){
                return value / 100;
            }},
            { label: '状态', name: 'status', index: 'status', width: 30 ,formatter: function(value, options, row){
                if(value == "1"){
                    return '<span class="label label-primary">处理中</span>';
                }else if(value == "2"){
                    return '<span class="label label-success">已完成</span>';
                }else if(value == "-1"){
                    return '<span class="label label-danger">已关闭</span>';
                }
            }},
            { label: '收款渠道', name: 'channel', index: 'channel', width: 30 ,formatter: function(value, options, row){
                if('alipay'==value){
                    return '支付宝';
                } else if('weixinPay' == value){
                    return '微信';
                }
                return value;
            }},
            { label: '收款账户', name: 'userAccount', index: 'user_account', width: 70 },
            { label: '姓名', name: 'name', index: 'name', width: 30 },
            { label: '手机号', name: 'phone', index: 'phone', width: 50 },
            { label: '剩余金币', name: 'banlance', index: 'banlance', width: 40 },
            { label: '下单时间', name: 'createTime', index: 'create_time', width: 60}
        ],
		viewrecords: true,
        height: $(window).height() - 130,
        rowNum: 30,
		rowList : [50,100,200],
        rownumbers: false,
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
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
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});
var running = false;
var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		q: {},
        successCount:0,
		withdrawals: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
        appendInfo:function (info) {
            $("#info-area").append('<p>' + info + ' ...</p>');
            setTimeout(function () {
                $('#info-area').scrollTop($('#info-area')[0].scrollHeight);
            },100);
        },
        appendWarn:function (info) {
            $("#info-area").append('<p class="text-warning">' + info + ' ...</p>');
            setTimeout(function () {
                $('#info-area').scrollTop($('#info-area')[0].scrollHeight);
            },100);
        },
        appendErr:function (info) {
            $("#info-area").append('<p class="text-danger">' + info + ' ...</p>');
            setTimeout(function () {
                $('#info-area').scrollTop($('#info-area')[0].scrollHeight);
            },100);
        },
        /**
         * 关闭一条提现
         * @param id
         * @param callback
         */
        closeWithdrawals:function (id,callback) {
            //询问关闭原因
            layer.prompt({title: '请输入关闭原因', formType: 2,area: [450,300],closeBtn: 0,scrollbar: false,cancel:function () {
                callback("CANCEL")
            }}, function(val, index){
                layer.close(index);
                var loading = layer.load(1, {
                    scrollbar: false,
                    shade: [0.1,'#fff'] //0.1透明度的白色背景
                });
                $.ajax({
                    type: "POST",
                    url: baseURL + "admin/withdrawals/close",
                    contentType: "application/x-www-form-urlencoded",
                    data: {oid: id,closeMsg:val},
                    success: function (r) {
                        layer.close(loading);
                        if(r.code == OK){
                            callback("OK");
                        }else{
                            callback(r.msg);
                        }
                    },
                    error: function (xhr) {
                        callback("网络错误");
                    }
                });
            });
        },

        /**
         * 提现
         * @param current
         * @param ids
         */
        doWithdrawals: function (current, ids) {
            if(current == ids.length){
                //操作完成 显示关闭按钮
                $("#info-progress").width( "100%");
                $("#info-progress").removeClass("active");
                vm.appendInfo("操作已完成");
                vm.reload();
                running = false;
                return;
            }else if(current > ids.length){
                $("#info-progress").removeClass("active");
                vm.appendInfo("操作已终止");
                running = false;
                return;
            }else{
                $("#info-progress").removeClass("active");
                $("#info-progress").width( current / ids.length * 100 + "%");
            }

            vm.appendInfo("[" + ids[current] + "] 开始转账");
            $.ajax({
                type: "POST",
                url: baseURL + "admin/withdrawals/allow",
                contentType: "application/x-www-form-urlencoded",
                data: {id: ids[current]},
                success: function (r) {

                    if (r.code === OK) {
                        vm.successCount = vm.successCount + 1;
                        vm.appendInfo("[" + ids[current] + "] 转账成功");
                        vm.doWithdrawals(++current,ids);
                        return;
                    }

                    //记录错误
                    if(r.code == 521){
                        //非中断性错误，跳过此单
                        vm.appendWarn("[" + ids[current] + "] " + r.msg);
                        vm.doWithdrawals(++current,ids);
                        return;
                    }
                    vm.appendErr("[" + ids[current] + "] " + r.msg);

                    //致命错误
                    layer.confirm("[" + ids[current] + "] " + r.msg, {
                        closeBtn: 0,
                        scrollbar: false,
                        btn: ['重试','跳过','关闭此提现','取消'],
                        btn1: function(index){
                            //重试
                            layer.close(index);
                            vm.appendInfo("[" + ids[current] + "] 选择->重试提现");
                            vm.doWithdrawals(current,ids);
                        },
                        btn2: function(index){
                            //跳过
                            layer.close(index);
                            vm.appendInfo("[" + ids[current] + "] 选择->跳过");
                            vm.doWithdrawals(++current,ids);
                        },
                        btn3: function(index){
                            //关闭此提现
                            vm.appendInfo("[" + ids[current] + "] 选择->关闭提现");
                            vm.closeWithdrawals(ids[current],function (result) {
                                if(result == "OK"){
                                    layer.close(index);
                                    vm.appendInfo("[" + ids[current] + "] 关闭提现成功");
                                    vm.doWithdrawals(++current,ids);
                                }else if(result != "CANCEL") {
                                    alert(result);
                                }
                            });
                            return false;//阻止对话框关闭
                        },
                        btn4: function(index){
                            //取消
                            layer.close(index);
                            vm.appendInfo("选择->终止操作");
                            vm.doWithdrawals(ids.length+1,ids);
                        }
                    });
                }
            });
        },
        allow: function () {
            var ids = getSelectedRows();
            ids = JSON.parse(JSON.stringify(ids));
            if(ids == null){
                return ;
            }

            //准备进度条
            $("#info-progress").width("0");
            $("#info-area").html("");
            vm.successCount = 0;

            var loading = layer.load(1, {
                scrollbar: false,
                shade: [0.1,'#fff'] //0.1透明度的白色背景
            });
            var rmbSum = 0,rmbMax = 0;
            for(var i = 0;i<ids.length;i++){
                var rowData = $("#jqGrid").jqGrid('getRowData',ids[i]);
                if(rowData && rowData.status == '<span class="label label-primary">处理中</span>' && rowData.rmb){
                    var temp = parseInt(rowData.rmb);
                    rmbSum += temp;
                    if(temp > rmbMax) rmbMax = temp;
                }else{
                    ids.splice(i,1);
                    i--;
                }
            }
            layer.close(loading);

            vm.appendInfo("准备就绪 ...<br/>本次提现数量：" + ids.length + "，提现总额：" + rmbSum + "元，最高：" + rmbMax + "元");
            layer.open({
                title: '提现进度',
                closeBtn: 0,
                scrollbar: false,
                type: 1,
                btn: ['开始','关闭'],
                skin: 'layui-layer-rim', //加上边框
                area: ['520px', '340px'], //宽高
                content: $("#info-panel"),
                btn1: function(index){
                    if(ids.length > 0){
                        if(running){
                            alert("转账已经开始，请勿重复点击");
                            return false;
                        }
                        layer.confirm('确定为所选的提现申请付款吗？', {btn: ['确定','取消']}, function(index){
                            layer.close(index);

                            vm.appendInfo("开始提现");
                            running = true;
                            vm.doWithdrawals(0,ids);
                        });
                    }else{
                        vm.appendWarn("没有有效的提现申请，请重新选择");
                    }
                    return false;
                },
                btn2: function(){
                    //检查是否已停止
                    if(running){
                        alert("转账未结束，请勿中断");
                    }
                    return !running;
                }
            });
        },
        refuse:function () {//批量关闭提现
             window.ids = getSelectedRows();
            if(ids == null){
            	return;
        	}
            //询问关闭原因
            layer.prompt({title: '请输入关闭原因,将统一使用该原因关闭所选提现', formType: 2,area: [450,300],closeBtn: 0,scrollbar: false,cancel:function () {
                callback("CANCEL")
            }}, function(val, index){
                layer.close(index);
                var loading = layer.load(1, {
                    scrollbar: false,
                    shade: [0.1,'#fff'] //0.1透明度的白色背景
                });
                $.ajax({
                    type: "POST",
                    url: baseURL + "admin/withdrawals/close",
                    contentType: "application/x-www-form-urlencoded",
                    data: {oid: ids.join(","),closeMsg:val},
                    success: function (r) {
                        layer.close(loading);
                        if(r.code == OK){
                            alert("关闭成功",function () {
                                vm.reload();
                            });
                        }else{
                            alert(r.msg);
                        }
                    },
                    error: function (xhr) {
                        alert("网络错误");
                    }
                });
            });

		},
		getInfo: function(id){
			$.get(baseURL + "admin/withdrawals/info/"+id, function(r){
                vm.withdrawals = r.withdrawals;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                page:page,
                postData: vm.q
            }).trigger("reloadGrid");
		}
	}
});