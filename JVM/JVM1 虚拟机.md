# JVM1 虚拟机

## 概述

### 虚拟机和Java虚拟机

> 所谓虚拟机(Virtual Machine)，就是一台虚拟的计算机，它是一款软件，用来执行一系列虚拟计算机指令。大体上，虚拟机可以分为系统虚拟机和程序虚拟机
>
> - Visual Box， VMware 就属于系统虚拟机，它们完全是对物理计算机的仿真，提供了一个可运行完整操作系统的软件平台
> - 程序虚拟机的典型代表就是Java虚拟机，它专门为执行单个计算机程序而设计

#### Java虚拟机

- Java虚拟机是一台执行Java字节码的虚拟计算机，它拥有独立的运行机制，其运行的Java字节码也未必由Java语言编译而成
- JVM平台的各个语言可以共享Java虚拟机带来的跨平台性、优秀的垃圾回收器，以及可靠的即时编译器
- **Java技术的核心就是Java虚拟机(JVM， Java Virtual Machine)**

### 作用

```
Java虚拟机就是二进制字节码的运行环境，负责装载字节码到内部，解释编译为对应平台上的机器指令执行。每条Java指令，Java虚拟机规范中都有详细的定义，如怎么取操作数，怎么处理操作数，处理结果放在哪里
```

### 特点

- 一次编译，到处运行
- 自动内存管理
- 自动垃圾回收功能



## JVM的位置

### 操作系统上的位置

![image-20201201192311821](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201201192311821.png)

```
JVM是运行在操作系统之上的，它与硬件没有直接的交互。
```

### Java配置上的位置

![img](https://pic4.zhimg.com/80/0cc3f4a15d3184391a98a7b1c58f6e5f_720w.jpg?source=1940ef5c)

- JRE： Java Runtime Environment

  ```
  JRE顾名思义是java运行时环境，包含了java虚拟机，java基础类库。是使用java语言编写的程序运行所需要的软件环境，是提供给想运行java程序的用户使用的。
  JRE 是最小的运行环境
  ```

- JDK： Java Development Kit

  ```
  JDK顾名思义是java开发工具包，是程序员使用java语言编写java程序所需的开发工具包，是提供给程序员使用的。JDK包含了JRE，同时还包含了编译java源码的编译器javac，还包含了很多java程序调试和分析的工具：jconsole，jvisualvm等工具软件，还包含了java程序编写所需的文档和demo例子程序。
  ```

- JAVA的三个版本

  ```
  1.JSE 指标准版一般用于用户学习JAVA语言的基础也是使用其他两个版本的基础主要用于编写C/S项目和提供标准的JAVA类库，是所有基于Java语言开发的基础，该版本主要用于开发桌面应用程序。
  
  2. JEE 指企业版依托互连网技术提供企业级平台应用说白了就是用来构建大型网站和B/S系统 ，作为一个企业版本，主要是给出一个开发企业级应用架构的解决方案，同时给出了在这个架构中相关组件以供开发人员使用，例如我们连接数据库所用的JDBC。
  
  3.JME 指移动版为小型移动器械搭建使用平台主要是用来为手机编程，制作手机相关软件的 三个版本一个是做C/S项目如QQ 一个是做网站如163 一个是做手机系统如大部分手机的小游戏 ，是针对移动设备，嵌入式系统的开发。
  ```

  

