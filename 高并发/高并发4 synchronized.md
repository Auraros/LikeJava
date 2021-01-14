# 高并发4 synchronized

  

## synchronized最底层实现

```java
public class T {
    static volatile int i = 0;
    
    public static void n() { i++; }
    
    public static synchronized void m() {}
    
    publics static void main(String[] args) {
        for(int j=0; j<1000_000; j++) {
            m();
            n();
        }
    }
} 

```

java -XX:+UnlockDiagonositicVMOptions -XX:+PrintAssembly T

C1 Compile Level 1 (一级优化)

C2 Compile Level 2 (二级优化)

找到m() n()方法的汇编码，会看到 lock comxchg .....指令



## synchronized实现过程

- Java代码层面： synchronized

- 字节码层面：

  ```
  monitorenter
  操作指令
  monitorexit
  ```

- 执行过程中自动升级

- CPU底层实现： lock comxchg





