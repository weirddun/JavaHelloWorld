import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author Weird_Dun
 * ABA问题的解决
 */
public class AbaDemo {
    static AtomicReference atomicReference = new AtomicReference(100);
    static AtomicStampedReference atomicStampedReference = new AtomicStampedReference(100, 1);

    public static void main(String[] args) {
        new Thread(()->{
            atomicReference.compareAndSet(100, 101);
            atomicReference.compareAndSet(101, 100);
        },"t1").start();

        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(atomicReference.compareAndSet(100, 2019)+"\t"+atomicReference.get());
        },"t2").start();

        try{TimeUnit.SECONDS.sleep(2);} catch (InterruptedException e) {e.printStackTrace();}

        System.out.println("解决ABA问题");

        new Thread(()->{
            int stamp = atomicStampedReference.getStamp();
            System.out.println(Thread.currentThread().getName()+"\t第一次版本号" +stamp);
            //暂停1秒第三线程
            try{TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
            atomicStampedReference.compareAndSet(100, 101, atomicStampedReference.getStamp(), atomicStampedReference.getStamp()+1);
            System.out.println(Thread.currentThread().getName()+"\t第二次版本号" +atomicStampedReference.getStamp());
            atomicStampedReference.compareAndSet(101, 100, atomicStampedReference.getStamp(), atomicStampedReference.getStamp()+1);
            System.out.println(Thread.currentThread().getName()+"\t第三次版本号" +atomicStampedReference.getStamp());
        },"t3").start();

        new Thread(()->{
            int stamp = atomicStampedReference.getStamp();
            System.out.println(Thread.currentThread().getName()+"\t第一次版本号" +stamp);
            try{TimeUnit.SECONDS.sleep(2);} catch (InterruptedException e) {e.printStackTrace();}
            boolean result = atomicStampedReference.compareAndSet(100, 2019, 1, atomicStampedReference.getStamp()+1);
            System.out.println(Thread.currentThread().getName()+"\t修改成功否： "+result+"\t当前最新实际版本号："+atomicStampedReference.getStamp());
        },"t4").start();
    }
}
