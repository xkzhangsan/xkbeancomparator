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
		test3();

	}

	/**
	 * 对比所有属性，使用StringBuilder
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

	/**
	 * 对比所有属性，使用StringBuffer
	 */
	public static void test3() {
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

		String s = BeanComparator.compareBean(b1, b2, true);
		System.out.println(s);
	}

}
