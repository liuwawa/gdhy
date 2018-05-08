package io.renren.modules.job.task;

import io.renren.modules.admin.service.GoldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by ITMX on 2018/2/10.
 */
@Component("goldResetTask")
public class GoldResetTask {

    @Autowired
    private GoldService goldService;

    public void resetToday(){
        goldService.resetToday();
    }
}
