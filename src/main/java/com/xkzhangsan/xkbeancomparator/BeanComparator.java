package com.xkzhangsan.xkbeancomparator;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.ArrayUtils;

public class BeanComparator {

	/**
	 * 对比bean属性 
	 * Contrast bean properties with StringBuilder processing by
	 * default
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public static String compareBean(Object source, Object target) {
		return compareBean(source, target, null);
	}

	/**
	 * 对比bean属性，支持属性名称map，只对比map中包含的属性值 
	 * Compare the bean properties, support the
	 * property name map, and only compare the property values contained in the
	 * map
	 * 
	 * @param source
	 * @param target
	 * @param propertyTranslationMap
	 * @return
	 */
	public static String compareBean(Object source, Object target, Map<String, String> propertyTranslationMap) {
		if (source == null) {
			throw new RuntimeException("source is null.");
		}
		if (target == null) {
			throw new RuntimeException("target is null.");
		}
		if (!source.getClass().equals(target.getClass())) {
			throw new RuntimeException("source.class and target.class is not same.");
		}

		Class<?> clazz = source.getClass();
		Field[] fields = clazz.getDeclaredFields();
		if (ArrayUtils.isEmpty(fields)) {
			throw new RuntimeException("source.class has no property.");
		}
		for (Field field : fields) {
			if (!PropertyTypeUtil.isSampleProperty(field.getType())) {
				throw new RuntimeException("source.class has complex property.");
			}
		}
		return compareProperty(source, target, clazz, fields, propertyTranslationMap);
	}

	/**
	 * 比较所有属性值，根据传参转换属性名称，组装结果（核心逻辑） 
	 * Compare all attribute values, transform
	 * attribute names based on parameters, assemble results (core logic)
	 * 
	 * @param source
	 * @param target
	 * @param clazz
	 * @param fields
	 * @param propertyTranslationMap
	 * @param isochronous
	 * @return
	 */
	private static String compareProperty(Object source, Object target, Class<?> clazz, Field[] fields,
			Map<String, String> propertyTranslationMap) {
		StringBuilder sBuilder = new StringBuilder();
		for (Field field : fields) {
			String name = field.getName();
			String nameMapped = null;
			if (!(propertyTranslationMap == null || propertyTranslationMap.isEmpty())) {
				if (!propertyTranslationMap.containsKey(name)) {
					continue;
				} else {
					nameMapped = propertyTranslationMap.get(name);
				}
			}
			Class<?> propertyClazz = field.getClass();
			Object sourcePropertyValue = getValueByField(source, clazz, name);
			Object targetPropertyValue = getValueByField(target, clazz, name);
			if (nameMapped != null) {
				name = nameMapped;
			}
			comparePropertyValue(sBuilder, name, propertyClazz, sourcePropertyValue, targetPropertyValue);
		}
		return sBuilder.toString();
	}

	/**
	 * 获取属性值
	 * Get attribute value
	 * 
	 * @param obj
	 * @param clazz
	 * @param name
	 * @return
	 */
	private static Object getValueByField(Object obj, Class<?> clazz, String name) {
		PropertyDescriptor descriptor;
		Object value = null;
		try {
			descriptor = new PropertyDescriptor(name, clazz);
			Method method = descriptor.getReadMethod();
			value = method.invoke(obj);
		} catch (IntrospectionException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return value;
	}

	/**
	 * 对比属性值
	 * Contrast attribute value
	 * 
	 * @param sBuilder
	 * @param name
	 * @param propertyClazz
	 * @param sourcePropertyValue
	 * @param targetPropertyValue
	 */
	private static void comparePropertyValue(StringBuilder sBuilder, String name, Class<?> propertyClazz,
			Object sourcePropertyValue, Object targetPropertyValue) {
		if (!Objects.equals(sourcePropertyValue, targetPropertyValue)) {
			String sourcePropertyValueStr = getFormatStr(sourcePropertyValue);
			String targetPropertyValueStr = getFormatStr(targetPropertyValue);
			sBuilder.append(name + ":" + sourcePropertyValueStr + "->" + targetPropertyValueStr + ",");
		}
	}
	
	/**
	 * 格式化属性
	 * Format attribute
	 * @param propertyValue
	 * @return
	 */
	private static String getFormatStr(Object propertyValue){
		if(propertyValue == null){
			return "";
		}
		if(propertyValue instanceof Date){
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date)propertyValue);
		}else{
			return propertyValue.toString();
		}
	}
}
