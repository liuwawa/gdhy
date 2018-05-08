package io.renren.modules.admin.controller;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;
import io.renren.common.utils.TaskClassification;
import io.renren.modules.admin.entity.TaskRecordEntity;
import io.renren.modules.admin.service.TaskRecordService;
import io.renren.modules.stats.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 任务记录
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2017-12-24 17:59:05
 */
@RestController
@RequestMapping("/admin/taskrecord")
public class TaskRecordController {
	@Autowired
	private TaskRecordService taskRecordService;
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("admin:taskrecord:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<TaskRecordEntity> taskRecordList = taskRecordService.queryList(query);
		int total = taskRecordService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(taskRecordList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}

	@RequestMapping("/info")
	public R info(@RequestParam("id") Long id){
		String date= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

		HashMap<String, Map> data = new HashMap<>();
		for (TaskClassification.CODE code:TaskClassification.CODE.values()) {

			Integer sum = taskRecordService.querySumReward(id,code.getCode() , null,null);
			Integer todaySum = taskRecordService.querySumReward(id, code.getCode(),date ,null);
			Map<String, Integer> sumMap = new HashMap();
			sumMap.put("sum",sum==null?0:sum);
			sumMap.put("todaySum",todaySum==null?0:todaySum);

			data.put(code.getName(),sumMap);
		}
		return R.ok().put("data",data);
	}

	/**
	 * 汇总某天的24个时段
	 * @return
	 */
	@RequestMapping("/SummaryOneDay")
	public R SummaryOneDay(@RequestParam(required = false,value = "time") String time,@RequestParam("user_id") Long userId){
		Date date;
		if(StringUtils.isBlank(time)){
			date=new Date();
		}else{
			try {
				date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
			} catch (ParseException e) {
				e.printStackTrace();
				return R.error("时间格式错误");
			}
		}

		List<Integer> list=new ArrayList<>();
		for (int i=0;i<24;i++){
			Integer sum = taskRecordService.querySumReward(userId, null, DateUtils.getHoursByNum(date, i),DateUtils.getHoursByNum(date, i+1));
			list.add(sum==null?0:sum);
		}
		return R.ok().put("list",list);
	}
}
