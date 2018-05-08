package io.renren.modules.admin.controller;

import io.renren.common.annotation.SysLog;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;
import io.renren.modules.admin.entity.CommentListEntity;
import io.renren.modules.admin.service.CommentListService;
import io.renren.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 用户反馈信息
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2017-12-29 16:21:04
 */
@RestController
@RequestMapping("/admin/commentlist")
public class CommentListController extends AbstractController {
	@Autowired
	private CommentListService commentListService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("admin:commentlist:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<CommentListEntity> commentListList = commentListService.queryList(query);
		int total = commentListService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(commentListList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("admin:commentlist:info")
	public R info(@PathVariable("id") Long id){
		CommentListEntity commentList = commentListService.queryObject(id);
		return R.ok().put("commentList", commentList);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("admin:commentlist:save")
	public R save(@RequestBody CommentListEntity commentList){
		commentListService.save(commentList);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改用户反馈备注")
	@RequestMapping("/update")
	@RequiresPermissions("admin:commentlist:update")
	public R update(String remarks,Integer id){
		Map<String,Object> pa=new HashMap<String,Object>();
		pa.put("modify_time",new Date());
		pa.put("modify_user",getUserId());
		pa.put("remarks",remarks);
		pa.put("id",id);
		pa.put("is_solve","1");
		pa.put("limit","30");
		pa.put("page","1");
		pa.put("sidx","");
		pa.put("order","asc");
		Query query = new Query(pa);
		commentListService.update(query);
		return R.ok();
	}

	@SysLog("重置用户反馈")
	@RequestMapping("/resetfeedback")
	@RequiresPermissions("admin:commentlist:update")
	public R resetfeedback(Integer id){
		Map<String,Object> pa=new HashMap<String,Object>();
		pa.put("modify_time",new Date());
		pa.put("modify_user",getUserId());
		pa.put("remarks","");
		pa.put("id",id);
		pa.put("is_solve","0");
		pa.put("limit","30");
		pa.put("page","1");
		pa.put("sidx","");
		pa.put("order","asc");
		Query query = new Query(pa);
		commentListService.update(query);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("admin:commentlist:delete")
	public R delete(@RequestBody Long[] ids){
		commentListService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
