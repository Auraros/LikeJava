# JVM11 本地方法栈



## 本地方法接口

### 背景

Java使用起来非常方便，然而有些层次的任务用Java实现起来不容易，或者我们对程序的效率很在意。

**与Java环境外交互**

有时Java应用需要与Java环境交互，这是本地方法存在的重要原因

![image-20201207224142779](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201207224142779.png)

红框就是我们的本地方法接口

### 定义

一个native method就是一个Java调用非Java代码的接口

一个Native Method是这样一个Java方法：

> 该方法实现由非Java语言实现，比如C，这个特征并非Java所特有，很多其他的编程语言都有这一机制，比如在C++中，你可以用extern "C" 告知C++编译器去调用一个C的函数。

![image-20201207224659181](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201207224659181.png)



## 本地方法栈

### 定义

Java虚拟机栈用于管理Java方法的调用，而本地方法栈用于管理本地方法的调用。线程私有

![image-20201207225729600](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201207225729600.png)

- 本地方法是C语言实现的

### 实现原理

是Native Method Stack中登记native方法，在Execution Wngine在实现。

![image-20201207230113907](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201207230113907.png)



**当某个线程调用一个本地方法时，它就进入了一个全新的并且不再受虚拟机限制的世界，它可以和虚拟机拥有同样的权限**

- 本地方法可以通过本地方法接口来访问虚拟机内部的运行时数据区
- 甚至可以直接使用本地处理器中的寄存器
- 直接从本地内存的堆中分配任意数量的内存

