# 高并发5 volatile

 

```java
public class HelloVolatile{
    volatile boolean running = true;
    //boolean runninng = true;
  	
    void m(){
		System.out.println(" m start");
        while(running){
            
        }
        System.out.print("m end");
    }
    
    publci static void main(String[] args){
        HelloVolatile t = new HelloVolatile();
        
        new Thread(t::m, "t1").start();
        
        try{
            TimeUnit.SECONDS.sleep(1);
        }catch(InterruptionException e){
            e.printStackTrace();
        }
        
        t.running = flase;
    }
    
}


```

```
1. 加入 volatile 之后，参数可见，所以程序会结束，但是不加不会结束
```

