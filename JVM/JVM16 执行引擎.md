# JVM16 执行引擎

![image-20201211184234259](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201211184234259.png)

**虚拟机**是一个相对于"物理机"的概念，这两种机器都有代码执行能力，其区别是物理机的执行引擎是直接建立在处理器、缓存、指令集和操作系统层面上的，而虚拟机的执行引擎是由软件自行实现的，能够执行那些不被硬件直接支持的指令集格式。

## 背景

> JVM的主要任务是负责装载字节码到其内部，但字节码并不能够直接运行在操作系统之上，因为字节码指令并非等价于本地机器指令，它内部包含的仅仅只是一些能被JVM所识别的字节码指令、符号表，以及其他辅助信息。
>
> 想让一个Java程序运行起来，**执行引擎(Execution Engine)的任务就是将字节码指令解释/编译为对应平台上的本地机制指令才可以**。简单来说，JVM中的执行引擎充当了高级语言翻译为机器语言的机器。



## 工作过程

![image-20201211185120359](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201211185120359.png)



**javac 前端编译器的执行步骤**

![image-20201211185736348](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201211185736348.png)



![image-20201211185429809](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201211185429809.png)



## 解释器编译器

**什么是解释器(Interpreter)，什么是JIT编译器**

解释器:当Java虚拟机启动时会根据预定义的规范对字节码采用逐行解释的方式执行，将每条字节码文件中的内容"翻译"为对平台的本地机器指令执行。

JIT(Just In Time Compiler)编译器:就是虚拟机将源代码直接编译成和本地机器平台相关的机器语言。



**为什么说Java是半编译半解释型语言**

现在JVM在执行Java代码的时候，通常会将解释执行与编译执行二者结合起来进行



## 机器码，指令，汇编，高级语言

### 机器码

![image-20201211193644701](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201211193644701.png)

### 指令

![image-20201211214412687](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201211214412687.png)

### 指令集

![image-20201211214503682](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201211214503682.png)

### 汇编语言

![image-20201211215348457](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201211215348457.png)



### 高级语言

![image-20201211215408459](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201211215408459.png)



![image-20201211215445630](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201211215445630.png)



### 字节码

![image-20201211215759880](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201211215759880.png)

![image-20201211215822429](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201211215822429.png)



## 解释器

![image-20201211220225234](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201211220225234.png)

![image-20201211220239009](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201211220239009.png)

#### 工作机制

![](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201211224438659.png)



![image-20201211231647878](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201211231647878.png)



## JIT编译器

![image-20201211231751160](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201211231751160.png)

![image-20201211231832376](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201211231832376.png)



![image-20201211232359422](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201211232359422.png)



Hotspot的执行方式

![image-20201211232548975](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201211232548975.png)

![image-20201211233429444](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201211233429444.png)



## 热点代码及探测方式

![image-20201211234044044](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201211234044044.png)

![image-20201211234446963](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201211234446963.png)

#### 方法调用计数器

![image-20201211234803430](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201211234803430.png)

![image-20201211234951130](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201211234951130.png)

**热点衰度**

![image-20201211235802312](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201211235802312.png)



## HotSpot设置执行方法

![image-20201212003623061](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201212003623061.png)

![image-20201212004012357](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201212004012357.png)



### JIT分类

![image-20201212004557526](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201212004557526.png)

![image-20201212005109984](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201212005109984.png)



## 优点

![image-20201212005205669](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201212005205669.png)



![image-20201212005452326](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201212005452326.png)

![image-20201212005718104](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201212005718104.png)