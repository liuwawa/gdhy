package io.renren.modules.admin.controller;

import io.renren.common.annotation.SysLog;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;
import io.renren.modules.admin.entity.TaskListEntity;
import io.renren.modules.admin.service.TaskListService;
import io.renren.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 任务列表
 *
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2017-12-27 12:56:40
 */
@RestController
@RequestMapping("/admin/tasklist")
public class TaskListController extends AbstractController {
	@Autowired
	private TaskListService taskListService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("admin:tasklist:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<TaskListEntity> taskListList = taskListService.queryList(query);
		int total = taskListService.queryTotal(query);
		PageUtils pageUtil = new PageUtils(taskListList, total, query.getLimit(), query.getPage());
		return R.ok().put("page", pageUtil);
	}

	@RequestMapping("/all")
	@RequiresPermissions("admin:tasklist:list")
	public R all(){
		List<TaskListEntity> taskListList = taskListService.queryList(Collections.emptyMap());
		Map<Integer,String> typeId2Name = new HashMap<>();
		for (TaskListEntity type : taskListList) {
			typeId2Name.put(type.getId(),type.getName());
		}
		return R.ok().put("list",typeId2Name);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("admin:tasklist:info")
	public R info(@PathVariable("id") Integer id){
		TaskListEntity taskList = taskListService.queryObject(id);
		return R.ok().put("taskList", taskList);
	}
	
	/**
	 * 保存
	 */
	@SysLog("添加任务")
	@RequestMapping("/save")
	@RequiresPermissions("admin:tasklist:save")
	public R save(@RequestBody TaskListEntity taskList){
		taskList.setCreateUser(getUserId());
		taskListService.save(taskList);
		return R.ok();
	}

	@SysLog("激活任务")
	@RequestMapping("/activated")
	@RequiresPermissions("admin:tasklist:activated")
	public R activated(@RequestParam Integer id ,@RequestParam Boolean flag){

		TaskListEntity entity = taskListService.queryObject(id);
		entity.setActivated(flag?1:0);
		taskListService.update(entity);

		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改任务")
	@RequestMapping("/update")
	@RequiresPermissions("admin:tasklist:update")
	public R update(@RequestBody TaskListEntity taskList){
		taskList.setModifyUser(getUserId());
		taskListService.update(taskList);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@SysLog("删除任务")
	@RequestMapping("/delete")
	@RequiresPermissions("admin:tasklist:delete")
	public R delete(@RequestBody Integer[] ids){
		taskListService.deleteBatch(ids);
		return R.ok();
	}
	
}
