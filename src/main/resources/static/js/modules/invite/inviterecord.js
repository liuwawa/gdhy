$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'invite/inviterecord/list',
        datatype: "json",
        colModel: [			
			{ label: '师傅ID', name: 'master', index: 'master', width: 50},
			{ label: '徒弟ID', name: 'apprentice', index: 'apprentice', width: 72, key: true  },
			{ label: '收徒时间', name: 'createTime', index: 'create_time', width: 72 },
			{ label: '收益', name: 'profit', index: 'profit', width: 72 },
            { label: '阅读收益', name: 'readProfit', index: 'read_profit', width: 72 },
            { label: '已发放', name: 'gave', index: 'gave', width: 72 },
			{ label: '待发放', name: 'waitGive', index: 'wait_give', width: 72 },
			{ label: '描述', name: 'title', index: 'title', width: 72 },
			{ label: '邀请码', name: 'inviteCode', index: 'invite_code', width: 72 },
			{ label: '奖励规则', name: 'rewardRule', index: 'reward_rule', width: 72 },
			{ label: '下次奖励', name: 'nextReward', index: 'next_reward', width: 72 },
			{ label: '上次奖励', name: 'lastRewardDate', index: 'last_reward_date', width: 72 },
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
            apprentice: '',
            inviteCode: ''
		},
		inviteRecord: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		del: function (event) {
			var apprentices = getSelectedRows();
			if(apprentices == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "invite/inviterecord/delete",
                    contentType: "application/json",
				    data: JSON.stringify(apprentices),
				    success: function(r){
						if(r.code == OK){
							alert('操作成功', function(index){
								$("#jqGrid").trigger("reloadGrid");
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		getInfo: function(apprentice){
			$.get(baseURL + "invite/inviterecord/info/"+apprentice, function(r){
                vm.inviteRecord = r.inviteRecord;
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