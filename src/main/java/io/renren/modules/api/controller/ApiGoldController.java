package io.renren.modules.api.controller;

import io.renren.common.utils.R;
import io.renren.modules.admin.entity.GoldEntity;
import io.renren.modules.admin.entity.GoldExchangeEntity;
import io.renren.modules.admin.entity.WithdrawalsEntity;
import io.renren.modules.admin.service.DataDictionariesService;
import io.renren.modules.admin.service.GoldExchangeService;
import io.renren.modules.admin.service.GoldService;
import io.renren.modules.admin.service.WithdrawalsService;
import io.renren.modules.api.annotation.Login;
import io.renren.modules.api.annotation.LoginUser;
import io.renren.modules.api.entity.UserEntity;
import io.renren.modules.api.utils.AppLogUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * Created by ITMX on 2018/1/2.
 */
@RestController
@RequestMapping("/api/gold")
public class ApiGoldController {

    @Autowired
    private AppLogUtils appLogUtils;
    @Autowired
    private GoldService goldService;
    @Autowired
    private GoldExchangeService goldExchangeService;
    @Autowired
    private WithdrawalsService withdrawalsService;
    @Autowired
    private DataDictionariesService dataDictionariesService;

    /**
     * 获取我的金币
     * @param userId
     * @return
     */
    @Login
    @PostMapping("mine")
    public R mine(@RequestAttribute Long userId){

        GoldEntity goldEntity = goldService.queryObject(userId);

        return R.ok().put("data",goldEntity);
    }

    /**
     * 获取兑换选项列表
     * @param user
     * @return
     */
    @Login
    @PostMapping("exchangeList")
    public R exchangeList(@LoginUser UserEntity user,
                          @RequestParam(required = false,defaultValue = "1.0.0") String version){

        List<GoldExchangeEntity> list = goldExchangeService.queryList(Collections.emptyMap());
        //查询用户是否提现过 提现状态：-1已关闭，1处理中，2已完成
        Integer count = withdrawalsService.queryCountByUserIdAndStatus(user.getUserId(),2);
        //提现过
        if(count!=null&&count!=0){
            //对查询的list集合进行处理，去除100rmb该对象
            for (int i = 0; i < list.size(); i++) {
                if(list.get(i).getRmb()==100){
                    list.remove(i);
                }
            }
        }
        GoldEntity goldEntity = goldService.queryObject(user.getUserId());

        String tip = dataDictionariesService.queryByKey("提现提示", version);

        return R.ok().put("list",list)
                .put("surplus",goldEntity.getSurplus())
                .put("alipayAccount",user.getAlipayAccount())
                .put("alipayName",user.getAlipayName())
                .put("weixinPayName",user.getWeixinPayName())
                .put("weixinPayPhone",user.getWeixinPayPhone())
                .put("tip",tip);
    }

    /**
     * 发起提现
     * @param rmb
     * @param channel
     * @param user
     * @return
     */
    @Login
    @PostMapping("withdrawals")
    public R withdrawals(@RequestParam Integer rmb,
                         @RequestParam String channel,
                         @LoginUser UserEntity user){

        //检查用户是否绑定手机号
        if(StringUtils.isEmpty(user.getPhone())){
            return R.error("请先绑定手机号码");
        }

        List<WithdrawalsEntity> count = withdrawalsService.pageByUser(user.getUserId(), 1, 1, 1);
        if(count!=null && count.size() > 0){
            return R.error("兑换失败，请等待上一订单处理完成后再发起提现");
        }

        Integer result = null;
        try {
            result = withdrawalsService.createNewEntity(rmb, channel, user);
        } catch (DuplicateKeyException e) {
            appLogUtils.w(AppLogUtils.LOG_TYPE.GOLD, AppLogUtils.OPERATION.GOLD_WITHDRAWALS,"提现" + rmb/100.0 + "元失败，订单号重复",user.getUserId());
            return R.error("提现失败，订单号重复，请重试");
        }
        if(result == 1){
            appLogUtils.i(AppLogUtils.LOG_TYPE.GOLD, AppLogUtils.OPERATION.GOLD_WITHDRAWALS,"提现" + rmb/100.0 + "元",user.getUserId());
            return R.ok();
        }
        String err = "";
        switch (result){
            case -100:
                err = "成长值不足";
                break;
            case -200:
                err = "剩余提现份数不足";
                break;
            case -300:
                err = "当日提现次数过多";
                break;
            case -400:
            case -500:
                err = "扣除用户成长值不成功";
                break;
            case -600:
            case -700:
                err = "插入提现记录不成功";
                break;
            case -800:
            case -900:
                err = "扣除剩余提现份数不成功";
                break;
            case -90:
                err = "提现金额有误，请重试";
                break;
            default:
                err = "未知异常：" + result;
        }

        appLogUtils.e(AppLogUtils.LOG_TYPE.GOLD, AppLogUtils.OPERATION.GOLD_WITHDRAWALS,"提现" + rmb/100.0 + "元失败，" + err,user.getUserId(),Integer.toString(result));

        if(result == -300){
            return R.error("今日提现次数过多");
        }

        return R.error("提现失败");
    }

    /**
     * 我的提现记录
     * @return
     */
    @Login
    @PostMapping("withdrawalsList")
    public R withdrawalsList(@RequestAttribute Long userId,
                             @RequestParam(required = false) Integer status,
                             @RequestParam Integer page,
                             @RequestParam Integer limit){
        List<WithdrawalsEntity> list = withdrawalsService.pageByUser(userId, status, page, limit);
        return R.ok().put("list",list);
    }
}
