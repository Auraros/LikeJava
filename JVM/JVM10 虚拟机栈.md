# JVM10 虚拟机栈

## 背景

> 由于跨平台性的设计，Java的指令都是根据栈来设计的，不同平台的CPU架构不同，所以不能设计为基于寄存器的。
>
> 优点：跨平台，编译器容易实习
>
> 缺点：性能下降，实现同样的功能需要更多的指令

### 内存中的栈和堆

**栈是运行时单位，而堆是存储的单位**

栈解决程序运行时的问题，即程序如何执行，或者说如何处理数据。

堆解决的是数据存储的问题，即数据怎么放、放在哪里

如图：上半部分为堆，下半部分为栈

<img src="C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201205124625605.png" alt="image-20201205124625605" style="zoom:67%;" />



## 定义

> Java虚拟机栈(Java Virtual Machine Stack),早期也叫Java栈，每个线程在创建时都会建立一个虚拟机栈，其内部保存一个个栈帧(Stack Frame)，对应着一次次的Java方法调用。

### 特点

- 线程私有的
- 生命周期与线程一致

### 作用

主管Java程序的运行，保存方法的局部变量(8种基本数据类型、对象的引用地址)、部分结果，并参与方法的调用和返回。

```
1. 局部变量 vs 成员变量(或属性)
2. 基本数据类型变量  vs  引用类型变量(类、数组、接口)
```

如图：

![image-20201205195734604](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201205195734604.png)

### 优点

- 栈是一种快速有效的分配存储方式，访问速度仅次于程序计数器
- JVM直接堆JAVA栈的操作只有两个：
  - 每个方法执行，伴随着进栈(入栈、压栈)
  - 执行结束后的出栈工作
- 对于栈来说不存在垃圾回收问题
  - GC （不存在）;  OOM（有） 

### 栈中的异常

Java虚拟机规范允许Java栈的大小是动态的或者是固定不变的。

- 固定大小的Java虚拟机栈

  每个线程的Java虚拟机栈容量可以在线程创建的时候独立锁定，如果线程请求分配的栈容量超过Java虚拟机栈的最大容量，Java虚拟机会抛出**StackOverflowError** 异常。

- 可动态扩展Java虚拟机栈

  在尝试扩展的时候无法申请到足够的内存，或者在创建新的线程时没有足够的内存去创建对应的虚拟机栈，那Java虚拟机将会抛出 **OutOfMemoryError**

### 设置栈大小

我们可以使用参数 -Xss 选项来设置线程的最大栈空间，栈的大小直接决定了函数调用的最大可达深度。

```java
/**
 * @Author : Auraros
 * @Description :
 * @Data : 2020-12-05 20:31
 * @Version : 1.0
 */
public class StackErrorTest {

    private static int count = 0;
    public static void main(String[] args) {
        System.out.println(count);
        count++;
        main(args);

    }
}

```

在没有设置大小的时候：

![image-20201205203342978](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201205203342978.png)

设置栈大小 -Xss 256k

![image-20201205203600385](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201205203600385.png)

结果如下：![image-20201205203630267](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201205203630267.png)



## 栈的存储单位

### 栈帧

#### 定义

- 每个线程都有自己的栈，栈中的数据都是以 **栈帧(Stack Frame)**的格式存在。
- 这个线程上正在执行的每个方法都各自对应一个栈帧
- 栈帧是一个内存块，是一个数据集，维系着方法执行过程中的各种数据信息。

#### 运行原理

在一条活动线程中，一个时间点上，只会有一个活动的栈帧。即只有当前正在执行的方法的栈帧(栈顶栈帧)是有效的，这个栈帧被称为**当前栈帧**(Current Frame) ，与当前栈帧相对应的方法就是**当前方法**(Current Method),定义这个方法的类就是 **当前类**(Current Class)

![image-20201205212621952](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201205212621952.png)

注意：

- 不同线程中的栈帧不能相互引用，即不同栈中的栈帧不能调用
- Java有两种返回函数的方式：
  - 正常的函数返回，使用return指令
  - 抛出异常，不管使用哪种方式，都会导致栈帧被弹出



#### 栈帧内部结构

每个栈帧中存储着：

- **局部变量表**(Local Variables)
- **操作数栈** (Operand Stack) 或者叫表达式栈
- **动态链接**(Dynamic Linking) 或指向运行时常量池的方法引用
- **方法返回地址**(Return Adress) 或方法正常退出或者异常推出的定义
- 一些附加信息

![image-20201205215129217](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201205215129217.png)

### 局部变量表

#### 定义

局部变量表也被称为局部变量数组或本地变量表

**存储数据类型**

- 定义为一个数字数组，主要用于存储方法参数和定义在方法体内的局部变量。（包括**各类基本数据类型**、**对象引用**(reference),以及returnAddress类型）

![image-20201206001735360](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201206001735360.png)

![image-20201206001747634](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201206001747634.png)



**安全性**

- 由于局部变量表是线程**独有**数据，不存在数据安全问题。

- 局部变量表所需的容量大小是编译期确定下来的，并保存在方法的Code属性的maxiumum local variable数据项，在方法运行期间是不会改变局部变量表大小的。

  ```
  你编译的时候就可以查看javap得到局部变量表的长度了
  ```



#### 字节码文件结构剖析

![image-20201206001735360](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201206001735360.png)

![image-20201206002753230](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201206002753230.png)

```
1. Name 是main方法
2. Decriptor 是输入参数和输出参数
3. Access flags 是访问表示
code下有两个文件
```

![image-20201206095540813](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201206095540813.png)

```
字节码指令
```

![image-20201206095735282](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201206095735282.png)

```
行号表：
Start PC 字节码指令行号
Line Number java代码的行号
```

![image-20201206095950774](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201206095950774.png)

```
局部变量表：
name:局部变量名字
drscriptor:表述 表示变量类型
index： 索引
start PC: 字节码指令行号
Length: 和start Pc组成作用域的范围   startPC ~ startPC + length
```



#### Slot

##### 定义

局部变量表，最基本的存储单元是Slot(变量槽)。参数的值存放总是在局部变量数组的index0开始的，到数组长度-1索引结束。

##### 存放标准

在局部变量表里面，32位以内的类型只占用一个slot(包括returnAdress类型)， 64位的类型(long 和 double) 占用两个slot

- byte、short、char 、float 在存储前被转换为int，boolean也被转换为int，0表示false，非0表示true
- long 和 double则占据两个Slot

##### 运行原理

- **建立索引**：JVM会为局部变量表中的每一个Slot都分配一个访问索引，通过这个索引可以成功访问到局部变量表中指定的局部变量。

- **复制变量**：当一个实例方法被调用的时候，它的方法参数和方法体内部定义的局部变量将会被**按照顺序被复制**到局部变量中的每一个Slot上

  > 如果需要访问局部变量表中一个64bit的局部变量时，只需要使用前一个索引即可

- **this存放**：如果当前帧是由构造方法或者实例方法(**静态方法除外**)创建的，那么对象的引用this将会被存放在index为0的slot处，其余参数按照参数表顺序继续排列

<img src="C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201206101844122.png" alt="image-20201206101844122" style="zoom:50%;" />

```java
public static void testStatic(){
    //不可以，因为this变量不存在在当前方法的局部变量表中
    //System.out.println(this.count);
}
```

##### 代码理解

![image-20201206102445311](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201206102445311.png)

![image-20201206102525172](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201206102525172.png)

```
非静态方法所以 this会反正该首位。
```

如果删掉 String info

![image-20201206102626358](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201206102626358.png)

![image-20201206102643900](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201206102643900.png)

```
变成了三个变量
```

![image-20201206102848517](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201206102848517.png)

![image-20201206102911097](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201206102911097.png)

```
因为weight占两个字节，所以索引+2
```

##### 重复利用

栈帧中的局部变量表中的槽位可以重用，如果一个局部变量表过了其作用域，那么在其作用域之后申明的新的局部变量就很有可能会复用过期的局部变量的槽位，从而达到节省资源的目的。

<img src="C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201206103549612.png" alt="image-20201206103549612" style="zoom:70%;" />

![image-20201206103628744](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201206103628744.png)

```
b start PC 索引 为 4  ，结束为 8
这个时候就进行重复利用，使用之前已经销毁的 b的slot位置
```

**静态变量与局部变量的对比**

```
变量的分类:
按照数据类型分:   
	1. 基本数据类型  
	2.引用数据类
按照类中申明位置:  
	1. 成员变量:使用之前，都经过默认初始化赋值 
		(1)类变量 : linking的prepare阶段:给类变量默认赋值 ---> initial阶段: 给类变量显式赋值即静态代码块赋值
		(2)实例变量static : 随着对象的创建，会在堆空间中分配实例变量空间，并进行默认赋值
	2.局部变量: 在使用前，必须要进行显式赋值，否则编译不通过
```

```java
public void testTemp(){
	int num;
    System.out.println(num);//报错，变量num未进行初始化
}
```

##### 其他

- 在栈帧中，与性能调优关系最为密切的部分就是前面提到的局部变量表，在方法执行时，虚拟机使用局部变量表完成方法的传递
- 局部变量表中的变量也是重要的垃圾回收根节点，只要被局部变量表中直接或间解引用的对象都**不会**被回收。



### 操作数栈

#### 定义

每个独立的栈帧中除了包含局部变量表以外，还包含一个后进先出(LIFI)的操作数栈，也可以称为表达式栈(Expression Stack)。

**操作数栈** 主要用于保存计算过程的中间结果，同时作为计算机过程中变量临时的存储空间。

在方法执行过程中，根据字节码指令，往栈中写入数据或提取数据，即入栈(push) / 出栈 (pop)

- 某些字节码指令将值压入操作数栈，其余的字节码指令将操作数取出栈，使用它们后再把结果压入栈
- 比如: 执行复制、交换、求和等操作

![image-20201206110648389](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201206110648389.png)

#### 运行原理

- **创建**：操作数栈就是JVM执行引擎的一个工作区，当一个方法刚开始执行的时候，一个新的栈帧也会被随着创建出来，这个方法的操作数栈是空的。
- **长度**：每个操作数栈都会拥有一个明确的栈深度用于存储数值，其所需的最大深度在**编译期**就定义好了，保存在方法的Code属性中，为max_stack的值
- **存储**：栈中任何一个元素都是可以任意的Java数据类型
  - 32 bit 类型数据占用一个栈单位深度
  - 64 bit 类型数据占用两个栈单位深度
- **返回值**：如果被调用的方法带有返回值的话，其返回值将会被压入当前栈帧的操作数栈中，并更新PC寄存器中下一条执行的字节码指令
- **检验**：操作数栈中元素的数据类型必须与字节码指令的序列严格匹配，这由编译器在编译器期间进行验证，同时在类加载过程中类检验阶段的数据分析阶段再次检验

#### 代码解析

![image-20201206113710458](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201206113710458.png)

![image-20201206113909602](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201206113909602.png)

每部解释如下：
![image-20201206114144015](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201206114144015.png)

![image-20201206114213934](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201206114213934.png)

![image-20201206114326801](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201206114326801.png)

![image-20201206114409654](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201206114409654.png)

![image-20201206114452801](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201206114452801.png)

![image-20201206114510445](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201206114510445.png)

![image-20201206114555532](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201206114555532.png)

![image-20201206114536893](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201206114536893.png)

**面试题：i++和++i的区别是什么**

代码：

```java
public void add(){
	//第1类问题
    int i1 = 10;
    i1++;
    
    int i2 = 10;
    ++i2;
    
    //第二类问题
    int i3 = 10;
    i4 = i3++;
    
    int i5 = 10;
    int 16 = ++i5;
    
    //第3类问题
    int i7 = 10;
    i7 = i7++;
    
    int i8 = 10;
    i8 = ++i8;
    
    //第四类
    int i9 = 10;
    int i10  = i9++ + ++i9;
}
```

第一个问题:

![image-20201206120930645](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201206120930645.png)

## 栈顶缓存技术

### 背景

基于栈式架构的虚拟机所使用的零地址指令更加紧凑，但完成一项操作的时候必然需要使用更多的入栈和出栈指令，这同时也就意味着需要更多的指令分派(instruction dispath) 次数和内存 读/写次数

由于操作数是存储在内存中的，因此频繁地执行内存读写操作必然会影响执行速度，为了解决这个问题，HotSpot JVM的设计者们提出了栈顶缓存(Tos， Tof-of-Stack dispatch) 技术

### 定义

**栈顶缓存技术：**将栈顶元素全部缓存在物理CPU的寄存器中，以此此降低对内存的读写次数，提升执行引擎的执行效率。



### 优点

- 指令更少
- 执行速度快


## 动态链接

### 定义

**动态链接**：每个栈帧内部都包含一个指向 **运行时常量池**中该栈帧所属方法的引用。包含这个引用的目的就是炜乐支持当前方法的代码能够实现**动态链接(Dynamic Linking）**比如：invokedynamic指令

### 运行原理

在Java源文件被编译到字节码文件中时，所有的变量和方法都作为符号引用(Symbolic Reference)保存在class文件的常量池里。比如: 描述一个方法调用了另外的其他方法时，就是通过常量池中指向方法的符号引用来表示，那么**动态链接的作用就是为了将这些符号引用转换为调用方法的直接引用**.

### 代码解析

![image-20201207130126775](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201207130126775.png)

![image-20201207170337750](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201207170337750.png)

```
橙色为符号引用，指向常量池的链接
```

![image-20201207163941447](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201207163941447.png)

### 面试题

**为什么要设置常量池？**

常量池的作用，就是为了提供一些符号和常量，便于指令的识别。



## 方法调用

### 链接

在JVM中，将**符号引用**转换为调用方法的**直接引用**与方法的绑定机制相关。

#### 定义

##### 静态链接

当一个字节码文件被装载进JVM内部时，如果**被调用的目标在编译期可知**，且运行期保持不变时，这种情况下将调用方法的符号引用转换为直接引用的过程称之为静态链接。

##### 动态链接

如果被调用的方法在**编译期无法被确定**下来，只能够在程序运行期将调用方法的符号引用转换为直接引用，由于这种转换过程具备动态性，因此也就被称为动态链接。



**直接引用**

![image-20201207170541448](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201207170541448.png)

**符号引用**

![image-20201207170337750](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201207170337750.png)



### 绑定

#### 定义

对应的方法的绑定机制为:早期绑定(Early Binding) 和 晚期绑定(Late Binding) 。 **绑定是一个字段、方法或者类在符号引用被替换为直接引用的过程，者仅仅发生一次。**

##### 早期绑定

指被调用的目标方法如果在编译期可知，且运行期保持不变时，即可将这个方法与所属的类型进行绑定，这样一来，由于明确了被调用的目的方法究竟是哪一个，因此也就可以使用静态链接的方式将符号引用转换为直接引用。

<img src="C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201207173024456.png" alt="image-20201207173024456" style="zoom:67%;" />

**晚期绑定**

如果被调用的方法在编译期无法被确定下来，只能够在程序运行期根据实际的类型绑定相关的方法，这种绑定方法也就被称为晚期绑定。

<img src="C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201207173117085.png" alt="image-20201207173117085" style="zoom:67%;" />

### 虚方法与非虚方法

#### 定义

**非虚方法**

如果方法在编译期就确定了具体的调用版本，这个版本在运行时是不可变的，这样的方法叫做非虚方法。

静态方法、私有方法、final方法、实例构造器、父类方法都是非虚方法

**虚方法**

其他方法都是虚方法

复习：

子类对象的多态性的使用前提：1. 类的继承  2. 方法的重写

#### 虚方法表

在面向对象的编程中，会很频繁的使用到动态分派，如果在每次动态分派的过程都要重新在类的方法元数据中搜索合适的目标的话可能影响执行效率。为了提高性能， **在类的方法区建立一个虚方法表(virtual method table)**非虚方法不会出现在表中 来实现。使用索引表来代替查找。

每个类中都有一个虚方法表，表中存放着各个方法的实际入口。

**虚方法什么时候被创建？**

虚方法表会在类加载的链接阶段被创建并开始初始化，类的变量初始值准备完成之后，JVM会把该类的方法表也初始化完毕。

![image-20201207201715575](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201207201715575.png)

**代码例子**

<img src="C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201207202104235.png" alt="image-20201207202104235" style="zoom:50%;" />



<img src="C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201207202321706.png" alt="image-20201207202321706" style="zoom:50%;" />



dog的虚方法表

<img src="C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201207202450169.png" alt="image-20201207202450169" style="zoom:67%;" />

可卡犬的虚方法表

<img src="C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201207202707876.png" alt="image-20201207202707876" style="zoom:67%;" />

猫的虚方法表

<img src="C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201207202744381.png" alt="image-20201207202744381" style="zoom:67%;" />

### 方法调用指令

#### 普通调用指令

1.  **invokestatic**： 调用静态方法，解析阶段确定唯一方法版本
2.  **invokespecial**：调用< init > 方法、私有及父类方法，解析阶段确定唯一方法版本
3. invokevirtual: 调用所有虚方法(包括 final)
4. invokeinterface： 调用接口方法

#### 动态调用指令

5. invokedynamic: 动态解析出需要调用的方法，然后执行

前四条指令固化在虚拟机内部，方法的调用执行不可人为干预，而invokedynamic指令则支持由用户确定方法版本，**其中invokestatic指令和invokespecial指令调用的方法称为非虚方法，其余的为虚方法。**

#### invokedynamic指令

##### 定义

是Java为了实现  【动态类型语言】支持而做的一种改进。

lambda表达式的出现，擦有了invokedynamic的指令生成。

##### 动态语言和静态语言

```
静态：
Java: String info = "lele"; //info = 123 报错
动态：
JS: var name = "lele"; //var name = 10; 不报错
Python: info = 130.5;  //info = 'name';
```

**区别**

在于对类型的检查是在编译期还是在运行期，满足前者就是静态类型语言，反之就是动态类型语言。

说白一点： **静态类型语言就是判断变量自身的类型信息；**

​					**动态类型语言是判断变量值的类型信息**

变量没有类型信息，变量值才有类型信息。



#### 方法重写的本质

##### 重写的本质

1. 找到操作数栈顶的第一元素所执行的对象的实际类型，记做 C
2. 如果类型C中找到常量中的描述符合简单名称都相符的方法，则进行访问权限校验，如果通过则返回这个方法的直接引用，查找过程结束；如果不通过，则返回 java.lang.IllagalAccessError 异常。
3.  否则，按照继承关系从下往上一次对c的各个父类进行第2步的搜索和验证过程
4. 如果始终没有找到合适的方法，则抛出 java.lang.AbstactMethodError异常



## 方法返回地址

### 定义

一个方法的结束，有两种方式：

- 正常执行完成
- 出现未处理的异常，非正常退出

无论通过哪种方式退出，在方法退出后都返回到该方法被调用的位置，方法无法正常退出时，调用者的pc计数器的值作为返回地址，即调用该方法的指令的下一条指令的地址。而通过异常退出的，返回地址是要通过异常表来确定的，栈帧中一般不会保存这部分信息。

### 方法返回指令

#### 正常返回

- ireturn (当前返回值是boolean、byte、char、short和int类型时使用) 

- lreturn、freturn、dreturn以及areturn，
- return指令提供声明为void的方法，实例初始化方法、类和接口的初始化方法。

#### 异常返回

方法执行出现异常，并且异常没有方法进行处理。

方法执行过程抛出异常时的异常处理，存储在一个异常处理表，方便在发生异常的时候找到异常处理的代码

![image-20201207212259718](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201207212259718.png)

## 一些附加信息

栈帧中还允许携带与Java虚拟机实现相关的一些附加信息。例如：对程序调试提供支持的信息。



## 面试问题

- 举例栈溢出的情况? （StackOverflowError)
  - 通过 -Xss设置栈的大小； OOM
- 调整栈大小，就能保证不出现溢出吗？
  - 不能
- 分配的栈内存越大越好吗？
  - 不是
- 垃圾回收是否会涉及到虚拟机栈？
  - 不会
- 方法中定义的局部变量是否线程安全？
  - 具体问题具体分析

### 线程安全

如果只有一个线程才可以操作此数据，则必是线程安全的。

如果有多个线程可操作此数据，则此数据是共享数据，如果不考虑同步机制的话，会存在线程安全问题。

```java
public static void method1(){
    
    // StringBuilder:线程安全
    StringBuilder s1 = new StringBuilder();
    s1.append("a");
    s1.append("b");
    
}

public static void method2(StringBuilder sBuilder){
    //sBuilder是线程不安全的
    sBuilder.append("a");
    sBuilder.append("b");
}

public static StringBuilder method3(){
    //线程不安全
	StringBuilder s1 = new StringBuilder();
    s1.append("a");
    s1.append("b");
    return s1;
}
```

