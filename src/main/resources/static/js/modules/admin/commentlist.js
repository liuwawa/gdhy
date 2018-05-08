$(function () {
    $.get(baseURL + "sys/user/userId2Name", function(r) {
        if (r.code != OK) {
            alert(r.msg);
            return;
        }
        var userId2Name = r.userId2Name;
        $("#jqGrid").jqGrid({
            url: baseURL + 'admin/commentlist/list',
            datatype: "json",
            colModel: [
                { label: '用户', name: 'userId', index: 'user_id', width: 20 ,formatter: function (value, options, row) {
                    return '<a href="javascript:showUserDetail(' + value + ')">' + value + '</a>';
                }},
                { label: '意见', name: 'opinion', index: 'opinion', width: 80 }, 			
                { label: '截图', name: 'imgUrl', index: 'img_url', width: 16 ,formatter: function(value, options, row){
                    return value?'<a class="btn btn-default show-photo" data-imgs="' + value +  '"><i class="fa fa-search"></i></a>':"";
                }},
                { label: '手机号码', name: 'phone', index: 'phone', width: 40 },
                { label: 'app版本', name: 'appEdition', index: 'app_edition', width: 25 },
                { label: '系统版本', name: 'sysName', index: 'sys_name', width: 30 ,formatter: function(value, options, row){
                    return value + "(" + row.sysEdition + ")";
                }},
                { label: '手机标识', name: 'phoneMark', index: 'phone_mark', width: 40 },
                { label: '设备ID', name: 'phoneEquipmentId', index: 'phone_equipment_id', width: 40 },
                { label: '提交时间', name: 'submitTime', index: 'submit_time', width: 42 },
                { label: '解决时间', name: 'modifyTime', index: 'modify_time', width: 42 },
                { label: '解决者', name: 'modifyUser', index: 'modify_user', width: 25  , formatter: function(value, options, row){
                    var name = userId2Name[value];
                    return name?name:'';
                }},
                { label: '备注', name: 'remarks', index: 'remarks', width: 80 }
            ],
            viewrecords: true,
            height: $(window).height() - 130,
            rowNum: 30,
            rowList : [50,100,200],
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
                $(".show-photo").click(function () {
                    var data = [];
                    var imgs = this.getAttribute("data-imgs").split(",");
                    for (var i = 0;i<imgs.length;i++) {
                        data.push({pid:i,src: imgs[i],alt:""});
                    }
                    layer.photos({
                        photos: {"data":data}
                        ,anim: 5
                    });
                });
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
		commentList: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.commentList = {};
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
            layer.prompt({
                formType: 3,
                title: '解决反馈'
            }, function(value, index, elem){
                layer.closeAll();
                $.ajax({
                    type: "POST",
                    url: baseURL + "admin/commentlist/update",
                    data:{
                        'remarks':value,
						'id':id
					},
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
            });
		},
		saveOrUpdate: function (event) {
			var url = vm.commentList.id == null ? "admin/commentlist/save" : "admin/commentlist/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.commentList),
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
				    url: baseURL + "admin/commentlist/delete",
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
			$.get(baseURL + "admin/commentlist/info/"+id, function(r){
                vm.commentList = r.commentList;
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