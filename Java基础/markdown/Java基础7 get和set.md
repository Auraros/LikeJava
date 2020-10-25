# Java基础7 get和set

**使用前提**

当要访问被private封装的属性时

**提供访问方式的原因**

之所以用private封装，又对外提供访问方式（set get），是因为可以在访问方式中加入逻辑判断等语句，对访问的数据进行操作，其中操作包括给属性赋值和查看属性，提高代码的健壮性。

```java
class Student{
    private int age;
    public int getAge(){
		return age;
    }
    public void setAge(int a){
        if(a>0 && a<200){
            age = a;
        }else{
			System.out.println("输入错误");
        }
    }
}
public class Test{
	public static void main(String[] args){
		Student student = new Student();
		student.setAge(18);
		System.out.println("年龄： "+student.getAge());
        }
    
}
```

**封装性的体现：**

1. 属性私有性
2. 不对外暴露私有的方法
3. 单例模式
4. ...