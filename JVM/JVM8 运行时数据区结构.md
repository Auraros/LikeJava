# JVM8 运行时数据区结构

![image-20201202220029377](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201202220029377.png)

## 内存

内存是非常重要的系统资源，是硬盘和CPU的中间仓库及桥梁，承载着操作系统和应用程序的实时运行。JVM内存布局规定了Java在运行过程中内存申请、分配、管理的策略，保证了JVM的高效稳定运行。 **不同的JVM对于内存的划分方式和管理机制存在着部分差异。**

![image-20201202220848614](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201202220848614.png)



### 共享区与非共享区

Java虚拟机有一些会随着虚拟机地启动而创建，随着虚拟机推出而销毁，另一些是与线程一一对应地，这些与线程对应地数据区域会随着线程的开始和结束而创建和销毁。

![image-20201202221236034](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201202221236034.png)

灰色为单独线程私有的，红色的为多个线程共享的：

- 每个线程： 独立包括程序计算器、栈、本地栈
- 线程间共享：堆、堆外内存（永久代或元空间、代码缓存）