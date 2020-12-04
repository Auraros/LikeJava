# JVM3 生命周期

### 虚拟机的启动

Java虚拟机的启动是通过**引导类加载器**(bootstrap class loader)创建一个初始类(initial class)来完成的，这个类是由虚拟机的具体实现指定的。

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

StackStruTest 是被系统类加载的，父类Object是被引导加载器加载的。

### 虚拟机的执行

- 一个运行中的Java虚拟机有着一个清晰的任务：执行Java程序
- 程序开始执行时他才运行，程序结束时他就停止
- **执行一个所谓的Java程序的时候，真真正正执行的是一个叫做Java虚拟机的进程**

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
        
       	try{
            Thread.sleep(6000);
        }catch(InterrupteredException e){
            e.printStackTrace();
        }
        
    }
}
```

**允许结束前**

<img src="C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201201215756077.png" alt="image-20201201215756077" style="zoom:70%;" />

允许结束后

<img src="C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201201220019046.png" alt="image-20201201220019046" style="zoom:85%;" />

### 虚拟机的推出

有如下的几种情况:

- 程序正常执行结束
- 程序在执行过程中遇到了异常或错误而异常终止
- 由于操作系统出现错误而导致Java虚拟机进程终止
- 某线程调用Runtime类或System类的exit方法，或Runtime类的halt方法，并且Java安全管理器也允许这次exit或halt操作

