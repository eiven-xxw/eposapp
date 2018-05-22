package com.eposapp.common.util;


import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * spring StringUtils扩展
 * @author eiven
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

	/**
	 * 首字母大写
	 *
	 * @param string
	 * @return
	 */
	public static String toUpperCaseForIndex(String str) {
		char[] methodName = str.toCharArray();
		methodName[0] = toUpperCase(methodName[0]);
		return String.valueOf(methodName);
	}
	/**
	 * 字符转成大写
	 * @param chars
	 * @return
	 */
	public static char toUpperCase(char chars) {
		if (97 <= chars && chars <= 122) {
			chars ^= 32;
		}
		return chars;
	}

	public static String getMapKeyValue(Map dataMap, String key) {
		return dataMap.get(key)==null?"":dataMap.get(key).toString();
	}

	public static boolean hasLength(CharSequence str) {
		return str != null && str.length() > 0;
	}

	public static boolean hasLength(String str) {
		return str != null && !str.isEmpty();
	}

	public static boolean hasText(CharSequence str) {
		return hasLength(str) && containsText(str);
	}

	public static boolean hasText(String str) {
		return hasLength(str) && containsText(str);
	}

	private static boolean containsText(CharSequence str) {
		int strLen = str.length();

		for(int i = 0; i < strLen; ++i) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}

		return false;
	}

	public static String listToString(List list, char separator) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i)).append(separator);
		}
		return sb.toString().substring(0, sb.toString().length() - 1);
	}

	public static List<String> stringToList(String str, String spliter) {
		if (isNotEmpty(str) && isNotEmpty(spliter)) {
			List<String> result = new ArrayList<String>();
			String[] arr = str.split(spliter);
			for (String s1 : arr) {
				result.add(s1);
			}
			return result;
		}
		return null;
	}
	public static boolean isNumeric(String str) {
		if (StringUtils.isNotEmpty(str)) {
			Pattern pattern = Pattern.compile("[0-9]*");
			Matcher isNum = pattern.matcher(str);
			if (!isNum.matches()) {
				return false;
			}
			return true;
		}
		return false;
	}

	public static String arrayToString(String[] strs, char separator) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < strs.length; i++) {
			sb.append(strs[i]).append(separator);
		}
		return sb.toString().substring(0, sb.toString().length() - 1);
	}

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static boolean isNotEmpty(String str) {
		return !StringUtils.isEmpty(str);
	}

	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}
	public static boolean isNotBlank(String str) {
		return !StringUtils.isBlank(str);
	}

	/**
	 * 通过设置的数字区间，产生一个区间的随机数. 
	 * @param a  
	 * @param b  
	 * @return 随机值 ，不会返回最大的值。
	 */
	public static Integer getRandomByQJ(int a, int b) {
		int s=0;
		if(a==b){
			s=a;
		}else{
			//求得最小值
			int min =a>b?b:a;
			//求得两值差
			int fw= Math.abs(a-b);
			//产生随机数
			int sj=(int)(Math.random()*fw);
			s=min+sj;
		}
		return s;
	}
	/**
	 * 文件中文下载时候使用的转码类
	 * @param isMSIE
	 * @param s
	 * @return
	 */
	public static String toUtf8String(boolean isMSIE, String s) {
		if ( isMSIE ) {
			try {
				s = java.net.URLEncoder.encode(s, "UTF8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			try {
				s = new String(s.getBytes("UTF-8"), "ISO-8859-1");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return s;
	}
	/**
	 * 把中文转成Unicode码
	 * @param str
	 * @return
	 */
	public static String chinaToUnicode(String str){
		String result="";
		for (int i = 0; i < str.length(); i++){
            int chr1 = (char) str.charAt(i);
			//汉字范围 \u4e00-\u9fa5 (中文)
            if(chr1>=19968&&chr1<=171941){
                result+="\\u" + Integer.toHexString(chr1);
            }else{
            	result+=str.charAt(i);
            }
        }
		return result;
	}
	/**
	 * Unicode转换成 中文
	 * @param unicodeStr
	 * @return
	 */
	public static String unicodeToString(String unicodeStr) {
		//待开发
		return null;
	}
	/**
	 * 判断是否为中文字符
	 * @param c
	 * @return
	 */
	public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }
	/**
	 * java去除字符串中的空格、回车、换行符、制表符
	 * @return
	 */
	public static String replaceBlank(String str){
		String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
		/*	    笨方法：String s = "你要去除的字符串";
		        1.去除空格：s = s.replace('\\s','');
		        2.去除回车：s = s.replace('\n','');
		注：\n 回车(\u000a) 
		 \t 水平制表符(\u0009) 
		 \s 空格(\u0008) 
		 \r 换行(\u000d) */

	}


	/**
	 * Object 对象转换成字符串
	 * @param obj
	 * @return
	 */
	public static String toStringByObject(Object obj){
		return toStringByObject(obj,false,null);
	}
	/**
	 * Object 对象转换成字符串
	 * @param obj
	 * @return
	 */
	public static String toStringByObject(Object obj, boolean isNeedTrim){
		return toStringByObject(obj,isNeedTrim,null);
	}
	/**
	 * Object 对象转换成字符串，并可以根据参数去掉两端空格
	 * @param obj
	 * @return
	 */
	public static String toStringByObject(Object obj, boolean isNeedTrim, String dataType){
		if(obj==null){
			return "";
		}else{
			if(isNeedTrim){
				return obj.toString().trim();
			}else{
				//如果有设置时间格式类型，这转换
				if(StringUtils.hasText(dataType)){
					if(obj instanceof Timestamp){
						return DateConverUtil.getStrbyDate((Timestamp)obj,dataType);
					}else if(obj instanceof Date){
						return DateConverUtil.getStrbyDate((Timestamp)obj,dataType);
					}
				}
				return obj.toString();
			}
		}
	}
	/**
	 * Object 对象转换成Integer
	 * @param obj
	 * @return 转换失败== 0
	 */
	public static Integer toIntegerByObject(Object obj){
		try {
			return Integer.valueOf(toStringByObject(obj,true));
		} catch (Exception e) {
			return 0;
		}
	}
	
	/**
	 * Object 对象转换成Double
	 * @param obj
	 * @return 转换失败== 0.0
	 */
	public static Double toDoubleByObject(Object obj){
		try {
			return Double.valueOf(toStringByObject(obj,true));
		} catch (Exception e) {
			return 0.0d;
		}
	}
	/**
	 * Object 对象转换成Float
	 * @param obj
	 * @return 转换失败== 0.0
	 */
	public static Float toFloatByObject(Object obj){
		try {
			return Float.valueOf(toStringByObject(obj,true));
		} catch (Exception e) {
			return 0.0f;
		}
	}
	
	/**
	 * List<String>转换成 xx1,xx2  字符串
	 * @param list
	 * @return
	 */
	public static String toStringBySpilt(List<String> list){
		if(list!=null&&list.size()>0){
			StringBuffer sb=new StringBuffer();
			for(int i=0,b=list.size();i<b;i++){
				String s=list.get(i);
				if(StringUtils.hasText(s)){
					if(i<b-1){
						sb.append(s).append(",");
					}else{
						sb.append(s);
					}
				}
			}
			return sb.toString();
		}else{
			return "";
		}
	}
	/**
	 * List<String>转换成 'xx1','xx2'  字符串
	 * @param list
	 * @return
	 */
	public static String toStringBySqlIn(List<String> list){
		if(list!=null&&list.size()>0){
			StringBuffer sb=new StringBuffer();
			for(int i=0,b=list.size();i<b;i++){
				String s=list.get(i);
				if(StringUtils.hasText(s)){
					if(i<b-1){
						sb.append("'").append(s).append("',");
					}else{
						sb.append("'").append(s).append("'");
					}
				}
			}
			return sb.toString();
		}else{
			return "'-0'";
		}
	}
	/**
	 * Object[] 转换成 'xx1','xx2'  字符串
	 * @return
	 */
	public static String toStringBySqlIn(String[] ss){
		if(ss!=null&&ss.length>0){
			StringBuffer sb=new StringBuffer();
			for(int i=0,b=ss.length;i<b;i++){
				String s=ss[i];
				if(StringUtils.hasText(s)){
					if(i<b-1){
						sb.append("'").append(s).append("',");
					}else{
						sb.append("'").append(s).append("'");
					}
				}
			}
			return sb.toString();
		}else{
			return "";
		}
	}
	/**
	 * 根据传入的String数组，清除空值，包括""。
	 * @param objs
	 * @return  Object[]数组
	 */
	public static String[] getArrayByArray(String[] objs){
		if(objs!=null&&objs.length>0){
			List<String> list=new ArrayList<String>();
			for(String o:objs){
				if(hasText(o)){
					list.add(o);
				}
			}
			String[] ss=new String[list.size()];
			for(int i=0,b=list.size();i<b;i++){
				ss[i]=list.get(i);
			}
			return ss;
		}else{
			return new String[]{};
		}
	}
	/**
	 * 获取32位UUID
	 * @return
	 */
	public static String getUUID32(){
		return UUID.randomUUID().toString().replaceAll("-","");
	}
	/**
	 * 获取36位UUID
	 * @return
	 */
	public static String getUUID36(){
		return UUID.randomUUID().toString();
	}
	/**
	 * 字符串类型的值 转换成特定的基本类型
	 * @param str 传入的字符串类型的值
	 * @param type 传入的转换的基本类型的 ，例如：java.lang.Long
	 * @return 转换后的值
	 * @说明：暂时不支持数组类型，该功能还待严格测试
	 */
	public static Object strToType(Object str, String type){
		Object obj=str;
		if(str!=null){
			if("java.lang.Integer".equals(type)||"int".equals(type)){
				if(str instanceof Double){
					obj=(int)((double) Double.valueOf(str.toString()));
				}else{
					obj= Integer.valueOf(str.toString());
				}
	    	}else if("java.lang.Long".equals(type)||"long".equals(type)){
	    		obj= Long.parseLong(str.toString());
	    	}else if("java.lang.String".equals(type)){
	    		obj=str.toString();
	    	}else if("java.util.Date".equals(type)){
	    		if(!(str instanceof Date)){
	    			obj=DateConverUtil.getDatebyStr(str.toString());
	    		}
	    	}else if("java.lang.Double".equals(type)||"double".equals(type)){
	    		obj= Double.valueOf(str.toString());
	    	}else if("java.lang.Short".equals(type)||"short".equals(type)){
	    		obj= Short.valueOf(str.toString());
	    	}else if("java.lang.Boolean".equals(type)||"boolean".equals(type)){
	    		obj= Boolean.valueOf(str.toString());
	    	} else if("java.lang.Float".equals(type)||"float".equals(type)){
	    		obj= Float.valueOf(str.toString());
	    	} 
		}
		return obj;
	}


}
