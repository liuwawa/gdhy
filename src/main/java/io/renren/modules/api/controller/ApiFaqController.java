package io.renren.modules.api.controller;

import io.renren.common.utils.R;
import io.renren.modules.admin.entity.FaqEntity;
import io.renren.modules.admin.entity.FaqTypeEntity;
import io.renren.modules.admin.service.FaqService;
import io.renren.modules.admin.service.FaqTypeService;
import io.renren.modules.api.annotation.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * 常见问题
 * Created by ITMX on 2017/12/20.
 */
@RestController
@RequestMapping("/api/faq")
public class ApiFaqController {

    @Autowired
    private FaqTypeService faqTypeService;
    @Autowired
    private FaqService faqService;

    /**
     * 分页获取常见问题分类列表
     * @param page
     * @param limit
     * @return
     */
    @Login
    @PostMapping("/type")
    public R type(@RequestParam Integer page,
                  @RequestParam Integer limit){
        if(page <= 0 ) page = 1;
        if(limit <= 0 ) limit = 10;

        HashMap<String, Object> map = new HashMap<>();
        map.put("offset",(page - 1) * limit);
        map.put("limit",limit);
        map.put("sidx","sort");
        map.put("order","ASC");
        List<FaqTypeEntity> list = faqTypeService.queryList(map);
        return R.ok("获取成功").put("list",list);
    }

    /**
     * 分页获取某分类下的问题列表
     * @param type
     * @param page
     * @param limit
     * @param version
     * @return
     */
    @Login
    @PostMapping("/question")
    public R question(@RequestParam Integer type,
                      @RequestParam Integer page,
                      @RequestParam Integer limit,
                      @RequestParam String version){
        if(page <= 0 ) page = 1;
        if(limit <= 0 ) limit = 10;
        List<FaqEntity> list = faqService.queryByType(type, page, limit);

        return R.ok("获取成功").put("list",list);
    }
}
