package com.eposapp.common.util;

import com.eposapp.common.helper.Reflector;
import com.eposapp.entity.SysUserEntity;
import org.springframework.ui.Model;

import javax.persistence.Table;
import javax.xml.crypto.Data;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author: eiven
 * @Date: Created in 20:49 2018/5/18
 */
public class EntityUtil {
    public static final String SET_METHOD = "set";

    public static final String GET_METHOD = "get";

    public static final String CLASSTYPE_PRE = "class ";

    /**
     * 过滤属性不设值
     * */
    public static final List<String> FILTER_FIELD = Arrays.asList("createId","createTime","updateId","updateTime");


    /**
     * 获取Hql表名
     * */
    public static  String getHqlTableName(Class clazz){
        return clazz.getSimpleName();
    }
    /**
     * 获取Sql表名
     * */
    public static  String getSqlTableName(Class clazz){
        Table annotation = (Table)clazz.getAnnotation(Table.class);
        return annotation.name();
    }



    public static void putMapDataIntoEntity(Map<String, Object> map, Object entity)  {
        try {
            if (entity != null && map != null&& map.size()>0) {
                //获取传入实体类的属性Filed数组
                //Field[] field_arr = Class.forName(entity.getClass().getCanonicalName()).getDeclaredFields();
               Set<Field> field_arr =  Reflector.getAllDeclaredFields(entity.getClass());
                //遍历数组
                for (Field field : field_arr) {
                    //获取属性名称
                    String fieldName = field.getName();
                    //判断map中是否存在对应的属性名称（注：这个方法要想使用就必须保证map中的key与实体类的属性名称一致）
                    if (!FILTER_FIELD.contains(fieldName)&&map.containsKey(fieldName)) {
                        // 获取属性的类型 字符串String fieldType = field.getGenericType().toString();
                        final Class<?> fieldTypeClz = field.getType();
                        //调用本类中的帮助方法来获取当前属性名对应的方法名（“set”为getMethodName方法的第二个参数）
                        String methodName = getMethodName(fieldName,SET_METHOD);
                        //获取当前key对应的值
                        Object fieldValue = map.get(fieldName);
                        //根据获取的方法名称及当前field的类型获取method对象
                        Method method = getDeclaredMethod(entity,methodName,fieldTypeClz);
                        //根据属性类型转换值类型
                        fieldValue =  setFieldValue(fieldValue,fieldTypeClz);
                        //调用当前实体类的方法将数值set进去
                        method.invoke(entity, fieldValue);
                    }
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    } /**
     * 循环向上转型, 获     * @param object : 子类对象
     * @param methodName : 父类中的方法名
     * @param parameterTypes : 父类中的方法参数类型
     * @return 子类或父类中的方法对象
     */

    public static Method getDeclaredMethod(Object object, String methodName, Class<?> ... parameterTypes){
        Method method = null ;
        for(Class<?> clazz = object.getClass() ; clazz != Object.class ; clazz = clazz.getSuperclass()) {
            try {
                method = clazz.getDeclaredMethod(methodName, parameterTypes) ;
                return method ;
            } catch (Exception e) {
                //这里甚么都不能抛出去。
                //如果这里的异常打印或者往外抛，则就不会进入
            }
        }
        return null;
    }

    /**
     * @author eiven
     * @param fieldName 属性名
     * @param methodType 获取方法类型（set or get）
     * @return 方法名称，反射使用
     */
    public static String getMethodName(String fieldName, String methodType) {
        String methodName = "";
        if (StringUtils.isNotBlank(fieldName)) {
            methodName =  StringUtils.toUpperCaseForIndex(fieldName);
        }
        return methodType+methodName;
    }

    /**
     * 根据属性类型转换值类型
     * */
    public static Object setFieldValue(Object fieldValue,Class<?> fieldTypeClz){
        Object newValue = null;
        if (fieldTypeClz.isAssignableFrom(String.class)){
            newValue = fieldValue==null?"":fieldValue.toString();
        }else if(fieldTypeClz.isAssignableFrom(Integer.class)){
            newValue = fieldValue==null?null:Integer.valueOf(fieldValue.toString());
        }else if(fieldTypeClz.isAssignableFrom(Double.class)){
            newValue =fieldValue==null?null: Double.valueOf(fieldValue.toString());
        }else if(fieldTypeClz.isAssignableFrom(Date.class)){
            newValue = fieldValue==null?null: DateConverUtil.getDatebyStr(fieldValue.toString());
        }else if(fieldTypeClz.isAssignableFrom(Timestamp.class)){
            newValue = fieldValue==null?null: Timestamp.valueOf(fieldValue.toString());
        }else if(fieldTypeClz.isAssignableFrom(BigInteger.class)){
            newValue = fieldValue==null?null: new BigInteger(fieldValue.toString());
        }
        return newValue;
    }



}
