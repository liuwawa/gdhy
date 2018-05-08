$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'invite/inviterewardrule/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '立即奖励', name: 'firstReward', index: 'first_reward', width: 80 }, 			
			{ label: '第1天', name: 'day1', index: 'day1', width: 80 },
			{ label: '第2天', name: 'day2', index: 'day2', width: 80 },
			{ label: '第3天', name: 'day3', index: 'day3', width: 80 },
			{ label: '第4天', name: 'day4', index: 'day4', width: 80 },
			{ label: '第5天', name: 'day5', index: 'day5', width: 80 },
			{ label: '第6天', name: 'day6', index: 'day6', width: 80 },
			{ label: '第7天', name: 'day7', index: 'day7', width: 80 },
			{ label: '发放条件', name: 'threshold', index: 'threshold', width: 80 }, 			
			{ label: '创建时间', name: 'createTime', index: 'create_time', width: 80 }, 			
			{ label: '规则生效', name: 'effectiveTime', index: 'effective_time', width: 80 }, 			
			{ label: '规则过期', name: 'expiryTime', index: 'expiry_time', width: 80 }			
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

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		q: {},
		inviteRewardRule: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
			$.ajax({
				type: "POST",
			    url: baseURL + "invite/inviterewardrule/save",
                contentType: "application/json",
			    data: JSON.stringify(vm.inviteRewardRule),
			    success: function(r){
			    	if(r.code === OK){
						alert('操作成功', function(index){
							vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		getInfo: function(id){
			$.get(baseURL + "invite/inviterewardrule/info/"+id, function(r){
                vm.inviteRewardRule = r.inviteRewardRule;
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