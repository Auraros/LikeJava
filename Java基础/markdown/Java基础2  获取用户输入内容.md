# Java基础2  获取用户输入内容



本文介绍Java中获取用户输入方法。主要是使用Scanner这个类，需要导入相关的包，相关代码实例如下：

```java
package lessons;

import java.until.Scanner;

/*
 *如何获取用户输入
 * 分输入整数，输入小数，输入字符串
 */
public class MyClass{
	public static void main(String[] args){
		
        Scanner scan = new Scanner(System.in);
        System.out.println("请输入一些整数数字:");
        int user_input_int = scan.nextInt();
        System.out.println("刚刚输入的是:");
        System.out.println(user_input_int);
        
        Scanner scan1 = new Scanner(System.in);
        System.out.println("请输入一些小数:");
        double user_input_decimal = scan.nextDouble();
        System.out.println("刚刚输入的是:");
        System.out.println(user_input_decimal);
        
        Scanner scan1 = new Scanner(System.in);
        System.out.println("请输入一些字符串:");
        double user_input_string = scan.nextLine();
        System.out.println("刚刚输入的是:");
        System.out.println(user_input_string);
	}
}
```

解释：

```
Scanner scan = new Scanner(System.in);
```

这行代码的意思就是初始化一个Scanner实例，scan是类Scanner的一个具体实例对象。