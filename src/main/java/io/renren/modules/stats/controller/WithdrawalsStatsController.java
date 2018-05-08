package io.renren.modules.stats.controller;

import io.renren.common.utils.DateUtils;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;
import io.renren.modules.stats.entity.WithdrawalsStatsEntity;
import io.renren.modules.stats.service.WithdrawalsStatsService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;


/**
 * 提现统计
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-02-05 11:20:29
 */
@RestController
@RequestMapping("/stats/withdrawalsstats")
public class WithdrawalsStatsController {
	@Autowired
	private WithdrawalsStatsService withdrawalsStatsService;

	@RequestMapping("/range")
	@RequiresPermissions("stats:withdrawalsstats:range")
	public R range(@RequestParam String type,
				   @RequestParam String dateRange) throws ParseException {
		String[] range = dateRange.split(" - ");
		if(range.length!=2){
			return R.error("日期范围有误");
		}

		List<WithdrawalsStatsEntity> list=null;

		switch (type){
			case "HOUR":
				list =withdrawalsStatsService.RangeByHour(type
						, DateUtils.format(DateUtils.parse(range[0])).split(" ")[0]
						, DateUtils.format(DateUtils.parse(range[1])).split(" ")[0]
						,DateUtils.parse(range[0]).getHours(),DateUtils.parse(range[1]).getHours());
				break;
			case "YEAR":
				list =withdrawalsStatsService.RangeByCustom(type,range[0],range[1],"than_year");
				break;
			case "MONTH":
				list =withdrawalsStatsService.RangeByCustom(type,range[0],range[1],"than_day");
				break;
			case "DAY":
				list =withdrawalsStatsService.RangeByCustom(type,range[0],range[1],"than_day");
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
	@RequiresPermissions("stats:withdrawalsstats:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<WithdrawalsStatsEntity> withdrawalsStatsList = withdrawalsStatsService.queryList(query);
		int total = withdrawalsStatsService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(withdrawalsStatsList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("stats:withdrawalsstats:info")
	public R info(@PathVariable("id") Integer id){
		WithdrawalsStatsEntity withdrawalsStats = withdrawalsStatsService.queryObject(id);
		
		return R.ok().put("withdrawalsStats", withdrawalsStats);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("stats:withdrawalsstats:save")
	public R save(@RequestBody WithdrawalsStatsEntity withdrawalsStats){
		withdrawalsStatsService.save(withdrawalsStats);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("stats:withdrawalsstats:update")
	public R update(@RequestBody WithdrawalsStatsEntity withdrawalsStats){
		withdrawalsStatsService.update(withdrawalsStats);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("stats:withdrawalsstats:delete")
	public R delete(@RequestBody Integer[] ids){
		withdrawalsStatsService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
