package io.renren.modules.admin.controller;

import java.util.List;
import java.util.Map;

import io.renren.modules.admin.entity.CommentEntity;
import io.renren.modules.admin.service.CommentService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;




/**
 * 
 * 
 * @author xieweilie
 * @date 2018-03-20 16:35:14
 */
@RestController
@RequestMapping("/admin/usercomment")
public class UsercommentController {

	//评论服务接口
	@Autowired
	private CommentService commentService;



	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("admin:usercomment:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<CommentEntity> usercommentList = commentService.queryList(query);
		int total = commentService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(usercommentList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{cid}")
	@RequiresPermissions("admin:usercomment:info")
	public R info(@PathVariable("cid") Integer cid){
		CommentEntity usercomment = commentService.queryObject(cid);
		
		return R.ok().put("usercomment", usercomment);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("admin:usercomment:save")
	public R save(@RequestBody CommentEntity usercomment){
		commentService.save(usercomment);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("admin:usercomment:update")
	public R update(@RequestBody CommentEntity usercomment){
		commentService.update(usercomment);
		
		return R.ok();
	}
	
	/**
	 * 删除就是将该评论的删除标识符改为1
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("admin:usercomment:delete")
	public R delete(@RequestBody Integer[] cids){

		commentService.deleteBatch(cids);
		
		return R.ok();
	}

	/**
	 * 释放标志为删除的文章，将删除标志改为0
	 */
	@RequestMapping("/release")
	@RequiresPermissions("admin:usercomment:release")
	public R release(@RequestBody Integer[] cids){

		commentService.releaseBatch(cids);
		return R.ok();
	}






}
