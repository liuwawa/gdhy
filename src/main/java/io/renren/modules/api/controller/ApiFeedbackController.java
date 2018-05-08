package io.renren.modules.api.controller;

import com.dingtalk.chatbot.DingtalkChatbotClient;
import com.dingtalk.chatbot.message.MarkdownMessage;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.R;
import io.renren.modules.admin.entity.CommentListEntity;
import io.renren.modules.admin.service.CommentListService;
import io.renren.modules.api.annotation.Login;
import io.renren.modules.api.dao.DeviceInfoDao;
import io.renren.modules.api.entity.DeviceInfo;
import org.apache.shiro.codec.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 用户反馈
 * Created by ITMX on 2017/12/20.
 */
@RestController
@RequestMapping("/api/feedback")
public class ApiFeedbackController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DingtalkChatbotClient dingtalkChatbotClient;
    @Value("${private.chatbot-webhook}")
    private String chatbotWebhook;

    @Autowired
    private CommentListService commentListService;
    @Autowired
    private DeviceInfoDao deviceInfoDao;


    /**
     *
     * @param phone 手机号
     * @param imgurl  图片地址用分号分割
     * @param opinion 意见
     * @param app_edition  app版本
     * @param sys_edition 系统版本
     * @param sys_name 系统名称
     * @param phone_mark  手机标识
     * @param phone_equipment_id 手机设备id
     * @return
     */
    @Login
    @PostMapping("/save")
    public R save(@RequestParam String phone,
                  @RequestParam(required = false) String imgurl,
                  @RequestParam(required = false) String opinion,
                  @RequestParam(required = false) String app_edition,
                  @RequestParam(required = false) String sys_edition,
                  @RequestParam(required = false) String sys_name,
                  @RequestParam(required = false) String phone_mark,
                  @RequestParam(required = false) String phone_equipment_id,
                  @RequestAttribute("userId") Long userId,
                  @RequestAttribute(value = "deviceId",required = false) String deviceId,
                  @RequestAttribute(value = "version",required = false) String version){
        CommentListEntity commentListEntity = new CommentListEntity();
        commentListEntity.setImgUrl(imgurl);
        commentListEntity.setOpinion(opinion);
        commentListEntity.setPhone(phone);
        commentListEntity.setAppEdition(app_edition);
        commentListEntity.setSysEdition(sys_edition);
        commentListEntity.setSysName(sys_name);
        commentListEntity.setPhoneMark(phone_mark);
        commentListEntity.setPhoneEquipmentId(phone_equipment_id);
        commentListEntity.setSubmitTime(new Date());
        commentListEntity.setUserId(userId);
        commentListEntity.setDelFlag(0);
        commentListEntity.setIsSolve(0);
        commentListService.save(commentListEntity);

        try {
            //推送到钉钉群
            MarkdownMessage message = new MarkdownMessage();
            message.setTitle("用户反馈");
            message.add(MarkdownMessage.getHeaderText(3, "用户反馈："));
            message.add(MarkdownMessage.getReferenceText(opinion));
            if(imgurl !=null) {
                String[] split = imgurl.split(";");
                for (String s : split) {
                    message.add(MarkdownMessage.getImageText(s));
                }
            }
            message.add("* * *");//换行
            message.add("-  用户ID：" + userId);
            message.add("-  APP版本：" + version);
            message.add("-  手机号：" + phone);
            message.add("-  设备ID：" + deviceId);
            message.add("-  反馈时间：" + DateUtils.format(new Date()));
            //查询设备信息
            DeviceInfo deviceInfo = deviceInfoDao.queryObject(userId);
            String deviceInfoDetail = deviceInfo.getDeviceInfo();
            deviceInfoDetail = new String(Base64.decode(deviceInfoDetail));
            message.add("-  设备信息：" + deviceInfoDetail);

            dingtalkChatbotClient.send(chatbotWebhook,message);
        } catch (Exception e) {
            logger.warn("意见反馈推送到钉钉群",e);
        }
        return R.ok();
    }
}
