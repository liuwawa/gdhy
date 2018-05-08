package io.renren.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2016年10月27日 下午9:59:27
 */
public class R extends HashMap<String, Object> {

	public R() {
		put("code", MsgStatus.OK.value());
		put("status", true);
		put("msg", MsgStatus.OK.getReasonPhrase());
		put("timestamp",System.currentTimeMillis());
	}
	
	public static R error() {
		return error(MsgStatus.INTERNAL_SERVER_ERROR);
	}

	public static R error(MsgStatus msgStatus) {
		return error(msgStatus.value(),msgStatus.getReasonPhrase());
	}

	public static R error(String msg) {
		return error(MsgStatus.INTERNAL_SERVER_ERROR.value(), msg);
	}
	
	public static R error(int code, String msg) {
		R r = new R();
		r.put("status",false);
		r.put("code", code);
		r.put("msg", msg);
		return r;
	}

	public static R ok(String msg) {
		R r = new R();
		r.put("msg", msg);
		return r;
	}
	
	public static R ok(Map<String, Object> map) {
		R r = new R();
		r.putAll(map);
		return r;
	}
	
	public static R ok() {
		return new R();
	}

	public R put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
