# JVM5 类加载器



![image-20201201224511793](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201201224511793.png)

主要分为三个部分：

> - 加载阶段(Loading)：
>   - 引导类加载器(BootStrap ClassLoader)
>   - 扩展类加载器(Extension ClassLoader)
>   - 系统类加载器(Application ClassLoader)
>   - 自定义加载器
> - 连接阶段(Linking)
>   - 验证(Verify)
>   - 准备(Prepare)
>   - 解析(Resolve)
> - 初始化阶段(Initialization)
>   - 初始化



### 示例1

![image-20201201225102931](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201201225102931.png)

- 首先编写代码

  ```java
  package com.auraros.java;
  
  /**
   * @Author : Auraros
   * @Description :
   * @Data : 2020-12-01 22:52
   * @Version : 1.0
   */
  public class HelloLoader {
  
      public static void main(String[] args) {
          System.out.println("谢谢ClassLoader加载我");
          System.out.println("下辈子还");
      }
  }
  ```

- 运行过程

  ```
  if 先判断类HelloLoader是否已经被加载:
  case no: ClassLoader进行加载然后进行链接，如果此时有异常则抛出异常
  case yes: 进行链接
  初始化HelloLoader
  调用HelloLoader.main()
  ```




## ClassLoader

ClassLoader类，它是一个抽象类，其后所有的类加载器都继承自ClassLoader(不包括启动类加载器)

| 方法名称                                             | 描述                                                         |
| ---------------------------------------------------- | ------------------------------------------------------------ |
| getParent()                                          | 返回该类加载的超类加载器                                     |
| loadClass(String name)                               | 加载名称为name的类，返回结果为java.lang.Class类的实例        |
| findClass(String name)                               | 查找名为name的类，返回结果为java.lang.Class类的实例          |
| findLoadedClass(String name)                         | 查找名为name的已经被加载过的类，返回结果为java.lang.Class类的实例 |
| defineClass(String name, byte[] b, int off, int len) | 把字节数组b中的内容转换为一个Java类，返回结果为java.lang.Class类的实例 |
| resolveClass(Class<?> e)                             | 连接指定的一个Java类                                         |

**获取ClassLoader的途径**

- 方式一：获取当前类的ClassLoader

  ```
  clazz.getClassLoader()
  ```

- 方式二：获取当前线程上下文的ClassLoader

  ```
  Thread.currentThread().getContextClassLoader()
  ```

- 方式三：获取当前系统的ClassLoader

  ```
  ClassLoader.getSystemClassLoader()
  ```

- 方式四：获取调用者的ClassLoader

  ```
  DriverManager.getCallerClassLoader()
  ```

  



### 类加载过程一：Loading

```
加载阶段(Loading)的加载器有：
- 引导类加载器(BootStrap ClassLoader)
- 扩展类加载器(Extension ClassLoader)
- 系统类加载器(Application ClassLoader)
加载：
1. 通过一个类的全限定名获取定义此类的二进制字节流
2. 将这个字节流所代表的静态存储结构转化为方法区的运行时数据结构
3. 在内存中生成一个代表这个类的java.lang.Class对象，作为方法区这个类的各种数据的访问入口
```

**加载.class文件的方式**

- 从本地系统中直接加载
- 通过网络获取，典型场景：Web Applet
- 从zip压缩包中读取，称为日后jar、war格式的基础
- 运行时计算生成，使用最多的是：动态代理技术
- 由其他文件生成，典型场景：JSP应用
- 从专有数据库中提取.class文件，比较少见
- 从加密文件中获取，防止被反编译



> - JVM支持两种类型的类加载器，分别为引导类加载器(Bootstrap ClassLoader) 和 自定义类加载器 (User-Defined ClassLoader).
>
> - 再Java虚拟机规范将所有派生于抽象类的ClassLoader的类加载器都划分为自定义类加载器。





使用快捷键 ctrl + N 和 crtl + H 可以看到继承关系

![image-20201202200204148](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201202200204148.png)



```java
package com.auraros.java;

/**
 * @Author : Auraros
 * @Description :
 * @Data : 2020-12-02 20:11
 * @Version : 1.0
 */
public class ClassLoaderTest {

    public static void main(String[] args) {

        //获取系统类加载器
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        System.out.println(systemClassLoader); //sun.misc.Launcher$AppClassLoader@18b4aac2

        //获取其上层，扩展类加载器
        ClassLoader systemClassLoaderParent = systemClassLoader.getParent();
        System.out.println(systemClassLoaderParent); //sun.misc.Launcher$ExtClassLoader@1b6d3586

        //获取其上层,试图获取 BootstrapClassLoader ,获取不到引导类加载器
        ClassLoader parentParent = systemClassLoaderParent.getParent();
        System.out.println(parentParent); // null

        //对于用户自定义类加载器是谁 , 系统类加载器加载
        ClassLoader classLoader = ClassLoaderTest.class.getClassLoader();
        System.out.println(classLoader); //sun.misc.Launcher$AppClassLoader@18b4aac2

        //String类是引导类加载器进行加载。--> java的核心类库都是使用引导类加载器
        ClassLoader classLoader1 = String.class.getClassLoader();
        System.out.println(classLoader1); //null
    }

}

```

**启动类加载器（引导类加载器， Bootstrap ClassLoader）**

```
1. 这个类加载使用 C/C++语言实现的，嵌套在JVM内部
2. 它用来加载Java的核心库(JAVA_HOME/jre/lib/lib/rt.jar、resources.jar或者sun.boot.class.path路径下的内容)，用于提供JVM自身需要的类
3. 并不继承自 java.lang.ClassLoader，没有父加载器
4. 加载扩展类和应用程序类加载器，并指定为他们的父类加载器
5. 出于安全考虑，Bootstrap启动只加载类加载器包名为java、javax、sun等开头的类
```

**扩展类加载器**（Extension ClassLoader）

```
1. Java语言编写，由sun.misc.Launcher$ExtClassLoader实现
2. 派生于ClassLoader类
3. 父类加载器为启动类加载器
4. 从java.ext.dirs系统数学所指定的目录中加载类库，或从JDK的安装目录jre/lib/ext子目录下载库类。如果用于放于这个目录下，也会由该加载
```

**应用程序类加载器(系统类加载器， AppClassLoader)**

```
1. java语言编写，由sun.misc.Launcher$AppClassLoader实现
2. 派生于ClassLoader类
3. 父类加载器为扩展类加载器
4. 它负责加载环境变量classpath或系统属性 java.class.path 指定路径下的类库
5. 该类加载是程序中默认的类加载器，一般来说，Java应用类都是由它完成的
6. 通过ClassLoader#getSystemClassLoader() 方法可以得到该类加载器
```

演示代码：

```java
package com.auraros.java;

import sun.security.ec.CurveDB;

import java.net.URL;
import java.security.Provider;
import java.util.Properties;

/**
 * @Author : Auraros
 * @Description :
 * @Data : 2020-12-02 20:47
 * @Version : 1.0
 */
public class ClassLoaderTest1 {

    public static void main(String[] args) {

        System.out.println("*******启动类加载器*******");
        //获取BootstrapClassLoader能够加载的api的路径
        URL[] urls = sun.misc.Launcher.getBootstrapClassPath().getURLs();
        for (URL url : urls) {
            System.out.println(url.toExternalForm());
        }

        //从上面的路径中随意选择一个类，来看看他加载 的是什么类
        ClassLoader classLoader = Provider.class.getClassLoader();
        System.out.println(classLoader);//null的引导类加载器



        System.out.println("********扩展类加载器********");
        String properties = System.getProperty("java.ext.dirs");
        for (String path : properties.split(";")) {
            System.out.println(path);
        }

        //从上面的路径中随意选择一个类，来看看他加载 的是什么类
        ClassLoader classLoader1 = CurveDB.class.getClassLoader();
        System.out.println(classLoader1); //sun.misc.Launcher$ExtClassLoader@77459877

    }


}

```

结果：

![image-20201202210337358](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201202210337358.png)



**用户自定义类加载器**

在Java的日常应用程序开发中，类的加载几乎是由上述3种类加载器相互配合执行的，在必要的时候，我们可以自定义加载器类，来定制类的加载方式

**为什么要自定义类加载器？**

- 隔离加载类
- 修改类加载的方式
- 扩展加载源
- 防止源码泄露



### 类加载过程二： Linking

**验证(Verify)**

- 目的在于确保Class文件的字节流中包含信息符合当前虚拟机要求，保证被加载类的正确性，不会危害虚拟机自身安全
- 主要包括四种验证，文件格式验证，元数据验证，字节码验证，符号引用验证



例子：

字节码文件开头的标识：

![image-20201202003256913](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201202003256913.png)



**转备(Prepare)**

- 为类变量分配内存并且设置该类变量的默认初始值，即零值
- 这里不包含final修饰的static，因为final在编译的时候就会分配了，准备阶段会显式初始化
- 这里不会为实例变量分配初始值，类变量会分配在方法区中，而实例变量是会随着对象一起分配到Java堆中

```
private static int a = 1; //prepare: a = 0 ---> initail : a = 1

如果是
final int a = 1; //编译直接是1
```



**解析(Redolve)**

- 将常量池内的符号引用转换为直接引用的过程
- 事实上，解析操作往往会伴随着JVM在执行完初始化后再执行
- 符号引用就是一组符号来描述所引用的目标。符号引用的字面量形式明确定义在《Java虚拟机规范》的Class文件格式中。直接引用就是直接向目标的指针、针对偏移量或一个间接定位到目标的句柄
- 解析动作主要针对类或接口、字段、类方法、接口方法、方法类型等。

![image-20201202003054601](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201202003054601.png)



**初始化**

- 初始化阶段就是执行类构造器方法< clinit >() 的过程 （不需要定义）
- 此方法不需定义，是javac编译器自动收集类中所有类变量的赋值动作和静态代码中的语句合并而来
- 构造器方法中的指令按语句在源文件出现的顺序执行
- 如果该类有一个父类，会先把父类的< clinit>先加载



**例子1：static 加载**

```java
package com.auraros.java;

/**
 * @Author : Auraros
 * @Description :
 * @Data : 2020-12-01 22:52
 * @Version : 1.0
 */
public class HelloLoader {

    private static int num = 1;

    static {
        num = 2;
        number = 20;
    }

    private static int number = 10; //linking之prepare:number = 0 -----> initial: 20 --> 10;

    public static void main(String[] args) {
        System.out.println(number);
    }


}
```

自动生成的 < clinit > 类

![image-20201202193106184](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201202193106184.png)

字节码文件如下：

![image-20201202193029050](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201202193029050.png)

**例子2：非static加载**

```java
package com.auraros.java;

/**
 * @Author : Auraros
 * @Description :
 * @Data : 2020-12-01 22:52
 * @Version : 1.0
 */
public class HelloLoader {

    private static int num = 1;

    static {
        num = 2;
        number = 20;
    }
    private int a = 3;
    private static int b = 4;

    private static int number = 10; //linking之prepare:number = 0 -----> initial: 20 --> 10;

    public static void main(String[] args) {
        System.out.println(number);
    }


}
```

![image-20201202193246925](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201202193246925.png)

经过观察可以发现，只有 static 才会被 该类加载。

![image-20201202193659333](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201202193659333.png)



**例子3：父类加载**

```java
package com.auraros.java;

/**
 * @Author : Auraros
 * @Description :
 * @Data : 2020-12-01 22:52
 * @Version : 1.0
 */
public class HelloLoader {

    static class Futher{
        public static int a = 1;

        static{
            a = 2;
        }

    }

    static class son extends Futher{

        public  static int  b = a;
    }

    public static void main(String[] args) {
        //最先加载object类，然后HelloLoader类
        //先加载Futher,再son类
        System.out.println(son.b); //2
    }
}
```



### 结构

<img src="C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201202193820206.png" alt="image-20201202193820206" style="zoom:67%;" />

```
1. 任何一个类声明以后，内部至少存在一个类的构造器
对应就是字节码文件中的 <init>
```

