$(function () {
    $.get(baseURL + "sys/user/userId2Name", function(r) {
        if (r.code != OK) {
            alert(r.msg);
            return;
        }
        var userId2Name = r.userId2Name;
        $("#jqGrid").jqGrid({
            url: baseURL + 'admin/activitycentral/list',
            datatype: "json",
            colModel: [
                {label: '标记', name: 'classification', index: 'classification', width: 80},
                {label: '标题', name: 'title', index: 'title', width: 80},
                {label: '图片地址', name: 'imgUrl', index: 'img_url', width: 80},
                {label: '描述', name: 'desc', index: 'desc', width: 80},
                {label: '打开方式', name: 'tag', index: 'tag', width: 80, formatter: function(value, options, row){
                    return value==1?"app内打开":(value==2?"内部浏览器打开":(value==3?"外部浏览器打开":""));
                }},
                {label: '按钮文字', name: 'btnText', index: 'btntext', width: 80},
                {label: '开始时间', name: 'startTime', index: 'start_time', width: 80},
                {label: '结束时间', name: 'endTime', index: 'end_time', width: 80},
                {label: '创建时间', name: 'createTime', index: 'create_time', width: 80},
                {label: '创建者', name: 'createUser', index: 'create_user', width: 80, formatter: function(value, options, row){
                    var name = userId2Name[value];
                    return name?name:'';
                }},
                {label: '修改时间', name: 'modifyTime', index: 'modify_time', width: 80},
                {label: '修改者', name: 'modifyUser', index: 'modify_user', width: 80, formatter: function(value, options, row){
                    var name = userId2Name[value];
                    return name?name:'';
                }},
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
		q: {},
		activityCentral: {
		}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.activityCentral = {
				tag:0
			};
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
			var url = vm.activityCentral.id == null ? "admin/activitycentral/save" : "admin/activitycentral/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.activityCentral),
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
				    url: baseURL + "admin/activitycentral/delete",
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
			$.get(baseURL + "admin/activitycentral/info/"+id, function(r){
                vm.activityCentral = r.activityCentral;
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

laydate.render({
    elem: '#starttime'
    , trigger: 'click'
    , type: 'datetime'
    , done: function (value, date, endDate) {
        vm.activityCentral.startTime = value;
    }
});
laydate.render({
    elem: '#endtime'
    , trigger: 'click'
    , type: 'datetime'
    , done: function (value, date, endDate) {
        vm.activityCentral.endTime = value;
    }
});