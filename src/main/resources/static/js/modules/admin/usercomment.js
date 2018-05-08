$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'admin/usercomment/list',
        datatype: "json",
        colModel: [			
			{ label: '评论ID', name: 'cid', index: 'cid', width: 50, key: true },
			{ label: '用户ID', name: 'uid', index: 'uid', width: 80 ,formatter: function (value, options, row) {
                return '<a href="javascript:showUserDetail(' + value + ')">' + value + '</a>';
            }},
			{ label: '文章ID', name: 'aid', index: 'aid', width: 80},
			{ label: '用户名', name: 'uName', index: 'uName', width: 80 },
			{ label: '评论', name: 'comment', index: 'comment', width: 80 },
			{ label: '提交评论时间', name: 'commentDate', index: 'commentDate', width: 80 },

        ],
		viewrecords: false,
        height: $(window).height() - 130,
        rowNum: 30,
		rowList : [30,50,100],
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
});

var vm = new Vue({
	el:'#rrapp',
	data:{
        q:{
            aid: null,

        },
		showList: true,
		title: null,
		usercomment: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		del: function (event) {
			var cids = getSelectedRows();
			if(cids == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "admin/usercomment/delete",
                    contentType: "application/json",
				    data: JSON.stringify(cids),
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
		reload: function (event) {

			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData:{'aid': vm.q.aid,'delFlag': vm.q.delFlag},
                page:page
            }).trigger("reloadGrid");
		}
	}
});