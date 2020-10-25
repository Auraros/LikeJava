# Java基础10 方法重写

## 区别

**方法重载**

在同一个类中,出现多个同名的方法,参数列表不同,与返回值类型,修饰符无关

**方法重写**

子类中出现和父类中一模一样的方法(包括返回值类型,方法名,参数列表)



## 重写

**注意事项：**

- 重写的方法必须要和父类一模一样(包括返回值类型,方法名,参数列表)
- 重写的方法可以使用@Override注解来标识
- 子类中重写的方法的访问权限不能低于父类中方法的访问权限

权限修饰符： private < 默认 < protected < public



**为什么重写方法**

- 当父类中的方法无法满足子类需求的时候,需要方法重写

- 当子类具有特有的功能的时候,就需要方法重写



**静态方法与非静态方法重写的区别**

- 在Java中静态方法可以被继承，但是不能被覆盖，即不能重写。
- 如果子类中也含有一个返回类型、方法名、参数列表均与之相同的静态方法，那么该子类实际上只是将父类中的该同名方法进行了隐藏，而非重写。
- 父类引用指向子类对象时，只会调用父类的静态方法。所以，它们的行为也并不具有多态性。

```java
//重写都是方法的重写和属性无关
public class B{
    
    public static void test(){
        System.out.println("B=>test()");
    }
}

//注意这个是staic方法
public class A extends B{
    
    public static void test(){
        System.out.println("A=>test()")
    }
}

public class Application{
    //静态方法：方法的调用只和左边，定义的类有关
    public static void main(String[] args){
        A a=new A();
        a.test();
        
        //父类的引用指向子类
        B b=new A();
        b.test();
    }
}
//输出结果
A=>test()
B=>test()
```

```java
public class B{
    
    public void test(){
        System.out.println("B=>test()");
    }
}

public class A extends B{
    
    public void test(){
        System.out.println("A=>test()")
    }
}

public class Application{
    //非静态：重写
    public static void main(String[] args){
        A a=new A();
        a.test();
        
        
        B b=new A();//子类重写了父类的方法
        b.test();
    }
}
/*
A=>test()
A=>test()
*/
```

