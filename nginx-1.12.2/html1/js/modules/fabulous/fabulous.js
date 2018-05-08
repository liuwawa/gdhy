$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'fabulous/fabulous/list',
        datatype: "json",
        colModel: [			
			{ label: 'fid', name: 'fid', index: 'fid', width: 50, key: true },
			{ label: '用户ID', name: 'uid', index: 'uid', width: 80 }, 			
			{ label: '文章ID', name: 'aid', index: 'aid', width: 80 }			
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
		fabulous: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.fabulous = {};
		},
		update: function (event) {
			var fid = getSelectedRow();
			if(fid == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(fid)
		},
		saveOrUpdate: function (event) {
			var url = vm.fabulous.fid == null ? "fabulous/fabulous/save" : "fabulous/fabulous/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.fabulous),
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
			var fids = getSelectedRows();
			if(fids == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "fabulous/fabulous/delete",
                    contentType: "application/json",
				    data: JSON.stringify(fids),
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
		getInfo: function(fid){
			$.get(baseURL + "fabulous/fabulous/info/"+fid, function(r){
                vm.fabulous = r.fabulous;
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