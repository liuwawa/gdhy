$(function () {
    $.get(baseURL + "sys/user/userId2Name", function(r) {
        if (r.code != OK) {
            alert(r.msg);
            return;
        }
        var userId2Name = r.userId2Name;
        $("#jqGrid").jqGrid({
            url: baseURL + 'admin/faqtype/list',
            datatype: "json",
            colModel: [
                {label: 'id', name: 'id', index: 'id', width: 50, key: true},
                {label: '分类名称', name: 'title', index: 'title', width: 80},
                {label: '排序', name: 'sort', index: 'sort', width: 80},
                {label: '相关链接', name: 'relevantUrl', index: 'relevant_url', width: 80},
                {label: '创建者', name: 'createUser', index: 'create_user', width: 80, formatter: function(value, options, row){
                    var name = userId2Name[value];
                    return name?name:'';
                }},
                {label: '创建时间', name: 'createTime', index: 'create_time', width: 80},
                {label: '修改者', name: 'modifyUser', index: 'modify_user', width: 80, formatter: function(value, options, row){
                    var name = userId2Name[value];
                    return name?name:'';
                }},
                {label: '修改时间', name: 'modifyTime', index: 'modify_time', width: 80},
            ],
            viewrecords: true,
            height: $(window).height() - 130,
            rowNum: 30,
            rowList: [50, 100, 200],
            rownumbers: true,
            rownumWidth: 25,
            autowidth: true,
            multiselect: true,
            pager: "#jqGridPager",
            jsonReader: {
                root: "page.list",
                page: "page.currPage",
                total: "page.totalPage",
                records: "page.totalCount"
            },
            prmNames: {
                page: "page",
                rows: "limit",
                order: "order"
            },
            gridComplete: function () {
                //隐藏grid底部滚动条
                $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
            }
        });
    })
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		faqType: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.faqType = {};
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
			var url = vm.faqType.id == null ? "admin/faqtype/save" : "admin/faqtype/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.faqType),
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
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			
			confirm('删除该分类将同时删除该分类下的问题，确定要操作吗？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "admin/faqtype/delete",
                    contentType: "application/json",
				    data: JSON.stringify(ids),
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
		getInfo: function(id){
			$.get(baseURL + "admin/faqtype/info/"+id, function(r){
                vm.faqType = r.faqType;
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