package com.eposapp.common.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @date 2018年3月19日
 * @author eiven
 */
public class RequestUtil {

	private static final Logger log = LoggerFactory.getLogger(RequestUtil.class);


	
	public static boolean isLocalAddress(HttpServletRequest request) {
		if (request.getRequestURL().toString().toLowerCase().contains("localhost")) {
			return true;
		}
		return false;
	}

	public static boolean isAjax(HttpServletRequest request) {
		boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		String ajaxFlag = (null == request.getParameter("ajax")) ? "false" : request.getParameter("ajax");
		boolean isAjax = ajax || ajaxFlag.equalsIgnoreCase("true");
		return isAjax;
	}

	public static Cookie getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				log.info(cookie.getName());
				if (cookie.getName().equals(name)) {
					return cookie;
				}
			}
		}
		return null;
	}

	public static void delCookie(HttpServletRequest request, HttpServletResponse response, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				log.info(cookie.getName());
				if (cookie.getName().equals(name)) {
					Cookie delCookie = new Cookie(name, null);
					cookie.setMaxAge(0);
					cookie.setPath("/");
					response.addCookie(delCookie);
				}
			}
		}

	}

	public static String addWxParam(String url, String id) {
		return addParam(url, "ssid", id);
	}

	/**
	 * 增加或更新 refers参数
	 * 
	 * @param url
	 * @param referId,
	 *            axx=运营团 uxx=用户
	 * @return
	 */
	public static String addReferParam(String url, String referId) {
		String mainUrl = getMainUrl(url);
		String qs = getQueryString(url);

		Map<String, String> params = getParamsMap(qs);
		if (params == null) {
			params = new HashMap();
		}
		String refers = "";
		if (params.containsKey("refers")) {
			if (params.get("refers") != null) {
				refers = params.get("refers");
				// 如果已经有了则跳过
				if (!refers.contains(referId)) {
					refers = refers.trim() + "," + referId;
				}
			}

		}
		if (StringUtils.isEmpty(refers)) {
			refers = referId;
		}
		params.put("refers", refers);
		StringBuilder result = new StringBuilder();
		result.append(mainUrl);
		result.append("?");
		int index = 0;
		for (Object key : params.keySet()) {
			if(index <= 0) {
				index++;
			} else {
				result.append("&");
			}
			result.append(key.toString() + "=" + params.get(key).toString());
		}
		return result.toString();
	}

	public static String addParam(String url, String paramName, String paramValue) {
		if (StringUtils.isNotEmpty(url)) {
			if (url.contains("?")) {
				return url += "&" + paramName + "=" + paramValue;
			} else {
				return url += "?" + paramName + "=" + paramValue;
			}
		} else {
			return url;
		}
	}

	public static String getMainUrl(String url) {
		if (StringUtils.isNotEmpty(url) && url.contains("?")) {
			return url.substring(0, url.indexOf("?"));
		}
		return url;
	}

	public static String getQueryString(String url) {
		if (StringUtils.isNotEmpty(url) && url.contains("?")) {
			return url.substring(url.indexOf("?"), url.length());
		}
		return "";
	}

	/**
	 * 去掉url中的路径，留下请求参数部分
	 * 
	 * @param strURL
	 *            url地址
	 * @return url请求参数部分
	 */
	private static String truncateUrlPage(String strURL) {
		String strAllParam = null;
		String[] arrSplit = null;

		strURL = strURL.trim();

		arrSplit = strURL.split("[?]");
		if (strURL.length() > 1) {
			if (arrSplit.length > 1) {
				if (arrSplit[1] != null) {
					strAllParam = arrSplit[1];
				}
			}
		}
		return strAllParam;
	}

	public static Map<String, String> getParamsMap(String url) {
		Map<String, String> mapRequest = new HashMap<String, String>();

		String[] arrSplit = null;

		String strUrlParam = truncateUrlPage(url);
		if (strUrlParam == null) {
			return mapRequest;
		}
		// 每个键值为一组 www.2cto.com
		arrSplit = strUrlParam.split("[&]");
		for (String strSplit : arrSplit) {
			String[] arrSplitEqual = null;
			arrSplitEqual = strSplit.split("[=]");

			// 解析出键值
			if (arrSplitEqual.length > 1) {
				// 正确解析
				mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);

			} else {
				if (arrSplitEqual[0] != "") {
					// 只有参数没有值，不加入
					mapRequest.put(arrSplitEqual[0], "");
				}
			}
		}
		return mapRequest;
	}

	public static String getBasePath(HttpServletRequest request) {
		if (request == null) {
			return null;
		}
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName();
		if (request.getServerPort() == 80 || request.getServerPort() == 443) {
			;
		} else {
			basePath += ":" + request.getServerPort();
		}
		basePath += path;
		return basePath;
	}
	
	public static String getBaseServerPath(HttpServletRequest request) {
		if (request == null) {
			return null;
		}
		// + ":" + request.getServerPort() + path + "/";
		String basePath = request.getScheme() + "://" + request.getServerName();
		if(request.getServerPort() == 80 || request.getServerPort() == 443) {
			;
		} else {
			basePath += ":" + request.getServerPort();
		}
		return basePath;
	}
	

	/**
	 * 仅限用于demo.xxx.com这种
	 * 
	 * @param request
	 * @param domain
	 * @return
	 */
	public static String getDomainBasePath(HttpServletRequest request, String domain) {
		if (request == null) {
			return null;
		}
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName().replaceAll("www", domain);
		if (request.getServerPort() == 80 || request.getServerPort() == 443) {
			;
		} else {
			basePath += ":" + request.getServerPort();
		}
		basePath += path + "/";
		return basePath;
	}

	public static String correctEmail(String url) {
		return url.replace("!", ".");
	}

	public static boolean isWechat(HttpServletRequest request) {
		String userAgent = request.getHeader("user-agent");
		if (StringUtils.isNotEmpty(userAgent)) {
			userAgent = userAgent.toLowerCase();
			// 是微信浏览器
			if (userAgent.indexOf("micromessenger") > 0) {
				return true;
			}
		}
		return false;
	}

	static String phoneReg = "\\b(ip(hone|od)|android|opera m(ob|in)i" + "|windows (phone|ce)|blackberry"
			+ "|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp" + "|laystation portable)|nokia|fennec|htc[-_]"
			+ "|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
	static String tableReg = "\\b(ipad|tablet|(Nexus 7)|up.browser" + "|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
	static Pattern phonePat = Pattern.compile(phoneReg, Pattern.CASE_INSENSITIVE);
	static Pattern tablePat = Pattern.compile(tableReg, Pattern.CASE_INSENSITIVE);

	public static boolean isMobile(HttpServletRequest request) {
		String userAgent = request.getHeader("user-agent");
		if (StringUtils.isNotEmpty(userAgent)) {
			userAgent = userAgent.toLowerCase();
			Matcher matcherPhone = phonePat.matcher(userAgent);
			Matcher matcherTable = tablePat.matcher(userAgent);
			if (matcherPhone.find() || matcherTable.find()) {
				return true;
			}
		}
		return false;
	}

	public static String getBrowerCode(HttpServletRequest request) {
		if (isWechat(request)) {
			return "wechant";
		} else if (isMobile(request)) {
			return "mobile";
		} else {
			return "pc";
		}
	}

	public static String correctParam(String param) {
		if (StringUtils.isNotEmpty(param)) {
			param = param.replace("+", "%20");
			param = param.replace("/", "%2F");
			param = param.replace("?", "%3F");
			param = param.replace("%", "%25");
			param = param.replace("#", "%23");
			param = param.replace("&", "%26");
		}
		return param;
	}

	public static int getScreenWidth(HttpServletRequest request) {
		if (RequestUtil.isMobile(request)) {
			return 800;
		}
		return 800;
	}

	public static String getReferIdFromRefers(String refers) {
		// TODO Auto-generated method stub
		if(StringUtils.isNotEmpty(refers)) {
			if(StringUtils.isNumeric(refers)) {
				return refers;
			}
			String[] arr = refers.split(",");
			for(int i= arr.length-1; i>=0; i--) {
				if(StringUtils.isNotEmpty(arr[i])) {
					String str = arr[i].trim();
					if(str.startsWith("u")) {
						return str.replace("u", "");
					}
				}
			}
		}
		return null;
	}
	


	public static void main(String[] args) {
		// String url = "http://localhost:8080/";
		// String url = "http://5555.yld.com";
		// String dm = getSubDomainName(url);
		// System.out.println(dm);
		// String ab = ".";
		// System.out.println(Base64.encodeAsString("1"));
		// System.out.println(replaceSubdomainToParam("http://demo.mingkeyun.com/pay/payCourseOrder?courseId=243"));
		//String url = "/abc/cc?refers=a1&courseId=1&bbb=2";
		//System.out.print(RequestUtil.addReferParam(url, "u222"));
		//String url = "http://ssh.yinlidao.com/lession/lessionPlay?from=course&courseId=256&lessionId=192";
		//String okey = CacheConsts.Ssid_Key_Prefix + 3l;
		//String key = URLEncoder.encode((Base64.encodeAsString(okey)));
		//String url1 = RequestUtil.addWxParam(url, key);
		//System.out.println(url1);
		//String refers = "u1, u2, a1, a3";
		//System.out.println(getReferIdFromRefers(refers));
		String url = "http://ssh.yinlidao.com/lession/lessionDetail?from=course&courseId=312&lessionId=296";
		System.out.println(RequestUtil.addReferParam(url, "u11"));
	}

	
}
