$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'invite/invitecode/list',
        datatype: "json",
        colModel: [			
			{ label: '邀请码', name: 'inviteCode', index: 'invite_code', width: 50, key: true },
			{ label: '用户', name: 'userId', index: 'user_id', width: 80 },
			{ label: '状态', name: 'state', index: 'state', width: 80 , formatter: function(value, options, row){
                return value==0?"正常":(value==-1?"禁用":"作废");
            }},
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
			userId:null
		},
		inviteCode: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		enable: function (event) {
			var inviteCodes = getSelectedRows();
			if(inviteCodes == null){
				return ;
			}
			
			confirm('确定要启用选中的邀请码？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "invite/invitecode/enable",
                    contentType: "application/json",
				    data: JSON.stringify(inviteCodes),
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
		disable: function (event) {
			var inviteCodes = getSelectedRows();
			if(inviteCodes == null){
				return ;
			}

			confirm('禁用后该用户不会自动生成新邀请码。确定要禁用选中的邀请码？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "invite/invitecode/disable",
                    contentType: "application/json",
				    data: JSON.stringify(inviteCodes),
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
        abandon: function (event) {
			var inviteCodes = getSelectedRows();
			if(inviteCodes == null){
				return ;
			}

			confirm('作废后用户将重新生成一个邀请码。确定要废弃选中的邀请码？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "invite/invitecode/abandon",
                    contentType: "application/json",
				    data: JSON.stringify(inviteCodes),
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