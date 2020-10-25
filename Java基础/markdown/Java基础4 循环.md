# Java基础4 循环

> 循环一共包括三种循环 for循环，while循环，do while循环



## while循环

```java
package lessons;
 
public class MyClass {

	public static void main(String[] args) {
		int a=1;
		while(a < 11){
			System.out.println(a);
			a++;	
		}
	}
}
```

很简单满足条件，这个循环经常会在数据结构用到，比如ListNode head节点循环遍历：

```java
while(head != null){
	head = head.next;
}
```



## do-while循环

```java
package lessons;
 
public class MyClass {
	public static void main(String[] args) {
		int x = 1;
		int sum1 = 0;
		do{
			sum1 += x;
			x++;
		   }while(x <= 100);
		System.out.println(sum1);
	}
}
```

do-while比较少用到，但是一般可以作为先执行一次在判断条件。



## for循环

```java
package Lessons;
 
public class MyClass {
	public static void main(String[] args) {	
		int [] myarry = {100,67,234,89,178};
		for(int i=0; i< myarry.length; i++){
			System.out.println(myarry[i]);
		}
	}
}
```

```java
package Lessons;

public class MyClass {
	public static void main(String[] args) {
		int sum = 0;
		for (int i=0; i<=100; i++){
			sum += i;
		}
		System.out.println(sum);
	}
}
```

for循环一般用于遍历数组元素或者进行n次循环对于遍历数组元素还有更好的操作：

```java
int [] myarry = {100,67,234,89,178};
for(int my : marry){
	System.out.println(my);
}
```

```java
int [] myarry = "abcdefg";
for(char my : marry.toCharArray()){
	System.out.println(my);
}
```

