package io.renren.common.validator;

import io.renren.common.exception.RRException;
import io.renren.common.utils.MsgStatus;
import org.apache.commons.lang.StringUtils;

/**
 * 数据校验
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2017-03-23 15:50
 */
public abstract class Assert {

    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new RRException(message,MsgStatus.INVALID_PARAMETER.value());
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new RRException(message,MsgStatus.INVALID_PARAMETER.value());
        }
    }

    public static void minLen(String text, Integer len,String msg) {
        if (text == null || text.length() < len) {
            throw new RRException(msg,MsgStatus.INVALID_PARAMETER.value());
        }
    }
}
