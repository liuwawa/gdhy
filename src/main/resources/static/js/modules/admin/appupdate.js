$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'admin/appupdate/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 15, key: true },
			{ label: 'APPID', name: 'appId', index: 'app_id', width: 20 },
			{ label: '版本', name: 'version', index: 'version', width: 20 },
			{ label: 'apk下载地址', name: 'apkUrl', index: 'apk_url', width: 30 },
			{ label: '强制更新', name: 'force', index: 'force', width: 20 ,formatter: function(value, options, row){
                return value?'是':'否';
            }},
			{ label: '描述', name: 'describe', index: 'describe', width: 90 },
			{ label: '开放下载', name: 'openDown', index: 'open_down', width: 20 ,formatter: function(value, options, row){
                return value?'是':'否';
            }},
			{ label: '开放时间', name: 'openDownTime', index: 'open_down_time', width: 30 },
			{ label: '创建时间', name: 'createTime', index: 'create_time', width: 30 },
			{ label: '创建者', name: 'createUser', index: 'create_user', width: 20 },
			{ label: '修改时间', name: 'modifyTime', index: 'modify_time', width: 30 },
			{ label: '修改者', name: 'modifyUser', index: 'modify_user', width: 20 }
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
        }
    });
    new AjaxUpload('#upload', {
        action: baseURL + 'sys/oss/upload?token=' + token,
        name: 'file',
        autoSubmit:true,
        responseType:"json",
        onSubmit:function(file, extension){
            $('.fa-fw').show();
            document.getElementsByClassName("saveOrUpdate")[0].setAttribute("disabled","disabled");
        },
        onComplete : function(file, r){
			$('.fa-fw').hide();
			document.getElementsByClassName("saveOrUpdate")[0].removeAttribute("disabled");
            if(r.code == OK){
                vm.appUpdate.apkUrl=r.url;
            }else{
                alert(r.msg);
            }
        }
    });
    laydate.render({
        type:'datetime',
        elem: '#openDownTime'
        ,trigger: 'click'
        ,done: function (value, date, endDate) {
            vm.appUpdate.openDownTime = value;
        }
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		q: {},
		appUpdate: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.appUpdate = {
                force:'0',
                openDown:'0',
                apkUrl:'',
                openDownTime:''
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
			var url = vm.appUpdate.id == null ? "admin/appupdate/save" : "admin/appupdate/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.appUpdate),
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
				    url: baseURL + "admin/appupdate/delete",
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
			$.get(baseURL + "admin/appupdate/info/"+id, function(r){
                vm.appUpdate = r.appUpdate;
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
