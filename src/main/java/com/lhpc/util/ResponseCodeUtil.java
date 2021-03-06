package com.lhpc.util;

public class ResponseCodeUtil {

	private ResponseCodeUtil() {

	}

	public static final int SUCCESS = 2000;// 响应成功
	public static final int UNFINISHED_TRIP = 3001;// 存在未完成行程
	public static final int PARAMETER_MISS = 4001;// 参数不完整
	public static final int NO_USER = 4002;// 用户不存在
	public static final int NO_DATA = 4003;// 暂无数据
	public static final int PHONE_ERROR = 4004;// 手机号格式错误
	public static final int NO_LOGIN = 4005;// 用户未登录
	public static final int LOGIN_ERROR = 4006;// 登录失败
	public static final int CODE_ERROR = 4007;// 验证码不正确
	public static final int CODE_BEYOND = 4008;// 发送验证码超出限制
	public static final int SMS_SEND_FAILED = 4009; // 验证码发送失败
	public static final int BOOKING_REPEAT = 4100; // 车单预定重复
	public static final int CODE_REPEAT = 4101; // code重复
	public static final int SEAT_LACK = 4101; // 车主座位不足
	public static final int PUSH_ERROR = 4102; // 推送失败
	public static final int FLUSH_TOKEN_ERROR = 4103; // 刷新失败
	public static final int OPENID_ERROR = 4105; // openid错误

	public static final int PAY_NOTIFY_ERROR = 4201;//支付回调失败

	public static final int WALLET_EMPTY = 4104; // 余额不足

	public static final int ERROR = 5000;// 操作失败
	public static final int SYSTEM_ERROR = 5005;// 系统错误

}
