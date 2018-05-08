package io.renren.modules.api.interceptor;

import io.jsonwebtoken.Claims;
import io.renren.common.exception.RRException;
import io.renren.common.utils.MsgStatus;
import io.renren.common.utils.SignUtils;
import io.renren.modules.api.annotation.Login;
import io.renren.modules.api.utils.JwtUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

/**
 * 权限(Token)验证
 * Created by ITMX on 2017/12/6.
 */
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${private.app_secret}")
    private String app_secret;

    @Value("${private.signTimeout}")
    private int signTimeout;

    public static final String USER_KEY = "userId";
    private String START_TIME_KEY = "startTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

//        logger.debug("请求地址：" + request.getRequestURI());

        request.setAttribute(START_TIME_KEY,System.currentTimeMillis());

        //先验证签名
        Map<String, String[]> parameterMap = request.getParameterMap();
        if(!parameterMap.containsKey("timestamp")){
            throw new RRException(MsgStatus.LOST_TIMESTAMP);
        }
        if(!parameterMap.containsKey("sign")){
            throw new RRException(MsgStatus.INVALID_SIGNATURE);
        }
        long div = System.currentTimeMillis() - Long.parseLong(request.getParameter("timestamp"));
        //检查签名是否已过期
        if(Math.abs(div) > signTimeout * 1000){
            throw new RRException(MsgStatus.INVALID_TIMESTAMP);
        }

        String sign = SignUtils.doSign(parameterMap, "app_secret=" + app_secret);
        if(!Objects.equals(request.getParameter("sign"), sign)){
            throw new RRException(MsgStatus.INVALID_SIGNATURE);
        }

        Login annotation;
        if(handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(Login.class);
        }else{
            return true;
        }

        //获取用户凭证
        String token = request.getHeader(jwtUtils.getHeader());
        if(StringUtils.isBlank(token)){
            token = request.getParameter(jwtUtils.getHeader());
        }

        if(StringUtils.isBlank(token)){//如果token是空的
            if(annotation != null){//如果存在Login注解表示必须登录
                throw new RRException(MsgStatus.INVALID_AUTHORIZED);
            }
            return true;
        }

        Claims claims = jwtUtils.getClaimByToken(token);
        if(claims == null || jwtUtils.isTokenExpired(claims.getExpiration())){
            if(annotation != null){//如果存在Login注解表示必须登录
                throw new RRException(MsgStatus.INVALID_AUTHORIZED);
            }
            return true;
        }

        //设置userId到request里，后续根据userId，获取用户信息
        request.setAttribute(USER_KEY, Long.parseLong(claims.getSubject()));

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        logger.info("请求地址：" + request.getRequestURI() + " 处理耗时: " + (System.currentTimeMillis() - (long)request.getAttribute(START_TIME_KEY)));

        super.postHandle(request, response, handler, modelAndView);
    }
}
