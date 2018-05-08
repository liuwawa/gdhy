package io.renren.modules.admin.controller;

import io.renren.common.annotation.SysLog;
import io.renren.common.utils.MsgStatus;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;
import io.renren.modules.admin.entity.WithdrawalsEntity;
import io.renren.modules.admin.service.WithdrawalsService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 提现记录
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-01-08 09:09:41
 */
@RestController
@RequestMapping("/admin/withdrawals")
public class WithdrawalsController {
	@Autowired
	private WithdrawalsService withdrawalsService;

    private static Logger logger = LoggerFactory.getLogger(WithdrawalsController.class);

    @RequestMapping("/sumByUser/{userId}")
	@RequiresPermissions("admin:withdrawals:info")
    public R sumByUser(@PathVariable("userId") Long userId){
		Integer sum = withdrawalsService.sumByUser(userId,null);
		Integer todaysum = withdrawalsService.sumByUser(userId,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

		return R.ok().put("sum",sum == null ? 0 : sum).put("todaysum",todaysum == null ? 0 : todaysum);
	}

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("admin:withdrawals:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<WithdrawalsEntity> withdrawalsList = withdrawalsService.queryList(query);
		int total = withdrawalsService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(withdrawalsList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("admin:withdrawals:info")
	public R info(@PathVariable("id") String id){
		WithdrawalsEntity withdrawals = withdrawalsService.queryObject(id);
		
		return R.ok().put("withdrawals", withdrawals);
	}

	@SysLog("允许提现")
	@PostMapping("allow")
	@RequiresPermissions("admin:withdrawals:allow")
	public R allow(@RequestParam String id,HttpServletRequest request){

		//TODO 检查操作IP，是否异地操作
//		String ipAddr = IPUtils.getIpAddr(request);
//		IPUtils.checkIP(ipAddr);

		//查询订单
		WithdrawalsEntity entity = withdrawalsService.queryObject(id);
		if(entity == null){
			String err = "订单不存在";
			logger.error(err);
			return R.error(err);
		}

		//判断订单状态
		if(entity.getStatus() != 1){
			String err = "此订单非处理中状态";
			logger.error(err + "："+ id);
			return R.error(MsgStatus.WITHDRAWALS_EXCEPTION.value(), err);
		}

		if(entity.getRmb()<100){
			String err = "单笔提现最低1元";
			logger.error(err);
			return R.error(err);
		}

		//判断提现方式
		if("alipay".equals(entity.getChannel())){
			//支付宝提现
			withdrawalsService.useOfAlipay(entity);
		}else if("weixinPay".equals(entity.getChannel())){
			//微信零钱提现
			withdrawalsService.useOfWeixinPay(entity);
		}else{
			String err = "不支持的提现方式：" + entity.getChannel();
			logger.error(err);
			return R.error(err);
		}

		return R.ok();
	}

	/**
	 * 拒绝提现申请
	 */
	@SysLog("关闭提现申请")
	@RequestMapping("/close")
	@RequiresPermissions("admin:withdrawals:close")
	public R close(@RequestParam String oid,@RequestParam String closeMsg){
		String[] ids = oid.split(",");
		for (String id : ids) {
			withdrawalsService.close(id,closeMsg);
		}
		return R.ok();
	}

}
