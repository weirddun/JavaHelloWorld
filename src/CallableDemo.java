import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

class MyThread implements Callable<Integer>{

	@Override
	public Integer call() throws Exception {
		System.out.println("**********************come in callable");
		return 1024;
	}
}
/**
 * 实现Callable接口获得多线程，有返回值并抛出异常
 */
public class CallableDemo {
	public static void main(String[] args) throws ExecutionException, InterruptedException {
		FutureTask<Integer> futureTask = new FutureTask<>(new MyThread());
		Thread t1 = new Thread(futureTask,"AA");
		t1.start();
		int r1 = 100;
		int r2 = futureTask.get();

		System.out.println(".......result\t"+(r1+r2));
	}
}
