# JVM13 方法区



## 栈、堆、方法区的交互关系

**从线程共享与否的角度来看**

![image-20201209162138048](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201209162138048.png)

从栈、堆、方法区的交互关系

![image-20201210105527002](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210105527002.png)



## 方法区

![image-20201210105611673](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210105611673.png)

![image-20201210105624438](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210105624438.png)

![image-20201210105717254](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210105717254.png)

## Hotspot中方法区的演进

![image-20201210110117327](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210110117327.png)

![image-20201210110423434](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210110423434.png)

![image-20201210110618094](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210110618094.png)



## 设置内存大小

![image-20201210110749419](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210110749419.png)

![image-20201210110832762](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210110832762.png)

## 如何解决 OOM

![image-20201210110948473](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210110948473.png)



## 方法区的内部结构

![image-20201210111045818](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210111045818.png)

![image-20201210111121221](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210111121221.png)



### 类型信息

![image-20201210111153034](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210111153034.png)

### 域信息

![image-20201210111220994](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210111220994.png)



### 方法信息

![image-20201210111306800](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210111306800.png)



## 其他信息

![image-20201210111545983](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210111545983.png)

![image-20201210111627886](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210111627886.png)



## 运行时常量池 vs 常量池

### 常量池

#### 区别

- 方法区，内部包含了运行时常量池

- 字节码文件，内部包含了产量池

![image-20201210111833413](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210111833413.png)

![image-20201210112014799](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210112014799.png)

```
字节码文件经过类加载子系统，此时字节码文件的常量池就会变成运行时常量池
```

结构如下：

![image-20201210112218911](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210112218911.png)

```
magic 魔数
...
```

![image-20201210112337212](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210112337212.png)

一个有效的字节码文件中除了包含类的版本信息、字段、方法以及接口等描述信息外。还包含一项信息那就是常量池表(Constant Pool Table)，包括各种字面量和对类型、域和方法的符号引用。

**为什么需要常量池？**

一个Java源文件中的类、接口，编译后产生一个字节码文件。而Java中的字节码需要数据支持，通常这种数据会很大以至于不能直接存到字节码里，换一种方法，可以存到常量池，这个字节码包含了指向常量池的引用。在动态里按揭的时候会用到运行时常量池。

```
public class SimpleClass{
	public void sayHello(){
		System.out.println("hello");
	}
}
```

![image-20201210112846555](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210112846555.png)

函数test1的字节码

![image-20201210113215772](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210113215772.png)

#符号变量 对应着常量池的信息

![image-20201210113244832](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210113244832.png)



#### 常量池中有什么

```
几种在常量池内存存储的数据类型包括：
数值型
字符串型
类引用
字段以用
方法引用
```

```
public class MethodAreaTest2{
	public class void main(String[] args){
		Object obj = new Onbject();
	}
}
```

Object foo = new Object();会被编译如下字节码

![image-20201210113925091](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210113925091.png)



#### 小结

常量池，可以看作是一张表，虚拟机指令根据这张常量表找到要执行的类名、方法名、参数类型、字面量等类型



### 运行时常量池

![image-20201210114317253](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210114317253.png)



**举例使用**

![image-20201210114922619](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210114922619.png)

第一步：

![image-20201210115248053](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210115248053.png)

![image-20201210115414447](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210115414447.png)

![image-20201210115431849](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210115431849.png)

![image-20201210115522414](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210115522414.png)

![image-20201210115542619](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210115542619.png)

![image-20201210115558662](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210115558662.png)

![image-20201210115613904](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210115613904.png)

![image-20201210115740665](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210115740665.png)

![image-20201210115804972](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210115804972.png)

![image-20201210115822988](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210115822988.png)

![image-20201210115943628](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210115943628.png)

![image-20201210115958911](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210115958911.png)

![image-20201210120018491](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210120018491.png)

![image-20201210120030683](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210120030683.png)

![image-20201210120131134](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210120131134.png)



## 方法区演进细节

![image-20201210120337504](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210120337504.png)

![image-20201210121102030](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210121102030.png)

![image-20201210121141816](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210121141816.png)

![image-20201210121205069](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210121205069.png)



**永久代为什么要被元空间替换**

随着Java8的到来，`HotSpot VM`中再也见不到永久代了。这并不意味着类的元数据信息也消失了。这些数据被移到了一个与堆不相连的本地内存区域，这个区域叫做元空间(`Mteaspace`)

由于类的元数据分配在本地内存中，元空间的最大可分配空间就是系统可用内存空间。

**原因：**

1. 为永久代设置空间大小是很难确定的

   在某些场景下，如果动态加载类过多，容易产生Perm区的OOM。比如某个实际Web工程中，因为功能点较多，在运行过程中，要不断动态加载很多类，经常出现致命错误。

   ```
   java.lang.OutOfMemoryError:PermGen
   ```

   而元空间和永久代之间最大的区别在于:元空间并不在虚拟机中，而是使用本地内存，元空间大小仅受本地内存限制

2. 对永久代进行调优是很困难



**StringTable为什么要调整**

![image-20201210135250545](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210135250545.png)



## 静态变量存放在哪

![image-20201210140623230](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210140623230.png)

![image-20201210140648011](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210140648011.png)

![image-20201210141003628](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210141003628.png)



## 方法区垃圾回收

![image-20201210141433925](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210141433925.png)

![image-20201210141934211](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210141934211.png)

![image-20201210142038078](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210142038078.png)