# JVM12 堆

## 核心概述

![image-20201208110008650](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201208110008650.png)

### 定义

《Java虚拟机规范》中对Java堆的描述是:所有的对象实例以及数组都应当在运行时分配在堆上。(几乎，有些对象出现在栈上分配)，一个JVM实例只存在一个堆内存，堆也是Java内存管理的核心区域。

#### 运行路径

- **创建**：在JVM启动的时候被创建，其空间大小也就确定了，是JVM管理的最大一块的内存空间, 可以调节。
- **结束**：在方法结束后，堆的对象不会马上被移除，仅仅在垃圾收集的时候才会被移除

- 《Java虚拟机规范》规定，堆可以处于物理上不连续的内存空间中，但在逻辑上它应该被视为连续的。
- 所有线程共享Java堆，这里还可以划分线程私有的缓冲区(Thread Local Allocation Buffer, TLAB)

![image-20201208112513691](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201208112513691.png)

### 内存

#### 内存结构

基于分代收集理论：

![image-20201208113334931](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201208113334931.png)

约定：

- 新生区 <-> 新生代 <-> 年轻代
- 养老区 <-> 老年代 <-> 老年代
- 永久区 <-> 永久代

S0 和 S1 始终只有一个空间能用

<img src="C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201208113907926.png" alt="image-20201208113907926" style="zoom:67%;" />

#### 堆空间大小设置

`Java`堆区用于存储`Java`对象实例，那么堆的大小在JVM启动时就已经设定好了，可以通过选项"`-Xmx`"和 "`-Xms`"来进行设置：

- “`-Xms`” 用于表示堆区的起始内存，等价于 `-XX:InitalHeapSize`
- "`-Xmx`" 用于表示堆区的最大内存，等价于 `-XX:MaxHeapSize`

一旦堆区内存大小超过了"`-Xmx`"所指定的最大内存时，将会抛出`OutOfMemoryError`异常。

**注意：**通常会将 `-Xms` 和 `-Xmx`两个参数配置相同的值，其目的是为了能够**在Java垃圾回收机制请理完堆区后不需要重新分隔计算堆区的大小，从而提高性能。**

默认情况下，初始内存大小: 物理电脑内存的大小/64

​						最大内存大小：物理电脑内存大小/ 4

**代码解析**

```java
package com.auraros.java;

/**
 * @Author : Auraros
 * @Description :
 * 1. 设置堆空间大小的参数
 * -Xms 用来设置堆空间 (年轻代 + 老年代）的初始内存大小
 *      -X 是Jvm的运行参数
 *      ms 是 memory start
 * -Xmx 永安里设置堆空间 （年轻代+老年代）的最大内存大小
 *
 *
 * 2. 默认堆空间大小
 *      初始内存大小: 物理电脑内存的大小/64
 *      最大内存大小：物理电脑内存大小/ 4
 *
 *3. 查看参数：
 *      方式1： jps / jstat -gc 进程id
 *		方式2： -XX： +PrintGCDetails	
 * @Data : 2020-12-08 12:29
 * @Version : 1.0
 */
public class HeapSpaceInitial {

    public static void main(String[] args) {

        //返回java虚拟机中的堆内存总流
        long initialMemory = Runtime.getRuntime().totalMemory() / 1024 /1024;
        //返回Java虚拟机试图使用的最大堆内存
        long maxMemory = Runtime.getRuntime().maxMemory() /1024 /1024;

        System.out.println("-Xms:" + initialMemory + "M");

        System.out.println("-Xmx:" + maxMemory + " M");

        System.out.println("系统内存大小为:" + initialMemory * 64.0 /1024 + "G");

        System.out.println("系统内存大小为:" + maxMemory* 4.0 /1024 + "G");

    }


}

```

![image-20201208123700004](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201208123700004.png)

更改内存大小：

![image-20201208124240889](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201208124240889.png)

结果如下：

![image-20201208124252584](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201208124252584.png)



#### OutOfMemeory举例

```java
package com.auraros.java;

import java.util.ArrayList;

/**
 * @Author : Auraros
 * @Description :
 * @Data : 2020-12-08 12:54
 * @Version : 1.0
 */
public class OOM {

    public static void main(String[] args) {

        ArrayList<Object> arrayList = new ArrayList<>();

        while(true){
            arrayList.add(1);
        }

    }

}

```

结果如下：
![image-20201208125714276](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201208125714276.png)



## 年轻代与老年代

### 定义

存储在JVM中的Java对象可以被划分为两类：

- 一类是生命周期较短的瞬时对象，这类对象的创建和消亡都非常迅速
- 另一类对象的生命周期非常长，在某些极端的情况下能与JVM的生命周期保持一致

`Java`堆区进一步细分的话，可以划分为年轻代(`YoungGen`) 和老年代(`OldGen`) ,其中年轻代又可以划分为`Eden`空间、`Survivor0`空间和`Survivor1`空间(有时也叫做`from`区、`to`区)

![image-20201208152838966](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201208152838966.png)

### 空间占比

#### 新生代与老年代的空间占比

下面的参数一般不会调：

![image-20201208154617614](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201208154617614.png)

配置新生代与老年代在堆结构的占比：

- 默认 -XX：NewRatio = 2 , 表示新生代占1，老年代占2
- 可修改 -XX: NewRatio = 4 , 表示新生代占1， 老年代占4

#### 新生代中的空间占比

在`HotSpot`中，`Eden`空间和另外两个`Survivor`空间缺省占比的比例是`8:1:1`. 但是真实情况下的比例并不是 `8:1:1`，因为JVM有自适应内存分配策略。要想删除，则添加参数` -XX: -UseAdaptiveSizePolicy`

当然，开发人员可以通过选项"`-XX:SurvivorRatio`"调整这个空间比例。

- 几乎所有的`Java`对象都是在`Eden`区被`new`出来的
- 绝大部分的`Java`对象的销毁都在新生代进行了

## 对象分配过程

> 为新对象分配内存是一件非常严谨和复杂的任务，JVM的设计者们不仅需要考虑内存如何分配、在哪里分配等问题，并且由于内存分配算法与内存回收算法密切相关，所以还需要考虑GC执行完内存回收后是否会在内存空间中产生内存碎片。

过程如下：

1. new 的对象先放 eden区，此区有大小限制
2. 当eden元的空间填满时，程序又需要创建对象，JVM的垃圾回收器堆eden区进行垃圾回收(Minor GC)，将eden区中的不再被其他对象所引用的对象进行销毁。再加载新的对象放到eden区
3. 然后将eden区中的剩余对象移动到幸存者0区
4. 如果再次触发垃圾回收，此时上次幸存下来的放到幸存者0区，如果没有就回收，就会放到幸存者1区
5. 如果再次经历垃圾回收，此时会重新放回幸存者0区，接着再去幸存者1区
6. 什么时候去养老区呢？可以设置次数，默认是15次
   - 可设置参数：-XX：MaxTenuringThreshold=< N >进行设置

#### 图解

![image-20201208161022503](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201208161022503.png)

![image-20201208161244911](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201208161244911.png)

![image-20201208161407016](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201208161407016.png)

注意： S1和S0区满得时候并不会触发GC，有可能会出现直接晋升老年区

**总结：**

- 针对幸存者 s0,s1区的总结：赋值之后有交换，谁空谁是to
- 关于垃圾回收：频繁在新生区收集，很少在养老区收集，几乎不在永久区/原空间收集

#### 特殊情况

![image-20201208162041082](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201208162041082.png)

#### 代码举例

![image-20201208162750280](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201208162750280.png)

使用Java VisualVM可以看到

<img src="C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201208162929708.png" alt="image-20201208162929708" style="zoom:50%;" />

eden园区在不停的增长，增长满了就执行GC操作，然后S0和S1会替换，old会增加。

### 常用调优工具

- JDK命令行
- Jconsole
- VisualVM
- Jprofiler
- Java Flight Recorder
- GCViewer
- GC Easy



## Minor GC、Major GC与Full GC

### 定义

JVM在进行GC时，并非每次都对上面三个内存区域（新生代、老年代；方法去）一起回收的，大部分时候回收的都是指新生代。

针对HotSpot VM的实现，它里面的GC按照会收区域又分为两大种类型：一种是**部分收集**(Partial GC),一种是**整堆收集**(Full GC)

- 部分收集：不是完整收集整个Java堆的垃圾收集，又分为：
  - **新生代收集**(Minor GC / Young GC):只是新生代(eden、S0、S1）垃圾收集
  - **老年代收集**(Major GC / Old GC):只是老年代的垃圾收集
    - 目前只有 CMS GC会有单独收集老年代的行为
    - 注意：很多时候Major GC会和Full GC混淆使用，需要具体分辨是老年代回收还是整堆回收
  - **混合收集**(Mixed GC): 收集整个新生代以及部分来年代的垃圾收集
    - 目前，只有G1 GC 会有这在行为
- 整堆收集(Full GC): 收集整个 java堆和方法区的垃圾收集

### 触发条件

#### Minor GC触发机制

1. 当**年轻代空间不足**时，就会触发Minor GC，这里的年轻代满指的是Eden代满，Survivor满不会引发GC(每次 Minor GC 会请理年轻代的内存)
2. 因为Java对象大多是具备朝生夕灭的特性，所以Minor GC非常频繁，一般回收速度也比较快
3. Minor GC会引发STW，暂停其他用户的线程，等垃圾回收结束，用户线程才恢复运行。

```
在发生Minor GC之前，虚拟机会检查老年代最大可用的连续空间是否大于新生代所有对象的总空间
	如果大于，则此次Minor GC是安全的
	如果小于，则虚拟机会查看 -XX:HandlePromotionFailture设置值是否允许担保失败。
		如果HandlePromotionFailure=true,那么会继续检查老年代最大可用连续空间是否大于历次晋升到老年代的对象的平均大小
			如果大于，尝试进行一次GC，但这次Minor GC依然是有风险的
			如果小于，则改为进行一次Full GC
		如果HadnlePromotionFailure=false，则改为进行一次Full GC
```

![image-20201208171321664](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201208171321664.png)

#### Major GC / Full GC 触发机制

老年代GC(Major GC/Full GC)触发机制：

- 指发生在老年代GC，对象从老年代消失时，我们说"Major GC"或"Full GC"发生了。
- 出现了Major GC，京忏悔伴随至少一次的Minor GC(不是一定的)
  - 老年代空间不足的时候，先尝试触发Minor GC,如果之后还空间不足，则触发Major GC
- Major GC的速度一般会比Minor GC慢10倍以上，STW时间更长
- 如过Major GC后，内存还不足，就OOM

#### Full GC触发机制

触发 Full GC 执行的情况有如下五种：

- 调用System.gc()时，系统建议执行Full GC，但不是必然执行

- 老年代空间不足

- 方法去空间不足

- 通过Minor GC后进入老年代的平均大小大于老年代的可用内存

- 由Eden区、survivor space0(From Space)区向 Survivor space1（To Space）区复制时，对象大小大于To Space可用内存，则把对象转存到老年代，且老年代的可用内存小于该对象大小。

  说明: full gc 是开发中尽量避免的。

### 代码解析

<img src="C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201208172503321.png" alt="image-20201208172503321" style="zoom:60%;" />

加入参数：

```
-Xms9m -Xmx9m -XX:+PrintGCDetail
```

![image-20201208172743077](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201208172743077.png)



## 堆空间分代思想

### 背景

**为什么需要把Java堆分代？不分代就不能正常工作了吗？**

经研久，不同对象的生命周期不同，70% - 99%的对象是临时对象。

- 新生代：有Eden，两块大小相同的Survior(又称为 from/ to，S0/S1)构成，to总是空。
- 老年代：存放新生代中经历多次GC仍然存活的对象

![image-20201208185147733](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201208185147733.png)

**为什么需要把Java堆分代？不分代就不能正常工作了吗？**

其实不分代完全可以，分代的唯一理由就是优化GC性能，如果没有分代，那所有的对象都在一块，就如图把一个学习的人都关在一个教室，GC的时候要找到哪些对象没有，这样就会对堆内所有区域进行扫描，而很多对象是朝生夕死的，如果分代的话，把新建的对象放到一个位置，当GC的时候先把这块存储"朝生夕死"对象的区域进行回收，就会腾出很大的空间出来。



## 内存分配策略

又称为 对象提升(Promotion) 规则

如果对象在Eden出生并经过一次MinorGc后仍然存活，并且能被Survivor容纳的话，将被移动到Survivor空间中，并将对象年龄设为1。对象在Survivor区中每熬过一次MinorGC，年龄就增加1岁，当年龄增加到一定的程度(默认为15岁)就会被晋升到老年代中。

参数设置:

```
-XX:MaxTenuringThreashold
```



## 对象分配内存TLAB

### 背景

**为什么有TLAB（Thread Local Allocation Buffer）**

堆区是线程共享区域，任何线程都可以访问到堆区中的共享数据

由于对象实例的创建在JVM中非常频繁，因此在并发环境下从堆中划分内存是线程不安全的。**为了避免多个线程操作一个地址，需要使用加锁等机制，进而影响分配速度。**

### 定义

从内存模型而不是垃圾收集的角度，对Eden区域继续进行划分，JVM为每个线程分配了一个私有的缓存区域，包含在Eden空间内。

多线程同时分配内存时，使用TLAB可以避免一系列的非线程安全问题，同时还能提升内存分配的吞吐量，因此我们可以将这种内存分配方式称为**快速分配策略**.

![image-20201208192233293](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201208192233293.png)

### 说明

尽管不是所有对象实例都能够在TLAB中成功分配内存，但JVM确实是将TLAB作为内存分配的首选。

- `-XX:UseTLAB`

  设置是否开启TLAB空间。

- `-XX：TLABWasteTargetPercent`

  默认情况下，TLAB空间的内存非常小，仅仅 1%

一旦对象在TLAB空间内存分配失败，JVM就会尝试通过使用加锁机制确保数据操作的原子性，而直接在Eden空间中分配内存。

![image-20201208192748030](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201208192748030.png)



## 小结堆空间

- 查看所有的参数的默认初始值

  ```
  -XX:+PrintFlagsInitial
  ```

- 查看所有的参数的最终值

  ```
  -XX:+PrintFlagsFinal
  ```

- 查看某单个参数的指令

  ```
  jps: 查看当前运行中的进程
  jinfo -flag SurvivorRatio 进程id
  ```

- 初始堆空间内存,默认物理内存1/64

  ```
  -Xms:
  ```

- 最大堆空间内存,默认物理内存的1/4

  ```
  -Xmx:
  ```

- 设置新生代大小 初始值及最大值

  ```
  -Xmn：
  ```

- 配置新生代与老年代在堆结构的占比

  ```
  -XX:NewRatio
  ```

- 设置新生代中Eden和S0/S1空间的比例

  ```
  -XX:SurvivorRatio:
  ```

- 设置新生代垃圾的最大年龄

  ```
  -XX:MaxTenuringThreshold
  ```

- 输出详细的GC处理日志

  ```
  -XX:+PrintGCDetails
  ```

  - 打印gc简要信息 

    ```
    -XX:+PrintGC
    -verbose:gc
    ```

- 是否设置空间分配担保

  ```
  -XX:HandlePromotionFailure
  ```

  

## 堆的不唯一选择

> 随着JIT编译期的发展与逃逸分析技术逐渐成熟，栈上分配、标量替换优化技术将会导致一些微妙的变化，所有对象分配到堆上也渐渐变得不那么“绝对”。

### 逃逸分析

**定义**

这是一种可以有效减少Java程序中同步负载和内存堆分配压力的跨函数全局数据流分析算法。

**作用**

通过逃逸分析，Java Hotspot编译器能够分析出一个新的对象的引用的使用范围从而决定是否要将这个对象分配到堆上。

**行为**

逃逸分析的基本行为就是分析对象动态作用域:

- 当一个对象在方法中被定义后，对象只在方法内部使用，则认为没有发生逃逸

  ```
  public void my method(){
  	V v = new V();
  	...
  	v = null;
  }
  ```

- 当一个对象在方法中被定义后，对象在外部方法被引用，则认为发生了逃逸，例如作为调用参数传递到其他地方

  ```
  public static StringBuffer createStringBuffer(String s1, String s2){
  	StringBuffer sb = new StringBuffer();
  	sb.append(s1);
  	sb.append(s2);
  	return sb;
  }
  ```

  如果想不逃出

  ```
  public static StringBuffer createStringBuffer(String s1, String s2){
  	StringBuffer sb = new StringBuffer();
  	sb.append(s1);
  	sb.append(s2);
  	return sb.toString();
  }
  ```



## 代码优化

使用逃逸分析，编译器可以对diamagnetic做如下优化

### 栈上分配

将堆分配转化为栈分配。如果一个对象在子程序中被分配，要使指向该对象的指针永远不会逃逸，对象可能是栈分配的候选，而不是堆分配

**场景**

给成员变量赋值、方法返回值、实例引用传递

**应用**

添加变量参数设置

```
-DoExcapeAnalysis 关闭逃逸检查，就不会进行栈上分配
+DoExcapeAnalysis 开启逃逸检查，会进行栈上分配
```

开启时间为 4ms   , 关闭为 70ms

**原因**

```
因为关闭了逃逸检查，就会直接存放到堆内存区，这样会执行GC，执行GC会导致用户进程暂停，严重导致程序运行时间过长。
```



### 同步省略

**原因**

线程同步的代价是非常高的，同步的后果就是降低并发性和性能

> 如果一个对象被发现只能从一个线程被访问到，那么对于这个对象的操作可以不考虑同步。

**原理**

```
在动态编译同步块的时候，JIT编译器可以借助逃逸分析来判断同步块所使用的锁对象是否只能够被一个线程访问而没有被发布到其他线程。如果没有，那么JIT编译器在编译这个同步块的时候就会取消对这部分代码的同步。这样就能大大提高并发性和性能。叫同步省略也叫锁消除
```

**例子分析**

```java
public void f(){
	Object hollis = new Object();
	synchronized(hollis){
		System.out.println(hollis);
	}
}
```

代码中对hollis这个对象进行加锁，但是hollis对象的生命周期只在f()方法中，并不会被其他线程访问到，所以在JIT编译阶段会被优化掉

```java
public void f(){
	Object hollis = new Object();
	System.out.println(hollis);
}
```

<img src="C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201209110747333.png" alt="image-20201209110747333" style="zoom:67%;" />

字节码文件还是存在，只是运行的时候会不考虑这个而情况发生

### 分离对象或标量替换

**背景**

有的对象可能不需要作为一个连续的内存结构存在也可以被访问到，那么对象的部分或全部可以不存在内存，而是存储在CPU寄存器中

**定义**

**标量(Scalar)**是指一个无法再分解成更小的数据。Java中原始数据类型就是标量。

**聚合量(Aggregate)**还可以分解的数据，对象就是聚合量

**示例**

在JIT阶段，如果经过逃逸分析，发现一个对象不会被外界访问的话，那么经过JIT优化，就会把这个对象分解拆成若干个其中包含若干个成员变量来代替，这就是标量替换

```java
public static void main(String[] args){
    alloc();
}

private static void alloc(){
    Point point = new Point(1,2);
    System.out.println("point.x =" + point.x+"; point.y" + point.y);
}

class Point{
    private int x;
    private int y;
}
```

以上代码，经过标量替换后，变成：

```java
private static void alloc(){
	int x = 1;
    int y = 2;
    System.out.println("point.x =" + point.x+"; point.y" + point.y);
}
```

**好处**

可以大大减少内存的占用，因为一旦不需要创建对象了，那么就不再需要分配堆内存了。

**参数设置**

默认开启了标量替换，允许将对象打散分配在栈上面

```
-XX:+EliminateAllocations
```

```
-server -Xmx100m -Xms100m -XX:+DoEscapeAnalysis -XX:+PrintGC -XX:+EliminateAllocations
```

- -server: 启动服务器模式，因为在server下才能逃逸分析
- -XX:+DoEscapeAnalysis：启用逃逸分析
- -Xmx100m： 指定了堆空间最大10M
- -XX：+PrintGC  打印GC日志
- -XX: +EliminateAllocations: 开启标量替换，允许将对象打散分散在栈上