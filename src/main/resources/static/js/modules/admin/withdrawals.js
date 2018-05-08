$(function () {
    var item = window.localStorage.getItem("parent_param");
    var item2 = window.localStorage.getItem("user_id");
    var data;
    if(item){
        data={"id":item,"user_id":item2};
        window.localStorage.removeItem("parent_param");
        window.localStorage.removeItem("user_id");
    }
    $("#jqGrid").jqGrid({
        url: baseURL + 'admin/withdrawals/list',
        datatype: "json",
        colModel: [			
			{ label: 'ID', name: 'id', index: 'id', width: 90, key: true },
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
			{ label: '收款账户', name: 'userAccount', index: 'user_account', width: 80 },
			{ label: '姓名', name: 'name', index: 'name', width: 30 },
			{ label: '手机号', name: 'phone', index: 'phone', width: 50 },
			{ label: '剩余金币', name: 'banlance', index: 'banlance', width: 40 },
			{ label: '商户支付单号', name: 'payNo', index: 'pay_no', width: 85 },
			{ label: '下单时间', name: 'createTime', index: 'create_time', width: 40, formatter: function(value, options, row){
                return value?value.substring(5,16):'';
            }},
			{ label: '完成时间', name: 'finishTime', index: 'finish_time', width: 40, formatter: function(value, options, row){
                return value?value.substring(5,16):'';
            }},
			{ label: '关闭时间', name: 'closeTime', index: 'close_time', width: 40, formatter: function(value, options, row){
                return value?value.substring(5,16):'';
            }},
			{ label: '关闭原因', name: 'closeMsg', index: 'close_msg', width: 80 }
        ],
		viewrecords: true,
        height: $(window).height() - 130,
        rowNum: 30,
		rowList : [50,100,200],
        rownumbers: false,
        rownumWidth: 25, 
        autowidth:true,
        multiselect: false,
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
        postData:data
        ,
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		q: {
            status:""
        },
		withdrawals: {}
	},
	methods: {
		query: function () {
			vm.reload();
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

function GetQueryString(name){
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}
var id=GetQueryString("showRecordDetailsid");
if(id){
    vm.q={"user_id":id};
    vm.reload();
}