# Java基础1  变量和类型

```
变量就是一个用来存储值的容器。变量作用在内存中，变量是可变的，计算机断电，变量就丢失，不工作。
```

$$
变量 变量名称 = 变量值
$$

### 变量命名规则

- 标识符由字母、数字、下划线“_”、美元符号“$”或者人民币符号“￥”组成，并且首字母不能是数字。
- 不能把关键字和保留字作为标识符。
- 标识符没有长度限制。
- 标识符对大小写敏感。

### 练习

```java
package lessons;
 
/*
 * 这里介绍Java中变量和变量类型
   byte (数字， 1 字节) 
   short (数字， 2字节)  
   int (数字， 4字节) 
   long (数字， 8字节)
   float (单精度数字， 4字节) 
   double （双精度数字， 8字节）
   char (一个字母，2字节) 
   boolean (true or false, 1字节) 
 */
public class MyClass {
 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int my_byte = 10;
		short my_short_number = 20;
		int x = 3000;
		long y = 579083245;
		float my_fload_number = (float)4.5;
		double my_PI = 3.1415926;
		char my_char = 'A';
		boolean is_true = false;
		
		System.out.println(my_byte);
		System.out.println(my_short_number);
		System.out.println(x);
		System.out.println(y);
		System.out.println(my_fload_number);
		System.out.println(my_PI);
		System.out.println(my_char);
		System.out.println(is_true);
 
	}
 
}
```

注:不同变量类型默认的字节存储。Java中小数默认是double类型，例如上面（float）4.5，如果你直接写4.5，会在4.5这个地方出红色波浪线，表示这个地方有错误。