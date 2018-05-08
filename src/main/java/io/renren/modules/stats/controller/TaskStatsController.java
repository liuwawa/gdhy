package io.renren.modules.stats.controller;

import io.renren.common.utils.DateUtils;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;
import io.renren.modules.stats.entity.TaskStatsEntity;
import io.renren.modules.stats.service.TaskStatsService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;


/**
 * 任务活跃情况
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-02-05 11:20:29
 */
@RestController
@RequestMapping("/stats/taskstats")
public class TaskStatsController {
	@Autowired
	private TaskStatsService taskStatsService;


	@RequestMapping("/range")
	@RequiresPermissions("stats:taskstats:range")
	public R range(@RequestParam String type,
				   @RequestParam String dateRange) throws ParseException {
		String[] range = dateRange.split(" - ");
		if(range.length!=2){
			return R.error("日期范围有误");
		}

		List<TaskStatsEntity> list=null;

		switch (type){
			case "HOUR":
				list =taskStatsService.RangeByHour(type
						, DateUtils.format(DateUtils.parse(range[0])).split(" ")[0]
						, DateUtils.format(DateUtils.parse(range[1])).split(" ")[0]
						,DateUtils.parse(range[0]).getHours(),DateUtils.parse(range[1]).getHours());
				break;
			case "YEAR":
				list =taskStatsService.RangeByCustom(type,range[0],range[1],"than_year");
				break;
			case "MONTH":
				list =taskStatsService.RangeByCustom(type,range[0],range[1],"than_day");
				break;
			case "DAY":
				list =taskStatsService.RangeByCustom(type,range[0],range[1],"than_day");
				break;
			default:
				return R.error("系统繁忙");
		}
		return R.ok().put("list",list);
	}

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("stats:taskstats:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<TaskStatsEntity> taskStatsList = taskStatsService.queryList(query);
		int total = taskStatsService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(taskStatsList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("stats:taskstats:info")
	public R info(@PathVariable("id") Integer id){
		TaskStatsEntity taskStats = taskStatsService.queryObject(id);
		
		return R.ok().put("taskStats", taskStats);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("stats:taskstats:save")
	public R save(@RequestBody TaskStatsEntity taskStats){
		taskStatsService.save(taskStats);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("stats:taskstats:update")
	public R update(@RequestBody TaskStatsEntity taskStats){
		taskStatsService.update(taskStats);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("stats:taskstats:delete")
	public R delete(@RequestBody Integer[] ids){
		taskStatsService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
