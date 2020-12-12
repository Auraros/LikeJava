# JVM14  对象实例化

## 对象实例化

![image-20201210233749351](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210233749351.png)



构建对象的方法：

![image-20201210234305664](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201210234305664.png)



构建对象步骤：

![image-20201211100149362](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201211100149362.png)

#### **判断对象对应的类是否加载、链接、初始化**

虚拟机遇到一条new指令，首先去检查这个指令的参数能否在Metaspace的常量池中定位到一个类的引用符号，并且检查这个符号引用代表的类是否已经被加载、解析和初始化。(即判断类元素信息是否存在)如果没有，那么在双亲委派模式夏，使用当前类加载器已ClassLoader+包名+类名为key进行查找对应的class文件，如果没有找到这个文件，则抛出ClassNotFoundException异常，如果找到，则进行类加载，并生成对应的Class对象。

#### **为对象分配内存**

首先计算对象空间占用大小，接着在堆中划分一块内存给新对象。如果实例成员是引用变量，仅分配引用变量的空间即可，即四个字节。

(1) 如果内存规整——指针碰撞

如果内存是规整的，那么虚拟机采用的是**指针碰撞算法**(Bump The Pointer)来为对象分配内存。

意思是所有用过的内存在一边，空闲的内存在另外以便，中间放着一个指针作为分界点的指示器，分配内存就仅仅是把指针向空闲那边挪动一段与对象大小象等的距离罢了。如果垃圾收集器选择的是Serial、ParNew这种基于压缩算法的，虚拟机采用这种分配方式，一般使用带有compact(整理)过程的收集器时，使用指针碰撞。

(2) 如果内存不规整——空闲列表分配

如果内存不是规整的，已使用的内存和未使用的内存相互交错，那么虚拟机将采用的是空闲列表法来为对象分配内存。

意思是虚拟机维护了一个列表，记录上哪些内存块是可用的，再分配的时候从列表中找到一块足够大的空间划分给对象实例，并更新列表上的内容，这种分配方式称为“空闲列表”(Free List)

**选择哪种分配方式由Java堆是否规整决定，而Java堆是否规整又由垃圾收集器是否拥有压缩整理功能决定。**

#### 处理并发安全问题

堆是共享区域，CAS失败从事、区域加锁保证更新的原子性

每个线程分配一块TLAB



#### 初始化分配到的空间

所有属性设置默认值，保证对象实例字段在不赋值的时候可以使用。

给对象的属性赋值操作：

1. 属性的默认初始化
2. 显示初始化 / 代码块中初始化
3. 构造器中初始化



#### 设置对象的对象头

将对象的所属类(即类的元数据信息)、对象的HashCode和对象的GC信息、锁信息等数据存储在对象头中，这个过程的具体设置方式取决于JVM实现。



#### 使用init方法进行初始化

在Java程序的角度来看，初始化才正式开始，初始化成员变量，执行实例化代码块，调用类的构造方法，并把堆内对象的首地址赋值给引用变量。

一般来说(由字节码中是否跟随有invokespecial指令所决定)，呢哇指令之后会接着就是执行方法，把对象按照程序员的意愿进行初始化，这样一个可用的对象才算完整创建出来。



## 对象的内存布局

![image-20201211103148399](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201211103148399.png)

![image-20201211103554758](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201211103554758.png)



![image-20201211103816604](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201211103816604.png)



## 对象的访问定位

![image-20201211105024585](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201211105024585.png)



对象访问定位图示：

![image-20201211104646717](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201211104646717.png)



**句柄访问**

![image-20201211105201927](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201211105201927.png)

优点：

reference中存储稳定句柄地址，对象被移动(垃圾收集时移动对象很普遍)时只会改变句柄中实例数据指针即可，reference本身不需要更改



**直接指针**

![image-20201211105413446](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201211105413446.png)