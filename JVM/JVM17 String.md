# JVM17 String



## 基本特性

- 字符串，使用一对""引起来表示
  - String s1 = "heallo world";  //字面量的定义方式
  - String s2 = new String("hello World")；//实例化方式

- 声明为final的，不可继承
- 实现了Serializable接口，表示：字符串是支持序列化的
- 实现了Comparable接口，表示：字符串是可以比较大小的
- **jdk8之前内部定义了final char[] value 用于存储字符串数据，jdk9时改为byte[]**



## 存储结构变更

**结论：**String 再也不用char[]来存储啦，改成了byte[]加上编码标记，节约了一些空间。

```java
public final class String implement java.io.Serializable,Comparable<String>,CharSequence{
    @Stable
    private final byte[] value;
}
```

那StringBuffer 和 StringBuilder是否无动于衷？

```
String-related classes such as AbstractStringBuilder, StringBuilder, and StringBuilder will be update to use the same representation,as will the HotSpot VM's intrinsic(固有的、内置)string operation.
```



## 定义

String： **代表不可变的字符序列。简称:不可变性**

- 当字符串重新赋值时，需要重写指定内存区域赋值，不能使用原有的value进行赋值
- 当对现有的字符串进行连接操作时，也需要重新指定内存区域赋值，不能使用原有的value进行赋值
- 当调用String的replace()方法修改指定或字符串，也需要重新指定内存区域赋值，不能使用原有的value进行赋值

**字面量的方式(区别于new)给一个字符串赋值，此时的字符串值声明在字符串常量池中。**

```java
public calss StringTest{
	
    @Test
    public void test1(){
		String s1 = "abc";  //字面量定义的方式，"abc存储在字符串常量池中的"
        String s2 = "abd"; //公用字符串常量池中的同一个
        s1 = "hello";  //重新造一个叫hello
        
        System.out.plintln(s1 == s2); //判断地址:false
        System.out.println(s1); 
        System.out.println(s2);
    }
    
    @Test
    public void test2(){
        String s1 = "abc";  //字面量定义的方式，"abc存储在字符串常量池中的"
        String s2 = "abd"; //公用字符串常量池中的同一个
        s2 += "def";       //重新创建"abddef"
        System.out.println(s1); 
        System.out.println(s2);
        
    }
    
    @Test
    public void test3(){
        String s1 = "abc";
        String s2 = s1.replace('a', 'm');
        System.out.println(s1); 
        System.out.println(s2);
    }
}
```

练习题

```java
public class StringExer{

    String str = new String("good");
    char[] ch = {'t', 'e', 's', 't'};
    
    public void change(String str, char ch[]){
        str = "test ok";
        ch[0] = 'b'
    }
    
    public static void main(String[] args){
        StringExer ex = new StringExer();
        ex.change(ex.str, ex.ch);
        System.out.println(ex.str); //good
        System.out.println(ex.ch); //best
    }
}
```



## 字符串常量池

**字符串常量池中是不会存储相同的内容的字符串的。**

- String的String Pool是一个固定大小的Hashtable，默认值大小长度是1009.如果放进String Pool的String非常多，就会造成Hash冲突验证，从而导致链表会很长，而链表长了之后直接会造成的影响就是当前调用String.intern时性能会大幅下降
- 使用-XX:StringTableSize可设置StringTable长度



## String的内存分配

- 在Java语言中有8种基本数据类型和一种比较特殊的String。这些类型是为了使她们在运行过程中速度更快、更节省内存，都提供了一种常量池的概念。

- 常量池类似一个Java系统级别提供的缓存。8种基本数据类型的常量池都是系统协调的。String类型的常量池比较特殊，它的方法有两种：

  - 直接使用双引号声明出来的String对象会直接存储在常量池中

    比如: String info = "hello world"

  - 如果不是用双引号声明的String对象，可以使用S停提供的intern（）方法

![image-20201212171523188](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201212171523188.png)

**为什么JDK7 StringTable要调整？**

1. PermSize默认比比较小
2. 永久代垃圾回收频率低



## String 的基本操作

<img src="C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201212171913938.png" alt="image-20201212171913938" style="zoom:67%;" />

Java语言规范里要求完全相同的字符串字面量，应该包含同样的Unicode字符序列(包含同一份码点序列的常量)，并且必须是指向同一个String类实例。

![image-20201212172101319](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201212172101319.png)

![image-20201212172809379](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201212172809379.png)



## 字符串拼接操作

- 常量与常量的拼接结果在常量池，原理是编译期优化
- 常量池中不会存在相同内容的常量
- 只要其中有一个是变量，结果就在堆中(不是常量池中的堆)。变量拼接的原理是StringBuilder
- 如果拼接的结果调用intern()方法，则主动将常量池中还没有的字符串对象放入池中，并返回此对象地址

代码详解:

```java
import org.junit.Test;

/**
 * @Author : Auraros
 * @Description :
 * @Data : 2020-12-12 17:47
 * @Version : 1.0
 */
public class StringTest {

    @Test
    public void test1(){

        String s1 = "a" + "b" + "c";
        String s2 = "abc";  //一定是放在字符串常量池中的，将此地址赋值给s2
        /**
         * 最终， Java编译成.class，再执行class
         * String s1 = "abc";
         * String s2 = "abc";
         */
        System.out.println(s1 == s2);       //true
        System.out.println(s1.equals(s2));  //true
    }

    /**反编译结果
     * @Test
     *     public void test1() {
     *         String s1 = "abc";
     *         String s2 = "abc";
     *         System.out.println(s1 == s2);
     *         System.out.println(s1.equals(s2));
     *     }
     */

    @Test
    public void test2(){

        String s1 = "javaEE";
        String s2 = "hadoop";

        String s3 = "javaEEhadoop";
        String s4 = "javaEE" + "hadoop";  //编译期优化
        //如果拼接符号前后出现了变量，则需要在堆空间中去new String(),具体内容为拼接以后的结果
        String s5 = s1 + "hadoop";
        String s6 = "javaEE" + s2;
        String s7 = s1 + s2;

        System.out.println(s3 == s4); //true
        System.out.println(s3 == s5); //false
        System.out.println(s3 == s6); //false
        System.out.println(s3 == s7); //false
        System.out.println(s5 == s6); //false
        System.out.println(s5 == s7); //false
        System.out.println(s6 == s7); //false

        //intern(): 判断字符串常量池中是否存在javaEEhadoop值，
        //存在的则返回常量池中的地址，不存在则创建
        String s8 = s6.intern();
        System.out.println(s3 == s8); //true

    }

    /**
     *  @Test
     *     public void test2() {
     *         String s1 = "javaEE";
     *         String s2 = "hadoop";
     *         String s3 = "javaEEhadoop";
     *         String s4 = "javaEEhadoop";
     *         String s5 = s1 + "hadoop";
     *         String s6 = "javaEE" + s2;
     *         String s7 = s1 + s2;
     *         System.out.println(s3 == s4);
     *         System.out.println(s3 == s5);
     *         System.out.println(s3 == s6);
     *         System.out.println(s3 == s7);
     *         System.out.println(s5 == s6);
     *         System.out.println(s5 == s7);
     *         System.out.println(s6 == s7);
     *         String s8 = s6.intern();
     *         System.out.println(s3 == s8);
     */

    @Test
    public void test3(){

        String s1 = "a";
        String s2 = "b";
        String s3 = "ab";
        String s4 = s1 + s2;//
		/**
		 * 如下的s1 + s2的执行细节
		 * 1. StringBuilder s = new StringBuilder();
		 * 2. s.append("a");
		 * 3. s.append("b");
		 * 4. s.toString();  --> 类似于 new String("ab")
		 */ 
        System.out.println(s3 == s4);//false

    }
   

}

```

test4的字节码如下：

![image-20201212192805663](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201212192805663.png)

```
先new一个StringBuilder
初始化构造器
取出第一个值a
调用append加上
取出第二个值b
调用append加上
```

```java
	@Test
    public void test4(){
        final String s1 = "a"; //相当于已经是变量
        final String s2 = "b"; 
        String s3 = "ab";
        String s4 = s1 + s2;
        System.out.println(s3 == s4); //true

    }
```



### 查看执行效率

通过stringBuilder的append()的方式添加字符串的方式要远高于string字符串的拼接方式。因为:自始至终之创建了一个对象。

```java
@Test
    public void test6(){

        long start = System.currentTimeMillis();

//        method1(100000); //9022
        method2(100000); //8

        long end = System.currentTimeMillis();
        System.out.println((end - start));
    }

    public void method1(int hightLevel){
        String src = "";
        for (int i = 0; i < hightLevel; i++) {
            src = src + "a"; //每次循环都会创建一个stringbulider
        }
    }

    public  void method2(int hightLevel){
        StringBuilder src = new StringBuilder();
        for (int i = 0; i < hightLevel; i++) {
            src.append("a");
        }
    }
```

查看一下，第一个是效率特别低的。因为：

1. 拼接的方法每次都会创建一个StringBuilder还会创建一个String，所以有20000对象。第二种方法只有一个对象。

2. 内存中由于创建了较多的stringbuilder和string，有可能会引发GC。

改进的空间:

在实际开发中，如果基本确定要前前后后要添加的字符串不高于某个限定值highLevel，调用 StringBilder(highLevel):

```
public  void method2(int hightLevel){
        StringBuilder src = new StringBuilder(100000);
        for (int i = 0; i < hightLevel; i++) {
            src.append("a");
        }
    }
```



## intern函数

如果不是双引号声明的String对象，可以使用String提供的intern方法：intern方法会从字符串常量池中查询当前字符串是否存在，若不存在就会将当前字符串放入常量池中。

```
String myInfo = new String("I love hello world").intern();
```

也就是说，如果在任意字符串上调用String.intern方法，那么其返回结果所指向的那个类实例，必须和直接以常量形式出现的字符串实例完全相同。因此，下列表达式的值必定是true：

```
("a" + "b" + "c").intern() == "abc"
```

通俗点讲，Interned String就是确保字符串在内存里只有一份拷贝，这样可以节约内存空间，加快字符串操作任务的执行速度。注意，这个值会被放在字符串内部池(String Intern Pool).

### 使用

```java
public class StringIntern1{
    public static void main(String[] args){
     	String s = new String("1");
        s.intern(); //字符串常量池中已经存在"1"
        String s2 = "1";
        System.out.println(s == s2); //jdk6:false / jdk7/8:false
        
        String s3 = new String("1") + new String("1"); //s3变量记录地址为 new String("11")
        //执行完上一行的代码以后，字符串常量池中是否存在"11";
        //答案: 不存在
        s3.intern(); //在字符串常量池中生成"11" 。如何理解:jdk6：创建了一个新的对象"11",也就有新的地址。jdk7当中: 此时常量池中并没有创建"11",而是创建了一个执行堆空间中new String("11")的地址
        String s4 = "11"; //使用的是上一行代码执行时，常量池中生成的"11"的地址
        System.out.println(s3 == s4); //jdk6:false / jdk7/8:true
    }
}
```

<img src="C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201213001208468.png" alt="image-20201213001208468" style="zoom:67%;" />

<img src="C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201213001310848.png" alt="image-20201213001310848" style="zoom:67%;" />

练习:

```

```

### 执行效率空间

![image-20201213104840775](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201213104840775.png)

未使用intern:

![image-20201213105437895](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201213105437895.png)

使用intern:

![image-20201213105558840](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201213105558840.png)

### 总结

总结String 的 intern()的使用:

- jdk1.6：将这个字符串对象尝试放入串池中
  - 如果串池有，则不会放入，返回已有串池中的对象的地址
  - 如果没有，会把此对象复制一份，放入串池，并返回串池中的对象地址
- jdk1.7起: 将这个字符串对象尝试放入串池中
  - 如果串池有，则不会放入，返回已有串池中的对象的地址
  - 如果没有，**则会把对象的引用地址复制一份**，放入串池，并返回串池中的引用地址

对于程序中大量存在的字符串，尤其是存在很多重复字符串时，使用intern()可以节省内存空间。



## StringTable垃圾回收

```
/**
 * @Author : Auraros
 * @Description :
 * 命令行: -Xms15m -Xmx15m -XX:+PrintStringTableStatistics -XX:+PrintGCDetails
 * @Data : 2020-12-13 11:08
 * @Version : 1.0
 */
public class GCTest {

    public static void main(String[] args) {

        for (int i = 0; i < 100; i++) {
            String.valueOf(i).intern();
        }
        
    }
    
}


```

![image-20201213111529048](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201213111529048.png)



**问题一： new String("ab")会创建几个对象**

```java
public class StringNewTest {

    public static void main(String[] args) {
        String str = new String("ab");
    }
}
```

看字节码：

<img src="C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201212233736984.png" alt="image-20201212233736984" style="zoom:67%;" />

两个对象：

```
new #2 
invokespecial #4
```





**问题二：new String("a") + new String("b")呢**

```java
/**
 * @Author : Auraros
 * @Description :
 * @Data : 2020-12-12 23:33
 * @Version : 1.0
 */
public class StringNewTest {

    public static void main(String[] args) {
        //String str = new String("ab");
        String str = new String("a") + new String("b");
    }
}

```



<img src="C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201212234625647.png" alt="image-20201212234625647" style="zoom:67%;" />

```
对象1:  new StringBuilder()
对象2:  new String("a")
对象3:  常量池中的 "a"
对象4:  new String("b")
对象5:  常量池中的 "b"

深入剖析:
StringBuilder的toString方法：
对象6: new String("ab")
强调一下: toString()的调用，在字符串常量池中，没有生成“ab”
```



## G1在垃圾回收器

### 背景

对许多Java应用(有大的也有小的)做的测试得出一下结论：

- 堆存活数据集合里面String对象占了25%
- 堆存活数据集合里面重复的String对象有13.5%
- String的对象平均长度是45

许多大规模的Java应用的瓶颈在于内存。**String1.equals(string2)=true**堆上存在重复的String对象必然是一种内存的浪费。这个项目将G1垃圾收集器中实现自动持续堆重复的String对象进行去重，这样就能避免浪费内存。

### 步骤

![image-20201213124414680](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201213124414680.png)

### 命令行操作

```
UseStringDeduplication(bool):开启String去重，默认是不开启的，需要手动开启
PrintStringDeduplicationSatistics(bool)：打印详细的去重统计信息
StringDeduplicationAgeThreshold(uintx)：达到这个年龄的String对象被认为是去重的候选对象
```



## 问题

### 问题一

```
String a = "chenssy";
String b = "chenssy";
```

a、b和字面上的chenssy都是指向JVM字符串常量池中的"chennsy"对象，她们指向一个对象

```
String c = new String("chenssy");
```

new关键字一定会产生一个对象chenssy(这个chenssy与上面的chenssy不同)，同时这个对象是存储在堆中的。所以上面应该是产生了两个对象:保存在栈中的c和保存在堆中的chenssy。但是Java根本不存在两个完全一样的字符串对象。故堆中的chenssy应该是引用字符串常量池chenssy。所以c、chenssy、池chenssy的关系是c--> chenssy --> 池chenssy

<img src="C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201212132819570.png" alt="image-20201212132819570" style="zoom:50%;" />

**虽然a、b、c、chenssy是不同的对象，但是从String的内部结构我们是可以理解上面的。String c = new String("chenssy");虽然c的内容是创建在堆中，但是他的内部value还是指向JVM常量池的chenssy的value，它构造chenssy时所用的参数依然是chenssy字符串常量。**

练习一：

```java
public viod test{
   String   str1="aaa";
   String  str2="aaa";
   System.out.println(str1==str2); //True
}
```

分析：当执行String str1="aaa"时，JVM首先会去字符串池中查找是否存在"aaa"这个对象，如果不存在，则在字符串池中创建"aaa"这个对象，然后将池中"aaa"这个对象的引用地址返回给字符串常量str1，这样str1会指向池中"aaa"这个字符串对象；如果存在，则不创建任何对象，直接将池中"aaa"这个对象的地址返回，赋给字符串常量。当创建字符串对象str2时，字符串池中已经存在"aaa"这个对象，直接把对象"aaa"的引用地址返回给str2，这样str2指向了池中"aaa"这个对象，也就是说str1和str2指向了同一个对象，因此语句System.out.println(str1 == str2)输出：true。

练习二：

```java
public void test2(){
    String str3=new String("aaa");
    String str4=new String("aaa");
    System.out.println("===========test2============");
    System.out.println(str3==str4);//false 可以看出用new的方式是生成不同的对象 
}
```

分析： 采用new关键字新建一个字符串对象时，JVM首先在字符串池中查找有没有"aaa"这个字符串对象，如果有，则不在池中再去创建"aaa"这个对象了，直接在堆中创建一个"aaa"字符串对象，然后将堆中的这个"aaa"对象的地址返回赋给引用str3，这样，str3就指向了堆中创建的这个"aaa"字符串对象；如果没有，则首先在字符串池中创建一个"aaa"字符串对象，然后再在堆中创建一个"aaa"字符串对象，然后将堆中这个"aaa"字符串对象的地址返回赋给str3引用，这样，str3指向了堆中创建的这个"aaa"字符串对象。当执行String str4=new String("aaa")时， 因为采用new关键字创建对象时，每次new出来的都是一个新的对象，也即是说引用str3和str4指向的是两个不同的对象，因此语句System.out.println(str3 == str4)输出：false。

练习四：

```java

/**
 * 编译期确定
 */
public void test3(){
    String s0="helloworld";
    String s1="helloworld";
    String s2="hello"+"world";
    System.out.println("===========test3============");
    System.out.println(s0==s1); //true 可以看出s0跟s1是指向同一个对象 
    System.out.println(s0==s2); //true 可以看出s0跟s2是指向同一个对象 
}
```

分析：因为例子中的s0和s1中的"helloworld”都是字符串常量，它们在编译期就被确定了，所以s0==s1为true；而"hello”和"world”也都是字符串常量，当一个字符串由多个字符串常量连接而成时，它自己肯定也是字符串常量，所以s2也同样在编译期就被解析为一个字符串常量，所以s2也是常量池中"helloworld”的一个引用。所以我们得出s0==s1==s2。



练习五：

```
/**
 * 编译期无法确定
 */
public void test4(){
    String s0="helloworld"; 
    String s1=new String("helloworld"); 
    String s2="hello" + new String("world"); 
    System.out.println("===========test4============");
    System.out.println( s0==s1 ); //false  
    System.out.println( s0==s2 ); //false 
    System.out.println( s1==s2 ); //false
}
```

分析：用new String() 创建的字符串不是常量，不能在编译期就确定，所以new String() 创建的字符串不放入常量池中，它们有自己的地址空间

s0还是常量池中"helloworld”的引用，s1因为无法在编译期确定，所以是运行时创建的新对象"helloworld”的引用，s2因为有后半部分new String(”world”)所以也无法在编译期确定，所以也是一个新创建对象"helloworld”的引用。



练习六:

```

/*
 * 编译期无法确定
 */
public void test7(){
    String s0 = "ab"; 
    String s1 = "b"; 
    String s2 = "a" + s1; 
    System.out.println("===========test7============");
    System.out.println((s0 == s2)); //result = false
}
```

JVM对于字符串引用，由于在字符串的"+"连接中，有字符串引用存在，而引用的值在程序编译期是无法确定的，即"a" + s1无法被编译器优化，只有在程序运行期来动态分配并将连接后的新地址赋给s2。所以上面程序的结果也就为false。

附加:

```kava
public void test9(){
    String s0 = "ab"; 
    final String s1 = "b"; 
    String s2 = "a" + s1;  
    System.out.println("===========test9============");
    System.out.println((s0 == s2)); //result = true
}
```

分析：和例子7中唯一不同的是s1字符串加了final修饰，对于final修饰的变量，它在编译时被解析为常量值的一个本地拷贝存储到自己的常量池中或嵌入到它的字节码流中。所以此时的"a" + s1和"a" + "b"效果是一样的。故上面程序的结果为true。

```
/**
 * 编译期无法确定
 */
public void test10(){
    String s0 = "ab"; 
    final String s1 = getS1(); 
    String s2 = "a" + s1; 
    System.out.println("===========test10============");
    System.out.println((s0 == s2)); //result = false 
    
}
 
private static String getS1() {  
    return "b";   
}
```

这里面虽然将s1用final修饰了，但是由于其赋值是通过方法调用返回的，那么它的值只能在运行期间确定，因此s0和s2指向的不是同一个对象，故上面程序的结果为false。



问题六

```

/**
 * 继续-编译期无法确定
 */
public void test5(){
    String str1="abc";   
    String str2="def";   
    String str3=str1+str2;
    System.out.println("===========test5============");
    System.out.println(str3=="abcdef"); //false
```

分析：因为str3指向堆中的"abcdef"对象，而"abcdef"是字符串池中的对象，所以结果为false。JVM对String str="abc"对象放在常量池中是在编译时做的，而String str3=str1+str2是在运行时刻才能知道的。new对象也是在运行时才做的。而这段代码总共创建了5个对象，字符串池中两个、堆中三个。+运算符会在堆中建立来两个String对象，这两个对象的值分别是"abc"和"def"，也就是说从字符串池中复制这两个值，然后在堆中创建两个对象，然后再建立对象str3,然后将"abcdef"的堆地址赋给str3。

步骤： 
1)栈中开辟一块中间存放引用str1，str1指向池中String常量"abc"。 

2)栈中开辟一块中间存放引用str2，str2指向池中String常量"def"。 

3)栈中开辟一块中间存放引用str3。

4)str1 + str2通过StringBuilder的最后一步toString()方法还原一个新的String对象"abcdef"，因此堆中开辟一块空间存放此对象。

5)引用str3指向堆中(str1 + str2)所还原的新String对象。 

6)str3指向的对象在堆中，而常量"abcdef"在池中，输出为false