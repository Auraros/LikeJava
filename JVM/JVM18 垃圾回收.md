# JVM18 垃圾回收

<img src="C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201213125307645.png" alt="image-20201213125307645" style="zoom:67%;" />

常见问题：

![image-20201213134545918](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201213134545918.png)



## 定义

垃圾是指在运行程序中没有任何指针指向的对象，这个对象就是需要被内存回收的垃圾。

**垃圾收集**三个经典问题：

- 哪些内存需要回收？
- 什么时候回收？
- 如何回收？

垃圾收集机制是Java的招牌能力，极大提高了开发效率



## Java垃圾回收机制

自动内存管理，无需开发人员手动参与内存的分配与回收，这样降低内存泄漏和内存溢出的风险。

自动内存管理机制，将程序员从繁重的内存管理中释放出来，可以更专业的专注于业务开发。

**Java堆是垃圾收集器的工作重点**

从次数上讲：

- 频繁收集Young区域
- 较少收集old区
- 基本不动Perm区



## 垃圾标记阶段

### 对象存活判断

在堆里存放几乎所有的Java对象的实例，在GC执行垃圾回收之前，首先需要区分出内存中哪些是存活对象，哪些是死亡的对象。只有被标记为已经死亡的对象，GC才会执行垃圾回收时，释放掉其所占用的内存的空间，因此这个过程可以称为**垃圾标记阶段**。

如何判断是否是一个死亡对象？

**当一个对象已经不在被任何一个存活对象继续引用时，就可以宣判已经是死亡**

判断对象存活一般有两种方式： **引用计数算法**和 **可达性分析算法**



#### **引用计数法**

**引用计数法(Reference Counting)**比较简单，对每个对象保存一个整形的引用计数器属性，用于记录被引用的情况。

对于一个对象A，只要有任何一个对象引用了A，则A的引用计数器加1；当引用失效时，引用计数器就减一，只要记录对象被引用的情况。

**优点：**实现简单，垃圾对象便于辨识；判定效率高，回收没有延迟性

**缺点：**

- 它需要单独的字段存储计数器，这样的做法增加了存储空间的开销。
- 每次赋值都需要更新计数器，伴随着加法和减法操作，这增加了时间开销
- 引用计数器有一个严重的问题，即无法处理循环引用的情况，这是一条致命缺陷，导致Java在垃圾回收器中没有使用这类算法。 

**总结**

引用计数算法，是很多语言的资源回收选择，例如Python，它更是同时支持引用计数和垃圾收集机制。

Python如何解决循环引用？

- 手动解除：很好理解，就是在合适的时候，解除引用关系
- 使用弱引用weakref。



#### 可达性分析

**可达性分析又称为根搜索算法、追踪性垃圾收集(Tracing Garbag Coolection)**

相对于引用计数算法而言，可达性分析算法不仅同样具备实现简单和执行高效等特点，更重要的是该算法可以有效**解决在引用计数算法中循环引用的问题，防止内存泄漏的发生。**



**基本思路**

- 可达性分析算法是以根对象集合(GC Roots)为起始点，按照从上至夏的方式搜索被根对象集合所连接的目标对象是否可达。
- 使用可达性分析算法后，内存中的存活对象都会被根对象集合直接或间解连接着，搜索所走过的路径称为**引用链(Reference Chain)**
- 如果目标对象没有任何引用链相连，则是不可达的，就意味着对象已经死亡，可以标记为垃圾对象。
- 在可达性分析算法中，只有能够被根对象集合直接或间解连接的对象才是存活对象。

![image-20201213191726146](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201213191726146.png)

**在Java语言中，GC Roots包括以下几种元素：**

1. 虚拟机栈中引用的对象(放在局部变量表)

- 比如：各个线程被调用的方法中使用到的参数、局部变量等

2. 本地方法栈内JNI(通常说的本地方法)引用的对象

3. 方法区中静态属性引用的对象

- 比如：Java类的引用类型静态变量

4. 方法区中常量引用的对象

- 比如：字符串常量池中的引用

5. 所有被同步锁synchronized持有的对象

6. Java虚拟机内的引用

- 基本数据类型对象的Class对象，一些常驻的异常对象，系统类加载器

7. 反映java虚拟机内部情况的JMXBean、JVMTI中注册的回调、本地代码缓存

**小技巧：由于Root采用栈方式存放变量和指针，所以如果一个指针，它保存堆内存里面的对象，但是自己又不在堆内存中，那么他就是一个Root**



**注意**：

如果要用可达性分析算法来判断内存是否可以回收，那么分析工作必须在一个能保障一致性的快照中进行，这点不满足的话分析结果的准确性就无法保证。

这点也是导致GC进行时必须"Stop The World"的原因。



## 对象的finalization机制

Java语言提供了对象终止(finalization)机制来允许开发人员提供**对象被销毁之前的自定义处理逻辑。**

当前垃圾回收器发现没有引用指向一个对象，即: 垃圾回收此对象之前，总会先调用这个对象的finalize()方法。

finalize()方法允许在子类中被重写，用于在对象被回收时进行资源释放。通常在这个方法中进行一些资源释放和请理工作。比如关闭文件、套接字和数据库连接。

### 不要主动调用

永远不要主动调用某个对象的finalize()方法，应该交给垃圾回收机制调用。理由包括如下：

- 在finalize()时可能会导致对象复活。
- finalize()方法的执行时间是没有保障的，它完全由GC线程决定，极端情况下，若不发生GC，则finalize()方法将没有执行机会。
- 一个糟糕的finalize()严重影响GC的性能

由于finalize的方法，虚拟机中的对象一般处于三种可能状态：

> 如果从所有根节点都无法访问到某个对象，说明对象已经不在使用了，一般来说，此对象需要被回收。但事实上，也并非是"非死不可“的，这时候它们暂时处于”缓刑“阶段。**一个无法触及的对象有可能在某一条件下”“复活*自己。*如果这样，那么对他的回收就是不合理的，所以由以下三种状态：
>
> - 可触及的：从根节点开始，可以达到这个对象
> - 可复活的：对象的所有引用都被释放，但是对象有可能在finalize(0fu）复活
> - 不可触及的：对象的finalize()被调用，并且没有复活，那么就会进入不可触及状态，不可触及的对象不可能被复活，因为finalize()只会被调用一次。
>
> 以上3种状态中，是由于finalize()方法的存在，进行的区分，只有在对不可触及时才可以被回收。

具体过程：

![image-20201213201340936](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201213201340936.png)

复活实例:

```java
/**
 * @Author : Auraros
 * @Description : 测试object中的finalize放方法
 * @Data : 2020-12-13 20:23
 * @Version : 1.0
 */
public class CanReliveObj {

    public static CanReliveObj obj;

//    @Override
//    protected void finalize() throws Throwable{
//
//        super.finalize();
//        System.out.println("调用当前类重写");
//        obj = this;
//    }


    public static void main(String[] args) throws InterruptedException {


        obj = new CanReliveObj();

        obj = null;
        System.gc();
        System.out.println("第一次GC");
        Thread.sleep(2000);
        if(obj == null){
            System.out.println("obj is dead");
        }
        else{
            System.out.println("obj is alive");
        }
        System.out.println("第二次GC");
        obj = null;
        System.gc();
        Thread.sleep(2000);
        if(obj == null){
            System.out.println("obj is dead");
        }
        else{
            System.out.println("obj is alive");
        }
        

    }

}

```

<img src="C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201213203201224.png" alt="image-20201213203201224" style="zoom:67%;" />

去掉注释:

<img src="C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201213203249752.png" alt="image-20201213203249752" style="zoom:67%;" />



## OOM使用JProfile进行查找

代码：

<img src="C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201213221011310.png" alt="image-20201213221011310" style="zoom:67%;" />

设置参数:

```
-Xms8m -Xmx8m -XX:+HeapDumpOnOutOfMemoryError
```

生成如下文件:

![image-20201213221242700](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201213221242700.png)

点击Biggest Objects,当前程序超大对象

![image-20201213221333840](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201213221333840.png)

![image-20201213221434018](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201213221434018.png)



## 垃圾清除阶段

当成功区分出内存中存活对象和死亡对象后，GC 接下来的任务就是执行垃圾回收，释放掉无用对象所占用的内存空间，以便有足够的可用内存空间为新对象分配内存。

 目前JVM中比较常见的三种垃圾收集算法是:

- 标记-清除算法(Mark-Sweep)
- 复制算法(Copying)
- 标记-压缩算法(Mark-Compact)

 

### 标记-清除算法

**背景：**

标记-清除算法是一种非常基础和常见的垃圾回收算法，该算法被用在Lisp语言。

**执行过程:**

当堆中的有效内存空间(avaliabel memory)被耗尽的时候，就会停止整个程序(Stop the world)，然后进行两项工作，第一项是标记，第二项是删除。

- 标记:Collector从引用根节点开始遍历，标记所有被引用的对象，一般是在对象的Header中记录为可达对象
- 清除:Coolector对堆内存从头到尾进行线性的遍历，如果发现某个对象在Header中没有标记为可达对象，则将其收回

<img src="C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201213225333305.png" alt="image-20201213225333305" style="zoom:50%;" />

**缺点**

- 效率不高
- 在进行GC的时候，需要停止整个应用程序，导致用户体验差
- 这种方式清理出来的空闲内存是不连续的，产生内存碎片，需要维护一个空闲列表

**何为清除？**

这些所谓的清除并不是真的置空，而是把需要清除的对象地址保存在空闲的地址列表里，有新的对象需要加载的时候，判断垃圾位置空间是否够，够就存放。



### 复制算法

**背景**

为了解决标记-清除算法在垃圾收集效率方面的缺陷

**核心思想**

将活着的内存空间分为两块，每次只使用其中的一块，在垃圾回收时将正在使用的内存中的存活对象复制到未被使用的内存块中，之后清除正在使用内存块中所有对象，交换两个内存的角色，最后完成垃圾回收。

![image-20201213230903332](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201213230903332.png)



### 标记-压缩算法

**背景**

复制算法的高效性是建立在存活对象少，垃圾对象多的前提下。这种情况下在新生代经常发生，但是在老年代，更常见的情况下是大部分对象都是存活对象，如果依然使用复制算法，由于存活对象比较多，复制成本也比较高。**基于老年代垃圾回收的特性，需要使用其他算法。**

标记-清除算法的却可以应用在老年代中，但是该算法不仅执行效率低下，而且在执行完内存回收后会产生内存碎片，所以JVM在此基础上进行了优化。



![image-20201213234424930](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201213234424930.png)



**定义**

标记-压缩算法的最终效果等同于标记-清除算法执行完成后，再进行一次内存碎片整理/因此也叫 标记-清除-压缩(Mark-Sweep-Compact)算法。

二者的本质差异在于标记-清除算法是一种非移动式的回收算法，标记-压缩是移动式，是否移动回收后的存活对象是一项优缺点并存的风险决策



**优点**

- 消除了标记-清除算法当中，内存区域分散的缺点，我们需要给新对象分配内存时，JVM只需要持有一个内存的起始地址即可
- 消除了复制算法当中内存减半的高额代价

**缺点**

- 从效率来说，标记-压缩算法要低于复制算法。
- 移动对象的同时，如果对象被其他对象引用，则还需要调整引用的地址
- 移动过程中，需要全程暂停用户应用程序 STW



#### 总结

![image-20201214001227184](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201214001227184.png)



## 分代收集算法

### 背景

不同的对象的生命周期是不一样的。因此，**不同生命周期的对象可以采取不同的收集方式，以便提高回收效率。**一般是把Java堆分为新生代和老年代，这样就可以根据各个年代的特点使用不同的回收算法，以提高垃圾回收的效率。

### 定义

在HotSpot中，基于分代的概念，GC所使用的内存回收算法必须结合年轻代和老年代各自的特点。

**年轻代**(Young Gen)

```
特点： 区域相对老年代较小，对象生命周期短、存活率低，回收频繁

这种情况下复制算法的回收整理，速度是最快的，复制算法的效率只和当前存活对象大小有关，因此很适用于年轻代的回收，而复制算法内存利用率不高的问题，通过hotspot的两个survivor的设计得到缓解
```

**老年代**( Tenured Gen)

```
特点： 区域比较大，对象生命周期长，存活率高，回收不及年轻代频繁

这种情况下存在大量存活率高的对象，复制算法明显显得不合适，一般是由标记-清除或者标记-清除和标记-整理的混合实现：
1. Mark阶段的开销与存活对象的数量成正比
2. Sweep阶段的开销与所管理区域的大小成正比
3. Compact阶段的开销与存活的对象的数据成正比
```



以HotSpot中的CMS回收器为例，CMS是基于Mark-Sweep实现的，对于对象的回收效率很高。而对于碎片问题，CMS采用基于Mark-Compact算法的Serial Old回收器作为补偿措施：当内存回收不佳(碎片导致的Concurrent Mode Failure时)将采用Serial Old执行 Full GC以达到对老年代内存的整理。



## 增量收集算法

### 背景

> 在垃圾回收的过程中，应用软件将处于一种Stop The World的状态，应用程序的所有线程都会被挂起，暂停一切正常工作，将严重影响用户体验或系统的稳定性。

### 基本思想

如果一次将所有的垃圾进行处理，需要造成系统长时间的停顿，那么就可以让垃圾收集线程和应用线程交替执行，每次，垃圾收集只收集小片区域的内存空间，接着切换到应用程序，一次返回，直到收集完成。

总的来说，增量收集算法的基础仍是传统的标记-清除和复制算法。



### 缺点

使用这种方式，由于在垃圾回收过程中，间断性地还执行了应用程序代码，所以能减少系统地停顿时间，但是，因为线程切换和上下文转换地消耗，会使得垃圾回收地总体成本上升，造成系统吞吐量下降。



## 分区算法

一般来说，在相同地条件下，堆空间越大，一次GC时需要的时间就越长，有关GC产生的停顿也就越长。为了更好地控制GC产生地停顿时间，将一块大的内存区域分割成多个小块，根据目标地停顿时间，每次合理地回收若干个小区间，而不是整个堆空间，从而减少一次GC所产生地停顿。

分代算法按照对象的生命周期长短划分两个部分，分区算法将整个堆空间划分成连续的不同小区间。每个小区间都时独立使用的，独立回收，这种算法的好处是可以控制一次回收多少个小区间。

![image-20201214203259031](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201214203259031.png)



**注意：**

**这些只是基本算法实现思路，实际GC实现过程要复杂得多，目前还在发展在二的前沿GC都是复合算法，并且并行和并发兼备。**





## 垃圾回收一些概念

### System.gc()

> 在默认情况下，通过System.gc()或者 Runtime.getRuntime().gc()的调用，会显示触发Full GC，同时对老年代和新生代进行回收，尝试释放被丢弃对象的内存。
>
> 然而，System.gc() 调用一个免责声明，无法保证对垃圾收集器的调用。
>
> JVM实现者可以通过调用System.gc()调用来决定JVM的GC行为，一般情况下，垃圾回收应该是自动进行的，无须手动触发，否则就太麻烦了。在一些特殊情况下，如我们正在编写一个性能基准。

```java
public class SystemGCTest{
    public static void main(String[] args){
        new SystemGCTest();
        System.gc(); //tixingjvm的垃圾回收器执行gc，但是不确定是否能马上执行gc
        
        System.runFinalization();//强制调用使用引用对象的finalize()方法。
        
    }
    
    @Override
    protect void finalize() throws Throwable{
        super.finalize();
        System.out.print("SystemGCTest 重写了finalize")；
    }
}
```

```
public void localvarGC3(){
	{
		bytes[] buffer  = new byte[10*1024*1024]; 
	}
	System.gc(); //不会被回收
}

//局部变量表实际上有两个数据，一个是this,一个是这个buffer,但是，通过字节码文件，只显示出来一个，第二个并没有被其他调用。所以还是指向buffer。
```

```
public void localvarGC3(){
	{
		bytes[] buffet  = new byte[10*1024*1024]; 
	}
	int value = 10;
	System.gc(); //不会被回收
}
//局部变量表实际上有两个数据，一个是this,一个是value,通过字节码文件，只显示出来一个，第二个是一个value，value的数据讲buffer进行了覆盖所以buffer就没有被指向了。
```



### 内存溢出

> 内存溢出是引发程序崩溃的罪魁祸首之一。
>
> 由于GC一直在发展，所有一般情况下，除非应用程序占用的内存增长速度非常快，造成的垃圾回收已经跟不上内存消耗的速度，否则不太容易出现OOM的情况。

javadoc中对OutOfMemoryError的解释是，没有空闲内存，并且垃圾收集器也无法提供更多的内存。 

**Java虚拟机内存不够，原因如下：**

1. **Java虚拟机的堆内存设置不够**

   ```
   比如： 可能存在内存泄漏问题，也很有可能是堆的大小不合理，比如我们要处理比较可观的数据量，但是没有显式指定JVM堆大小或者数值偏小，我们可以通过参数-XMs、-Xms来调整。
   ```

2. **代码中创建了大量大对象，并且长时间不能被垃圾收集器收集(存在被引用**

   ```
   老年代JDK不断添加新类型的时候，永久代出现OutOfMemoryError也非常多见。类似于intern字符串缓存占用太多的空间，也会导致OOM问题。是java.lang.OutOfMemoryError:PermGen space
   
   随着元数据区的引入，方法区已经不再那么窘迫，出现异常为: java.lang.OutOfMemoryError: Metaspace
   ```

   



### 内存泄漏 (Memory Leak)

也被成作是"存储渗漏"。严格来说，只有对象不会被程序用到了，但是GC又不能回收他们的情况，才叫内存泄漏。

但实际情况很多时候，一些不太好的时间会导致对象的生命周期变得很长甚至导致OOM，也可以叫做宽泛意义上的"内存泄漏"。

尽管内存泄漏并不会立刻引起程序崩溃，但是一旦发生内存泄漏，程序中的可用内存就会被逐步蚕食，直到耗尽所有的内存，最终出现OutOfMemory异常，导致程序崩溃。

这里的存储空间并不是指物理内存，而是指虚拟内存大小，这个虚拟内存大小取决于磁盘交换区设定的大小。

![image-20201215125813500](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201215125813500.png)



**举例：**

1. 单例模式

   ```
   单例的生命周期和应用程序是一样长的，所以单例程序中，如果持有对外部对象的引用的话，那么这个外部对象是不能够被回收的，则会导致内存泄漏问题。
   ```

2. 一些提供close的资源未关闭导致内存泄漏

   ```
   数据库连接(dataSource.getConnection())，网络连接(socket)和io连接必须手动close，否则是不能被回收的
   ```

   





### Stop The World

Stop-the-World, 简称STW， 指的是GC事件发生过程中，会产生应用程序的停顿。**停顿产生时整个应用程序线程会被暂停的，没有任何响应。**这个停顿称为STW

- 可达性分析算法中枚举根节点(GC Roots)会导致所有Java执行线程停顿。
  - 分析工作必须在一个能确保一致性的快照中进行
  - 一致性指整个分析期间整个执行系统看起来像被冻结在某个时间节点上
  - 如果出现分析过程对象引用关系还在不断变化，则分析结果的准确性无法保证。

STW事件和采用哪款GC无关，所有的GC都有这个事件。

哪怕是G1也不能完全避免STW情况发生，只能说垃圾回收器越来越优秀，回收效率越来越高，尽可能地缩短了事件。

STW是JVM在后台自动发起和自动完成地，在用户不可见的情况下，把用户正常的工作线程全部停掉。

```java
public class StopTheWorldDemo{
    public static class WorkThread extends Thread{
        List<byte[]> list = new ArrayList<byte[]>();
        
        public void run(){
            try{
				while(true){
                    for(int i = 0; i < 1000; i++){
                        byte[] buffer = new byte[1024];
                        list.add(buffer);
                    }
                    
                    if(list.size() > 10000){
                        list.clear();
                        System.gc();
                    }
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        
    }
    
    public static class PrintThread extends Thread{
        public final long startTime = System.currentTimeMillis();
        public void run(){
			try{
                while(true){
                    //每秒打印时间信息
                    long t = System.currentTimeMills() - startTime;
                    System.out.println( t / 1000 + "." + t % 1000);
                    Thread.sleep(1000);
                }
            }catch(Execption ex){
                ex.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args){
        WorkThread w = new WorkThread();
        PrintThread p = new PrintThread();
        //w.start();
        p.start();
    }
    
}
```

![image-20201215224731839](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201215224731839.png)



### 垃圾回收的并行和并发

#### 并发

在操作系统中，是指一个时间段中有两个程序都处于已启动运行到运行完毕之间，且这几个程序都是在同一个处理器上运行。

并发不是正真意义上的“同时进行”，只是把CPU把一个时间段划分成几个时间片段，然后在这几个时间区间来回切换，由于CPU处理的速度非常快，只要时间间隔处理得当，既可以让用户感觉是多个程序在同时运行。

![image-20201215225826127](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201215225826127.png)

#### 并行(Parallel)

当系统有一个以上CPU时，当一个CPU执行一个进程时，另一个CPU可以执行另一个进程，两个进程互不抢占CPU资源，可以同时进行，我们称之为并行。

其实决定并行的因素不是CPU的数量，而是CPU的核心数量，比如一个CPU多个核也可以并行。

![image-20201215230235246](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201215230235246.png)



#### 二者对比

并发，指的是多个事情，在同一时间段内同时发生了。

并行，指的是多个事情，在同一时间上同时发生了。

并发的多个任务之间是互相抢占资源的。

并行的多个任务之间是不互相抢占资源的。



#### 在垃圾回收中

**并行**：指多条垃圾收集线程并行工作，但此时用户线程仍处于等待状态。

- 如 ParNew、Parallel Scavenge、Parallel Old

**串行：**

- 相较于并行的概念，单线程执行
- 如果内存不够，则程序暂停，启动JVM垃圾回收器进行垃圾回收，回收完，再启动程序的线程

**并发：**指用户线程于垃圾收集线程同时执行(但不一定是并行的，可能会交替执行)，垃圾会首先线程在执行时不会停顿用户程序的运行。

- 用户程序在继续运行，而垃圾收集程序线程运行于另一个CPU上
- 如: CMS,G1



### 安全点与安全区域

#### 安全点

程序执行时并非在所有地方都能停顿下来开始GC，只有在特定的位置才能停顿下来开始GC，这些位置称为“安全点”(SafePoint)

Safe Point的选择很重要，如果太少可能导致GC等待时间太长，如果太频繁可能导致运行时的新跟那个问题。大部分指令的执行时间都非常短暂，通常会根据是否具有让程序长时间执行的特征。

比如：选择一些执行时间比较长的指令作为Safe Point，如方法调用、循环跳转和异常跳转等。



**如何在GC发生时，检查所有线程都跑到了最近的安全点停顿下来呢**

- 抢先式中断

  首先终端所有线程，如果还有现场不在安全点，就恢复线程，让线程跑到安全点

- 主动式中断

  设置一个终端标志，各个线程运行到Safe Ponit的时候主动轮询到这个标志，如果标志为真，则将自己进行中断挂起



#### 安全区域

![image-20201215233627605](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201215233627605.png)

**安全区域是指在一段代码片段中，对象的引用关系不会发生变化，在这个区域中 的任何位置开始GC都是安全的。**可以把Safe Region 看作是被扩展的SafePoint

**实际执行时：**

1. 当线程运行到Safe Region的代码时，首先标识已经进入了Safe Region，如果这段时间内发生GC，JVM会忽略标识为Safe Region的状态线程。
2. 当线程即将离开Safe Region时，会检查JVM是否已经完成GC，如果完成了，则继续运行，否则线程必须等待直到收到可以安全离开Safe Region信号为止。



### 引用

> 我们希望能描述这样一类对象：当内存空间还够的时候，则能保留在内存中；如果内存空间在进行垃圾收集后还是很紧张，则可以抛弃这些对象。

**高频面试题:**强引用、软引用、虚引用有什么区别？具体场景是什么？

在JDK1.2版之后，Java对引用的概念进行了扩充，将引用分为强引用(Strong Refrence)、软引用(Soft Reference)、弱引用(Weak Reference)和虚引用(Phantom Reference)4种，这4种引用强度一次逐渐减弱。

除强引用外，其他3种引用均可以在java.lang.ref包中找到它们的身影。

![image-20201215235524400](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201215235524400.png)



Reference子类中有终结者引用是包内可见的，其他3种引用类均为public，可以在应用程序中直接使用：

- **强引用:**(StrongReference): 最传统的"引用"的定义，是指在程序代码中普遍存在的引用赋值，即类似"Object obj = new Object()"这种引用关系，无论在任何情况下，只要强引用关系还存在，垃圾收集器就永远不会回收掉被引用的对象。
- **软引用**：(SoftReference)：在系统将要发生内存溢出之前，将会把这些对象列入回收返回之中进行第二次回收，如果这次回收还没有足够的内存，才会排除内存溢出异常。
- **弱引用:**(WeakReference): 被弱引用关联的对象只能生存到下一次垃圾收集前，当垃圾收集器工作时，无论内存空间是否足够，都会回收掉被弱引用关联对象。
- **虚引用：**(PhantomRefernce)：一个对象是否有虚引用的存在，完全不会对其生存时间构成影响，也无法通过虚引用来获得一个对象的实例，为对象设置虚引用关联的唯一i目的就是能在这个对象被收集器回收时收到一个系统通知。

#### 强引用

在Java程序中，最常见的引用类型是强引用(普通系统99%以上都是强引用)，也就是我们最常见的普通对象引用，也就是默认的引用类型。

当在Java语言中使用new操作符创建一个新的对象，并将其赋值给一个变量时，这个变量就成为指向该对象的一个强引用。

**强引用的对象是可触及的，垃圾收集器就永远不会回收掉被引用的对象。**

强引用是造成Java内存泄漏的主要原因之一。



**例子：**

```
StringBuffer str = new StringBuffer("Hello world");
```

局部变量str指向StringBuffer实例所在的堆空间，通过str可以操作该实例，那么str就是StringBuffer实例的强引用。

<img src="C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201216104858452.png" alt="image-20201216104858452" style="zoom:67%;" />



**特点：**

- 强引用可以直接访问目标对象
- 强引用所指向的对象在任何时候都不会被系统回收，虚拟机宁愿抛出OOM异常，也不会回收强引用所指向的对象
- 强引用可能导致内存泄漏、



#### 软引用(Soft Reference) ——内存不足即回收

**![image-20201216105138007](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201216105138007.png)**

特点:

- 当内存足够时，不会回收软引用对象
- 当内存不够时，才会回收软引用对象

例子:

```
Object obj = new Object();  声明强引用
SoftReferene<Object> sf = new SoftReference<Object>(obj);

obj = null; //销毁强引用 
```

代码如下:

```java
//输入参数 -Xms10m -Xmx10m
public class SoftRefenceTest{
	public static class User{
        public User(int id, String name){
            this.id = id;
            this.name = name;
        }
        
        public int id;
        public String name;
        
        @Override
        public String toString(){
			return "[id="+id+",name="+name+"]";
        }
    }
    
    public static void main(String[] args){
        SoftReference<User> userSofetRef = new SoftRefence<User>(new User(1, "hello"));
        
        //User u1 = new User(1, "hello");
        //SoftReference<User> userSofetRef = new SoftRefence<User>(u1);
        //u1 = null;
        
        System.out.println(userSoftRef.get()); //还在
        
        System.gc();//内存足够，不会回收软引用的可达对象
        System.out.println("After GC:");
        System.out.println(userSoftRef.get()); //内存足够，还在
        
        try{
            byte[] b = new byte[1024*1024*7]; //这个加入后内存不够，爆OOM
        }catch(Throwable e){
            e.printStackTrace();  //打印异常
        }finally{
            System.out.println(userSoftRef.get()); //因为内存不够软引用被请理，输出null
        }
        
    }
}
```



#### 弱引用(Weak Reference)——发现即回收

![image-20201216111516468](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201216111516468.png)

```
Object obj = new Object();  声明强引用
WeakReferene<Object> sf = new WeakReference<Object>(obj);

obj = null; //销毁强引用 
```

弱引用对象与软引用对象的最大不同就在于，当GC在进行回收时，需要通过算法检查是否回收软引用对象，而对于弱引用对象，GC总是进行回收。弱引用对象更容易、更快被GC回收。

**面试题：你开发中用过WeakHashMap吗**

```
//输入参数 -Xms10m -Xmx10m
public class SoftRefenceTest{
	public static class User{
        public User(int id, String name){
            this.id = id;
            this.name = name;
        }
        
        public int id;
        public String name;
        
        @Override
        public String toString(){
			return "[id="+id+",name="+name+"]";
        }
    }
    
    public static void main(String[] args){
        WeakReference<User> userSofetRef = new WeakRefence<User>(new User(1, "hello"));
        
        //User u1 = new User(1, "hello");
        //SoftReference<User> userSofetRef = new SoftRefence<User>(u1);
        //u1 = null;
        
        System.out.println(userSoftRef.get()); //还在
        
        System.gc();//内存足够，不会回收软引用的可达对象
        System.out.println("After GC:");
        System.out.println(userSoftRef.get()); //内存足够，还在
        
       
        
    }
}
```



#### 虚引用(Phantom Reference)——对象回收跟踪

![image-20201216182824823](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201216182824823.png)

![image-20201216183335950](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201216183335950.png)

```
Object obj = new Object();  声明强引用
ReferenceQueue phantomQueue = new ReferenceQueue();
PhantomReferene<Object> sf = new PhantomReference<Object>(obj);

obj = null; //销毁强引用 
```



## 垃圾回收器

### 评估GC的性能指标

- **吞吐量：**运行用户代码的时间占总运行时间的比例
  - 总运行时间：程序运行时间+内存回收时间
- 垃圾收集开销：吞吐量的补数，垃圾收集所用时间与运行总时间比例
- **暂停时间：**执行垃圾收集时，程序的工作现场被暂停的时间
- 收集频率：相对于应用程序的执行，收集操作发生的频率
- **内存占用：**Java堆区所占的内存大小
- 快速：一个对象从诞生到回收所经历的时间

标深色的三个共同构成一个"不可能三角"，三者总体的表现会随着技术的进步而越来越好，一款优秀的收集器童话通常最多只能满足其中的两项。

简单来说，主要抓住这两点:

- 吞吐量
- 暂停时间



#### 吞吐量

![image-20201216203057782](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201216203057782.png)

#### 暂停时间

![image-20201216203505006](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201216203505006.png)

#### 对比

![image-20201216204129750](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201216204129750.png)

现在标准： **在最大吞吐量的情况下，降低停顿时间**



## 垃圾收集器

七个经典的垃圾收集器

- 串行回收器： Serial、Serial Old
- 并行回收器：ParNew、Parallel Scavenge、Parallel Old
- 并发回收器：CMS、G1

<img src="C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201216211848039.png" alt="image-20201216211848039" style="zoom:67%;" />



### 与垃圾分代对应关系

![image-20201216212053970](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201216212053970.png)

![image-20201216212413532](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201216212413532.png)

![image-20201216214221418](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201216214221418.png)

![image-20201216214244774](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201216214244774.png)



**如何查看默认的垃圾收集器**

```
-XX:+PrintCommandLineFlags
```

查看命令行相关参数(包含使用的垃圾收集器)

使用命令指令：

```
jinfo -flag 相关垃圾回收器参数 进行ID
```



### Serial回收器：串行回收

Serial收集器是最基本、历史最悠久的垃圾收集器了。JDK1.3之前回收新生代唯一的选择。

Serial收集器作为HotSpot中Client模式下的默认新生代垃圾收集器。

**Serial**收集器采用复制算法、串行回收和"Stop-the-World"机制的方式执行内存回收。

除了年轻代之外，Serial收集器还提供了用于执行老年代垃圾收集的Serial Old收集器。Serial Old收集器同样也采用了串行回收和"STW"机制，只不过内存回收算法使用的是标记-压缩算法。

- Serail Old是运行在Client模式下默认的老年代的垃圾回收器
- Serial Old在Server模式下主要有两种用途:
  - 与新生代的Parallel Scavenge配合使用
  - 作为老年代MS收集器的后备垃圾收集方案

![image-20201216220816194](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201216220816194.png)

这个收集器是一个单线程的收集器，但它的“单线程”的意义并不仅仅说明它只会使用一个CPU或一条收集线程去完成垃圾收集工作，更重要的是在它进行垃圾收集时，必须暂停其他所有工作线程，知道它收集结束(Stop The World)。

#### 优点

![image-20201216221009562](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201216221009562.png) 

```java
// jdk1.9 
//配置参数 -XX:+PrintCommandLineFlags -XX:+UseSerialGC
public class GCUseTest{
    
    public static void main(String[] args){
        
        ArrayList<byte[]> list = new ArrayList<>();
        
        while(true){
            byte[] arr = new byte[100];
            list.add(arr);
            try{
                Thread.sleep(10);
            }catch(Interruption e ){
                e.printStackTrace();
            }
        }  
    } 
}
```



#### 总结

这种垃圾收集器大家了解，现在已经不是串行的了。而且在限定单核CPU才可以用，现在都不是单核的了。



### ParNew回收器: 并行回收

![image-20201216222121314](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201216222121314.png)

![image-20201216224343923](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201216224343923.png)



#### 优缺点

![image-20201216224714575](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201216224714575.png)



#### 参数设置

![image-20201216224932963](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201216224932963.png)

```java
//jdk1.9
// 配置参数 -XX:+PrintCommandLineFlags -XX:+UseParNewGC -XX:+UserConcMarkSweepGC
public class GCUseTest{
    
    public static void main(String[] args){
        
        ArrayList<byte[]> list = new ArrayList<>();
        
        while(true){
            byte[] arr = new byte[100];
            list.add(arr);
            try{
                Thread.sleep(10);
            }catch(Interruption e ){
                e.printStackTrace();
            }
        }  
    } 
}
```



#### 总结

基本不使用了



### Parallel 回收器：吞吐量优先

![image-20201216232125973](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201216232125973.png)

![image-20201216233239728](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201216233239728.png)

**示意图：**

![image-20201216234157309](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201216234157309.png)

**优点：**

![image-20201216234242710](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201216234242710.png)

**参数配置**

![image-20201216234349436](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201216234349436.png)

![image-20201216235234893](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201216235234893.png)

![image-20201217173301043](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201217173301043.png)



### CMS回收器：低延迟

![image-20201217173928485](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201217173928485.png)

![image-20201217175354117](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201217175354117.png) 

#### 工作原理

![image-20201217180319855](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201217180319855.png) 



![image-20201217180920644](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201217180920644.png)

![image-20201217181435979](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201217181435979.png)



注意：

![image-20201217181756484](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201217181756484.png)



![image-20201217194036381](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201217194036381.png)



![image-20201217203045255](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201217203045255.png)



![image-20201217205750588](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201217205750588.png)

#### 参数设置

![image-20201217212939593](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201217212939593.png)

![image-20201217213542933](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201217213542933.png)

**新特性说明**

![image-20201217214532789](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201217214532789.png)



### 总结

![image-20201217213808264](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201217213808264.png)



### G1回收器: 区域化分代式

#### 背景

![image-20201217214915180](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201217214915180.png)

**为什么名字叫 Garbage First(G1)**

![image-20201217215930423](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201217215930423.png)

#### 定义

![image-20201217221835711](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201217221835711.png)

#### 特点

![image-20201217222841983](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201217222841983.png)

![image-20201217224105352](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201217224105352.png)

![image-20201217224318117](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201217224318117.png)

![image-20201217225456932](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201217225456932.png)

**缺点**

![image-20201217230152777](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201217230152777.png)



#### 参数设置

![image-20201217230426905](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201217230426905.png)



![image-20201217231635086](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201217231635086.png)



#### 适用环境

![image-20201217231808315](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201217231808315.png)

#### rgion: 化整为零

![image-20201217232251972](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201217232251972.png)

![image-20201217232543939](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201217232543939.png)

![image-20201217232653979](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201217232653979.png)

![image-20201217232818919](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201217232818919.png)

![image-20201217232901239](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201217232901239.png)

-  Bump - the - point 指针碰撞
- TLAB： 单独线程独有数据区


#### G1垃圾收集过程

![image-20201217233240135](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201217233240135.png)

![image-20201217233536165](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201217233536165.png)

![image-20201217233651675](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201217233651675.png)



##### rembered Set

![image-20201217234154086](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201217234154086.png)

![image-20201217234930924](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201217234930924.png)



##### G1回收过程1： 年轻代GC

![image-20201217235356915](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201217235356915.png)

![image-20201217235824409](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201217235824409.png)

![image-20201217235931308](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201217235931308.png)

##### G1回收过程二：并发标记阶段

![image-20201218000509735](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201218000509735.png)



##### G1回收过程三: 混合回收

![image-20201218001005810](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201218001005810.png)

![image-20201218001054728](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201218001054728.png)



##### G1回收可选过程四: Full GC

![image-20201218001245628](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201218001245628.png)



![image-20201218001432172](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201218001432172.png)



#### 优化

![image-20201218001552026](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201218001552026.png)



### 总结

![image-20201218001649489](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201218001649489.png)

![image-20201218001726785](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201218001726785.png)

![image-20201218001821977](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201218001821977.png)



### 参数设置

![image-20201218002148345](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201218002148345.png)

![image-20201218002226459](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201218002226459.png)

![image-20201218002249780](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201218002249780.png)



### GC 日志分析

![image-20201218002425234](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201218002425234.png)

![image-20201218002441589](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201218002441589.png)

![image-20201218002511717](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201218002511717.png)

![image-20201218002545377](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201218002545377.png)



**分析工具**

![image-20201218002938592](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201218002938592.png)



### 垃圾回收器新发展

![image-20201218003037717](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201218003037717.png)

![image-20201218003212321](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201218003212321.png)



![image-20201218003241785](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201218003241785.png)



#### ZGC

![image-20201218003456557](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201218003456557.png)

![image-20201218003531615](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201218003531615.png)

![image-20201218003746201](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201218003746201.png)