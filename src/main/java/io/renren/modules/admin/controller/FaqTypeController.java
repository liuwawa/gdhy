package io.renren.modules.admin.controller;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;
import io.renren.modules.admin.entity.FaqTypeEntity;
import io.renren.modules.admin.service.FaqTypeService;
import io.renren.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 问题分类
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2017-12-20 16:43:30
 */
@RestController
@RequestMapping("/admin/faqtype")
public class FaqTypeController extends AbstractController{
	@Autowired
	private FaqTypeService faqTypeService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("admin:faqtype:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<FaqTypeEntity> faqTypeList = faqTypeService.queryList(query);
		int total = faqTypeService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(faqTypeList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("admin:faqtype:info")
	public R info(@PathVariable("id") Integer id){
		FaqTypeEntity faqType = faqTypeService.queryObject(id);
		
		return R.ok().put("faqType", faqType);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("admin:faqtype:save")
	public R save(@RequestBody FaqTypeEntity faqType){
		faqType.setCreateUser(getUserId());
		faqTypeService.save(faqType);
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("admin:faqtype:update")
	public R update(@RequestBody FaqTypeEntity faqType){
		faqType.setModifyUser(getUserId());
		faqTypeService.update(faqType);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("admin:faqtype:delete")
	public R delete(@RequestBody Integer[] ids){
		faqTypeService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
