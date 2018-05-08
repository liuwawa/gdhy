
//工具集合Tools
window.T = {};

/**
 * 获取url上的请求参数
 * 使用示例 location.href = http://localhost/index.html?id=123
 * T.p('id') --> 123;
 * @param name
 * @returns {null}
 */
T.p = function(name) {
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
};

//请求前缀
window.baseURL = "/";
window.OK = 200;

//登录token
var token = localStorage.getItem("token");
if(token == 'null'){
    parent.location.href = baseURL + 'login.html';
}

//jquery全局配置
$.ajaxSetup({
	dataType: "json",
	cache: false,
    headers: {
        "token": token
    },
    xhrFields: {
	    withCredentials: true
    },
    complete: function(xhr) {
        //token过期，则跳转到登录页面
        if(xhr.responseJSON && xhr.responseJSON.code == 401){
            parent.location.href = baseURL + 'login.html';
        }
    }
});

//jqGrid的配置信息
if($.jgrid){
    $.jgrid.defaults.width = 1000;
    $.jgrid.defaults.responsive = true;
    $.jgrid.defaults.styleUI = 'Bootstrap';

    //jqgrid全局配置
    $.extend($.jgrid.defaults, {
        ajaxGridOptions : {
            headers: {
                "token": token
            }
        }
    });
    $(function () {
        // setTimeout(function () {
        //     $("#jqGrid").setGridHeight($(window).height() - 130);
        // },500);
    })
}

/**
 * 权限判断
 * @param permission
 * @returns {boolean}
 */
function hasPermission(permission) {
    return window.parent.permissions.indexOf(permission) > -1;
}

/**
 * 重写alert
 * @param msg
 * @param callback
 */
window.alert = function(msg, callback){
	parent.layer.alert(msg, function(index){
		parent.layer.close(index);
		if(typeof(callback) === "function"){
			callback("ok");
		}
	});
};

/**
 * 重写confirm式样框
 * @param msg
 * @param callback
 */
window.confirm = function(msg, callback){
	parent.layer.confirm(msg, {btn: ['确定','取消']},
	function(){//确定事件
		if(typeof(callback) === "function"){
			callback("ok");
		}
	});
};

/**
 * 选择一条记录
 * @returns {*}
 */
function getSelectedRow() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if(!rowKey){
    	alert("请选择一条记录");
    	return ;
    }
    
    var selectedIDs = grid.getGridParam("selarrrow");
    if(selectedIDs.length == 0){
        return rowKey;
    }else if(selectedIDs.length > 1){
    	alert("只能选择一条记录");
    	return ;
    }
    
    return selectedIDs[0];
}

/**
 * 选择多条记录
 * @returns {*}
 */
function getSelectedRows() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if(!rowKey){
    	alert("请选择一条记录");
    	return ;
    }
    
    return grid.getGridParam("selarrrow");
}

/**
 * 判断是否为空
 * @param value
 * @returns {boolean}
 */
function isBlank(value) {
    return !value || !/\S/.test(value)
}

function getUUID() {
    var s = [];
    var hexDigits = "0123456789abcdef";
    for (var i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[14] = "4";
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);

    s[8] = s[13] = s[18] = s[23] = "-";

    var uuid = s.join("");
    return uuid;
}

/**
 * 显示用户信息详情
 * @param userId
 */
var showUserDetail = function(userId) {
    // layer.closeAll();
    // layer.open({
    //     type: 2,
    //     title: '',
    //     resize:false,
    //     scrollbar:false,
    //     move: true,
    //     // shadeClose: true,
    //     // shade: false,
    //     // maxmin: true, //开启最大化最小化按钮
    //     area: ['830px', '430px'],
    //     content: '/modules/userDetails/details.html?id='+userId
    // });

    var index = layer.open({
        type: 2,
        title:"dd",
        content: '/modules/admin/details.html?id='+userId,
        // area:['830px', '430px'],
        maxmin: true
    });
    layer.full(index);
    $(".layui-layer-title").remove();
};