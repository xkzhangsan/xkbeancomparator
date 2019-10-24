#xkbeancomparator is a Java bean contrast modification and outputs the difference. xkbeancomparator是一个java bean对比修改并输出差异的工具。

##0.dependency 依赖：

    <dependency>  
      <groupId>com.github.xkzhangsan</groupId>    
      <artifactId>xkbeancomparator</artifactId>       
      <version>0.0.1</version>    
    </dependency>    


##1.Common use 常见用处：

（1）The modified objects are compared to generate a change log
           对修改过的对象进行对比生成修改日志；  
（2）Compare partial field modifications and output the log based on field comments
            对比部分字段修改，根据字段注释输出日志。  

##2.Main function classes and usage 主要功能类和用法：

Class 主要类名称：BeanComparator.java  
Method 主要方法为：  
public static String compareBean(Object source, Object target)
public static CompareResult getCompareResult(Object source, Object target)
