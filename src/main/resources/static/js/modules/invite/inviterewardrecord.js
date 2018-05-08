$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'invite/inviterewardrecord/list',
        datatype: "json",
        colModel: [
			{ label: '师傅ID', name: 'master', index: 'master', width: 50, key: true },
			{ label: '徒弟ID', name: 'apprentice', index: 'apprentice', width: 80 },
			{ label: '标题', name: 'title', index: 'title', width: 80 },
			{ label: '描述', name: 'describe', index: 'describe', width: 80 },
			{ label: '奖励类型', name: 'day', index: 'day', width: 80 , formatter: function(value, options, row){
                return value == -1?'阅读提成':(value==0?"立即奖励":"第" + value + "天");
            }},
			{ label: '金币', name: 'gold', index: 'gold', width: 80 }, 			
			{ label: '奖励规则', name: 'rewardRule', index: 'reward_rule', width: 80 }, 			
			{ label: '创建时间', name: 'createTime', index: 'create_time', width: 80 }			
        ],
		viewrecords: true,
        height: $(window).height() - 130,
        rowNum: 30,
		rowList : [50,100,200],
        rownumbers: true, 
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

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		q: {
            master: '',
            apprentice: ''
		},
		inviteRewardRecord: {}
	},
	methods: {
		query: function () {
			vm.reload();
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