package io.renren.modules.invite.controller;

import io.renren.common.utils.DateUtils;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;
import io.renren.modules.api.service.UserService;
import io.renren.modules.invite.entity.InviteRecordEntity;
import io.renren.modules.invite.service.InviteRecordService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 收徒记录
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-03-19 21:46:21
 */
@RestController
@RequestMapping("/invite/inviterecord")
public class InviteRecordController {
	@Autowired
	private InviteRecordService inviteRecordService;

	@Autowired
	private UserService userService;
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("invite:inviterecord:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<InviteRecordEntity> inviteRecordList = inviteRecordService.queryList(query);
		int total = inviteRecordService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(inviteRecordList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 查询一个用户的师傅信息和收徒统计
	 * @param apprentice
	 * @return
	 */
	@RequestMapping("/mymaster")
	@RequiresPermissions("invite:inviterecord:list")
	public R infoByMaster(@RequestParam Long apprentice){
		InviteRecordEntity mymaster = inviteRecordService.queryByApprentice(apprentice);
		Map<String, Object> map = new HashMap<>();
		map.put("master",apprentice);
		//查询累计收徒数
		int inviteTotal = inviteRecordService.queryTotal(map);
		//查询今日收徒数
		int todayInviteCount = inviteRecordService.SummaryInvitationsNum(DateUtils.format(DateUtils.getToday()), DateUtils.format(new Date(DateUtils.getToday().getTime() + 86400000)));
		return R.ok().put("mymaster",mymaster)
				.put("inviteTotal",inviteTotal)
				.put("todayInviteCount",todayInviteCount);
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("invite:inviterecord:delete")
	public R delete(@RequestBody Long[] apprentice){
		inviteRecordService.deleteBatch(apprentice);
		
		return R.ok();
	}
	
}
