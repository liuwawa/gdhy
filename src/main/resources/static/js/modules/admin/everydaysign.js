$(function () {
    $.get(baseURL + "sys/user/userId2Name", function(r) {
        if (r.code != OK) {
            alert(r.msg);
            return;
        }
        var userId2Name = r.userId2Name;
        $("#jqGrid").jqGrid({
            url: baseURL + 'admin/everydaysign/list',
            datatype: "json",
            colModel: [
                {label: '天数', name: 'days', index: 'days', width: 50, key: true},
                {label: '金币奖励', name: 'reward', index: 'reward', width: 80},
                {label: '修改时间', name: 'modifyTime', index: 'modify_time', width: 80},
                {label: '修改者', name: 'modifyUser', index: 'modify_user', width: 80, formatter: function(value, options, row){
                    var name = userId2Name[value];
                    return name?name:'';
                }}
            ],
            viewrecords: true,
            height: $(window).height() - 130,
            rowNum: 30,
            rowList: [50, 100, 200],
            rownumbers: false,
            rownumWidth: 25,
            autowidth: true,
            multiselect: false,
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
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		q: {},
		everydaySign: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		update: function (event) {
			var days = getSelectedRow();
			if(days == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(days)
		},
		saveOrUpdate: function (event) {
			var url = vm.everydaySign.days == null ? "admin/everydaysign/save" : "admin/everydaysign/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.everydaySign),
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
		getInfo: function(days){
			$.get(baseURL + "admin/everydaysign/info/"+days, function(r){
                vm.everydaySign = r.everydaySign;
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