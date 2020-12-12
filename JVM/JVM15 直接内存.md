# JVM15 直接内存



## 概述

不是虚拟机运行时数据区的一部分，也不是《Java虚拟机规范》中定义的内存区域。直接内存是在Java堆外的、直接向系统申请的内存空间，来源于NIO，通过存在堆中的DirectByteBuffer操作Native内存。通常，访问直接内存的速度会优于Java堆，即读写性能高。



**IO  /  NIO**

IO 基于  byte[] /  char[]   Stream

NIO (New IO/ Non-Blocking IO)  基于 Buffer、 Chanel



## 对比

![image-20201211112106258](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201211112106258.png)

![image-20201211112141893](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201211112141893.png)



## OOM

![image-20201211112850275](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201211112850275.png)

![image-20201211113701069](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201211113701069.png)