package com.xkzhangsan.xkbeancomparator.test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.xkzhangsan.xkbeancomparator.BeanComparator;

public class BeanTest {

	public static void main(String[] args) {
		test1();
		test2();
	}

	/**
	 * 对比所有属性
	 * Compare all attributes
	 */
	public static void test1() {
		Bean b1 = new Bean();
		b1.setId(1);
		b1.setName("aa");
		b1.setBigD(new BigDecimal("111111111111.12"));
		b1.setDate(new Date());

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Bean b2 = new Bean();
		b2.setId(1);
		b2.setName("aa2");
		b2.setBigD(new BigDecimal("1111111111111.15"));
		b2.setDate(new Date());

		String s = BeanComparator.compareBean(b1, b2);
		System.out.println(s);
	}

	/**
	 * 只对比map中的属性，并根据key替换属性描述
	 * Compare only the attributes in the map, 
	 * and replace the attribute description based on the key
	 */
	public static void test2() {
		Map<String, String> map = new HashMap<>();
		map.put("name", "姓名");

		Bean b1 = new Bean();
		b1.setId(1);
		b1.setName("aa");
		b1.setBigD(new BigDecimal("111111111111.12"));
		b1.setDate(new Date());

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Bean b2 = new Bean();
		b2.setId(1);
		b2.setName("aa2");
		b2.setBigD(new BigDecimal("1111111111111.15"));
		b2.setDate(new Date());

		String s = BeanComparator.compareBean(b1, b2, map);
		System.out.println(s);
	}

}
