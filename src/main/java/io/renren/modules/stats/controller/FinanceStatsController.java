package io.renren.modules.stats.controller;

import io.renren.common.utils.DateUtils;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;
import io.renren.modules.stats.entity.FinanceStatsEntity;
import io.renren.modules.stats.service.FinanceStatsService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;


/**
 * 财务支出情况
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-02-05 11:20:29
 */
@RestController
@RequestMapping("/stats/financestats")
public class FinanceStatsController {
	@Autowired
	private FinanceStatsService financeStatsService;


	@RequestMapping("/range")
	@RequiresPermissions("stats:financestats:range")
	public R range(@RequestParam String type,
				   @RequestParam String dateRange) throws ParseException {
		String[] range = dateRange.split(" - ");
		if(range.length!=2){
			return R.error("日期范围有误");
		}

		List<FinanceStatsEntity> list=null;

		switch (type){
			case "HOUR":
				list =financeStatsService.RangeByHour(type
						, DateUtils.format(DateUtils.parse(range[0])).split(" ")[0]
						, DateUtils.format(DateUtils.parse(range[1])).split(" ")[0]
						,DateUtils.parse(range[0]).getHours(),DateUtils.parse(range[1]).getHours());
				break;
			case "YEAR":
				list =financeStatsService.RangeByCustom(type,range[0],range[1],"than_year");
				break;
			case "MONTH":
				list =financeStatsService.RangeByCustom(type,range[0],range[1],"than_day");
				break;
			case "DAY":
				list =financeStatsService.RangeByCustom(type,range[0],range[1],"than_day");
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
	@RequiresPermissions("stats:financestats:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<FinanceStatsEntity> financeStatsList = financeStatsService.queryList(query);
		int total = financeStatsService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(financeStatsList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("stats:financestats:info")
	public R info(@PathVariable("id") Integer id){
		FinanceStatsEntity financeStats = financeStatsService.queryObject(id);
		
		return R.ok().put("financeStats", financeStats);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("stats:financestats:save")
	public R save(@RequestBody FinanceStatsEntity financeStats){
		financeStatsService.save(financeStats);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("stats:financestats:update")
	public R update(@RequestBody FinanceStatsEntity financeStats){
		financeStatsService.update(financeStats);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("stats:financestats:delete")
	public R delete(@RequestBody Integer[] ids){
		financeStatsService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
