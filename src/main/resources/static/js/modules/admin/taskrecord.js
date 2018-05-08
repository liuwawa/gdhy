$(function () {
     var item = window.localStorage.getItem("taskRecord_id");
    var item2 = window.localStorage.getItem("user_id");
    var data;
    if(item){
        data={"id":item,"userId":item2};
        window.localStorage.removeItem("taskRecord_id");
        window.localStorage.removeItem("user_id");
    }
    $.get(baseURL + "admin/tasklist/all", function (r) {
        if(r.code!=OK){
            alert(r.msg);
            return;
        }
        vm.taskId2Name = window.taskId2Name = r.list;


        $("#jqGrid").jqGrid({
            url: baseURL + 'admin/taskrecord/list',
            datatype: "json",
            colModel: [
                { label: '用户ID', name: 'userId', index: 'user_id', width: 30 ,formatter: function (value, options, row) {
                    return '<a href="javascript:showUserDetail(' + value + ')">' + value + '</a>';
                }},
                { label: '时间', name: 'id', index: 'id', width: 50, key: true },
                { label: '任务类型', name: 'type', index: 'type', width: 50 ,formatter: function(value, options, row){
                    var name = taskId2Name[value];
                    return name?name:value;
                }},
                { label: '奖励金币', name: 'reward', index: 'reward', width: 30 },
                { label: '描述', name: 'describe', index: 'describe', width: 80 }
            ],
            viewrecords: true,
            height: $(window).height() - 80,
            rowNum: 30,
            rowList : [50,100,200],
            rownumbers: false,
            rownumWidth: 25,
            autowidth:true,
            multiselect: false,
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
            postData:data,
            gridComplete:function(){
                //隐藏grid底部滚动条
                $("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
            }
        });
    });

});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
        taskId2Name: window.taskId2Name,
		q: {
            typeId:""
        },
		taskRecord: {}
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
				postData:vm.q
            }).trigger("reloadGrid");
		}
	}
});