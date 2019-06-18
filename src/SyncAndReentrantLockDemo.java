/**
 * synchronized和Lock有什么区别？
 * 1 原始构成
 * synchronized是关键字，属于JVM层面，
 *     monitorenter（底层是通过monitor对象来完成，其实wait/notify等方法也依赖于monitor对象，只有在同步块中才能调用wait/notify等方法）
 *     monitorexit
 * Lock是具体类（java.util.concurrent.locks.lock）是api层面的锁
 *
 * 2 使用方法
 * synchronized 不需要用户去手动释放锁，当synchronized代码执行完后系统会自动让线程释放对锁的占用
 * ReentrantLock则需要用户去手动释放锁，如果没有主动释放锁，则会出现死锁现象。
 * 需要lock()和unlock()方法配合try/finally语句块来完成。
 *
 * 3 等待是否可中断
 * synchronized不可中断，除非抛出异常或者正常运行完成
 * ReentrantLock可中断，1 设置超时方法tryLock(long time TimeUnit unit)
 *                     2  lockInterruptibly()放代码块中，调用interrupt()方法可中断
 *
 * 4 加锁是否公平
 * synchronized非公平锁
 * ReentrantLock两者都可以，默认非公平锁，构造方法可以传入boolean值，true为公平锁，false为非公平锁。
 *
 * 5 锁绑定多个条件Condition
 * synchronized没有
 * ReentrantLock用来实现分组唤醒需要唤醒的线程们，可以精确唤醒，而不是像synchronized要么随机唤醒一个线程要么唤醒全部线程。
 */

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 题目：多线程之间按顺序调用，实现A->B->C三个线程启动，要求如下：
 * A打印5次，B打印10次，C打印15次
 * 紧接着
 * A打印5次，B打印10次，C打印15次
 * 。。。。。
 * 打印10轮
 */
class ShareResource{
	private int number = 1;
	private Lock lock = new ReentrantLock();
	Condition c1 = lock.newCondition();
	Condition c2 = lock.newCondition();
	Condition c3 = lock.newCondition();

	public void print5(){
		lock.lock();
		try {
			//1.判断
			while (number != 1){
				c1.await();
			}
			//2.干活
			for (int i = 1; i <=5 ; i++) {
				System.out.println(Thread.currentThread().getName()+"\t"+i);
			}
			//3.通知
			number = 2;
			c2.signal();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}

	}

	public void print10(){
		lock.lock();
		try {
			//1.判断
			while (number != 2){
				c2.await();
			}
			//2.干活
			for (int i = 1; i <=10 ; i++) {
				System.out.println(Thread.currentThread().getName()+"\t"+i);
			}
			//3.通知
			number = 3;
			c3.signal();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}

	}

	public void print15(){
		lock.lock();
		try {
			//1.判断
			while (number != 3){
				c3.await();
			}
			//2.干活
			for (int i = 1; i <=15 ; i++) {
				System.out.println(Thread.currentThread().getName()+"\t"+i);
			}
			//3.通知
			number = 1;
			c1.signal();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}

	}

}
public class SyncAndReentrantLockDemo {
	public static void main(String[] args) {
		ShareResource shareResource = new ShareResource();

		new Thread(() -> {
			for (int i = 1; i <=10 ; i++) {
				shareResource.print5();
			}
		},"A").start();

		new Thread(() -> {
			for (int i = 1; i <=10 ; i++) {
				shareResource.print10();
			}
		},"B").start();

		new Thread(() -> {
			for (int i = 1; i <=10 ; i++) {
				shareResource.print15();
			}
		},"C").start();
	}
}
