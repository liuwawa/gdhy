package io.renren.modules.api.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.google.gson.Gson;
import io.renren.common.exception.RRException;
import io.renren.common.utils.*;
import io.renren.config.AlipayConfig;
import io.renren.modules.admin.entity.AppUpdateEntity;
import io.renren.modules.admin.service.AppUpdateService;
import io.renren.modules.admin.service.DataDictionariesService;
import io.renren.modules.api.annotation.Login;
import io.renren.modules.api.annotation.LoginUser;
import io.renren.modules.api.dao.DeviceInfoDao;
import io.renren.modules.api.entity.DeviceInfo;
import io.renren.modules.api.entity.UploadType;
import io.renren.modules.api.entity.UserEntity;
import io.renren.modules.api.entity.UserOssEntity;
import io.renren.modules.api.service.UserOssService;
import io.renren.modules.api.service.UserService;
import io.renren.modules.api.utils.AppLogUtils;
import io.renren.modules.api.utils.SmsUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Created by ITMX on 2017/12/7.
 */
@RestController
@RequestMapping("/api/utils")
public class ApiUtilsController {

    @Autowired
    private SmsUtils smsUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private UserOssService userOssService;
    @Autowired
    private AlipayConfig alipayConfig;
    @Autowired
    private AppLogUtils appLogUtils;
    @Autowired
    private DataDictionariesService dataDictionariesService;
    @Autowired
    private AppUpdateService appUpdateService;
    @Autowired
    private RedisCacheUtils redisCacheUtils;
    @Autowired
    private DeviceInfoDao deviceInfoDao;

    @Value("${private.sms.smsTimeout}")
    private long smsTimeout;

    /**
     * 发送短信验证码
     * @param phone
     * @param type
     * @return
     */
    @PostMapping("sendSmsCode")
    public R sendSmsCode(@RequestParam String phone,
                         @RequestParam SmsUtils.SMS_TYPE type,
                         @LoginUser UserEntity user){

        if("".equals(phone) && user != null){
            phone = user.getPhone();
            if(phone == null){
                return R.error("请先绑定手机号");
            }
        }

        int sendResult = -1;
        switch (type){
            case SMS_LOGIN:
                sendResult = smsUtils.smsLogin(phone);
                break;
            case USER_REG:
                UserEntity userEntity = userService.queryByPhone(phone);
                if(userEntity!=null){
                    return R.error(MsgStatus.ALREADY_REGISTERED);
                }
                sendResult = smsUtils.userReg(phone);
                break;
            case MODIFY_OR_FIND_PASS:
                sendResult = smsUtils.modifyOrFindPass(phone);
                break;
            case MODIFY_PHONE:
                sendResult = smsUtils.modifyPhone(phone);
                break;
            case WITHDRAWALS:
                sendResult = smsUtils.withdrawals(phone);
                break;
            default:
                appLogUtils.e(AppLogUtils.LOG_TYPE.UTILS, AppLogUtils.OPERATION.SEND_SMS,"短信发送类型错误");
                throw new RRException(MsgStatus.NOT_SUPPORT_SMS_TYPE);
        }

        if(sendResult == 1) {
            appLogUtils.i(AppLogUtils.LOG_TYPE.UTILS, AppLogUtils.OPERATION.SEND_SMS,"短信发送成功：" + phone);
            return R.ok("发送成功");
        }else if(sendResult == -2){
            appLogUtils.w(AppLogUtils.LOG_TYPE.UTILS, AppLogUtils.OPERATION.SEND_SMS,"短信发送频率过高：" + phone);
            return R.error("短信发送频率过高，请稍后再试");
        }else if(sendResult == 0){
            appLogUtils.w(AppLogUtils.LOG_TYPE.UTILS, AppLogUtils.OPERATION.SEND_SMS,"短信发送失败：" + phone);
            return R.error("发送失败，请检查手机号码");
        }else {
            appLogUtils.e(AppLogUtils.LOG_TYPE.UTILS, AppLogUtils.OPERATION.SEND_SMS,"短信发送异常：" + phone);
            return R.error("发送异常");
        }
    }

    /**
     * 检查验证码是否正确接口
     * @param smsCode
     * @return
     */
    @Login
    @PostMapping("verifySms")
    public R verifySms(@RequestParam String smsCode,
                       @RequestParam SmsUtils.SMS_TYPE smsType,
                       @LoginUser UserEntity user){
        if(user.getPhone() == null){
            appLogUtils.e(AppLogUtils.LOG_TYPE.UTILS, AppLogUtils.OPERATION.VERIFY_SMS,"未绑定手机号",user.getUserId());
            return R.error("未绑定手机号");
        }

        //拿出验证码
        String code = redisCacheUtils.get("SMS:" + smsType + "_" + user.getPhone());
        if(code == null){
            appLogUtils.e(AppLogUtils.LOG_TYPE.UTILS, AppLogUtils.OPERATION.VERIFY_SMS,"未发送验证码",user.getUserId());
            return R.error("请先发送验证码");
        }

        //检查尝试次数是否过高
        Integer failCount = redisCacheUtils.get("VERIFY_SMS_FAIL:" + user.getPhone());
        if(failCount == null){
            failCount = 0;
        }else if(failCount >= 3){
            redisCacheUtils.set("VERIFY_SMS_FAIL:" + user.getPhone(),++failCount,smsTimeout);
            appLogUtils.w(AppLogUtils.LOG_TYPE.UTILS, AppLogUtils.OPERATION.VERIFY_SMS,"验证码错误次数过多",user.getUserId());
            return R.error("验证码错误次数过多，请重新发送验证码");
        }

        boolean equals = Objects.equals(code, smsCode);
        if(!equals){
            redisCacheUtils.set("VERIFY_SMS_FAIL:" + user.getPhone(),++failCount,smsTimeout);
            appLogUtils.w(AppLogUtils.LOG_TYPE.UTILS, AppLogUtils.OPERATION.VERIFY_SMS,"验证码有误",user.getUserId());
            return R.error("验证码有误");
        }
        return R.ok("验证码正确");
    }

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    @Login
    public R upload(@RequestParam("file") MultipartFile file,
                    @RequestParam UploadType type,
                    @RequestAttribute("userId") Long userId) {

        if (file.isEmpty()) {
            appLogUtils.e(AppLogUtils.LOG_TYPE.UTILS, AppLogUtils.OPERATION.UPLOAD_FILE,"文件上传失败：缺少文件，上传类型：" + String.valueOf(type),userId);
            throw new RRException(MsgStatus.EMPTY_FILE);
        }

        UserOssEntity userOssEntity;

        try {
            userOssEntity =  userOssService.uploadFile(file, type, userId);
        } catch (IOException e) {
            appLogUtils.e(AppLogUtils.LOG_TYPE.UTILS, AppLogUtils.OPERATION.UPLOAD_FILE, "多文件上传失败，IO异常", userId);
            return R.error("上传失败，请重试或更换图片后再试");
        }

        appLogUtils.i(AppLogUtils.LOG_TYPE.UTILS, AppLogUtils.OPERATION.UPLOAD_FILE,"文件上传成功：" + userOssEntity.getId(),userId);
        return R.ok("上传成功").put("url", userOssEntity.getUrl());
    }

    /**
     * 多图上传
     */
    @PostMapping("/imgs_upload")
    @Login
    public R imgsUpload(@RequestParam("file") MultipartFile[] file,
                        @RequestParam UploadType type,
                        @RequestAttribute("userId") Long userId) {
        List<String> urls = new ArrayList<>();
        try {
            for (MultipartFile aFile : file) {
                if (aFile.isEmpty()) {
                    appLogUtils.e(AppLogUtils.LOG_TYPE.UTILS, AppLogUtils.OPERATION.UPLOAD_FILE, "文件上传失败：缺少文件，上传类型：" + String.valueOf(type), userId);
                    throw new RRException(MsgStatus.EMPTY_FILE);
                }

                UserOssEntity userOssEntity = userOssService.uploadFile(aFile, type, userId);
                urls.add(userOssEntity.getUrl());
            }
        } catch (IOException e) {
            appLogUtils.e(AppLogUtils.LOG_TYPE.UTILS, AppLogUtils.OPERATION.UPLOAD_FILE, "多文件上传失败，IO异常", userId);
            return R.error("上传失败，请重试或更换图片后再试");
        }
        appLogUtils.i(AppLogUtils.LOG_TYPE.UTILS, AppLogUtils.OPERATION.UPLOAD_FILE, "多文件上传成功：" + new Gson().toJson(urls), userId);
        return R.ok("上传成功").put("urls", urls);
    }

    /**
     * 前端支付宝登录取参
     * @return
     */
    @PostMapping("/alipayLoginParam")
    public R alipayLoginParam(){
        try {
            HashMap<String, String> param = new HashMap<>();
            param.put("apiname","com.alipay.account.auth");
            param.put("method","alipay.open.auth.sdk.code.get");
            param.put("app_id",alipayConfig.getAppId());
            param.put("app_name","mc");
            param.put("biz_type","openservice");
            param.put("pid",alipayConfig.getPid());
            param.put("product_id","APP_FAST_LOGIN");
            param.put("scope","kuaijie");
            String target_id = UUID.randomUUID().toString().replace("-", "");
            param.put("target_id", target_id);
            param.put("auth_type","AUTHACCOUNT");
            param.put("sign_type","RSA2");

            String rsaSign = AlipaySignature.rsaSign(param, alipayConfig.getAppPrivateKey(), alipayConfig.getCharset());
            return R.ok("获取成功").put("rsaSign",rsaSign).put("target_id",target_id);
        } catch (AlipayApiException e) {
            appLogUtils.w(AppLogUtils.LOG_TYPE.UTILS, AppLogUtils.OPERATION.UPLOAD_FILE,"");
            throw new RRException("签名失败",e);
        }
    }

    /**
     * 数据字典查询
     * @return
     */
    @PostMapping("/dataDictionaries")
    public R dataDictionaries(@RequestParam String keys,
                              @RequestParam String version){
        String[] split;
        if(keys.contains(",")){
            split = keys.split(",");
        }else{
            split = new String[1];
            split[0] = keys;
        }
        ArrayList<String> list = new ArrayList<>();
        for(String key : split) {
            String val = dataDictionariesService.queryByKey(key,version);
            if(val == null) {
                list.add("");
            }else {
                list.add(val);
            }
        }

        return R.ok().put("data",list);
    }

    /**
     * 检查更新接口
     * @return
     */
    @PostMapping("/checkUpdate")
    public R checkUpdate(@RequestParam String version,
                         @RequestParam(required = false,defaultValue = "dzkandian") String appId,
                         @RequestAttribute(required = false) Long userId,
                         @RequestParam(required = false,defaultValue = "1") Integer versionCode){

        if(userId != null && userId == 95){
            AppUpdateEntity appUpdateEntity = appUpdateService.queryObject(4);
            return R.ok().put("lastVersion",appUpdateEntity);
        }

        AppUpdateEntity lastVersion = appUpdateService.getLastVersion(appId);
        //如果用户版本比lastVersion高或相等，则不返回更新信息
        if(lastVersion != null && VersionCompare.compare(version,lastVersion.getVersion()) >= 0){
            lastVersion = null;
        }
        if(lastVersion != null && versionCode == 1){
            lastVersion.setVersionCode(2);
        }
        return R.ok().put("lastVersion",lastVersion);
    }

    /**
     * 上传设备信息
     * @param info
     * @param imei
     * @param user
     * @return
     */
    @PostMapping("/uploadDeviceInfo")
    public R uploadDeviceInfo(@RequestParam String info,
                              @RequestParam(required = false,defaultValue = "1.0.0") String version,
                              @RequestParam(value = "deviceId",required = false,defaultValue = "") String imei,
                              @LoginUser UserEntity user){
        String level = dataDictionariesService.queryByKey("移动端安全级别", version);
        //如果不为数字
        if(!StringUtils.isNumeric(level)){
            return R.ok().put("level",Integer.parseInt(level));
        }

        if(StringUtils.isBlank(imei) && user == null){
            return R.ok();
        }

        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setUserId(user == null?0:user.getUserId());
        deviceInfo.setMd5(SignUtils.getMD5(info));
        //根据用户id和md5查询是否有该用户的设备信息
        Integer count = deviceInfoDao.queryCountByUserIdAndMd5(deviceInfo);
        //数据库中没有该用户的设备信息
        if(count==null){
            deviceInfo.setCreateTime(new Date());
            deviceInfo.setDeviceInfo(info);
            deviceInfo.setImei(imei);
            deviceInfoDao.save(deviceInfo);
        }
        return R.ok().put("level",Integer.parseInt(level));
    }

    @Value("${private.img-gen-salt}")
    private String imgGenSalt;
    @GetMapping("/imgGen")
    public void imgGen(@RequestParam String img,
                       @RequestParam String link,
                       @RequestParam String pos,
                       @RequestParam String sign,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException {


        String href = request.getRequestURL() + "?" + request.getQueryString();
        href = href.substring(0,href.indexOf("&sign="));//移除查询参数最后的签名字段

        //签名检查
        if(!Objects.equals(sign,SignUtils.getMD5(href + imgGenSalt))){
            return;
        }

        //拼接文件保存地址
        String filePath = "target/images/" + SignUtils.getMD5(img) + ".png";
        File file = new File(filePath);
        //检查文件夹是否存在
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        //检查文件是否存在
        if(!file.exists()){
            UrlResource bgResource = new UrlResource(img);
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            InputStream inputStream = bgResource.getInputStream();
            IOUtils.copy(inputStream,fileOutputStream);
            inputStream.close();
            fileOutputStream.close();
        }
        //生成二维码
        BufferedImage qrImg = new QRCodeUtils().encoderQRCoder(link);
        //打开背景图片
        FileInputStream fileInputStream = new FileInputStream(file);
        Image bgImg = ImageIO.read(fileInputStream);
        fileInputStream.close();
        BufferedImage resultImg = new BufferedImage(bgImg.getWidth(null), bgImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
        //获得坐标
        Graphics2D g = resultImg.createGraphics();
        g.drawImage(bgImg, 0, 0, null);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1));
        String[] split = pos.split(",");
        g.drawImage(qrImg, Integer.parseInt(split[0]), Integer.parseInt(split[1]), qrImg.getWidth(null), qrImg.getHeight(null), null);
        g.dispose();
        ImageIO.write(resultImg, "png", response.getOutputStream());
    }
}
