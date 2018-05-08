package io.renren.modules.invite.utils;

import io.renren.modules.invite.entity.InviteRewardRuleEntity;

/**
 * Created by ITMX on 2018/4/19.
 */
public class InviteRewardRuleUtils {

    public static Integer getTotal(InviteRewardRuleEntity entity) {
        return entity.getFirstReward()
                + entity.getDay1()
                + entity.getDay2()
                + entity.getDay3()
                + entity.getDay4()
                + entity.getDay5()
                + entity.getDay6()
                + entity.getDay7();
    }

    public static Integer getRewardByDay(InviteRewardRuleEntity entity,Integer nextReward) {
        switch (nextReward){
            case 0:
                return entity.getFirstReward();
            case 1:
                return entity.getDay1();
            case 2:
                return entity.getDay2();
            case 3:
                return entity.getDay3();
            case 4:
                return entity.getDay4();
            case 5:
                return entity.getDay5();
            case 6:
                return entity.getDay6();
            case 7:
                return entity.getDay7();
        }
        return 0;
    }
}
