package io.renren.modules.admin.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.google.gson.Gson;
import io.renren.common.exception.RRException;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.MsgStatus;
import io.renren.config.AlipayConfig;
import io.renren.modules.admin.Constants;
import io.renren.modules.admin.dao.WithdrawalsDao;
import io.renren.modules.admin.entity.WithdrawalsEntity;
import io.renren.modules.admin.service.WithdrawalsService;
import io.renren.modules.api.entity.UserEntity;
import io.renren.modules.api.utils.WeChatUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("withdrawalsService")
public class WithdrawalsServiceImpl implements WithdrawalsService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private WithdrawalsDao withdrawalsDao;

    @Autowired
    public WeChatUtils weChatUtils;

	@Override
	public WithdrawalsEntity queryObject(String id){
		return withdrawalsDao.queryObject(id);
	}
	
	@Override
	public List<WithdrawalsEntity> queryList(Map<String, Object> map){
		return withdrawalsDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return withdrawalsDao.queryTotal(map);
	}

	@Override
	public Integer createNewEntity(Integer rmb, String channel, UserEntity user) {

		WithdrawalsEntity withdrawalsEntity = new WithdrawalsEntity();
		if("alipay".equals(channel)) {
			withdrawalsEntity.setName(user.getAlipayName());
			withdrawalsEntity.setUserAccount(user.getAlipayAccount());
			if(StringUtils.isBlank(withdrawalsEntity.getName()) ||
					StringUtils.isBlank(withdrawalsEntity.getUserAccount())){
				throw new RRException("支付宝信息不完整");
			}
		}else if("weixinPay".equals(channel)){
			withdrawalsEntity.setName(user.getWeixinPayName());
			withdrawalsEntity.setUserAccount(user.getWeixinPayOpenid());
			withdrawalsEntity.setPhone(user.getWeixinPayPhone());
			if(StringUtils.isBlank(withdrawalsEntity.getName()) ||
					StringUtils.isBlank(withdrawalsEntity.getUserAccount()) ||
					StringUtils.isBlank(withdrawalsEntity.getPhone())){
				throw new RRException("微信钱包信息不完整");
			}
		}else{
			throw new RRException("暂不支持的提现渠道");
		}
		withdrawalsEntity.setId(DateUtils.format(new Date(),"yyyyMMddHHmmssSSS") + (int)((Math.random()*9+1)*100000));
		withdrawalsEntity.setChannel(channel);
		withdrawalsEntity.setRmb(rmb);
		withdrawalsEntity.setUserId(user.getUserId());
		withdrawalsEntity.setCreateTime(new Date());

		Map<String, Object> map;
		try {
			map = objectToMap(withdrawalsEntity);
		} catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			throw new RRException("系统繁忙");
		}
		map.put("result",0);
		withdrawalsDao.make(map);
		return MapUtils.getInteger(map,"result",0);
	}

	@Override
	public List<WithdrawalsEntity> pageByUser(Long userId, Integer status, Integer page, Integer limit) {
		return withdrawalsDao.pageByUser(userId,status,(page-1)*limit,limit);
	}

	/**
	 * 使用支付宝转账给用户
	 * @param entity
	 */
	@Override
	public void useOfAlipay(WithdrawalsEntity entity) {
		String err;

		//请使用真实的支付宝配置
		AlipayClient alipayClient = AlipayConfig.getAlipayClient();
//		DefaultAlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do",
//                "2016082700323638",
//                "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCB9Jh2Hj8bBaCwfM1ErgdfQd7zWMlAwyPwAGKIaRlpBC0NmVz+CDPRuf2+KeUumWStE7XfzJNsSnl6oqGaYBnAo7ecQEnpkLIGwfR2NUDD2kwAqIDtTZDUsZZHiMFABq4nY50TB2i2lJIB/7SP56cm/cFviKnqhreMVJ1J7U0j8Z8xZU+7bwfXbLQhIe4BP94nT9HcEUWrsU4KbarVtH++VUQrd6ndTqfBhLSq5OiHql8alYRq+uQ5eX9aqxAuEkMxvV1/Dq2M4S61Uv50o69bfXIatWcORlsQ4d8MqF1pF5Z5+jHcTymCwO1H+Ahc8mhFXsQ/4pkxQYCyiK4G07CJAgMBAAECggEAEBPeuN5NGF693pfSHWkaaGKR3rFhVK3PyGWDyzSVfDSUDlam9pa1MoE8L0ann/hCSFdj7Og2ZPNhzBZKv/qbtQWMNbyOaGMKY2OjhuxXsuNS+9HdAX7TKD1ARmFY5z7sVNsyF6SXA9+loLGt+Q/D0kMap13VB7rbAnWFcBv3qzaP+nVoCaybz4VaqkaZuj6U6AgT5XVO850Klzoj7lngYNIVFCpNaoUxt6QCd1immja74G0EOC3XbQEsYveMIsZ+p6luOBfNZ96dLet4Ue3coD+kVtyVa+Vy42iQTeFQkaTqk/w3xum2MZHFE880+eEKEbBSmD6XMSHLc3G5MRB+sQKBgQDMbm+txjHfCBwV1BoBQvHveCcysJ17+QSeXKqGd+Jgdsj+JG76mFIWo9QDOESS1kH5F+km6BMatNhAvluX3h/xLKfsq+gAtW1U8mVra4J9yl/afJ8sqh/qo7ph9NsVK5AN2DxIWJd/8UBrL+XPzrRPD6geF7lsuMmjKsIDt3wwswKBgQCivLox9puUybidzrIrnJLqj210bvtqcUEDZ2SS2+60eZwAHYiyp0m5wbFiaExJem1iX9nPpPCj6ymZaYq7DIxa6zDJj+X2/LV9rO6j6j6qfahfNf2XdJqrQLc4sLGsLC4cHazerjtaZ3Jn5q5L+ITznTMqv61sIcx28JBWnVa/0wKBgQDB+0z3fOiXtM9Jm0Ctz8NYsw+m1tYo7AsI5e9dt2ZUmIJyPUodeeny17P29i6DktF5NsojA/YU0mOdU8P/4GM462HQfvKfsYg0IepAuvxQtnasELLNpASzFndmOfylIvgHWxcgowfm/Jpe201bGc4Yc0fYfjSRyOp0bL863aH3ZwKBgGHwl1aOhl/rb1J6HTymBL1qCIO0b1dGRcxYo2zHNEglkD95htWOXI6RBcKl5pOqPL6h1SJkr/bXBrgvjrC4ApiNR1VnZw1YVZoHQ7AewAw3h+eVRMOsB5HdZueVX7iw9icxrOJvlYT5ER9685cBQHKlPzARH7weM0G50gRuuNDFAoGAY/7I6dNiCu62ex5xzhTBnHXaLt2aMPCkwGg8haTtT2HEL1X4FFdtAx6MOfqHBS4CfHR8sMd9uO4s/HBM1q11QiO48YprgRiOciiaN20Fc593RvTsxEa0+oS4u33lLA7CcL+avWwGqBjJSfGy/qPYwq4+kXxAk7Jb74J0gyl+XUs=",
//                "json",
//                "UTF-8",
//                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1qFyPbY9esKg0iXkZO0E41AV3ufBr5romoGFmOP7zYMmZJ5kR+XDgJ6dTLigTIyXhQs9zxyU08ThY3Puf55k3GXx0N53vpdOBVETiBXPOjuBSMv7ofUZCm/nOa9FsjV3+jaCabLFBM1TFyHAG+Whe8G009sTi0l8/nVrh3x5tlguplsvO8KRPN17zObEZlDBSELiLaf44xRHL5SDQLDH72bsmkvrIVhWtI9lTQYdUZnSSheNAa2sKmXWfl5hVM6YgJWU9x2Od5WD7GnieB0sB/CIXOymSO+7lXn4gvBrI60zE9AAYO68zPX6BXwZUc8yLIlk4uiBMqiTWF7QUeyxTwIDAQAB","RSA2");
		AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
		HashMap<String, String> map = new HashMap<>();
		map.put("out_biz_no",entity.getId());
		map.put("payee_type","ALIPAY_LOGONID");
		map.put("payee_account",entity.getUserAccount());
		map.put("amount", String.valueOf(entity.getRmb() / 100.0));
		map.put("payer_show_name","大众看点提现：" + map.get("amount") + "元");
		map.put("payee_real_name",entity.getName());
		map.put("remark","");

		String postData = new Gson().toJson(map);
		request.setBizContent(postData);
		AlipayFundTransToaccountTransferResponse response;
		try {
			 response = alipayClient.execute(request);
		} catch (AlipayApiException e) {
			err = "支付宝Api异常";
			logger.error(err,e);
			throw new RRException(err);
		}

		if(response.isSuccess()) {
			//检查订单号是否一致
			if (!entity.getId().equals(response.getOutBizNo())) {
				err = "[紧急情况]转账失败，订单号不一致。提现订单：" + entity.getId() +	"，支付宝返回：" + response.getOutBizNo();
				logger.error(err +  "；提交：" + postData + "，响应：" + response.getBody());
				throw new RRException(err);
			}
			//保存
			Date finishTime;
			try {
				finishTime = DateUtils.parse(response.getPayDate());
			} catch (ParseException e) {
				err = "支付完成时间格式有误";
				logger.error(err + "。" + response.getBody(),e);
				throw new RRException(err);
			}

            Integer finish = finish(response.getOutBizNo(), response.getOrderId(), finishTime);
            if(finish == 1){
				logger.info("支付宝转账成功：" + entity.getId());
				return;
			}else{
				err = "[注意]未更新提现订单状态，请检查日志";
				logger.error(err);
				throw new RRException(err);
			}
		}

		//账号错误，关闭提现订单
		if(Constants.WITHDRAWALS_USER_INFO_ERROR.contains(response.getSubCode())) {
			//TODO 请使用存储过程
			err = "用户支付宝账户有误或信息不完整：" + response.getSubMsg();
			close(entity.getId(),err);
			err = "[订单关闭]" + err;
			logger.error(err);
			throw new RRException(err,MsgStatus.WITHDRAWALS_EXCEPTION.value());
		}
		//该笔订单已经发过提现请求
		if("PAYMENT_INFO_INCONSISTENCY".equals(response.getSubCode())){
			err = "该订单已经提现过";
			logger.error(err);
			throw new RRException(err,MsgStatus.WITHDRAWALS_EXCEPTION.value());
		}
		//是否余额不足
		if("PAYER_BALANCE_NOT_ENOUGH".equals(response.getSubCode())){
			err = "[注意]公司支付宝账户余额不足，请前往支付宝平台充值";
			logger.error(err);
			throw new RRException(err);
		}
		//其它异常
		err = "支付宝转账失败：" + response.getSubMsg();
		logger.error(err + "。" + response.getBody());
		throw new RRException(err);
	}

	@Override
    public Integer finish(String id,String pay_no,  Date finishTime){
        return withdrawalsDao.finish(id,pay_no,finishTime);
    }

	/**
	 * 使用微信转账给用户
	 * @param entity
	 */
	@Override
	public void useOfWeixinPay(WithdrawalsEntity entity) {

		//封装数据
        HashMap<String, Object> paramMap = weChatUtils.createTransfersParam(entity);//拼接参数

		Map<String, String> responseHashMap;
		String error;
		try {
			responseHashMap = weChatUtils.transfers(paramMap);
		} catch (WeChatUtils.WeChatApiException e) {
			String errCode = e.getErrCode();
			if("OPENID_ERROR".equals(errCode)){//OPENID错误
				//"微信钱包授权失效，请到微信钱包重新授权"
				close(entity.getId(),"微信钱包授权失效，请到微信钱包重新授权");
				throw new RRException("[订单关闭]" + e.getMessage(),MsgStatus.WITHDRAWALS_EXCEPTION.value());

			}else if("NAME_MISMATCH".equals(errCode)){
				//"微信钱包真实姓名有误，请修改"
				close(entity.getId(),"您好，您的订单因为提交的微信账号和姓名不匹配，提现无法支付，收益已经返还。请确认提交的微信账户信息正确并且已经完成实名认证，即可确保此后订单成功到账。感谢您的支持和使用！");
				throw new RRException("[订单关闭]" + e.getMessage(),MsgStatus.WITHDRAWALS_EXCEPTION.value());

			}else if("V2_ACCOUNT_SIMPLE_BAN".equals(errCode)){
				//"微信钱包未实名认证，请打开微信完成实名认证之后再提现"
				close(entity.getId(),"您好，您的订单因为提交的微信账号未完成实名认证，提现无法支付，收益已经返还。请确认提交的微信账户信息正确并且已经完成实名认证，即可确保此后订单成功到账。感谢您的支持和使用！");
				throw new RRException("[订单关闭]" + e.getMessage(),MsgStatus.WITHDRAWALS_EXCEPTION.value());

			}else if("FATAL_ERROR".equals(errCode)){
				throw new RRException("两次请求商户单号一样，但是参数不一致",MsgStatus.WITHDRAWALS_EXCEPTION.value());
			}

			throw new RRException(e.getMessage());
		} catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException | UnrecoverableKeyException | KeyManagementException e) {
			logger.error("企业付款到零钱ssl证书错误",e);
			throw new RRException("证书错误",e);
		}

		Date paymentTime;
		try {
			paymentTime = DateUtils.parse(responseHashMap.get("payment_time"));
		} catch (ParseException e) {
			String err = "微信支付返回支付时间有误";
			logger.error(err,e);
			throw new RRException(err);
		}

		Integer finish = finish(entity.getId(), responseHashMap.get("payment_no"), paymentTime);//完成转账数据补齐

		if(finish == 1){
			logger.info("微信转账成功：" + entity.getId());
		}else{
			error = "[注意]未更新提现订单状态，请检查日志";
			logger.error(error);
			throw new RRException(error);
		}
	}

	@Override
	public void close(String oid, String closeMsg) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("createTime", new Date());
		map.put("result",0);
		map.put("oid",oid);
		map.put("closeMsg",closeMsg);
		withdrawalsDao.close(map);
		Integer result = MapUtils.getInteger(map, "result", 0);
		if(result!=1){
			logger.error("订单关闭失败：" + oid + "；result = " + result);
			throw new RRException("订单关闭失败：" + result);
		}
		logger.info("订单关闭成功：" + oid);
	}

	@Override
	public Integer sumByUser(Long userId ,String time) {
		return withdrawalsDao.sumByUser(userId,time);
	}

	@Override
	public Integer queryCountByUserIdAndStatus(Long userId, int status) {
		return withdrawalsDao.queryCountByUserIdAndStatus(userId,status);
	}

	/**
	 * 对象转Map
	 * @param obj
	 * @return
	 * @throws IntrospectionException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	private static Map<String, Object> objectToMap(Object obj) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
		if(obj == null)
			return null;

		Map<String, Object> map = new HashMap<>();

		BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor property : propertyDescriptors) {
			String key = property.getName();
			if (key.compareToIgnoreCase("class") == 0) {
				continue;
			}
			Method getter = property.getReadMethod();
			Object value = getter!=null ? getter.invoke(obj) : null;
			map.put(key, value);
		}

		return map;
	}
}
