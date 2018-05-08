package io.renren.modules.admin.controller;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;
import io.renren.modules.admin.entity.GoldExchangeEntity;
import io.renren.modules.admin.service.GoldExchangeService;
import io.renren.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 金币兑换
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-01-08 09:09:41
 */
@RestController
@RequestMapping("/admin/goldexchange")
public class GoldExchangeController extends AbstractController {
	@Autowired
	private GoldExchangeService goldExchangeService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("admin:goldexchange:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<GoldExchangeEntity> goldExchangeList = goldExchangeService.queryList(query);
		int total = goldExchangeService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(goldExchangeList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{rmb}")
	@RequiresPermissions("admin:goldexchange:info")
	public R info(@PathVariable("rmb") Integer rmb){
		GoldExchangeEntity goldExchange = goldExchangeService.queryObject(rmb);
		
		return R.ok().put("goldExchange", goldExchange);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("admin:goldexchange:save")
	public R save(@RequestBody GoldExchangeEntity goldExchange){
		goldExchange.setCreateUser(getUserId());
		goldExchangeService.save(goldExchange);
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("admin:goldexchange:update")
	public R update(@RequestBody GoldExchangeEntity goldExchange){
		goldExchange.setModifyUser(getUserId());
		goldExchangeService.update(goldExchange);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("admin:goldexchange:delete")
	public R delete(@RequestBody Integer[] rmbs){
		goldExchangeService.deleteBatch(rmbs);
		
		return R.ok();
	}
	
}
