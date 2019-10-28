# xkbeancomparator是一个java bean对比修改并输出差异的工具。  

xkbeancomparator is a Java bean contrast modification and outputs the difference.   

## 0.依赖 dependency：  

    <dependency>  
      <groupId>com.github.xkzhangsan</groupId>    
      <artifactId>xkbeancomparator</artifactId>       
      <version>0.0.1</version>    
    </dependency>    


## 1.常见用处 Common use：  

（1）对修改过的对象进行对比生成修改日志The modified objects are compared to generate a change log
           ；  
（2）对比部分字段修改，根据字段注释输出日志Compare partial field modifications and output the log based on field comments
            。  

## 2.主要功能类和用法 Main function classes and usage：  

主要类名称 Class：BeanComparator.java  
主要方法为 Method ：  
public static String compareBean(Object source, Object target)  
public static CompareResult getCompareResult(Object source, Object target)

## 3 实例 xkbeancomparator-samples （ https://github.com/xkzhangsan/xkbeancomparator-samples ）  

（1）添加pom依赖  

    <dependency>  
      <groupId>com.github.xkzhangsan</groupId>    
      <artifactId>xkbeancomparator</artifactId>       
      <version>0.0.1</version>    
    </dependency>    
    
（2）增加辅助日志类  

UserLog


import java.util.HashMap;  
import java.util.Map;  

import com.xkzhangsan.xkbeancomparator.BeanComparator;  
import com.xkzhangsan.xkbeancomparator.CompareResult;  

public class UserLog{

	private static final Map<String, String> propertyTranslationMap = new HashMap<>();

	static {
		propertyTranslationMap.put("name", "用户名");
		propertyTranslationMap.put("point", "积分");
	}
	
	public static CompareResult getCompareResult(Object source, Object target){
		return BeanComparator.getCompareResult(source, target, propertyTranslationMap);
	}
}

（3）   使用  

	@Test
	public void test1() {
		User u1 = new User();
		u1.setId(1);
		u1.setName("aa");
		u1.setPoint(new BigDecimal("111111111111.12"));

		User u2 = new User();
		u2.setId(1);
		u2.setName("aa2");
		u2.setPoint(new BigDecimal("111111111111.15"));
		CompareResult compareResult = UserLog.getCompareResult(u1, u2);
		if (compareResult.isChanged()) {
			System.out.println(compareResult.getChangeContent());
		}
	}

（4）说明 instructions  

上面是推荐用法，使用辅助日志类能统一维护一个java bean的注释map，简化调用。
The recommended usage, above, is to use secondary logging classes to uniformly maintain an annotated map of a Java bean, simplifying invocation.

欢迎提建议 Suggestions are welcome！

