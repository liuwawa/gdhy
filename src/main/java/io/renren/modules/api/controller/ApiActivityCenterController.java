package io.renren.modules.api.controller;

import io.renren.common.utils.R;
import io.renren.modules.admin.entity.ActivityCentralEntity;
import io.renren.modules.admin.service.ActivityCentralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ITMX on 2017/12/7.
 */
@RestController
@RequestMapping("/api/activitycenter")
public class ApiActivityCenterController {
    @Autowired
    private ActivityCentralService activityCentralService;

    @PostMapping("list")
    public R list(@RequestParam Integer page,
                  @RequestParam Integer limit){
        Map<String,Object> query=new HashMap<>();
        query.put("offset",(page - 1) * limit);
        query.put("limit",limit);
        query.put("order","desc");
        query.put("sidx","create_time");
        List<ActivityCentralEntity> activityCentralList = activityCentralService.queryList(query);
        return R.ok().put("list",activityCentralList);
    }
}
