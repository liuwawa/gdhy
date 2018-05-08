package io.renren.common.exception;

import io.renren.common.utils.MsgStatus;

/**
 * 自定义异常
 *
 * Created by ITMX on 2017/12/6.
 */
public class RRException extends RuntimeException {

    private String msg;
    private int code = MsgStatus.INTERNAL_SERVER_ERROR.value();
    
    public RRException(String msg) {
		super(msg);
		this.msg = msg;
	}
	
	public RRException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}
	
	public RRException(String msg, int code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}
	
	public RRException(String msg, int code, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}

	public RRException(MsgStatus msgStatus) {
		super(msgStatus.getReasonPhrase());
		this.msg = msgStatus.getReasonPhrase();
		this.code = msgStatus.value();
	}

	public RRException(MsgStatus msgStatus,Throwable e) {
		super(msgStatus.getReasonPhrase(),e);
		this.msg = msgStatus.getReasonPhrase();
		this.code = msgStatus.value();
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	
}
