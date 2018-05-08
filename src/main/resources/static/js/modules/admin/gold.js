$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'admin/gold/list',
        datatype: "json",
        colModel: [			
			{ label: '用户ID', name: 'id', index: 'id', width: 50, key: true ,formatter: function (value, options, row) {
                return '<a href="javascript:showUserDetail(' + value + ')">' + value + '</a>';
            }},
			{ label: '今日金币', name: 'today', index: 'today', width: 50 },
			{ label: '剩余金币', name: 'surplus', index: 'surplus', width: 50 },
			{ label: '总金币', name: 'total', index: 'total', width: 50 },
			{ label: '最后一次兑换', name: 'lastExchange', index: 'last_exchange', width: 50 },
			{ label: '最后一次任务', name: 'lastTask', index: 'last_task', width: 50 },
            { label: '今日阅读金币', name: 'todayRead', index: 'today_read', width: 50 },
            { label: '今日视频金币', name: 'todayVideo', index: 'today_video', width: 50 },
            { label: '昨日阅读金币', name: 'yesterdayRead', index: 'yesterday_read', width: 50 },
            { label: '昨日视频金币', name: 'yesterdayVideo', index: 'yesterday_video', width: 50 },
        ],
		viewrecords: true,
        height: $(window).height() - 80,
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
		q: {},
		gold: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		getInfo: function(id){
			$.get(baseURL + "admin/gold/info/"+id, function(r){
                vm.gold = r.gold;
            });
		},
        modify: function (event) {
            var userId = getSelectedRow();
            if(userId == null){
                return ;
            }
            layer.prompt({title: '增加 / 扣除 用户金币', formType: 0,maxlength: 8, scrollbar: false}, function(increase, index){
                if(isNaN(increase) || increase == 0){
                    return;
                }
                layer.prompt({title: '请输入理由', formType: 2, maxlength: 140,scrollbar: false,value: (increase > 0?'系统奖励':'系统扣除') + Math.abs(increase) + '金币'}, function(tip, index){
                    layer.closeAll();
                    $.ajax({
                        type: "POST",
                        url: baseURL + "admin/gold/modify/" + userId,
                        contentType: "application/x-www-form-urlencoded",
                        data: {"increase":increase,"tip":tip},
                        success: function(r){
                            if(r.code === OK){
                                layer.alert("操作成功，用户ID：" + userId + "<br/>" + tip,function(index){
                                    layer.close(index);
                                    vm.reload();
                                });
                            }else{
                                alert(r.msg);
                            }
                        }
                    });
                });
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