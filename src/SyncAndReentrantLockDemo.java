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
public class SyncAndReentrantLockDemo {

}
