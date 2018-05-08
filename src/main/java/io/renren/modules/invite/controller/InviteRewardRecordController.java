package io.renren.modules.invite.controller;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;
import io.renren.modules.invite.entity.InviteRewardRecordEntity;
import io.renren.modules.invite.service.InviteRewardRecordService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * 发放记录
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-03-20 10:14:28
 */
@RestController
@RequestMapping("/invite/inviterewardrecord")
public class InviteRewardRecordController {
	@Autowired
	private InviteRewardRecordService inviteRewardRecordService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("invite:inviterewardrecord:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<InviteRewardRecordEntity> inviteRewardRecordList = inviteRewardRecordService.queryList(query);
		int total = inviteRewardRecordService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(inviteRewardRecordList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}

//	@RequestMapping("/infobymaster")
//	@RequiresPermissions("invite:inviterewardrecord:list")
//	public R infoByMaster(@RequestParam Long master){
//		List<InviteRewardRecordEntity> inviteRewardRecordEntities = inviteRewardRecordService.pageByMaster(master, null, null);
//
//		return R.ok().put("data", inviteRewardRecordEntities);
//	}
}
