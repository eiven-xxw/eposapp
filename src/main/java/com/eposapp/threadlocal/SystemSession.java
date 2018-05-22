package com.eposapp.threadlocal;

import com.eposapp.common.constant.SysConstants;
import com.eposapp.common.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Description: 全局对象
 *
 */

public class SystemSession {
	private static final ThreadLocal<Map> THREADLOCAL = new ThreadLocal();

	private static final String ORGID_KEY = "orgId";

	private static final String DEPID_KEY = "departmentId";



	public static Map getUser() {
		Map userMap = THREADLOCAL.get();
		return userMap;
	}

	public static void removeUser() {
		THREADLOCAL.remove();
	}

	public static void setUser(Map user) {
		THREADLOCAL.remove();
		THREADLOCAL.set(user);
	}

	public static String getUserId() {
		Map userMap = getUser();
		if (userMap != null) {
			return String.valueOf(userMap.get(SysConstants.ID_CODE));
		} else {
			return null;
		}

	}


	public static String getOrgId() {
		Map userMap = getUser();
		if (userMap != null) {
			return StringUtils.getMapKeyValue(userMap,ORGID_KEY);
		}
		return null;
	}

	public static String getDepId() {
		Map userMap = getUser();
		if (userMap != null) {
			return StringUtils.getMapKeyValue(userMap,DEPID_KEY);
		}
		return null;
	}
}