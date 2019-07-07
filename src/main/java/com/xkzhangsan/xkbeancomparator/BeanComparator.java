package com.xkzhangsan.xkbeancomparator;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.ArrayUtils;

public class BeanComparator {

	/**
	 * 对比bean属性,默认使用StringBuilder处理
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public static String compareBean(Object source, Object target) {
		return compareBean(source, target, null, false);
	}

	/**
	 * 对比bean属性，支持选择StringBuffer or StringBuilder
	 * 
	 * @param source
	 * @param target
	 * @param isochronous use StringBuffer or StringBuilder
	 * @return
	 */
	public static String compareBean(Object source, Object target, boolean isochronous) {
		return compareBean(source, target, null, isochronous);
	}

	/**
	 * 对比bean属性，支持属性名称map，只对比map中包含的属性值
	 * 
	 * @param source
	 * @param target
	 * @param propertyTranslationMap
	 * @return
	 */
	public static String compareBean(Object source, Object target, Map<String, String> propertyTranslationMap) {
		return compareBean(source, target, propertyTranslationMap, false);
	}

	/**
	 * 对比bean属性,支持所有参数
	 * 
	 * @param source
	 * @param target
	 * @param propertyTranslationMap
	 * @param isochronous  use StringBuffer or StringBuilder
	 * @return
	 */
	public static String compareBean(Object source, Object target, Map<String, String> propertyTranslationMap,
			boolean isochronous) {
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
		return compareProperty(source, target, clazz, fields, propertyTranslationMap, isochronous);
	}

	/**
	 * 根据参数isochronous选择StringBuffer or StringBuilder，进行属性对比
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
			Map<String, String> propertyTranslationMap, boolean isochronous) {
		if (isochronous) {
			StringBuffer sBuffer = new StringBuffer();
			compareUnit(source, target, clazz, fields, propertyTranslationMap, sBuffer, null);
			return sBuffer.toString();
		} else {
			StringBuilder sBuilder = new StringBuilder();
			compareUnit(source, target, clazz, fields, propertyTranslationMap, null, sBuilder);
			return sBuilder.toString();
		}
	}

	/**
	 * 比较所有属性值，根据传参转换属性名称，组装结果（核心逻辑）
	 * 
	 * @param source
	 * @param target
	 * @param clazz
	 * @param fields
	 * @param propertyTranslationMap
	 * @param sBuffer
	 * @param sBuilder
	 */
	private static void compareUnit(Object source, Object target, Class<?> clazz, Field[] fields,
			Map<String, String> propertyTranslationMap, StringBuffer sBuffer, StringBuilder sBuilder) {
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
			if(nameMapped != null){
				name = nameMapped;
			}
			comparePropertyValue(sBuffer, sBuilder, name, propertyClazz, sourcePropertyValue, targetPropertyValue);
		}
	}

	/**
	 * 获取属性值
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
	 * 
	 * @param sBuffer
	 * @param sBuilder
	 * @param name
	 * @param propertyClazz
	 * @param sourcePropertyValue
	 * @param targetPropertyValue
	 */
	private static void comparePropertyValue(StringBuffer sBuffer, StringBuilder sBuilder, String name,
			Class<?> propertyClazz, Object sourcePropertyValue, Object targetPropertyValue) {
		if (!Objects.equals(sourcePropertyValue, targetPropertyValue)) {
			String sourcePropertyValueStr = sourcePropertyValue == null ? "" : sourcePropertyValue.toString();
			String targetPropertyValueStr = targetPropertyValue == null ? "" : targetPropertyValue.toString();
			if (sBuffer != null) {
				sBuffer.append(name + ":" + sourcePropertyValueStr + "->" + targetPropertyValueStr + ",");
			} else if (sBuilder != null) {
				sBuilder.append(name + ":" + sourcePropertyValueStr + "->" + targetPropertyValueStr + ",");
			}
		}
	}
}
