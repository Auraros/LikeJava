# JVM9 JVM线程与PC



## 线程

> 线程是一个程序里的运行单元，JVM允许一个应用有多个线程并行的执行。



### CPU时间片

CPU时间片即CPU分配给各个程序的时间，每个线程被分配一个时间段，称它为时间片。

- **宏观上：**打开多个应用程序同时运行
- **微观上：**由于只有一个CPU，一次只能处理程序要求的一步分，利用时间片轮流执行

![image-20201205113029520](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201205113029520.png)



### Hotspot JVM 中的线程

**一一对应**：每个线程都与操作系统的本地线程直接映射。当一个Java线程准备好执行以后，此时一个操作系统的本地线程也同时创建。Java线程执行结束之后，本地也会回收。

**操作系统的作用**：负责所有线程的安排调度到任何一个可用的CPU上，一旦本地线程初始化成功，就会调用Java线程中的run方法。

**注意：**

并行（排一行）：VS 串行（排一队）

并发（不一定同时执行，在一个时间内执行）



## PC寄存器

![image-20201205104401585](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201205104401585.png)

**JVM中的程序计数寄存器(Program Counter Register)**中，Register的命名源于CPU的寄存器，寄存器存储指令相关的现场信息。CPU只有把数据装载到寄存器才能运行。

注意：这里，不是广泛意义上的物理寄存器，或者将其翻译为PC计数器(或者指令计数器)会更好听一点，**JVM中的PC寄存器是对物理PC寄存器的一种抽象模拟。**



### 作用

![image-20201205104933706](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201205104933706.png)

PC寄存器是用来存储指令向下一条指令的地址，也即将要执行的指令代码，由执行引擎读取下一条指令。



### 特点

- **小：**它是一块很小的内存空间，几乎可以忽略不记，也是运行速度最快的存储区域。
- **线程私有：**每个线程都有着它自己的计数器。成名周期与线程的生命周期一致



### 作用

1. 它是程序控制流的指示器，分支、循环、跳转、异常处理、线程恢复等基础功能需要依赖这个指数器完成
2. 字节码解释器工作时就是通过改变这个计数器的值来选取下一条需要执行的字节码指令
3. 唯一没有该规定OutOtMermoryError情况的区域
4. 没有垃圾回收



### 示例

代码：

```java
/**
 * @Author : Auraros
 * @Description :
 * @Data : 2020-12-05 11:04
 * @Version : 1.0
 */
public class PCRegister {

    public static void main(String[] args) {

        int i = 10;
        int j = 20;
        int k = i + j;

        String s = "abc";
        System.out.println(i);
        System.out.println(k);

    }
}


```

![image-20201205111059541](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201205111059541.png)

解释：

```
0: bipush        10  //取出10
2: istore_1          //放到1的位置
3: bipush        20  //取出20
5: istore_2			 //放到2的位置
6: iload_1			 //加载1的值，就是10
7: iload_2			 //加载2的值，就是20
8: iadd    			 //相加
9: istore_3			 //放到3的位置
10: ldc           #2 //#2就是常量池中的常量信息                 // String abc
12: astore        4  //存放到4
14: getstatic     #3   //#3输出方法               // Field java/lang/System.out:Ljava/io/PrintStream;
17: iload_1
18: invokevirtual #4                  // Method java/io/PrintStream.println:(I)V
21: getstatic     #3                  // Field java/lang/System.out:Ljava/io/PrintStream;
24: iload_3
25: invokevirtual #4                  // Method java/io/PrintStream.println:(I)V
28: return

//指令地址、偏移地址 0，1，2，3
//操作指令：bipush等
```

![image-20201205111948403](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201205111948403.png)



### 两个常见问题

#### 问题一

- 使用PC寄存器存储字节码指令地址有什么用呢？(为什么使用PC寄存器记录当前线程的执行地址呢)

  ```
  因为CPU需要不停的切换各个线程，这时候切换回来以后，就得知道接着从哪开始继续执行
  JVM字节码解释器就需要通过改变PC寄存器得值来明确下一条指令应该执行什么样的字节码指令
  ```

- PC寄存器为什么被设定为线程私有

  ```
  为了能够准确地记录各个线程正在执行地当前字节码指令地址，最好地办法自然是为了每一个线程都分配一个PC寄存器。
  ```

  