

package com.eposapp.service;

import com.eposapp.common.util.JsonResult;
import com.eposapp.entity.SysUserEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
/**
 * @author eiven
 *
 * */
public interface SysUserService extends  BaseService<SysUserEntity,String>{

	/**
	 * 根据用户代码找用户信息
	 * @author eiven
	 * @param code 用户代码
	 * @return  Map<String,Object>
	 * */
	 Map<String,Object> getUserMapByCode(String code);

	 /**
	  * 用户信息保存或新增
	  * @author eiven
	  * @param paramsMap
	  **/
	 JsonResult doSaveOrUpdate(Map paramsMap);

	 /**
	  * 查询用户列表
	  * @param paramsMap 查询条件
	  * @return Map<String,Object>
	  * */
	 Map<String,Object> findUserList(Map paramsMap);

	 boolean layout(HttpServletRequest request);

	JsonResult doLogin(Map paramsMap);

}