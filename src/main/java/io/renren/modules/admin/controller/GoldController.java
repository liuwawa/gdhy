package io.renren.modules.admin.controller;

import io.renren.common.annotation.SysLog;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;
import io.renren.modules.admin.entity.GoldEntity;
import io.renren.modules.admin.service.GoldService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * 用户金币
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2017-12-24 17:59:05
 */
@RestController
@RequestMapping("/admin/gold")
public class GoldController {
	@Autowired
	private GoldService goldService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("admin:gold:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<GoldEntity> goldList = goldService.queryList(query);
		int total = goldService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(goldList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("admin:gold:info")
	public R info(@PathVariable("id") Long id){
		GoldEntity gold = goldService.queryObject(id);
		
		return R.ok().put("gold", gold);
	}

	/**
	 * 修改用户剩余金币
	 */
	@SysLog("增加/扣除用户金币")
	@RequestMapping("/modify/{id}")
	@RequiresPermissions("admin:gold:modify")
	public R modify(@PathVariable("id") Long id,
					@RequestParam Integer increase,
					@RequestParam String tip){

		int result = goldService.modifySurplusGold(id,increase,tip);
		switch (result){
			case 1:
				return R.ok();
			case 0:
				return R.error("未做任何操作");
			case -100:
				return R.error("扣除金币失败，扣除金币大于用户剩余金币");
			case -200:
				return R.error("操作失败，金币变动记录插入失败");
			case -400:
			case -500:
				return R.error("操作失败，修改用户剩余金币失败");
		}

		return R.error("扣除失败：" + result);
	}

}
