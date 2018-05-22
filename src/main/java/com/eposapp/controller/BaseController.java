package com.eposapp.controller;

import com.eposapp.common.util.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class BaseController {
	private static final Logger log = LoggerFactory.getLogger(BaseController.class);
			
	protected HttpServletRequest getCurrentRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}
	
	protected HttpServletResponse getCurrentResponse() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
	}


	protected String getBasePath() {
		return RequestUtil.getBasePath(getCurrentRequest());
	}
	
	protected boolean isMobile() {
		return RequestUtil.isMobile(getCurrentRequest());
	}
	
	protected boolean isWechat() {
		return RequestUtil.isWechat(getCurrentRequest());
	}

	protected ApplicationContext getApplicationContext() {
	     ApplicationContext ctx = RequestContextUtils.findWebApplicationContext(getCurrentRequest());
	     return ctx;
	}

}
