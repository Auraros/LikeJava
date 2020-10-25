# Java基础3 switch语句

这篇来介绍`Java`中`switch`语句的基本用法。一提到`switch`语句，我就会想起用成绩等级划分来举例这个`switch`的实现过程。相关`demo`代码如下：

```java
package lessons;

public class MyClass{
    public static void main(String[] args){
        int score = 90;
        switch(score)
        {
            case 90:
                System.out.println("优秀");
                break;
            case 80:
                System.out.println("良");
                break;
            default:
                System.out.println("分数等级未定义");
				break;
        }
    }
}
```

通过修改`score`这个变量的值，分别改成`80`,`70`,`60`,`50`来测试是调用哪个`case`下的打印语句。

注意：

这里`switch`语句`score`数据类型可以是`byte`,`short`,`int`,`char`，不是每个`case`下都有break，有些场景是不需要`break`.这里的break就是执行到这里就结束，不会执行接下来其他的`case`下的打印语句。`default`也可以没有，基本语法格式就像上面举例。