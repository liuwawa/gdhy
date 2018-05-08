package io.renren.modules.admin.controller;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;
import io.renren.modules.admin.entity.FaqEntity;
import io.renren.modules.admin.service.FaqService;
import io.renren.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 问题列表
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2017-12-20 16:43:30
 */
@RestController
@RequestMapping("/admin/faq")
public class FaqController extends AbstractController{
	@Autowired
	private FaqService faqService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("admin:faq:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<FaqEntity> faqList = faqService.queryList(query);
		int total = faqService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(faqList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("admin:faq:info")
	public R info(@PathVariable("id") Integer id){
		FaqEntity faq = faqService.queryObject(id);
		
		return R.ok().put("faq", faq);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("admin:faq:save")
	public R save(@RequestBody FaqEntity faq){
		faq.setCreateUser(getUserId());
		faqService.save(faq);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("admin:faq:update")
	public R update(@RequestBody FaqEntity faq){
		faq.setModifyUser(getUserId());
		faqService.update(faq);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("admin:faq:delete")
	public R delete(@RequestBody Integer[] ids){
		faqService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
