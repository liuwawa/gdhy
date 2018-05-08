$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'admin/tasklist/list',
        datatype: "json",
        colModel: [
            { label: 'ID', name: 'id', index: 'id', width: 20, key: true },
			{ label: '名称', name: 'name', index: 'name', width: 80 },
			{ label: '类型', name: 'type', index: 'type', width: 80 ,formatter: function(value, options, row){
                if(value == "daily"){
                    return '<span class="label label-primary">日常任务</span>';
                }else if(value == "novice"){
                    return '<span class="label label-success">新手任务</span>';
                }else if(value == "base"){
                    return '<span class="label label-info">基本任务</span>';
                }
            }},
			{ label: '奖励', name: 'goldMax', index: 'gold_max', width: 80 ,formatter: function(value, options, row){
                return row.goldMax == row.goldMini ? row.goldMax : row.goldMini + ' - ' + row.goldMax;
            }},
			{ label: '激活', name: 'activated', index: 'activated', width: 30 ,formatter: function(value, options, row){
                return value == 1 ?
                    '<i class="fa fa-toggle-on switch text-success" data-tid="' + row.id + '"></i>' :
                    '<i class="fa fa-toggle-off switch text-success" data-tid="' + row.id + '"></i>';
            }},
			{ label: '图标', name: 'icon', index: 'icon', width: 80 ,formatter: function(value, options, row){
                if(value){
                    return value == 'red_envelopes'?'红包':'金币';
                }
                return '';
            }},
			{ label: '收益描述', name: 'profit', index: 'profit', width: 80 },
			{ label: '链接', name: 'btnUrl', index: 'btn_url', width: 80 },
			{ label: '任务描述', name: 'describe', index: 'describe', width: 80 },
			{ label: '按钮文字', name: 'btnText', index: 'btn_text', width: 80 },
			{ label: '事件', name: 'event', index: 'event', width: 80 },
			{ label: '开放时间', name: 'openTime', index: 'open_time', width: 80 },
			{ label: '关闭时间', name: 'endTime', index: 'end_time', width: 80 },
			{ label: '最小用时', name: 'duration', index: 'duration', width: 80 },
			{ label: '最小版本', name: 'rersion', index: 'rersion', width: 80 },
			{ label: '排序', name: 'sort', index: 'sort', width: 80 },
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
            $(".switch").click(function(event){
                var id = this.dataset.tid;
                var flag = $(this).hasClass("fa-toggle-on");
                //激活/取消激活任务
                $.ajax({
                    type: "POST",
                    url: baseURL + "admin/tasklist/activated",
                    contentType: "application/x-www-form-urlencoded",
                    data: {"id":id,"flag":!flag},
                    success: function (r) {
                        if (r.code === OK) {
                            if(flag){
                                $(event.target).removeClass("fa-toggle-on").addClass("fa-toggle-off");
                            }else{
                                $(event.target).removeClass("fa-toggle-off").addClass("fa-toggle-on");
                            }
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            });
        }
    });
});

    window.vm = new Vue({
        el: '#rrapp',
        data: {
            showList: true,
            title: null,
            q: {},
            taskList: {}
        },
        methods: {
            query: function () {
                vm.reload();
            },
            add: function () {
                vm.showList = false;
                vm.title = "新增";
                vm.taskList = {
                    type: "daily",
                    icon:"gold",
                    event: "innerJump"
                };
            },
            update: function (event) {
                var id = getSelectedRow();
                if (id == null) {
                    return;
                }
                vm.showList = false;
                vm.title = "修改";

                vm.getInfo(id)
            },
            saveOrUpdate: function (event) {
                if (!vm.taskList.name) {
                    alert('任务名称不能为空');
                    return;
                }

                var url = vm.taskList.id == null ? "admin/tasklist/save" : "admin/tasklist/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.taskList),
                    success: function (r) {
                        if (r.code === OK) {
                            alert('操作成功', function (index) {
                                vm.reload();
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            },
            del: function (event) {
                var ids = getSelectedRows();
                if (ids == null) {
                    return;
                }

                confirm('确定要删除选中的记录？', function () {
                    $.ajax({
                        type: "POST",
                        url: baseURL + "admin/tasklist/delete",
                        contentType: "application/json",
                        data: JSON.stringify(ids),
                        success: function (r) {
                            if (r.code == OK) {
                                alert('操作成功', function (index) {
                                    $("#jqGrid").trigger("reloadGrid");
                                });
                            } else {
                                alert(r.msg);
                            }
                        }
                    });
                });
            },
            getInfo: function (id) {
                $.get(baseURL + "admin/tasklist/info/" + id, function (r) {
                    vm.taskList = r.taskList;
                });
            },
            reload: function (event) {
                vm.showList = true;
                var page = $("#jqGrid").jqGrid('getGridParam', 'page');
                $("#jqGrid").jqGrid('setGridParam', {
                    postData: vm.q,
                    page: page
                }).trigger("reloadGrid");
            }
        }
    });
    laydate.render({
        elem: '#openTime'
        , trigger: 'click'
        , type: 'datetime'
        , done: function (value, date, endDate) {
            vm.taskList.openTime = value;
        }
    });
laydate.render({
    elem: '#endTime'
    , trigger: 'click',
    type: 'datetime',
    min:value
    , done: function (value, date, endDate) {
        vm.taskList.endTime = value;
    }
});
