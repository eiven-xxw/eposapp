package com.eposapp.common.helper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * @author eiven
 * @date 2018年3月19日
 * Updates
 * .....
 * 
 */

public class JsonbjectParser {
	private static final Logger logger = LoggerFactory.getLogger(JsonbjectParser.class);
	
	private static Map<String, Object> simpleTypes = null;
	
	public JsonbjectParser() {
		if(simpleTypes == null) {
			simpleTypes = new HashMap<String, Object>();
			simpleTypes.put("int", Integer.parseInt("0"));
			simpleTypes.put("float", Float.parseFloat("0.0f"));
			simpleTypes.put("double", Double.parseDouble("0d"));
			simpleTypes.put("long", Long.parseLong("0"));
			simpleTypes.put("boolean", Boolean.parseBoolean("false"));
		}
	}

	
	
	/**
	 * parse Array
	 * 
	 * @param array
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> parseList(JSONArray array, Class<T> cls) throws Exception {
		List<T> list = new ArrayList<T>();
		if (array == null) {
			return list;
		}
		int size = array.size();
		if (size > 0) {
			for (int index = 0; index < size; index++) {
				Object obj = array.get(index);
				if(obj instanceof JSONObject) {
					JSONObject source = array.getJSONObject(index);
					if(source == null) {
						if(obj != null) {
							T object = (T)obj;
							list.add(object);
						} else {
							list.add(null);
						}
						//list.add(<T>obj);
					} else {
						T object = parseItem(source, cls);
						list.add(object);
					}
					
				} else if(obj instanceof String) {
					list.add((T)obj);
				} else if (simpleTypes.keySet().contains(obj.getClass())) {
					try {
						Object value = null;
						if("int".equals(obj.getClass().getSimpleName())) {
							value = Integer.parseInt(String.valueOf(obj));
						} else if("float".equals(obj.getClass().getSimpleName())) {
							value = Float.parseFloat(String.valueOf(obj));
						} else if("double".equals(obj.getClass().getSimpleName())) {
							value = Double.parseDouble(String.valueOf(obj));
						} else if("long".equals(obj.getClass().getSimpleName())) {
							value =  Long.parseLong(String.valueOf(obj));
						} else if("boolean".equals(obj.getClass().getSimpleName())) {
							value =  Boolean.parseBoolean(String.valueOf(obj));
						}
						list.add((T)value);
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				} 
			}
		}
		return list;
	}
	
	/**
	 * parse Item
	 * 
	 * @param jobject json 对象 代转换的数据
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	public  <T> T  parseItem(JSONObject jobject, Class<T> cls) throws Exception {
		final T object = cls.newInstance();
		if((object instanceof Map) && jobject != null) {
			((Map) object).putAll(jobject);
			return object;
		}
		final Set<Field> fields = Reflector.getAllDeclaredFields(cls);
		if (fields != null && fields.size() > 0) {
			for (Field field : fields) {
				final String fieldName = field.getName();
				if ("serialVersionUID".equals(fieldName)) {
					continue;
				}
				if(Modifier.isFinal(field.getModifiers())) {
					continue;
				}
				if(Modifier.isStatic(field.getModifiers())) {
					continue;
				}
				if(Modifier.isAbstract(field.getModifiers())) {
					continue;
				}
				if(Modifier.isSynchronized(field.getModifiers())) {
					continue;
				}
				field.setAccessible(true);
				final Class<?> fieldClz = field.getType();
				final String fieldClzName = fieldClz.getName();
				
				if(fieldClz.isPrimitive()) {
					if (simpleTypes.keySet().contains(fieldClzName)) {
						try {
							Object value = null;
							if("int".equals(fieldClzName)) {
								value = Integer.parseInt(jobject.getString(fieldName));
							} else if("float".equals(fieldClzName)) {
								value = Float.parseFloat(jobject.getString(fieldName));
							} else if("double".equals(fieldClzName)) {
								value = Double.parseDouble(jobject.getString(fieldName));
							} else if("long".equals(fieldClzName)) {
								value =  Long.parseLong(jobject.getString(fieldName));
							} else if("boolean".equals(fieldClzName)) {
								value =  Boolean.parseBoolean(jobject.getString(fieldName));
							}
							field.set(object, value);
						} catch (Exception e) {
							field.set(object, simpleTypes.get(fieldClzName));
						}
					} 
				} else {
					if(Collection.class.isAssignableFrom(fieldClz)) {
						ParameterizedType pt = (ParameterizedType) field.getGenericType();
						Class<?> fieldListClz = (Class<?>) pt.getActualTypeArguments()[0];
						try {
							field.set(object, parseList(jobject.getJSONArray(fieldName), fieldListClz));
						} catch (Exception e) {
							field.set(object, null);
						}
					} else {
						try {
							Object value = null;
							if(fieldClz.isAssignableFrom(String.class)) {
								value = jobject.get(fieldName);
							} else if(fieldClz.isAssignableFrom(Integer.class)) {
								value = Integer.parseInt(jobject.getString(fieldName));
							} else if(fieldClz.isAssignableFrom(Float.class)) {
								value = Float.parseFloat(jobject.getString(fieldName));
							} else if(fieldClz.isAssignableFrom(Double.class)) {
								value = Double.parseDouble(jobject.getString(fieldName));
							} else if(fieldClz.isAssignableFrom(Long.class)) {
								value =  Long.parseLong(jobject.getString(fieldName));
							} else if(fieldClz.isAssignableFrom(Boolean.class)) {
								value =  Boolean.parseBoolean(jobject.getString(fieldName));
							} else {
								value = parseItem(jobject.getJSONObject(fieldName), fieldClz);
							}
							field.set(object, value);
						} catch (Exception e) {
							if(simpleTypes.containsKey(fieldClz.getSimpleName().toLowerCase())) {
								field.set(object, simpleTypes.get(fieldClz.getSimpleName().toLowerCase()));
							} else {
								field.set(object,null);
							}
						}
						
//						if(fieldClz.isAssignableFrom(String.class)) {
//							try {
//								field.set(object, jobject.get(fieldName));
//							} catch (Exception e) {
//								field.set(object,null);
//							}
//						} else if(fieldClz.isAssignableFrom(Integer.class)) {
//							try {
//								field.set(object,  Integer.parseInt(jobject.getString(fieldName)));
//							} catch (Exception e) {
//								field.set(object,0);
//							}
//						} else if(fieldClz.isAssignableFrom(Long.class)) {
//							try {
//								field.set(object,  Long.parseLong(jobject.getString(fieldName)));
//							} catch (Exception e) {
//								field.set(object,0l);
//							}
//						} else if(fieldClz.isAssignableFrom(Double.class)) {
//							try {
//								field.set(object, Double.parseDouble(jobject.getString(fieldName)));
//							} catch (Exception e) {
//								field.set(object,0d);
//							}
//						} else if(fieldClz.isAssignableFrom(Float.class)) {
//							try {
//								field.set(object, Float.parseFloat(jobject.getString(fieldName)));
//							} catch (Exception e) {
//								field.set(object,0f);
//							}
//						} else {
//							try {
//								field.set(object, parseItem(jobject.getJSONObject(fieldName), fieldClz));
//							} catch (Exception e) {
//								field.set(object,null);
//							}
//						}
					}
				}	
			}
		}
		return object;
	}
}
