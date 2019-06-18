import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class MyResource{
	//标识位默认开启
	private volatile boolean FLAG = true;
	private AtomicInteger atomicInteger = new AtomicInteger();

	BlockingQueue<String> blockingQueue = null;

	public MyResource(BlockingQueue<String> blockingQueue) {
		this.blockingQueue = blockingQueue;
		System.out.println(blockingQueue.getClass().getName());
	}

	public void MyProd() throws Exception{
		String data = null;
		boolean retValue;
		while (FLAG){
			data = atomicInteger.incrementAndGet()+"";
			retValue = blockingQueue.offer(data);
			if(retValue){
				System.out.println(Thread.currentThread().getName()+"\t插入队列"+data+"成功");
			}else{
				System.out.println(Thread.currentThread().getName()+"\t插入队列"+data+"失败");
			}
			TimeUnit.SECONDS.sleep(1);
		}
		System.out.println(Thread.currentThread().getName()+"\t 生产停止");
	}

	public void MyConsumer() throws Exception {
		String result = null;
		while (FLAG){
			result = blockingQueue.poll(2L,TimeUnit.SECONDS);
			if (null == result || result.equalsIgnoreCase("")){
				FLAG = false;
				System.out.println(Thread.currentThread().getName()+"\t超时两秒，消费退出");
				return;
			}
			System.out.println(Thread.currentThread().getName()+"\t消费队列"+result+"成功");
		}
	}

	public void stop(){
		this.FLAG = false;
	}

}

/**
 * volatile/CAS/atomicInteger/BlockQueue/线程交互/原子引用
 */
public class ProdConsumer_BlockQueueDemo {
	public static void main(String[] args) {
		MyResource myResource = new MyResource(new ArrayBlockingQueue<>(10));

		new Thread(() -> {
			System.out.println(Thread.currentThread().getName()+"\t生产线程启动");
			try {
				myResource.MyProd();
			} catch (Exception e) {
				e.printStackTrace();
			}
		},"Prod").start();

		new Thread(() -> {
			System.out.println(Thread.currentThread().getName()+"\t消费线程启动");
			try {
				myResource.MyConsumer();
			} catch (Exception e) {
				e.printStackTrace();
			}
		},"Consumer").start();

		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("5秒时间到，停止");

		myResource.stop();
	}
}
