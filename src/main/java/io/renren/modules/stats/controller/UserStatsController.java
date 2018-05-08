package io.renren.modules.stats.controller;

import io.renren.common.utils.DateUtils;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;
import io.renren.modules.invite.service.InviteRecordService;
import io.renren.modules.stats.entity.UserStatsEntity;
import io.renren.modules.stats.service.UserStatsService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;


/**
 * 用户增长情况
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-02-05 11:20:29
 */
@RestController
@RequestMapping("/stats/userstats")
public class UserStatsController {
	@Autowired
	private UserStatsService userStatsService;
	@Autowired
	private InviteRecordService inviteRecordService;

	@RequestMapping("/range")
	@RequiresPermissions("stats:userstats:range")
	public R range(@RequestParam String type,
				   @RequestParam String dateRange) throws ParseException {
		String[] range = dateRange.split(" - ");
		if(range.length!=2){
			return R.error("日期范围有误");
		}

		List<UserStatsEntity> list=null;

		switch (type){
			case "HOUR":
					list =userStatsService.RangeByHour(type
						, DateUtils.format(DateUtils.parse(range[0])).split(" ")[0]
						, DateUtils.format(DateUtils.parse(range[1])).split(" ")[0]
						,DateUtils.parse(range[0]).getHours(),DateUtils.parse(range[1]).getHours());
				break;
			case "YEAR":
				list =userStatsService.RangeByCustom(type,range[0],range[1],"than_year");
				break;
			case "MONTH":
				list =userStatsService.RangeByCustom(type,range[0],range[1],"than_day");
				break;
			case "DAY":
				list =userStatsService.RangeByCustom(type,range[0],range[1],"than_day");
				break;
		}
		return R.ok().put("list",list);
	}

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("stats:userstats:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<UserStatsEntity> userStatsList = userStatsService.queryList(query);
		int total = userStatsService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(userStatsList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("stats:userstats:info")
	public R info(@PathVariable("id") Integer id){
		UserStatsEntity userStats = userStatsService.queryObject(id);
		
		return R.ok().put("userStats", userStats);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("stats:userstats:save")
	public R save(@RequestBody UserStatsEntity userStats){
		userStatsService.save(userStats);

		return R.ok();
	}
	
	/*
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("stats:userstats:update")
	public R update(@RequestBody UserStatsEntity userStats){
		userStatsService.update(userStats);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("stats:userstats:delete")
	public R delete(@RequestBody Integer[] ids){
		userStatsService.deleteBatch(ids);

		return R.ok();
	}
	
}
