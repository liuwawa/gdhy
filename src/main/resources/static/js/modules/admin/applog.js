
$(function () {

	//获取枚举列表用于翻译
    $.get(baseURL + "admin/applog/enums", function(r){
    	if(r.code != OK){
    		alert(r.msg);
    		return;
		}
    	 var logTypes = r.logTypes;
    	var  operations = r.operations;


        window.vm = new Vue({
            el:'#rrapp',
            data:{
                showList: true,
                title: null,
                userLog: {},
                q:{
                    logtype:"",
                    operation:"",
                    level:"",
                },
                logTypes:logTypes,
                operations:operations
            },
            methods: {
                query: function () {
                    vm.reload();
                },
                add: function(){
                    vm.showList = false;
                    vm.title = "新增";
                    vm.userLog = {};
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
                getInfo: function(id){
                    $.get(baseURL + "admin/applog/info/"+id, function(r){
                        vm.userLog = r.userLog;
                    });
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
        $('.js-example-basic-single').select2();
        laydate.render({
            type:'datetime',
            elem: '#input_date_search'
            ,range: true
            ,trigger: 'click'
            ,done: function (value, date, endDate) {
                vm.q.input_date = value;
                // $(".fast-c").removeClass("disabled");
            }
        });

        var item = window.localStorage.getItem("appLog_id");
        var item2 = window.localStorage.getItem("user_id");
        var data;
        if(item){
            data={"id":item,"user":item2};
            window.localStorage.removeItem("appLog_id");
            window.localStorage.removeItem("user_id");
        }

        $("#jqGrid").jqGrid({
            url: baseURL + 'admin/applog/list',
            datatype: "json",
            colModel: [
                { label: 'id', name: 'id', index: 'id', width: 10, key: true,hidden:true },
                { label: '日志类型', name: 'type', index: 'type', width: 25 ,
                    formatter: function(value, options, row){
                        return logTypes[value];
                    }},
                { label: '用户ID', name: 'user', index: 'user', width: 10 ,formatter: function (value, options, row) {
                    return value?'<a href="javascript:showUserDetail(' + value + ')">' + value + '</a>':'';
                }},
                { label: '访问IP', name: 'ip', index: 'ip', width: 25 },
                { label: '级别', name: 'level', index: 'level', width: 10 ,
                    formatter: function(value, options, row){
            			if(value > 20){
							return '<span class="label label-danger">错误</span>';
						}else if(value > 10){
							return '<span class="label label-warning">警告</span>';
						}else{
							return '<span class="label label-primary">信息</span>';
						}
                    } },
                { label: '操作', name: 'operation', index: 'operation', width: 25  ,
                    formatter: function(value, options, row){
                        return operations[value];
                    }},
                { label: '描述', name: 'describe', index: 'describe', width: 80 },
                { label: '应用ID', name: 'appId', index: 'app_id', width: 20 },
                { label: '实例ID', name: 'instanceId', index: 'instance_id', width: 20 },
                { label: '备注', name: 'remarks', index: 'remarks', width: 30 },
                { label: '时间', name: 'createTime', index: 'create_time', width: 30 }
            ],
            viewrecords: true,
            height: $(window).height() - 130,
            rowNum: 50,
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

$(document).on("change",'select#logtype',function(){
    vm.q.logtype=$(this).val();
});

$(document).on("change",'select#operation',function(){
    vm.q.operation=$(this).val();
});
$(document).on("change",'select#level',function(){
    vm.q.level=$(this).val();
});