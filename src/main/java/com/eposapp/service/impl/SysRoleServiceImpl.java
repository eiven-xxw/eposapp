

package com.eposapp.service.impl;

import com.eposapp.common.constant.JsonConstans;
import com.eposapp.common.constant.ResponseCodeConstans;
import com.eposapp.common.constant.SysConstants;
import com.eposapp.common.constant.TokenConstants;
import com.eposapp.common.util.*;
import com.eposapp.entity.SysRoleEntity;
import com.eposapp.entity.SysUserEntity;
import com.eposapp.repository.mysql.SysRoleRepository;
import com.eposapp.repository.mysql.SysUserRepository;
import com.eposapp.repository.redis.RedisRepository;
import com.eposapp.service.SysRoleService;
import com.eposapp.service.SysUserService;
import com.eposapp.threadlocal.SystemSession;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author eiven
 * */
@Service
public class SysRoleServiceImpl  implements SysRoleService {

	@Autowired
	private SysRoleRepository sysRoleRepository;

	@Override
	public JsonResult doSaveOrUpdate(Map paramsMap) {
		try {
			String  isVali= ValidationUtils.validation(paramsMap);
			if(!isVali.equals(ResponseCodeConstans.SUCCESS)){
				return JsonResult.putFail(isVali);
			}
			String id = StringUtils.getMapKeyValue(paramsMap,"id");
			String code = StringUtils.getMapKeyValue(paramsMap,"code");
			boolean isExistCode = sysRoleRepository.isExistCode(SysUserEntity.class,code,id);
			if(isExistCode){
				return JsonResult.putFail(JsonConstans.ERR_CODE_EXISTS);
			}
			SysRoleEntity sysRoleEntity = null;
			boolean isSave = false;
			if(StringUtils.isNotBlank(id)){
				sysRoleEntity = sysRoleRepository.findById(new SysRoleEntity(),id);
				EntityUtil.putMapDataIntoEntity(paramsMap,sysRoleEntity);
				isSave = sysRoleRepository.update(sysRoleEntity);
			}else{
				sysRoleEntity = new SysRoleEntity();
				EntityUtil.putMapDataIntoEntity(paramsMap,sysRoleEntity);
				isSave = sysRoleRepository.save(sysRoleEntity);
			}
			if(isSave){
				return JsonResult.putSuccess();
			}else{
				return JsonResult.putFail(JsonConstans.OPERATION_FAILURE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.putFail(JsonConstans.OPERATION_FAILURE);
		}
	}

	@Override
	public boolean delete(Class clazz, String id) {
		return sysRoleRepository.delete(clazz,id);
	}

	@Override
	public Map<String, Object> findRoleList(Map paramsMap) {
		String cnName = StringUtils.getMapKeyValue(paramsMap,"cnName");
		String pageNoStr = StringUtils.getMapKeyValue(paramsMap,"pageNo");
		String pageSizeStr = StringUtils.getMapKeyValue(paramsMap,"pageSize");
		String sql = "SELECT r.id,r.cnName,r.`code`,r.enName,r.createId,r.createTime," +
				"u.cnName AS createName,u.version from sys_role r\n" +
				"LEFT JOIN sys_user u ON u.id = r.createId" ;
		String orgId= SystemSession.getOrgId();
		boolean isHasWhere = false;
		if(StringUtils.isNotBlank(orgId)&& !SysConstants.ROOT_ID.equals(orgId)){
			sql+=" WHERE u.orgId ='"+ orgId +"'";
			isHasWhere = true;
		}
		Map<String,String> conditionParams = new HashMap<>(3);
		if(StringUtils.isNotBlank(cnName)){
			if(isHasWhere){
				sql +=" AND r.cnName =:cnName ";
			}else{
				sql +=" WHERE r.cnName =:cnName ";
			}
			conditionParams.put("cnName",cnName);
		}
		Integer pageNo =null;
		Integer pageSize =null;
		if(StringUtils.isNotBlank(pageNoStr)){
			pageNo = Integer.valueOf(pageNoStr);
		}
		if(StringUtils.isNotBlank(pageSizeStr)){
			pageSize = Integer.valueOf(pageSizeStr);
		}
		return sysRoleRepository.findByNativeQuery(sql,conditionParams,Transformers.ALIAS_TO_ENTITY_MAP,pageNo,pageSize);
	}
}