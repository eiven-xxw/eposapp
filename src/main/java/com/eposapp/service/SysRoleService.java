

package com.eposapp.service;

import com.eposapp.common.util.JsonResult;
import com.eposapp.entity.SysRoleEntity;
import com.eposapp.entity.SysUserEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author eiven
 *
 * */
public interface SysRoleService{

	/**
	 * 列表查询
	 * @author eiven
	 * @param paramsMap 查询参数
	 * @return Map
	 * */
	Map<String,Object>	findRoleList(Map paramsMap);



	/**
	 * 角色信息保存或新增
	 * @author eiven
	 * @param paramsMap
	 * @return JsonResult
	 **/
	JsonResult doSaveOrUpdate(Map paramsMap);

	/**
	 * 根据表的id删除数据
	 * @param  clazz
	 */
	boolean delete(Class clazz,String id);

}