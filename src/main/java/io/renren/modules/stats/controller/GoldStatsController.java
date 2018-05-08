package io.renren.modules.stats.controller;

import io.renren.common.utils.DateUtils;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;
import io.renren.modules.stats.entity.GoldStatsEntity;
import io.renren.modules.stats.service.GoldStatsService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;


/**
 * 金币领取情况
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-02-05 11:20:29
 */
@RestController
@RequestMapping("/stats/goldstats")
public class GoldStatsController {
	@Autowired
	private GoldStatsService goldStatsService;


	@RequestMapping("/range")
	@RequiresPermissions("stats:goldstats:range")
	public R range(@RequestParam String type,
				   @RequestParam String dateRange) throws ParseException {
		String[] range = dateRange.split(" - ");
		if(range.length!=2){
			return R.error("日期范围有误");
		}

		List<GoldStatsEntity> list=null;

		switch (type){
			case "HOUR":
				list =goldStatsService.RangeByHour(type
						, DateUtils.format(DateUtils.parse(range[0])).split(" ")[0]
						, DateUtils.format(DateUtils.parse(range[1])).split(" ")[0]
						,DateUtils.parse(range[0]).getHours(),DateUtils.parse(range[1]).getHours());
				break;
			case "YEAR":
				list =goldStatsService.RangeByCustom(type,range[0],range[1],"than_year");
				break;
			case "MONTH":
				list =goldStatsService.RangeByCustom(type,range[0],range[1],"than_day");
				break;
			case "DAY":
				list =goldStatsService.RangeByCustom(type,range[0],range[1],"than_day");
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
	@RequiresPermissions("stats:goldstats:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<GoldStatsEntity> goldStatsList = goldStatsService.queryList(query);
		int total = goldStatsService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(goldStatsList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("stats:goldstats:info")
	public R info(@PathVariable("id") Integer id){
		GoldStatsEntity goldStats = goldStatsService.queryObject(id);
		
		return R.ok().put("goldStats", goldStats);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("stats:goldstats:save")
	public R save(@RequestBody GoldStatsEntity goldStats){
		goldStatsService.save(goldStats);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("stats:goldstats:update")
	public R update(@RequestBody GoldStatsEntity goldStats){
		goldStatsService.update(goldStats);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("stats:goldstats:delete")
	public R delete(@RequestBody Integer[] ids){
		goldStatsService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
