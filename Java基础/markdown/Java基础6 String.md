# Java基础6 String

> 本文来介绍Java中的String，什么是String呢，字符串就是一序列字符组成的。Java中用关键字String表示字符串对象，严格来说，String时候对象，而不是变量类型。在自动化测试过程中，经常需要用到String对象，特别是断言的部分，需要进行字符串匹配判断。下面的例子，介绍了几个String基本的属性和方法。

## 常用方法

在力扣里面，感觉会将常用到下面几个方法

```java
package lesson;

public class MyClass{
	public static void main(String[] args){
		String str = "lwlsDSg"
		//把字符串转化为char[] 的数组
		char[] str1 = str.toCharArray();
		//['l','w','l','s','D','S','g']

		//提取字符串中第i个数字
		char str2 = str.charAt(4);
		//s

		//提取字符串中第i, j(不包括j)
		String str3 = str.substring(1,3);
		//"wl"
        
        //其他方法
        //计算字符长度
		System.out.println(str.length()); //7
		//转换小写
		System.out.println(str.toLowerCase()); //lwlsdsg
		//转换大写
		System.out.println(myString.toUpperCase());//LWLSDSG
		//字符串用加号连接
		String st1 = "Hello";
		String st2 = "world";
		System.out.println(st1 + st2); 
		//替换字母
		System.out.println(myString.replace('a', 'Y'));
		//查找某一个字母的索引
		System.out.println(myString.indexOf('w'));
    }
}
```

