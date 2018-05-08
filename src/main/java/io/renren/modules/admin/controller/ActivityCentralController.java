package io.renren.modules.admin.controller;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;
import io.renren.modules.admin.entity.ActivityCentralEntity;
import io.renren.modules.admin.service.ActivityCentralService;
import io.renren.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 活动中心
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-01-11 17:47:17
 */
@RestController
@RequestMapping("/admin/activitycentral")
public class ActivityCentralController extends AbstractController {
	@Autowired
	private ActivityCentralService activityCentralService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("admin:activitycentral:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<ActivityCentralEntity> activityCentralList = activityCentralService.queryList(query);
		int total = activityCentralService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(activityCentralList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("admin:activitycentral:info")
	public R info(@PathVariable("id") Long id){
		ActivityCentralEntity activityCentral = activityCentralService.queryObject(id);
		
		return R.ok().put("activityCentral", activityCentral);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("admin:activitycentral:save")
	public R save(@RequestBody ActivityCentralEntity activityCentral){
		activityCentral.setDelFlag(0);
		activityCentral.setCreateTime(new Date());
		activityCentral.setCreateUser(getUserId()+"");
		activityCentralService.save(activityCentral);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("admin:activitycentral:update")
	public R update(@RequestBody ActivityCentralEntity activityCentral){
		activityCentral.setModifyTime(new Date());
		activityCentral.setModifyUser(getUserId()+"");
		activityCentralService.update(activityCentral);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("admin:activitycentral:delete")
	public R delete(@RequestBody Long[] ids){
		activityCentralService.deleteBatch(ids);
		return R.ok();
	}
	
}
