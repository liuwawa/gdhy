package com.renren.gatling

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.asynchttpclient.{Param, RequestBuilderBase}
import io.gatling.http.response._
import java.nio.charset.StandardCharsets.UTF_8

import scala.concurrent.duration._

class ApiGatlingSimulationTest extends Simulation {

  /**
    * 用于取得当前时间戳
    */
  //  private val nowTimestamp = Iterator.continually(Map("now" -> System.currentTimeMillis()))

  /**
    * 请求参数签名
    */
  private val signCalculator = (request: Request, value: RequestBuilderBase[_]) => {
    val list = request.getFormParams
    val version = "1.0.0"
    val timestamp = String.valueOf(System.currentTimeMillis())
    list.add(new Param("timestamp", timestamp))
    list.add(new Param("version", version))

    var hasTimestamp = false
    list.forEach(p=>{
      if("_id".equals(p.getName) && "TIMESTAMP".equals(p.getValue)){
        hasTimestamp = true
        false
      }
    })
    if(hasTimestamp){
        list.add(new Param("id",timestamp))
        value.addFormParam("id",timestamp)
//        println("set id: " + timestamp)
    }

    value.addFormParam("timestamp", timestamp)
    value.addFormParam("version", version)
    val sign = SignUtils.doSign("app_secret=3d0bffb0-6f-4fd09950fc76-ac5c9ac8-16", list)
    value.addFormParam("sign", sign)
//    println("fuck sign: " + sign)
    hasTimestamp = false
  }

  /**
    * 用户登录
    */
  object Login {
    val feeder = csv("user.csv").queue //用户账号

    val req = feed(feeder) // every time a user passes here, a record is popped from the feeder and injected into the user's session
        .tryMax(1){exec(http("login")
        .post("/api/user/login")
        .formParam("password", "${passwd}")
        .formParam("phone", "${phone}")
        .check(jsonPath("$.data.token").ofType[String].saveAs("token")))}
        .exitHereIfFailed
//      .exec(session => {
//        val token = session("token").as[String]
//        println("登录成功：" + session("username").as[String])
//        session
//      })
  }

  /**
    * 获取时段奖励
    */
  object Duration {
    val req = tryMax(1){exec(http("Duration")
      .post("/api/task/duration")
      .formParam("_id", "TIMESTAMP")
      .formParam("token", "${token}"))}
  }

  /**
    * 获取签到列表
    */
  object SignRecord {
    val req = exec(http("SignRecord")
      .post("/api/task/signRecord")
      .formParam("token", "${token}"))
  }

  /**
    * 获取任务列表
    */
  object TaskList {
    val req = exec(http("TaskList")
      .post("/api/task/list")
      .formParam("token", "${token}"))
  }

  /**
    * 签到
    */
  object Sign {
    val req = tryMax(1){exec(http("Sign")
      .post("/api/task/sign")
      .formParam("token", "${token}"))
      .exec(SignRecord.req)}
  }

  /**
    * 获取我的金币
    */
  object MineGold {
    val req = exec(http("MineGold")
      .post("/api/gold/mine")
      .formParam("token", "${token}"))
  }

  /**
    * 获取用户信息
    */
  object UserInfo {
    val req = exec(http("UserInfo")
      .post("/api/user/info")
      .formParam("token", "${token}"))
  }

  /**
    * 完成绑定微信任务
    */
  object BindWechatTask {
    val req = tryMax(1){exec(http("BindWechatTask")
      .post("/api/task/finish")
      .formParam("_id", "TIMESTAMP")
      .formParam("typeId", "26")
      .formParam("token", "${token}"))}
      .exec(TaskList.req)
  }

  /**
    * 完成绑定支付宝任务
    */
  object BindAlipayTask {
    val req = tryMax(1){exec(http("BindAlipayTask")
      .post("/api/task/finish")
      .formParam("_id", "TIMESTAMP")
      .formParam("typeId", "39")
      .formParam("token", "${token}"))}
      .exec(TaskList.req)
  }

  /**
    * 完成完善个人信息任务
    */
  object FullInfoTask {
    val req = tryMax(1){exec(http("FullInfoTask")
      .post("/api/task/finish")
      .formParam("_id", "TIMESTAMP")
      .formParam("typeId", "28")
      .formParam("token", "${token}"))}
      .exec(TaskList.req)
  }

  /**
    * 完成首次提现任务
    */
  object WithdrawalsTask {
    val req = tryMax(1){exec(http("WithdrawalsTask")
      .post("/api/task/finish")
      .formParam("_id", "TIMESTAMP")
      .formParam("typeId", "31")
      .formParam("token", "${token}"))}
      .exec(TaskList.req)
  }

  /**
    * 完成绑定手机号任务
    */
  object BindPhoneTask {
    val req = tryMax(1){exec(http("BindPhoneTask")
      .post("/api/task/finish")
      .formParam("_id", "TIMESTAMP")
      .formParam("typeId", "27")
      .formParam("token", "${token}"))}
      .exec(TaskList.req)
  }

  /**
    * 获取金币兑换列表
    */
  object ExchangeList {
    val req = exec(http("ExchangeList")
      .post("/api/gold/exchangeList")
      .formParam("token", "${token}"))
  }

  /**
    * 获取分享数据
    */
  object GetShareInfo {
    val req = exec(http("GetShareInfo")
      .post("/api/utils/dataDictionaries")
      .formParam("keys", """APP分享标题,APP分享描述,APP分享链接""")
      .formParam("token", "${token}"))
  }

  /**
    * 获取提现记录
    */
  object WithdrawalsList {
    val req = exec(http("WithdrawalsList")
      .post("/api/gold/withdrawalsList")
      .formParam("limit", "15")
      .formParam("page", "1")
      .formParam("status", "1")
      .formParam("token", "${token}"))
  }

  /**
    * 获取任务记录
    */
  object TaskRecordList {
    val req = exec(http("TaskRecordList")
      .post("/api/task/record")
      .formParam("limit", "15")
      .formParam("page", "1")
      .formParam("token", "${token}"))
  }

  /**
    * 修改用户名
    */
  object ModifyUsername {
    val req = tryMax(1){exec(http("ModifyUsername")
      .post("/api/user/updateInfo")
      .formParam("key", "username")
      .formParam("value", "${username}")
      .formParam("token", "${token}"))}
  }

  /**
    * 修改头像
    */
  object ModifyAvatar {
    val req = tryMax(1){exec(http("ModifyAvatar")
      .post("/api/user/updateInfo")
      .formParam("key", "avatar")
      .formParam("value", "https://watch-everyday.oss-cn-shenzhen.aliyuncs.com/custom/20180102/a8c9d74a4ee243049452acf12332f53d.png")
      .formParam("token", "${token}"))}
  }

  /**
    * 修改性别
    */
  object ModifySex {
    val req = tryMax(1){exec(http("ModifySex")
      .post("/api/user/updateInfo")
      .formParam("key", "gender")
      .formParam("value", "1")
      .formParam("token", "${token}"))}
  }

  /**
    * 修改生日
    */
  object ModifyBirthday {
    val req = tryMax(1){exec(http("ModifyBirthday")
      .post("/api/user/updateInfo")
      .formParam("key", "birthday")
      .formParam("value", "2017-01-24")
      .formParam("token", "${token}"))}
  }

  /**
    * 修改支付宝账号
    */
  object ModifyAlipayAccount {
    val req = tryMax(1){exec(http("ModifyAlipayAccount")
      .post("/api/user/updateInfo")
      .formParam("key", "alipayAccount")
      .formParam("value", "nafkfa6640@sandbox.com")
      .formParam("token", "${token}"))}
  }

  /**
    * 修改支付宝姓名
    */
  object ModifyAlipayName {
    val req = tryMax(1){exec(http("ModifyAlipayName")
      .post("/api/user/updateInfo")
      .formParam("key", "alipayName")
      .formParam("value", "${username}")
      .formParam("token", "${token}"))}
  }

  /**
    * 修改微信钱包姓名
    */
  object ModifyWeixinPayName {
    val req = tryMax(1){exec(http("ModifyWeixinPayName")
      .post("/api/user/updateInfo")
      .formParam("key", "weixinPayName")
      .formParam("value", "小莫")
      .formParam("token", "${token}"))}
  }

  /**
    * 修改微信钱包手机号
    */
  object ModifyWeixinPayPhone {
    val req = tryMax(1){exec(http("ModifyWeixinPayPhone")
      .post("/api/user/updateInfo")
      .formParam("key", "weixinPayPhone")
      .formParam("value", "15992999163")
      .formParam("token", "${token}"))}
  }

  /**
    * 获取阅读奖励
    */
  object ReadNewsReward {
    val req = tryMax(1){exec(http("ReadNewsReward")
      .post("/api/task/read")
      .formParam("_id", "TIMESTAMP")
      .formParam("token", "${token}"))}
  }

  /**
    * 获取看视频奖励
    */
  object WatchVideoReward {
    val req = tryMax(1){exec(http("WatchVideoReward")
      .post("/api/task/watch")
      .formParam("_id", "TIMESTAMP")
      .formParam("token", "${token}"))}
  }

  /**
    * 完成新手任务：阅读一分钟
    */
  object ReadNewsOneMinuteTask {
    val req = tryMax(1){exec(http("ReadNewsOneMinuteTask")
      .post("/api/task/finish")
      .formParam("_id", "TIMESTAMP")
      .formParam("typeId", "29")
      .formParam("token", "${token}"))}
      .exec(TaskList.req)
  }

  /**
    * 完成新手任务：观看视频一分钟
    */
  object WatchVideoOneMinuteTask {
    val req = tryMax(1){exec(http("WatchVideoOneMinuteTask")
      .post("/api/task/finish")
      .formParam("_id", "TIMESTAMP")
      .formParam("typeId", "30")
      .formParam("token", "${token}"))}
      .exec(TaskList.req)
  }

  /**
    * 完成日常任务：阅读五分钟
    */
  object ReadNewsFiveMinuteTask {
    val req = tryMax(1){exec(http("ReadNewsOneMinuteTask")
      .post("/api/task/finish")
      .formParam("_id", "TIMESTAMP")
      .formParam("typeId", "35")
      .formParam("token", "${token}"))}
      .exec(TaskList.req)
  }

  /**
    * 完成新手任务：观看视频五分钟
    */
  object WatchVideoFiveMinuteTask {
    val req = tryMax(1){exec(http("WatchVideoOneMinuteTask")
      .post("/api/task/finish")
      .formParam("_id", "TIMESTAMP")
      .formParam("typeId", "36")
      .formParam("token", "${token}"))}
      .exec(TaskList.req)
  }

  /**
    * 发起一笔支付宝提现
    */
  object MakeWithdrawals {
    val req = tryMax(1){exec(http("MakeWithdrawals")
      .post("/api/gold/withdrawals")
      .formParam("channel", "alipay")
      .formParam("rmb", "100")
      .formParam("token", "${token}"))}
      .exec(ExchangeList.req)
  }

  /**
    * 常见问题分类列表
    */
  object FaqList {
    val req = tryMax(1){exec(http("FaqList")
      .post("/api/faq/type")
      .formParam("limit", "10")
      .formParam("page", "1")
      .formParam("token", "${token}"))}
  }

  /**
    * 常见问题列表
    */
  object FaqQuestion {
    val req = tryMax(1){exec(http("FaqQuestion")
      .post("/api/faq/question")
      .formParam("type", "80")
      .formParam("limit", "10")
      .formParam("page", "1")
      .formParam("token", "${token}"))}
  }

  /**
    * 意见反馈
    */
  object Feedback {
    val req = tryMax(1){exec(http("Feedback")
      .post("/api/feedback/save")
      .formParam("opinion", "give me more gold,more gold,more gold")
      .formParam("phone", "15992999123")
      .formParam("token", "${token}"))}
  }


  val httpConf = http
    .baseURL("http://192.168.1.253:8080")
    .acceptHeader("*/*")
    .acceptEncodingHeader("br, gzip, deflate")
    .acceptLanguageHeader("zh-Hans-CN;q=1")
    .authorizationHeader("Basic bHVhdXNlcjomKmpmbGthSA==")
    .contentTypeHeader("application/x-www-form-urlencoded")
    .userAgentHeader("LookEveryday/1.0.0 (iPhone; iOS 11.2.1; Scale/2.00)")
    .signatureCalculator(signCalculator)
    .check(jsonPath("$.status").ofType[Boolean].is(true))
//    .transformResponse { case response if response.isReceived =>
//      new ResponseWrapper(response) {
//        private val result = response.body.string
//        println(result)
//        override val body = new StringResponseBody(result, UTF_8)
//      }
//    }

//  val users = scenario("NewUser").exec(session => {session.set("token", "")})
//    .exec(
//      Login.req,
//      Duration.req,
//      pause(10),
//      SignRecord.req,
//      TaskList.req,
//      pause(5),
//      Sign.req,
//      TaskList.req,
//      pause(3),
//      BindWechatTask.req,
//      pause(2),
//      BindPhoneTask.req,
//      pause(1),
//      BindAlipayTask.req,
//      pause(2),
//      TaskList.req,
//      pause(3),
//      ModifyBirthday.req,
//      pause(1),
//      SignRecord.req,
//      TaskList.req,
//      pause(4),
//      TaskList.req,
//      pause(23),
//      ModifyAvatar.req,
//      pause(4),
//      ModifyUsername.req,
//      pause(4),
//      ModifySex.req,
//      pause(4),
//      SignRecord.req,
//      TaskList.req,
//      pause(3),
//      FullInfoTask.req,
//      pause(6),
//      MineGold.req,
//      UserInfo.req,
//      pause(6),
//      SignRecord.req,
//      TaskList.req,
//      pause(2),
//      TaskList.req,
//      pause(44),
//      ReadNewsReward.req,
//      pause(36),
//      ReadNewsReward.req,
//      pause(4),
//      SignRecord.req,
//      TaskList.req,
//      pause(1),
//      ReadNewsOneMinuteTask.req,
//      pause(2),
//      TaskList.req,
//      pause(38),
//      WatchVideoReward.req,
//      pause(37),
//      WatchVideoReward.req,
//      pause(21),
//      SignRecord.req,
//      TaskList.req,
//      pause(2),
//      WatchVideoOneMinuteTask.req,
//      pause(2),
//      TaskList.req,
//      ExchangeList.req,
//      pause(22),
//      ModifyAlipayName.req,
//      ModifyAlipayAccount.req,
//      pause(7),
//      MakeWithdrawals.req,
//      pause(1),
//      SignRecord.req,
//      TaskList.req,
//      pause(2),
//      WithdrawalsTask.req,
//      pause(4),
//      MineGold.req,
//      UserInfo.req,
//      pause(3),
//      ExchangeList.req,
//      pause(2),
//      MineGold.req,
//      UserInfo.req,
//      pause(3),
//      WithdrawalsList.req,
//      pause(4),
//      WithdrawalsList.req,
//      pause(1),
//      MineGold.req,
//      UserInfo.req,
////      pause(5),
////      GetShareInfo.req,
//      pause(17),
//      TaskRecordList.req,
//      pause(4),
//      TaskRecordList.req,
//      pause(3),
//      TaskRecordList.req,
//      MineGold.req,
//      UserInfo.req,
//      MineGold.req,
//      UserInfo.req,
//      pause(10),
//      MineGold.req,
//      UserInfo.req,
//      pause(4),
//      FaqList.req,
//      pause(1),
//      FaqQuestion.req,
//      pause(3),
//      UserInfo.req,
//      MineGold.req,
//      pause(62),
//      Feedback.req,
//      pause(1),
//      MineGold.req,
//      UserInfo.req,
//      pause(2),
//      SignRecord.req,
//      TaskList.req,
//      pause(2),
//      TaskList.req,
//      pause(42),
//      ReadNewsReward.req,
//      pause(61),
//      ReadNewsReward.req,
//      pause(32),
//      ReadNewsReward.req,
//      pause(32),
//      ReadNewsReward.req,
//      pause(32),
//      ReadNewsReward.req,
//      pause(32),
//      ReadNewsReward.req,
//      pause(32),
//      ReadNewsReward.req,
//      pause(32),
//      ReadNewsReward.req,
//      pause(5),
//      ReadNewsFiveMinuteTask.req,
//      pause(32),
//      WatchVideoReward.req,
//      pause(32),
//      WatchVideoReward.req,
//      pause(32),
//      WatchVideoReward.req,
//      pause(32),
//      WatchVideoReward.req,
//      pause(32),
//      WatchVideoReward.req,
//      pause(32),
//      WatchVideoReward.req,
//      pause(32),
//      WatchVideoReward.req,
//      pause(32),
//      WatchVideoReward.req,
//      pause(5),
//      WatchVideoFiveMinuteTask.req,
//      pause(5),
//      UserInfo.req,
//      MineGold.req,
//      pause(3),
//      ExchangeList.req,
//      pause(17),
//      MakeWithdrawals.req,
//      pause(9),
//      MakeWithdrawals.req,
//      pause(2),
//      UserInfo.req,
//      MineGold.req,
//      pause(1),
//      WithdrawalsList.req,
//      pause(3),
//      UserInfo.req,
//      MineGold.req
//    )

//  setUp(users.inject(rampUsers(2500) over (60 seconds))).protocols(httpConf)

  val oldUsers = scenario("oldUsers").exec(session => {session.set("token", "")})
    .exec(Login.req).repeat(5){
    exec(pause(32),ReadNewsReward.req,pause(1),WatchVideoReward.req)
  }

  setUp(oldUsers.inject(rampUsers(20000) over (60 seconds))).protocols(httpConf)
}