$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'generator/gold/list',
        datatype: "json",
        colModel: [			
			{ label: 'goldId', name: 'goldId', index: 'gold_id', width: 50, key: true },
			{ label: '剩余金币', name: 'surplusGold', index: 'surplus_gold', width: 80 }, 			
			{ label: '总金币', name: 'total', index: 'total', width: 80 }, 			
			{ label: '', name: 'userId', index: 'user_id', width: 80 }			
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
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
        }
    });
});



Vue.component('update-gold',
{
	template:'#update_gold',
	data: function() {
					return {
						sub_gold:'30',
						total_gold:'50'
					}
				},
			
});


//全局组件
///////////////////////////////////////////1,创建主键构造器
let profile = Vue.extend({
	//模板选项
	template: '<div><input type="date"/><p>今天已经是冬天了</p></div>'
});

//2,构建一个全局组件
Vue.component('my-date',profile);

///////////////////////////////////////////



/////////////////////////////////////3，父子组件
let child1 = Vue.extend({
	
});
let child2 = Vue.extend({
	
});
//父组件构造器
Vue.component('parent',{
	components:{
		'my-child1':child1,
		'my-child2':child2
	},
	template:'<div><my-child1></my-child1><my-child2></my-child2></div>'
});

///////////////////////////////////////////
			

///////////////////////////////////////////////////在div外面创建template

Vue.component('my-div',{
	template:'#my_div',
	props: ['a','b'],
	data(){
		return {
			msg: 'nihao',
			error: 'henbuhao'
		}
	}
});

////////////////////////////////////////////////////			
			
			
//////////////////////////////////////////////多层组件通信
let child3 = Vue.extend({
	template:'#my_title',
	props: ['title']
});
let child4 = Vue.extend({
	template:'#my_img',
	props: ['img']
});



//2,注册父组件
Vue.component('my-parent',{
	props: ['imgtitle','imgsrc'],
	components: {
		'child3': child3,
		'child4': child4
	},
	template: '#my_parent'
});

//////////////////////////////////////////////






var vm = new Vue({
	el:'#rrapp',

	components:{			//局部构造器
		'my-color':{
	//模板选项
	template: '<div><input type="color"><p>今天已经是夏天了</p></div>'
}
	},
	data:{
		showList: true,
		title: null,
		gold: {},
		modal:true,
		title: 'henbucuo',
		img: 'http'
		
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.gold = {};
		},
		update: function (event) {
			var goldId = getSelectedRow();
			if(goldId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(goldId)
		},
		saveOrUpdate: function (event) {
			var url = vm.gold.goldId == null ? "generator/gold/save" : "generator/gold/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.gold),
			    success: function(r){
			    	if(r.code === 0){
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
			var goldIds = getSelectedRows();
			if(goldIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "generator/gold/delete",
                    contentType: "application/json",
				    data: JSON.stringify(goldIds),
				    success: function(r){
						if(r.code == 0){
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
		getInfo: function(goldId){
			$.get(baseURL + "generator/gold/info/"+goldId, function(r){
                vm.gold = r.gold;
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