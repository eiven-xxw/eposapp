package com.eposapp.common.constant;


/**
 * @Description: TODO(结果返回)
 * @Author eiven
 * @date 2018年3月19日
 * Updates
 * 这里补充更新记录
 */
public final class JsonConstans {
	//通用返回结果
	public static final String ERR_UNKNOWN = "未知错误";
	public static final String ERR_WRONG_PARAM = "请求参数不完整";
	public static final String ERR_NOTVALID_SIGN ="请求校验无效";
	public static final String ERR_NOTVALID_LOGIN = "登录失效";
	public static final String ERR_NOTVALID_AUTH = "非法用户操作";
	public static final String ERR_NOTVALID_AUTH_WX = "微信端用户身份认证不通过";
	public static final String ERR_DUAL_LOGIN = "其它地方登陆";
	public static final String ERR_NOT_SUPPORTED = "暂不支持该操作";
	public static final String ERR_NOT_EXISTED = "记录不存在";
	public static final String ERR_MUST_LOGIN = "您的操作需要登录";
	public static final String ERR_WRONG_DOMAIN = "无效的域名";
	public static final String OPERATION_FAILURE = "操作失败";
	
	//用户相关的返回结果标识
	public static final String ERR_CODE_EXISTS = "Code已存在";
	public static final String ERR_USER_NOTFOUND = "用户不存在";
	public static final String ERR_USER_PASSORD_NOTMATCH = "Password_Not_Match";
	public static final String ERR_USER_EXISTS = "用户已存在";
	public static final String ERR_USER_WRONG_PASSWORD = "密码格式不正确";
	public static final String ERR_USER_LIMIT = "用户被禁止使用";
	public static final String ERR_USER_WXID_NOTMATCH = "用户微信号不匹配";
	public static final String ERR_USER_PASSORD_NOTEQUAL = "账号或密码输入错误";
	public static final String ERR_PASSORD_NOTEQUAL = "原密码输入错误";

	/**
	 * 接口返回成功状态true:成功
	 */
	public static final boolean RESULT_SUCCESS = true;
	/**
	 * 接口返回成功状态 false:失败
	 */
	public static final boolean RESULT_FAIL = false;
	//标准返回
	public static final String RESULT_SUCCESS_MSG = "success";
	//标准返回
	public static final String RESULT_FAIL_MSG = "failed";



	


}
