$(function () {
    $.get(baseURL + "sys/user/userId2Name", function(r) {
        if (r.code != OK) {
            alert(r.msg);
            return;
        }
        var userId2Name = r.userId2Name;
        $("#jqGrid").jqGrid({
            url: baseURL + 'admin/faq/list',
            datatype: "json",
            colModel: [
                {label: 'id', name: 'id', index: 'id', width: 20, key: true, hidden: true},
                {label: '所属分类', name: 'type', index: 'type', width: 20},
                {label: '问题', name: 'question', index: 'question', width: 80},
                {label: '答案', name: 'answer', index: 'answer', width: 80},
                {label: '排序', name: 'sort', index: 'sort', width: 10},
                {label: '相关连接', name: 'relevantUrl', index: 'relevant_url', width: 20},
                {label: '相关问题', name: 'relevantFaq', index: 'relevant_faq', width: 20},
                {label: '版本', name: 'version', index: 'version', width: 10},
                {label: '创建者', name: 'createUser', index: 'create_user', width: 20, formatter: function(value, options, row){
                    var name = userId2Name[value];
                    return name?name:'';
                }},
                {label: '创建时间', name: 'createTime', index: 'create_time', width: 20},
                {label: '修改者', name: 'modifyUser', index: 'modify_user', width: 20, formatter: function(value, options, row){
                    var name = userId2Name[value];
                    return name?name:'';
                }},
                {label: '修改时间', name: 'modifyTime', index: 'modify_time', width: 20},
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
		faq: {},
        faqTypeList: []
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.faq = {type:""};
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
			var url = vm.faq.id == null ? "admin/faq/save" : "admin/faq/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.faq),
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
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "admin/faq/delete",
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
			$.get(baseURL + "admin/faq/info/"+id, function(r){
                vm.faq = r.faq;
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
$.get(baseURL +  'admin/faqtype/list?_search=false&limit=1000&page=1&sidx=sort&order=asc',function (r) {
    vm.faqTypeList = r.page.list;
});