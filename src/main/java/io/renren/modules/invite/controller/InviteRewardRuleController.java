package io.renren.modules.invite.controller;

import java.util.List;
import java.util.Map;

import io.renren.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.invite.entity.InviteRewardRuleEntity;
import io.renren.modules.invite.service.InviteRewardRuleService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;


/**
 * 奖励规则
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-03-19 14:27:31
 */
@RestController
@RequestMapping("/invite/inviterewardrule")
public class InviteRewardRuleController extends AbstractController {
	@Autowired
	private InviteRewardRuleService inviteRewardRuleService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("invite:inviterewardrule:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<InviteRewardRuleEntity> inviteRewardRuleList = inviteRewardRuleService.queryList(query);
		int total = inviteRewardRuleService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(inviteRewardRuleList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("invite:inviterewardrule:info")
	public R info(@PathVariable("id") Integer id){
		InviteRewardRuleEntity inviteRewardRule = inviteRewardRuleService.queryObject(id);
		
		return R.ok().put("inviteRewardRule", inviteRewardRule);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("invite:inviterewardrule:save")
	public R save(@RequestBody InviteRewardRuleEntity inviteRewardRule){
		inviteRewardRule.setCreateUser(getUserId());
		inviteRewardRuleService.save(inviteRewardRule);
		return R.ok();
	}
}
