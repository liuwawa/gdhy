package io.renren.modules.admin.controller;

import io.renren.common.annotation.SysLog;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;
import io.renren.modules.admin.entity.EverydaySignEntity;
import io.renren.modules.admin.service.EverydaySignService;
import io.renren.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 签到奖励
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-01-04 10:36:14
 */
@RestController
@RequestMapping("/admin/everydaysign")
public class EverydaySignController extends AbstractController {
	@Autowired
	private EverydaySignService everydaySignService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("admin:everydaysign:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<EverydaySignEntity> everydaySignList = everydaySignService.queryList(query);
		int total = everydaySignService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(everydaySignList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{days}")
	@RequiresPermissions("admin:everydaysign:info")
	public R info(@PathVariable("days") Integer days){
		EverydaySignEntity everydaySign = everydaySignService.queryObject(days);
		
		return R.ok().put("everydaySign", everydaySign);
	}

	/**
	 * 修改
	 */
	@SysLog("修改签到奖励")
	@RequestMapping("/update")
	@RequiresPermissions("admin:everydaysign:update")
	public R update(@RequestBody EverydaySignEntity everydaySign){
		everydaySign.setModifyUser(getUserId());
		everydaySignService.update(everydaySign);
		return R.ok();
	}

}
