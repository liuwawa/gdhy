$(function () {
    var richArr = {};
    $.get(baseURL + "sys/user/userId2Name", function(r) {
        if (r.code != OK) {
            alert(r.msg);
            return;
        }
        var userId2Name = r.userId2Name;
        $("#jqGrid").jqGrid({
            url: baseURL + 'admin/datadictionaries/list',
            datatype: "json",
            colModel: [
                {label: '关键字', name: 'key', index: 'key', width: 80},
                {
                    label: '类型', name: 'type', index: 'type', width: 80,
                    formatter: function (value, options, row) {
                        return value == 0 ? "文本" : value == 1 ? "url" : value == 2 ? "富文本" : "";
                    }
                },
                {
                    label: '数据', name: 'value', index: 'value', width: 80,
                    formatter: function (value, options, row) {
                        if (row.type == 2) {
                            richArr[row.id] = value;
                            return value?'<i class="fa fa-search rich-text" data-rid="' + row.id + '"></i>':"暂无数据";
                        }
                        return value ? value : "暂无数据";
                    }
                },
                {label: 'app版本', name: 'version', index: 'version', width: 80},
                {label: '创建时间', name: 'createTime', index: 'create_time', width: 80},
                {label: '创建者', name: 'createUser', index: 'create_user', width: 80, formatter: function(value, options, row){
                    var name = userId2Name[value];
                    return name?name:'';
                }},
                {label: '修改时间', name: 'modifyTime', index: 'modify_time', width: 80},
                {label: '修改人', name: 'modifyUser', index: 'modify_user', width: 80, formatter: function(value, options, row){
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
                $(".rich-text").bind("click", function () {
                    parent.layer.open({
                        type: 1,
                        area: ['800px', '600px'], //宽高
                        content: richArr[this.getAttribute("data-rid")],
                    });
                });
            }
        });
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
            document.getElementsByClassName("saveOrUpdate")[0].removeAttribute("disabled");
            $('.fa-fw').hide();
            if(r.code == OK){
                $(".form-control")[5].value=r.url;
                vm.dataDictionaries.value=r.url;
            }else{
                alert(r.msg);
            }
        }
    });
    window.ue = UE.getEditor('text_value',{
        toolbars: [
            [
                "undo",
                "redo",
                "|",
                "bold",
                "italic",
                "underline",
                "fontborder",
                "strikethrough",
                "|",
                "forecolor",
                "backcolor",
                "insertorderedlist",
                "insertunorderedlist",
                "cleardoc",
                "lineheight",
                "|",
                "paragraph",
                "fontfamily",
                "fontsize",
                "indent",
                "|",
                "justifyleft",
                "justifycenter",
                "justifyright",
                "justifyjustify",
                "|",
                "touppercase",
                "tolowercase",
                "|",
                "simpleupload",
                "insertimage",
                "|",
                "horizontal",
                "date",
                "time",
                "|",
                "inserttable",
                "deletetable",
                "insertparagraphbeforetable",
                "insertrow",
                "deleterow",
                "insertcol",
                "deletecol",
                "mergecells",
                "mergeright",
                "mergedown",
                "splittocells",
                "splittorows",
                "splittocols",
                "|",
                "searchreplace",
                "wordCount"
            ]
        ],
        initialFrameWidth: 1050,
        initialFrameHeight:600
    });

    ue.ready(function () {});

    UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;
    UE.Editor.prototype.getActionUrl = function(action) {
        if (action == 'fileUploadServlet' ) {
            return baseURL + 'admin/datadictionaries/upload?token='+token;
        }else {
            return this._bkGetActionUrl.call(this, action);
        }
    }
});

 window.vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		q: {},
		dataDictionaries: {},
        operationTag:false,
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
            vm.showList = false;
            vm.title = "新增";
            vm.dataDictionaries = {
                type:0
            };
            ue.setContent("");
            document.getElementById("mobile-phone").src = "data:text/html;base64,PGh0bWw+PGhlYWQ+PG1ldGEgY2hhcnNldD0iVVRGLTgiPjxtZXRhIG5hbWU9InZpZXdwb3J0IiBjb250ZW50PSJ3aWR0aD1kZXZpY2Utd2lkdGgsaW5pdGlhbC1zY2FsZT0xLG1pbmltdW0tc2NhbGU9MSxtYXhpbXVtLXNjYWxlPTEsdXNlci1zY2FsYWJsZT1ubyI+PC9oZWFkPjxib2R5PjxkaXYgc3R5bGU9Im1hcmdpbi10b3A6IDUwJTttYXJnaW4tbGVmdDogLTE0cHg7dGV4dC1hbGlnbjogY2VudGVyO2NvbG9yOiAjOTk5O2ZvbnQtc2l6ZTogMTRweDsiPuivt+eCueWHu+mihOiniOaMiemSrjwvZGl2PjwvYm9keT48L2h0bWw+";
		},
        showIframe:function () {
		    if(!/^(http|https){1}:\/\/.*/i.test(vm.dataDictionaries.value)){
                alert("输入的地址必须是http://开头");
                return;
            }
            if(/^(http){1}:\/\/.*/i.test(vm.dataDictionaries.value)){
                window.open(vm.dataDictionaries.value,'_blank','');
                return ;
            }
		    document.getElementById("mobile-phone").src = vm.dataDictionaries.value;
        },
		update: function (event) {
            var id = getSelectedRow();
            if(id == null){
                return ;
            }
            vm.showList = false;
            vm.title = "修改";
            vm.operation=true;

            document.getElementById("mobile-phone").src = "data:text/html;base64,PGh0bWw+PGhlYWQ+PG1ldGEgY2hhcnNldD0iVVRGLTgiPjxtZXRhIG5hbWU9InZpZXdwb3J0IiBjb250ZW50PSJ3aWR0aD1kZXZpY2Utd2lkdGgsaW5pdGlhbC1zY2FsZT0xLG1pbmltdW0tc2NhbGU9MSxtYXhpbXVtLXNjYWxlPTEsdXNlci1zY2FsYWJsZT1ubyI+PC9oZWFkPjxib2R5PjxkaXYgc3R5bGU9Im1hcmdpbi10b3A6IDUwJTttYXJnaW4tbGVmdDogLTE0cHg7dGV4dC1hbGlnbjogY2VudGVyO2NvbG9yOiAjOTk5O2ZvbnQtc2l6ZTogMTRweDsiPuivt+eCueWHu+mihOiniOaMiemSrjwvZGl2PjwvYm9keT48L2h0bWw+";
            vm.getInfo(id);
        },
		saveOrUpdate: function (event) {
            if(vm.dataDictionaries.key==null||vm.dataDictionaries.key.trim()==""){
                alert("关键字不能为空", {time: 1000, icon:2});
                return;
            }

			if(vm.dataDictionaries.type==2){
                vm.dataDictionaries.value=ue.getContent();
                ue.setContent("");
            }
			var url = vm.dataDictionaries.id == null ? "admin/datadictionaries/save" : "admin/datadictionaries/update";
			$.ajax({
				type: "POST",
				url: baseURL + url,
				contentType: "application/x-www-form-urlencoded;charset=utf-8",
				data: vm.dataDictionaries,
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
				    url: baseURL + "admin/datadictionaries/delete",
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
			$.get(baseURL + "admin/datadictionaries/info/"+id, function(r){
                vm.dataDictionaries = r.dataDictionaries;

                if(vm.dataDictionaries.type==2){
                    ue.setContent( r.dataDictionaries.value);
                }
            });
		},
		reload: function (event) {
			vm.showList = true;
            ue.setContent("");
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		}
	}
});