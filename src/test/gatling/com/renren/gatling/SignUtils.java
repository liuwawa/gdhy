package com.renren.gatling;

import org.apache.commons.lang.StringUtils;
import org.asynchttpclient.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ITMX on 2018/1/25.
 */
public class SignUtils {

    private static Logger logger = LoggerFactory.getLogger(SignUtils.class);

    /**
     * 对map内的数据进行签名
     * @param info
     * @param secret 密钥：如key=115b63e80e7644c1a0df8d2d20e25f54
     * @return
     */
    public static String doSign(String secret,List<Param> info) {

        info.sort(Comparator.comparing(arg0 -> (arg0.getName())));

        StringBuilder sb = new StringBuilder();

        for (Param entry : info) {
            String entryKey = entry.getName();
            String entryValue = entry.getValue();

            //除去sign字段和空字段
            if ("sign".equals(entryKey) || entryValue == null || StringUtils.isEmpty(entryValue)) {
                continue;
            }
            sb.append(entryKey);
            sb.append("=");
            sb.append(entryValue);
            sb.append("&");
        }
        logger.debug("待签名参数：" + sb.toString());

        if (StringUtils.isEmpty(secret)) {
            sb.deleteCharAt(sb.length()-1);//删除最后一个&
        } else{
            sb.append(secret);//将密钥加入签名
        }

        String md5 = getMD5(sb.toString());
        logger.debug("计算出签名：" + md5);
        return md5;
    }

    /**
     * 对字符串md5加密
     *
     * @param str
     * @return
     */
    public static String getMD5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            String sign = new BigInteger(1, md.digest()).toString(16);
            //不足32位则0填充
            return "00000000000000000000000000000000".substring(0,32 - sign.length()) + sign;
        } catch (Exception e) {
            logger.error("MD5签名计算失败",e);
            return "";
        }
    }
}
