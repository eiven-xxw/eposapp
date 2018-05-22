package com.eposapp.interceptor;

import com.eposapp.common.constant.ResponseCodeConstans;
import com.eposapp.common.constant.SysConstants;
import com.eposapp.common.util.StringUtils;
import com.eposapp.repository.redis.RedisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Component
public class UserLoginHandlerInterceptor implements HandlerInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(UserLoginHandlerInterceptor.class);

	@Autowired
	private RedisRepository redisRepository;

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
	}
	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub
	}
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		CorsRegistry(response);
		logger.info("进入用户授权拦截器");
		String  userAccessToken = request.getHeader(SysConstants.USER_ACCESS_TOKEN);
		if(StringUtils.isBlank(userAccessToken)){
			logger.error("USER_ACCESS_TOKEN参数不存在");
			response.setHeader(SysConstants.RESPONSE_CODE, ResponseCodeConstans.USER_ACCESS_TOKNE_NOTINVAL);
			return false;
		}
		/**
		 *
		 * 获取redis中的用户
		 */
		Map user = redisRepository.getSession(userAccessToken);
		String realUserAccessToken = null;
		if(user!=null){
			realUserAccessToken  = String.valueOf(user.get(SysConstants.USER_TOKEN));
		}
		if(user==null||!userAccessToken.equals(realUserAccessToken)){
			logger.error("用户[{}]未登录或令牌[{}]已失效",userAccessToken);
			response.setHeader(SysConstants.RESPONSE_CODE, ResponseCodeConstans.USER_ACCESS_TOKNE_NOTINVAL);
			return false;
		}
		response.setHeader(SysConstants.RESPONSE_CODE, ResponseCodeConstans.SUCCESS);
		return true;
	}

	public void CorsRegistry(HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin","*");
		response.setHeader("Access-Control-Allow-Headers","*");
		StringBuffer exposeHeaders = new StringBuffer(SysConstants.RESPONSE_CODE);
		exposeHeaders.append(",");
		exposeHeaders.append(SysConstants.USER_ACCESS_TOKEN);
		response.setHeader("Access-Control-Expose-Headers",exposeHeaders.toString());

	}

}
