# JVM6 双亲委派机制

> Java虚拟机对class文件采用的是按需加载的方式，也就是说当需要使用该类的时候才会将它的class文件加载到内存生成class类型。
>
> 在加载某个类的时候，Java虚拟机采用的是**双亲委派模式**，即把请求交由父类处理，它是一种任务委派模式

![image-20201202212657861](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201202212657861.png)

工作原理：

- 如果一个类加载器收到了类加载的请求，它并不会自己先去加载，而是把这个请求委托给父类的加载器去执行
- 如果父类加载器还存在父类加载器，则进一步向上委托，直到最顶层加载器
- 如果父类加载器可以完成类加载器任务，就成功返回，倘若父类加载器无法完成子类的任务，子类才会尝试自己去加载，这就是双亲委派机制

```java
/**
 * @Author : Auraros
 * @Description :
 * @Data : 2020-12-02 21:22
 * @Version : 1.0
 */
public class StringTest {

    public static void main(String[] args) {

        String str = new String(); //并不会输出
        System.out.println("hello");
        
        StringTest test = new StringTest();
        System.out.prntln(test.getClass().getClassLoader); //AppClassLoader系统类加载器

    }
}
```

```java
在src包下创建java.lang.String.class
public class String{
	static{
		System.out.println("我是自定义的");
    }
}
```

运行结果不会输出，因为引导类加载器可以加载string类。



### 优点

- 避免类的重复加载
- 保护程序安全，防止核心API被随意篡改
  - 自定义类： java.lang.String



### 沙箱安全机制

```
自定义String类，但是在加载自定义String类的时候会率先使用引导类加载器加载，而引导类加载器在加载的过程中会先加载jdk自带的文件(rt.jar包中java\lang\String.class)，报错信息说没有main方法，因为rt.jar包中的String类。这样可以保证对java核心源代码的保护，这就是沙箱安全机制。
```

