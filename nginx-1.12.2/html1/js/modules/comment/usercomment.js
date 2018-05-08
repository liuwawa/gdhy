$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'comment/usercomment/list',
        datatype: "json",
        colModel: [			
			{ label: '评论ID', name: 'cid', index: 'cid', width: 50, key: true },
			{ label: '用户ID', name: 'uid', index: 'uid', width: 80 }, 			
			{ label: '文章ID', name: 'aid', index: 'aid', width: 80 }, 			
			{ label: '用户名', name: 'uname', index: 'uname', width: 80 }, 			
			{ label: '头像地址', name: 'headimg', index: 'headimg', width: 80 }, 			
			{ label: '评论', name: 'comment', index: 'comment', width: 80 }, 			
			{ label: '提交评论时间', name: 'commentdate', index: 'commentdate', width: 80 }			
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
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
		usercomment: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.usercomment = {};
		},
		update: function (event) {
			var cid = getSelectedRow();
			if(cid == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(cid)
		},
		saveOrUpdate: function (event) {
			var url = vm.usercomment.cid == null ? "comment/usercomment/save" : "comment/usercomment/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.usercomment),
			    success: function(r){
			    	if(r.code === 0){
						alert('操作成功', function(index){
							vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		del: function (event) {
			var cids = getSelectedRows();
			if(cids == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "comment/usercomment/delete",
                    contentType: "application/json",
				    data: JSON.stringify(cids),
				    success: function(r){
						if(r.code == 0){
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
		getInfo: function(cid){
			$.get(baseURL + "comment/usercomment/info/"+cid, function(r){
                vm.usercomment = r.usercomment;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		}
	}
});