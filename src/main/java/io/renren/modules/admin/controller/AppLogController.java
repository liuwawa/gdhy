package io.renren.modules.admin.controller;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;
import io.renren.modules.admin.entity.AppLogEntity;
import io.renren.modules.admin.service.AppLogService;
import io.renren.modules.api.utils.AppLogUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户日志
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2017-12-19 14:46:43
 */
@RestController
@RequestMapping("/admin/applog")
public class AppLogController {
	@Autowired
	private AppLogService appLogService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("admin:applog:list")
	public R list(@RequestParam Map<String, Object> params){

		Object temp=params.get("logtype");
		if(temp!=null&&!temp.equals("")){
			params.remove("logtype");
			params.put("type",temp);
		}
		if(params.containsKey("input_date")){
			String input_date = String.valueOf(params.get("input_date"));
			if(input_date!=null && input_date.contains(" - ")) {
				params.remove("input_date");
				String[] dates = input_date.split(" - ");
				if(dates.length != 2){
					return R.error("日期格式有误");
				}
				params.put("input_date_a",dates[0]);
				params.put("input_date_b",dates[1]);
			}
		}

		//查询列表数据
        Query query = new Query(params);


		List<AppLogEntity> userLogList = appLogService.queryList(query);
		int total = appLogService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(userLogList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("admin:applog:info")
	public R info(@PathVariable("id") String id){
		AppLogEntity userLog = appLogService.queryObject(id);
		return R.ok().put("userLog", userLog);
	}

	/**
	 * 日志类型列表和操作类型列表
	 * @return
	 */
	@RequestMapping("/enums")
	@RequiresPermissions("admin:applog:list")
	public R logType(){

		AppLogUtils.LOG_TYPE[] logTypes = AppLogUtils.LOG_TYPE.values();
		HashMap<Integer, String> logTypesMap = new HashMap<>();
		for (AppLogUtils.LOG_TYPE type:logTypes) {
			logTypesMap.put(type.getValue(),type.getName());
		}

		AppLogUtils.OPERATION[] operations = AppLogUtils.OPERATION.values();
		HashMap<Integer, String> operationsMap = new HashMap<>();
		for (AppLogUtils.OPERATION o:operations) {
			operationsMap.put(o.getValue(),o.getName());
		}

		return R.ok().put("logTypes",logTypesMap).put("operations",operationsMap);
	}
}
