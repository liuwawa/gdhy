$(function () {
    $.get(baseURL + "sys/user/userId2Name", function(r) {
        if (r.code != OK) {
            alert(r.msg);
            return;
        }
        var userId2Name = r.userId2Name;
        $("#jqGrid").jqGrid({
            url: baseURL + 'admin/goldexchange/list',
            datatype: "json",
            colModel: [
                {
                    label: '金额（元）',
                    name: 'rmb',
                    index: 'rmb',
                    width: 50,
                    key: true,
                    formatter: function (value, options, row) {
                        return value / 100;
                    }
                },
                {label: '金币', name: 'gold', index: 'gold', width: 80},
                {label: '剩余份数', name: 'surplus', index: 'surplus', width: 80},
                {label: '创建时间', name: 'createTime', index: 'create_time', width: 80},
                {
                    label: '创建者',
                    name: 'createUser',
                    index: 'create_user',
                    width: 80,
                    formatter: function (value, options, row) {
                        var name = userId2Name[value];
                        return name ? name : '';
                    }
                },
                {label: '修改时间', name: 'modifyTime', index: 'modify_time', width: 80},
                {
                    label: '修改者',
                    name: 'modifyUser',
                    index: 'modify_user',
                    width: 80,
                    formatter: function (value, options, row) {
                        var name = userId2Name[value];
                        return name ? name : '';
                    }
                }
            ],
            viewrecords: true,
            height: $(window).height() - 130,
            rowNum: 30,
            rowList: [50, 100, 200],
            rownumbers: false,
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
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		q: {},
		goldExchange: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.goldExchange = {};
		},
		update: function (event) {
			var rmb = getSelectedRow();
			if(rmb == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(rmb)
		},
		saveOrUpdate: function (event) {
			var url = vm.goldExchange.createTime == null ? "admin/goldexchange/save" : "admin/goldexchange/update";
			if(vm.goldExchange.rmb < 1){
				alert("兑换金额至少大于1");
				return;
			}
			//判断小数点位数
			var rmb = vm.goldExchange.rmb.toString();
			if(rmb.indexOf('.') != -1 && rmb.split(".")[1].length){
				alert("请不要超过两位小数");
				return;
			}

            var goldExchange = JSON.parse(JSON.stringify(vm.goldExchange));
            goldExchange.rmb = goldExchange.rmb * 100;
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(goldExchange),
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
			var rmbs = getSelectedRows();
			if(rmbs == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "admin/goldexchange/delete",
                    contentType: "application/json",
				    data: JSON.stringify(rmbs),
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
		getInfo: function(rmb){
			$.get(baseURL + "admin/goldexchange/info/"+rmb, function(r){
				r.goldExchange.rmb = r.goldExchange.rmb / 100;
                vm.goldExchange = r.goldExchange;
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