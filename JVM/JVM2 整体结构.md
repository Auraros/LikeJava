# JVM2 整体结构

```
HotSpot VM是目前市面上高性能虚拟机的代表作之一
```

![img](https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1388950624,4116108628&fm=26&gp=0.jpg)

```
灰色：每个线程独有一份
橙色：每个线程共享资源
```



### 架构模型

Java编译器输入的指令流基本上是一种**基于栈的指令集架构**(hotspot)，另一个指令集架构则是**基于寄存器的指令集架构**

两种架构的区别：

**基于栈的指令集架构**

- 设计和实现更简单，适用于资源受限的系统
- 避开了寄存器的分配难题：使用零地址指令方式分配
- 指令流的指令大部分是零地址指令，其执行过程依赖于操作栈。指令集更小，编译更容易实现
- 不需要硬件支持，可移植性更好，更好实现跨平台

**基于寄存器架构的特点**

- 典型的应用是 x86的二进制指令集：比如传统的PC以及Android的Davlik虚拟机
- 指令集架构则完全依赖硬件，可移植性差
- 性能优秀和执行更高效
- 花费更少的指令去完成一项操作
- 在大部分时间，基于寄存器架构的指令集往往都以一地址指令、二地址指令和三地址指令为主，而基于栈式架构的指令集却是以零地址指令为主。



### 举例

同样执行2+3这种逻辑操作，指令分别如下：

**基于栈的计算流程**

```
iconst_2 //常量2入栈
istore_1 //存到索引为1的位置
iconst_3 //常量3入栈
istore_2 //存到索引数位2的位置
iload_1  //拿出来
iload_2  //拿出来
iadd  //常量2 3 出栈，相加
istore_0 //结果5入栈
```

**基于寄存器的计算流程：**

```
mov eax,2
add eax,3
```



### 测试

**idea添加外部工具**

- 打开 External tools

![image-20201201204134930](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201201204134930.png)

- 添加

![image-20201201204244372](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201201204244372.png)

**编写代码**

```java
package com.auraros.java;

/**
 * @Author : Auraros
 * @Description :
 * @Data : 2020-12-01 20:23
 * @Version : 1.0
 */
public class StackStruTest {
    public static void main(String[] args) {

        int i = 2 + 3;

    }
}
```

编译运行

**找到字节码.class文件**

![image-20201201204336624](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201201204336624.png)

- 右键使用外部工具运行

![image-20201201204358739](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201201204358739.png)

**更改代码**

```java
package com.auraros.java;

/**
 * @Author : Auraros
 * @Description :
 * @Data : 2020-12-01 20:23
 * @Version : 1.0
 */
public class StackStruTest {
    public static void main(String[] args) {

        int i = 2;
        int j = 3;
        int z = i + j;

    }
}

```

- 查看结果

![image-20201201204558940](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201201204558940.png)



### 总结：

由于跨平台性的设计，Java的指令都是根据栈来设计的，不同平台的CPU架构不同，所以不能设计为基于寄存器的。



**为什么不将架构改成寄存器架构？**

```
设计和实现上简单，在非资源受限也可用。
```



### 栈特点

- 跨平台性
- 指令集小、指令多
- 执行性能比寄存器差一点