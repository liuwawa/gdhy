package io.renren.common.exception;

import com.dingtalk.chatbot.DingtalkChatbotClient;
import com.dingtalk.chatbot.message.MarkdownMessage;
import com.google.gson.Gson;
import io.renren.common.utils.*;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Map;

/**
 * 异常处理器
 *
 * Created by ITMX on 2017/12/6.
 */
@RestControllerAdvice
public class RRExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private DingtalkChatbotClient dingtalkChatbotClient;
	@Value("${private.chatbot-webhook}")
	private String chatbotWebhook;

	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler(RRException.class)
	public R handleRRException(RRException e){
		return R.error(e.getCode(),e.getMessage());
	}

	/**
	 * 数据库数据重复异常
	 * @param e
	 * @return
	 */
	@ExceptionHandler(DuplicateKeyException.class)
	public R handleDuplicateKeyException(DuplicateKeyException e){
		logger.error(e.getMessage(), e);
		try {
			//推送到钉钉群
			MarkdownMessage message = new MarkdownMessage();
			message.setTitle("抛了个异常");
			message.add(MarkdownMessage.getHeaderText(3, "数据重复异常："));
			HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
			message.add(MarkdownMessage.getReferenceText(e.getClass().getName() + "：" + e.getMessage()));

			message.add("* * *");//换行
			message.add("-  URL：" + request.getRequestURL().toString());
			message.add("-  IP：" + IPUtils.getIpAddr(request));
			message.add("-  时间：" + DateUtils.format(new Date()));
			message.add("-  数据：" + new Gson().toJson(request.getParameterMap()));

			dingtalkChatbotClient.send(chatbotWebhook,message);
		} catch (Exception e1) {
			logger.warn("数据重复异常推送到钉钉群",e1);
		}
		return R.error(MsgStatus.DATA_DUPLICATION);
	}

	/**
	 * 越权异常
	 * @param e
	 * @return
	 */
	@ExceptionHandler(AuthorizationException.class)
	public R handleAuthorizationException(AuthorizationException e){
		logger.error(e.getMessage(), e);
		return R.error(MsgStatus.UNAUTHORIZED);
	}

	/**
	 * http请求方法异常
	 * @param e
	 * @return
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public R handleInvalidMethodException(HttpRequestMethodNotSupportedException e){
		logger.error(e.getMessage(), e);
		try {
			//推送到钉钉群
			MarkdownMessage message = new MarkdownMessage();
			message.setTitle("抛了个异常");
			message.add(MarkdownMessage.getHeaderText(3, "HTTP请求方法异常："));
			HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
			message.add(MarkdownMessage.getReferenceText(e.getClass().getName() + "：" + e.getMessage()));

			message.add("* * *");//换行
			message.add("-  URL：" + request.getRequestURL().toString());
			message.add("-  IP：" + IPUtils.getIpAddr(request));
			message.add("-  时间：" + DateUtils.format(new Date()));
			message.add("-  数据：" + new Gson().toJson(request.getParameterMap()));

			dingtalkChatbotClient.send(chatbotWebhook,message);
		} catch (Exception e1) {
			logger.warn("HTTP请求方法异常推送到钉钉群",e1);
		}
		return R.error(MsgStatus.INVALID_METHOD);
	}

	/**
	 * 其它异常
	 * @param e
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public R handleException(Exception e){
		HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
		Map<String, String[]> parameterMap = request.getParameterMap();
		String params = new Gson().toJson(parameterMap);
		logger.error(e.getMessage() + ";params: " + params,e);
		try {
			//推送到钉钉群
			MarkdownMessage message = new MarkdownMessage();
			message.setTitle("抛了个异常");
			message.add(MarkdownMessage.getHeaderText(3, "其它异常："));
			message.add(MarkdownMessage.getReferenceText(e.getClass().getName() + "：" + e.getMessage()));

			message.add("* * *");//换行
			message.add("-  URL：" + request.getRequestURL().toString());
			message.add("-  IP：" + IPUtils.getIpAddr(request));
			message.add("-  时间：" + DateUtils.format(new Date()));
			message.add("-  数据：" + params);
			dingtalkChatbotClient.send(chatbotWebhook,message);
		} catch (Exception e1) {
			logger.warn("其它异常推送到钉钉群",e1);
		}
		return R.error(MsgStatus.INTERNAL_SERVER_ERROR);
	}
}
