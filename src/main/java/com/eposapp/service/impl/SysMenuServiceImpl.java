

package com.eposapp.service.impl;

import com.eposapp.common.constant.JsonConstans;
import com.eposapp.common.constant.ResponseCodeConstans;
import com.eposapp.common.util.EntityUtil;
import com.eposapp.common.util.JsonResult;
import com.eposapp.common.util.StringUtils;
import com.eposapp.common.util.ValidationUtils;
import com.eposapp.entity.SysMenuEntity;
import com.eposapp.repository.mysql.SysMenuRepository;
import com.eposapp.service.SysMenuService;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author eiven
 * */
@Service
public class SysMenuServiceImpl implements SysMenuService {

	@Autowired
	private SysMenuRepository sysMenuRepository;

	@Override
	public JsonResult doSaveOrUpdate(Map paramsMap) {
		try {
			String  isVali= ValidationUtils.validation(paramsMap);
			if(!isVali.equals(ResponseCodeConstans.SUCCESS)){
				return JsonResult.putFail(isVali);
			}
			String id = StringUtils.getMapKeyValue(paramsMap,"id");
			String code = StringUtils.getMapKeyValue(paramsMap,"code");
			boolean isExistCode = sysMenuRepository.isExistCode(SysMenuEntity.class,code,id);
			if(isExistCode){
				return JsonResult.putFail(JsonConstans.ERR_CODE_EXISTS);
			}
			SysMenuEntity sysMenuEntity = null;
			boolean isSave = false;
			if(StringUtils.isNotBlank(id)){
				sysMenuEntity = sysMenuRepository.findById(new SysMenuEntity(),id);
				if(sysMenuEntity==null){
					return JsonResult.putFail(JsonConstans.ERR_NOT_EXISTED);
				}
				EntityUtil.putMapDataIntoEntity(paramsMap,sysMenuEntity);
				isSave = sysMenuRepository.update(sysMenuEntity);
			}else{
				sysMenuEntity = new SysMenuEntity();
				EntityUtil.putMapDataIntoEntity(paramsMap,sysMenuEntity);
				isSave = sysMenuRepository.save(sysMenuEntity);
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
		return sysMenuRepository.delete(clazz,id);
	}

	@Override
	public Map<String, Object> findMenuList(Map paramsMap) {
		String cnName = StringUtils.getMapKeyValue(paramsMap,"cnName");
		String pageNoStr = StringUtils.getMapKeyValue(paramsMap,"pageNo");
		String pageSizeStr = StringUtils.getMapKeyValue(paramsMap,"pageSize");
		String sql = "SELECT menu.id,menu.cnName,menu.`code`,menu.enName,menu.icon,\n" +
				"	menu.isExpand,menu.isUse,menu.parentId,menu2.cnName AS parentName,\n" +
				"	menu2.`code` parentCode,menu.remark,menu.sequence,menu.url,menu.version\n" +
				" FROM sys_menu menu\n" +
				" LEFT JOIN sys_menu menu2 ON menu2.id = menu.parentId" ;
		Map<String,String> conditionParams = new HashMap<>(3);
		if(StringUtils.isNotBlank(cnName)){
				sql +=" WHERE menu.cnName =:cnName ";
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
		return sysMenuRepository.findByNativeQuery(sql,conditionParams,Transformers.ALIAS_TO_ENTITY_MAP,pageNo,pageSize);
	}
}