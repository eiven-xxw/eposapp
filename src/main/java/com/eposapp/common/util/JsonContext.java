package com.eposapp.common.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eposapp.common.helper.JsonbjectParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 用org.json实现的轻量级的json解析
 * @author Fred
 * @date 2018年3月19日
 * Updates
 * .....
 * 
 */

public class JsonContext {
	private static final Logger logger = LoggerFactory.getLogger(JsonContext.class);
	
	private static JsonContext instance = null;
	private static JsonbjectParser parser = null;
	
	private JsonContext() {
		;
	}
	
	public static JsonContext instance() {
		if(instance == null) {
			instance = new JsonContext();
			parser = new JsonbjectParser();
		}
		return instance;
	}
	
	public String toJsonString(Map<String, Object> map)  {
		return JSONObject.toJSONString(map);  
	}
	
	public String toJsonString(Collection<Object> collection) {
		return JSONObject.toJSONString(collection); 
	}
	
	public String toJsonString(Object obj)  {
		return JSONObject.toJSONString(obj);  
	}
	
	public <T> T parseItem(String jsonStr, Class<T> cls) {
		return parseItem(JSONObject.parseObject(jsonStr), cls);
	}
	
	public <T> T parseItem(JSONObject jsonObject, Class<T> cls) {
		try {
			return parser.parseItem(jsonObject, cls);
		} catch(Exception ex) {
			logger.error(ex.getMessage(), ex.getMessage());
		}
		return null;
	}
	
	public <T> List<T> parseList(String jsonStr, Class<T> cls)  {
		return parseList(JSONArray.parseArray(jsonStr), cls);
	}
	
	public <T> List<T> parseList(JSONArray array, Class<T> cls)  {
		try {
			return parser.parseList(array, cls);
		} catch(Exception ex) {
			logger.error(ex.getMessage(), ex.getMessage());
		}
		return null;
	}

	
	public static String toJson(Map<String, Object> map)  {
		return instance().toJsonString(map);  
	}
	
	public static String toJson(Collection<Object> collection) {
		return instance().toJsonString(collection); 
	}
	
	public static String toJson(Object obj)  {
		return instance().toJsonString(obj);  
	}
	
	public static <T> T jsonToObject(String jsonStr, Class<T> cls) {
		return instance().parseItem(jsonStr, cls);
	}
	
	public static <T> T jsonToObject(JSONObject jsonObject, Class<T> cls) {
		return instance().parseItem(jsonObject, cls);
	}
	
	public static <T> List<T> jsonToList(String jsonStr, Class<T> cls)  {
		return instance().parseList(jsonStr, cls);
	}
	
	public static <T> List<T> jsonToList(JSONArray array, Class<T> cls)  {
		return instance().parseList(array, cls);
	}
	
	
	public static void main(String[] args) {
		String ss = "{\"name\":[{\"id\":1,\"id\":2,\"id\":3,\"id\":4}]}";

		Map<String,String> map = new HashMap<String,String>();
		map.put("expireTime", "2");
		map.put("expireTime2", "2");
		String s=toJson(map);
		System.out.println(s);
		List list = jsonToList(ss, ArrayList.class);
		System.out.println(list);
	}
}
