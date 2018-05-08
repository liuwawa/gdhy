package io.renren.modules.admin.controller;

import java.util.List;
import java.util.Map;

import io.renren.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.admin.entity.AppUpdateEntity;
import io.renren.modules.admin.service.AppUpdateService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;


/**
 * 版本更新
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-02-07 11:06:17
 */
@RestController
@RequestMapping("/admin/appupdate")
public class AppUpdateController extends AbstractController {
	@Autowired
	private AppUpdateService appUpdateService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("admin:appupdate:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<AppUpdateEntity> appUpdateList = appUpdateService.queryList(query);
		int total = appUpdateService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(appUpdateList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("admin:appupdate:info")
	public R info(@PathVariable("id") Integer id){
		AppUpdateEntity appUpdate = appUpdateService.queryObject(id);
		
		return R.ok().put("appUpdate", appUpdate);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("admin:appupdate:save")
	public R save(@RequestBody AppUpdateEntity appUpdate){
		appUpdate.setCreateUser(getUserId());
		appUpdateService.save(appUpdate);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("admin:appupdate:update")
	public R update(@RequestBody AppUpdateEntity appUpdate){
		appUpdate.setModifyUser(getUserId());
		appUpdateService.update(appUpdate);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("admin:appupdate:delete")
	public R delete(@RequestBody Integer[] ids){
		appUpdateService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
