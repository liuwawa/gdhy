package io.renren.modules.admin.controller;

import com.alibaba.druid.util.StringUtils;
import io.renren.common.annotation.SysLog;
import io.renren.common.exception.RRException;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;
import io.renren.common.validator.Assert;
import io.renren.modules.admin.service.ManagerUserService;
import io.renren.modules.api.entity.UserEntity;
import io.renren.modules.api.service.UserService;
import io.renren.modules.api.utils.JwtUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

/**
 * 用户
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2017-12-16 15:08:17
 */
@RestController
@RequestMapping("/admin/user")
public class ManagerUserController {

	@Autowired
	private UserService userService;
	@Autowired
	private ManagerUserService managerUserService;
	@Autowired
	private JwtUtils jwtUtils;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("admin:user:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
        if(query.containsKey("diy_query_key")){
			if(!query.get("diy_query_key").equals("user_id")){
				query.put("diy_query_val","%"+query.get("diy_query_val")+"%");
			}
		}


		List<UserEntity> userList = managerUserService.queryList(query);
		int total = managerUserService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(userList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	/**
	 * 信息
	 */
	@RequestMapping("/info/{userId}")
	@RequiresPermissions("admin:user:info")
	public R info(@PathVariable("userId") Long userId){
		UserEntity user = managerUserService.queryObject(userId);
		
		return R.ok().put("user", user);
	}
	
	/**
	 * 保存
	 */
	@SysLog("新增用户")
	@RequestMapping("/save")
	@RequiresPermissions("admin:user:save")
	public R save(@RequestBody UserEntity user){
		user.setDelFlag(0);
		managerUserService.save(user);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改用户信息")
	@RequestMapping("/update")
	@RequiresPermissions("admin:user:update")
	public R update(@RequestBody UserEntity user){
		managerUserService.update(user);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@SysLog("删除用户")
	@RequestMapping("/delete")
	@RequiresPermissions("admin:user:delete")
	public R delete(@RequestBody Long[] userIds){
		managerUserService.deleteBatch(userIds);
		return R.ok();
	}

	/**
	 *
	 * @param userId
	 * @param disableMsg
	 * @param duration
	 * @return
	 */
	@SysLog("禁用用户")
	@RequiresPermissions("admin:user:disable")
	@PostMapping("/disable")
	public R disable(Long userId,String disableMsg,String duration){
		Assert.isBlank(disableMsg,"请输入被禁原因");

		Integer autoEnable = 1;
		Date autoEnableTime = null;

		GregorianCalendar calendar =new GregorianCalendar();
		Date now = new Date();
		calendar.setTime(now);
		if(StringUtils.isEmpty(duration)){
			//如果没有设置解禁时间，就不自动解禁
			autoEnable = 0;
		}else if("oneday".equals(duration)){
			calendar.add(5,+1);
			autoEnableTime = calendar.getTime();
		}else if("twoday".equals(duration)){
			calendar.add(5,+2);
			autoEnableTime = calendar.getTime();
		}else if("week".equals(duration)){
			calendar.add(5,+7);
			autoEnableTime = calendar.getTime();
		}else{
			try {
				autoEnableTime = DateUtils.parse(duration);
			} catch (ParseException e) {
				throw new RRException("日期格式有误",e);
			}
		}

		UserEntity userEntity = new UserEntity();
		userEntity.setUserId(userId);
		userEntity.setDisableMsg(disableMsg);
		userEntity.setAutoEnable(autoEnable);
		userEntity.setDisableTime(now);
		userEntity.setAutoEnableTime(autoEnableTime);
		userEntity.setDisableFlag(1);

		userService.update(userEntity);
		jwtUtils.abandoned(userId);

		return R.ok();
	}

	@SysLog("解禁用户")
	@RequiresPermissions("admin:user:enable")
	@RequestMapping("/enable")
	public R disable_button(Long userId){

		UserEntity userEntity = new UserEntity();
		userEntity.setUserId(userId);
		userEntity.setAutoEnable(0);
		userEntity.setDisableFlag(0);
		userService.update(userEntity);

		return R.ok();
	}
}
