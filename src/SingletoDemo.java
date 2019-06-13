/**
 * @author Weird_Dun
 */
public class SingletoDemo {
    private static SingletoDemo instance = null;
    /**
     *  @author Weird_Dun
     */
    private SingletoDemo()
    {
        System.out.println(Thread.currentThread().getName()+"\t 我是构造函数SingletoDemo");
    }

    /**
     * DCL 双端检锁机制
     * @author Weird_Dun
     */
    public static SingletoDemo getInstance()
    {
        if(instance == null)
        {
            synchronized (SingletoDemo.class)
            {
                if (instance == null)
                {
                    instance = new SingletoDemo();
                }
            }
        }
        return instance;
    }


    public static void main(String[] args) {

        for(int i=1;i<=1000;i++){
            new Thread(()->{
                SingletoDemo.getInstance();
            },String.valueOf(i)).start();
        }
    }
}
