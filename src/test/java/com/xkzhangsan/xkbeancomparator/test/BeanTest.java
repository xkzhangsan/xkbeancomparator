package com.xkzhangsan.xkbeancomparator.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.xkzhangsan.xkbeancomparator.BeanComparator;
import com.xkzhangsan.xkbeancomparator.CompareResult;

public class BeanTest {

	/**
	 * 对比所有属性
	 * Compare all attributes
	 */
	@Test
	public void test1() {
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
	@Test
	public void test2() {
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
	 * 对比所有属性
	 * Compare all attributes,use method getCompareResult
	 */
	@Test
	public void test3() {
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

		CompareResult compareResult = BeanComparator.getCompareResult(b1, b2);
		if(compareResult.isChanged()){
			System.out.println(compareResult.getChangeContent());
		}
	}

	/**
	 * 只对比map中的属性，并根据key替换属性描述
	 * Compare only the attributes in the map, 
	 * and replace the attribute description based on the key,use method getCompareResult
	 */
	@Test
	public void test4() {
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

		CompareResult compareResult = BeanComparator.getCompareResult(b1, b2, map);
		if(compareResult.isChanged()){
			System.out.println(compareResult.getChangeContent());
		}
	}
	
	/**
	 * 对比所有属性 for boolean
	 * Compare all attributes,use method getCompareResult
	 */
	@Test
	public void test5() {
		Bean b1 = new Bean();
		b1.setId(1);
		b1.setName("aa");
		b1.setFull(true);//isFull
		b1.setStarted(true);//started


		Bean b2 = new Bean();
		b2.setId(1);
		b2.setName("aa2");
		b2.setFull(false);
		b2.setStarted(false);
		CompareResult compareResult = BeanComparator.getCompareResult(b1, b2);
		if(compareResult.isChanged()){
			System.out.println(compareResult.getChangeContent());
		}
	}
	
	/**
	 * 对比所有属性 跳过复杂字段不处理
	 * Compare all attributes,use method getCompareResult
	 */
	@Test
	public void test6() {
		Bean b1 = new Bean();
		b1.setId(1);
		b1.setName("aa");
		b1.setFull(true);//isFull
		b1.setStarted(true);//started
		List<Integer> ids1 = new ArrayList<>(Arrays.asList(1,2,3)); // 跳过复杂字段list不处理
		b1.setIds(ids1);// 跳过复杂属性，不对比复杂属性内容


		Bean b2 = new Bean();
		b2.setId(1);
		b2.setName("aa2");
		b2.setFull(false);
		b2.setStarted(false);
		List<Integer> ids2 = new ArrayList<>(Arrays.asList(4,5,6));
		b2.setIds(ids2);		
		CompareResult compareResult = BeanComparator.getCompareResult(b1, b2);
		if(compareResult.isChanged()){
			System.out.println(compareResult.getChangeContent());
		}
	}	

}
