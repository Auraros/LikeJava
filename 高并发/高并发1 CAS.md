# 高并发1 CAS



## CAS 

Compare And Swap / 自旋 / 自旋锁 / 无锁

因为经常配合循环操作，直到完成位为止，所以泛指一类操作

cas(v, a, b) 变量v  期待值a  修改值b

ABA问题： 你的女朋友在离开你的这段儿时间经历了别的人，自旋就是你空转等待，一直等到她接纳你为止

![image-20201231191651637](C:\Users\Auraros\AppData\Roaming\Typora\typora-user-images\image-20201231191651637.png)

```
CAS： compare and swap 保证没有锁的状态下，对一个值进行更新
是乐观锁的一种实现。

并不需要用到锁。这里线程获取当前值E比如是0，然后计算结果值例如增加一等于一，然后比较当前E也就是0是不是还是0如果这个值被其他线程先更改过了，那就不相等，那就重复这样的操作。如果还是0那就证明没有线程操作过了，所以加上1，更新为1。
```

```
ABA问题：
刚开始初始值是一个0
1. 第一个线程A把0增加为1，还没写入
2. 但是这个时候 线程 B 把 0增加为2， 线程C 又把 2 改成了0
3. 这个时候线程A的0和现在这个值得0不是一个0

解决问题：
给0加一个版本号 0_1, 0_2
```

**源码分析：**

**Unsafe**

AtomicInterger:

```
public final int incrementAndGet() {
        for (;;) {
            int current = get();
            int next = current + 1;
            if (compareAndSet(current, next))
                return next;
        }
    }

public final boolean compareAndSet(int expect, int update) {
        return unsafe.compareAndSwapInt(this, valueOffset, expect, update);
    }
```

Unsafe：

```
public final native boolean compareAndSwapInt(Object var1, long var2, int var4, int var5);
```

运用：

```
package com.mashibing.jol;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class T02_TestUnsafe {

    int i = 0;
    private static T02_TestUnsafe t = new T02_TestUnsafe();

    public static void main(String[] args) throws Exception {
        //Unsafe unsafe = Unsafe.getUnsafe();

        Field unsafeField = Unsafe.class.getDeclaredFields()[0];
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);

        Field f = T02_TestUnsafe.class.getDeclaredField("i");
        long offset = unsafe.objectFieldOffset(f);
        System.out.println(offset);

        boolean success = unsafe.compareAndSwapInt(t, offset, 0, 1);
        System.out.println(success);
        System.out.println(t.i);
        //unsafe.compareAndSwapInt()
    }
}
```

jdk8u: unsafe.cpp:

cmpxchg = compare and exchange

```
UNSAFE_ENTRY(jboolean, Unsafe_CompareAndSwapInt(JNIEnv *env, jobject unsafe, jobject obj, jlong offset, jint e, jint x))
  UnsafeWrapper("Unsafe_CompareAndSwapInt");
  oop p = JNIHandles::resolve(obj);
  jint* addr = (jint *) index_oop_from_field_offset_long(p, offset);
  return (jint)(Atomic::cmpxchg(x, addr, e)) == e;
UNSAFE_END
```

jdk8u: atomic_linux_x86.inline.hpp

is_MP = Multi Processor  

```
inline jint     Atomic::cmpxchg    (jint     exchange_value, volatile jint*     dest, jint     compare_value) {
  int mp = os::is_MP();
  __asm__ volatile (LOCK_IF_MP(%4) "cmpxchgl %1,(%3)"
                    : "=a" (exchange_value)
                    : "r" (exchange_value), "a" (compare_value), "r" (dest), "r" (mp)
                    : "cc", "memory");
  return exchange_value;
}
```

jdk8u: os.hpp is_MP()

```
static inline bool is_MP() {
    // During bootstrap if _processor_count is not yet initialized
    // we claim to be MP as that is safest. If any platform has a
    // stub generator that might be triggered in this phase and for
    // which being declared MP when in fact not, is a problem - then
    // the bootstrap routine for the stub generator needs to check
    // the processor count directly and leave the bootstrap routine
    // in place until called after initialization has ocurred.
    return (_processor_count != 1) || AssumeMP;
  }
```

jdk8u: atomic_linux_x86.inline.hpp

```c++
#define LOCK_IF_MP(mp) "cmp $0, " #mp "; je 1f; lock; 1: "
```

最终实现：

cmpxchg = cas修改变量值

```assembly
lock cmpxchg 指令
```

硬件：

lock指令在执行后面指令的时候锁定一个北桥信号

（不采用锁总线的方式）



```
追溯到最后的源码：
lock cmpxchg 指令 （cmpxchg = cas修改变量值）

cmpxchg用于交换
lock 是用于锁定，在执行后面指令的时候锁定一个北桥信号
```

