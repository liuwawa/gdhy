$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'admin/user/list',
        datatype: "json",
        colModel: [
			{ label: 'ID', name: 'userId', index: 'user_id', width: 40, key: true ,formatter: function (value, options, row) {
                return '<a href="javascript:showUserDetail(' + value + ')">' + value + '</a>';
            }},
			// { label: '头像', name: 'avatar', index: 'avatar', width: 80 ,
             //    formatter: function(value, options, row){
			// 		if(value!=null && value!=""){
			// 			return '<img src="'+ value + '" style="width: 64px;height: 64px" class="img-circle">';
			// 		}
			// 		return "";
             //    }},
			{ label: '用户名', name: 'username', index: 'username', width: 80 },
			{ label: '手机号', name: 'phone', index: 'phone', width: 80 },
			{ label: '性别', name: 'gender', index: 'gender', width: 40 ,
                formatter: function(value, options, row){
                    if(value===0){
                    	return "";
					}else if(value===1){
                        return '<i class="fa fa-male text-primary" aria-hidden="true"></i>';
					}else if(value===2){
                        return '<i class="fa fa-female text-danger" aria-hidden="true"></i>';
                    }
                }},
			{ label: '账号状态', name: 'status', index: 'status', width: 50,
                formatter: function(value, options, row){
                    return value == "NORMAL" ?
                        '正常' :
                        '非正常';
                }},
			{ label: '微信绑定', name: 'weixinBindOpenid', index: 'weixin_bind_openid',width: 50 ,formatter: function(value, options, row){
                return value ? '<i class="fa fa-check-circle text-success" aria-hidden="true"></i>':'';
            }},
			{ label: '支付宝绑定', name: 'alipayBindOpenid', index: 'alipay_bind_openid',width: 50 ,formatter: function(value, options, row){
                return value ? '<i class="fa fa-check-circle text-success" aria-hidden="true"></i>':'';
            }},
			{ label: '启用', name: 'disableFlag', index: 'disable_flag', width: 50,
				formatter: function(value, options, row){
                return value === 0 ?
                    '<i class="fa fa-toggle-on switch text-success" data-user_id="' + row.userId + '" data-colname="disable_flag"></i>' :
                    '<i class="fa fa-toggle-off switch text-success" data-user_id="' + row.userId + '" data-colname="disable_flag"></i>';
            }},
			{ label: '注册时间', name: 'createTime', index: 'create_time', width: 80 },
			{ label: '资料更新时间', name: 'modifyTime', index: 'modify_time', width: 80 },
        ],
		viewrecords: true,
        height: $(window).height() - 130,
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
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
            setTimeout(initSwitch,0);
        }
    });
});

var vm = new Vue({
    el:'#rrapp',
    data: {
        q: {
            diy_query_key: ""
        },
        showList: true,
        title: null,
        user: {},
        querycolumnList: {
            'user_id' : "用户ID",
            'username' : "用户名",
            'phone' : "手机",
            'alipay_account' : "支付宝账户",
            'alipay_name' : "支付宝姓名",
            'weixin_pay_name' : "微信钱包姓名",
            'weixin_pay_phone' : "微信钱包手机号码"
        }
    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function(){
            vm.showList = false;
            vm.title = "新增";
            vm.user = {};
        },
        update: function (event) {
            var userId = getSelectedRow();
            if(userId == null){
                return ;
            }
            vm.showList = false;
            vm.title = "修改";
            vm.getInfo(userId)
        },
        saveOrUpdate: function (event) {
            var url = vm.user.userId == null ? "admin/user/save" : "admin/user/update";
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.user),
                success: function(r){
                    if(r.code === 200){
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
            var userIds = getSelectedRows();
            if(userIds == null){
                return ;
            }

            confirm('确定要删除选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: baseURL + "admin/user/delete",
                    contentType: "application/json",
                    data: JSON.stringify(userIds),
                    success: function(r){
                        if(r.code == 200){
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
        getInfo: function(userId){
            $.get(baseURL + "admin/user/info/"+userId, function(r){
                vm.user = r.user;
            });
        },
        reload: function (event) {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                postData:vm.q,
                page:page
            }).trigger("reloadGrid");
        }
    }
});

function initSwitch() {
    $(".switch").click(function(event){
        var thisSwitch = $(this);
        var uid = event.target.dataset.user_id;
        if(thisSwitch.hasClass("fa-toggle-on")){
            if(!hasPermission("admin:user:disable")){
                alert("权限不足");
                return;
            }
            var choicesHtml = '<input type="text" id="disable_msg" class="form-control" autocomplete="on" placeholder="被禁原因" style="margin-left: 15px;width: 191px;"/>' +
            '<div class="form-group col-sm-1" style="margin-bottom: 0px;width: 100%;">' +
            '<select class="form-control" id="duration" style="width: 100%;margin-top: 5px;" autocomplete="off">' +
            '<option value="" selected="selected">不自动解禁</option>' +
            '<option value="oneday">一天</option> ' +
            '<option value="twoday">两天</option> ' +
            '<option value="week">一周</option> ' +
            '</select> </div>';

            //禁用用户
            layer.open({
                title: '禁用用户',
                content:choicesHtml,
                yes: function(layero, index){
                    layer.load(1, {
                        shade: [0.1,'#fff'] //0.1透明度的白色背景
                    });
                    $.post(baseURL + "admin/user/disable",{
                        userId: uid,
                        disableMsg:$("#disable_msg").val(),
                        duration:$("#duration").val()
                    },function (r) {
                        layer.closeAll();
                        if(r.code == OK){
                            thisSwitch.removeClass("fa-toggle-on");
                            thisSwitch.addClass("fa-toggle-off");
                            alert("操作成功");
                            //vm.reload();
                        }else{
                            alert(r.msg);
                        }
                    });
                }
            });
        }else {
            if(!hasPermission("admin:user:enable")){
                alert("权限不足");
                return;
            }
            //解除禁用
            layer.open({
                title: '提示',
                content:'确定解除禁用？',
                yes: function(layero, index){
                    layer.load(1, {
                        shade: [0.1,'#fff'] //0.1透明度的白色背景
                    });
                    $.post(baseURL + "admin/user/enable",{
                        userId: uid
                    },function (r) {
                        layer.closeAll();
                        if(r.code == OK){
                            thisSwitch.removeClass("fa-toggle-off");
                            thisSwitch.addClass("fa-toggle-on");
                            alert("操作成功");
                            //vm.reload();
                        }else{
                            alert(r.msg);
                        }
                    });
                }
            });
        }
    });
}