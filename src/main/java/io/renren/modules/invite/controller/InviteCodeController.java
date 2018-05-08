package io.renren.modules.invite.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.invite.entity.InviteCodeEntity;
import io.renren.modules.invite.service.InviteCodeService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;


/**
 * 邀请码
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-03-18 16:28:29
 */
@RestController
@RequestMapping("/invite/invitecode")
public class InviteCodeController {
	@Autowired
	private InviteCodeService inviteCodeService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("invite:invitecode:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<InviteCodeEntity> inviteCodeList = inviteCodeService.queryList(query);
		int total = inviteCodeService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(inviteCodeList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}

	/**
	 * 启用
	 */
	@RequestMapping("/enable")
	@RequiresPermissions("invite:invitecode:enable")
	public R enable(@RequestBody Integer[] inviteCodes){
		inviteCodeService.enableBatch(inviteCodes);

		return R.ok();
	}

	/**
	 * 禁用
	 */
	@RequestMapping("/disable")
	@RequiresPermissions("invite:invitecode:disable")
	public R disable(@RequestBody Integer[] inviteCodes){
		inviteCodeService.disableBatch(inviteCodes);

		return R.ok();
	}

	/**
	 * 废弃
	 */
	@RequestMapping("/abandon")
	@RequiresPermissions("invite:invitecode:abandon")
	public R abandon(@RequestBody Integer[] inviteCodes){
		inviteCodeService.abandonBatch(inviteCodes);

		return R.ok();
	}
	
}
