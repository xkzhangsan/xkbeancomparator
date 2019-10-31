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

public class BeanComparator {
	
	/**
	 * 获得对比结果
	 * Obtain comparative results
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public static CompareResult getCompareResult(Object source, Object target) {
		CompareResult compareResult = new CompareResult();
		String result = compareBean(source, target);
		if(!"".equals(result)){
			compareResult.setChanged(true);
			compareResult.setChangeContent(result);
		}
		return compareResult;
	}
	
	/**
	 * 获得对比结果，支持属性名称map，只对比map中包含的属性值
	 * Get the comparison result, support the attribute name map, 
	 * and compare only the attribute values contained in the map
	 * 
	 * @param source
	 * @param target
	 * @param propertyTranslationMap
	 * @return
	 */
	public static CompareResult getCompareResult(Object source, Object target, Map<String, String> propertyTranslationMap) {
		CompareResult compareResult = new CompareResult();
		String result = compareBean(source, target, propertyTranslationMap);
		if(!"".equals(result)){
			compareResult.setChanged(true);
			compareResult.setChangeContent(result);
		}
		return compareResult;
	}	

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
		if (fields == null || fields.length ==0) {
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
			Object sourcePropertyValue = getValueByField(source, clazz, field);
			Object targetPropertyValue = getValueByField(target, clazz, field);
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
	 * @param field
	 * @return
	 */
	private static Object getValueByField(Object obj, Class<?> clazz, Field field) {
		Object result = null;
		if (field.getGenericType().toString().equals("boolean")) {
			String methodName = field.getName();
			if (!methodName.startsWith("is")) {
				methodName = "is" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
			}
			Method method = null;
			try {
				method = clazz.getMethod(methodName);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
			try {
				result = method.invoke(obj);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		} else {
			PropertyDescriptor descriptor;
			try {
				descriptor = new PropertyDescriptor(field.getName(), clazz);
				Method method = descriptor.getReadMethod();
				result = method.invoke(obj);
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
		}
		return result;
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
