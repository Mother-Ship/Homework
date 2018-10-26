package _20181028.ver8;

import java.time.Instant;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class PrinterThread extends Thread {
    private AtomicLong counter = new AtomicLong(0);
    private volatile boolean paused = false;
    private Semaphore semaphore = new Semaphore(1);

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public PrinterThread() {
        new Thread(
                () -> {
                    while (true) {
                        if (paused) {
                            //刚切换状态时，信号量还有一个，但是不能通过acquireUninterruptibly()方法取走它，否则守护线程会休眠
//                            semaphore.acquireUninterruptibly();
                        } else {
                            //这里不写if判断的话，管理的信号量会无限增多。。Semaphore类的构造函数给的量压根不是上限！！
                            //在打印线程启动前，Semaphore管理的信号量就已经有好几十个了。。日
                            if (semaphore.availablePermits() == 0) {
                                semaphore.release();
                            }
                            //设想：也可以不写if判断，直接在打印线程启动的时候，不中断的取光信号量就行
                            //不能这么写，否则是指数级增加信号量，分分秒 Exception in thread "Thread-4" java.lang.Error: Maximum permit count exceeded
//                            semaphore.release(semaphore.availablePermits());
//                            semaphore.release();
                        }
                    }
                }
        ).start();
    }

    @Override
    public void run() {
        //如果守护线程不判断当前管理的信号量的话，需要在打印线程启动的时候，先将守护线程之前release的permits全部拿走
        // ☆ 不可行，在这条命令执行到进入while循环时 已经又加了一大堆信号量
//        semaphore.acquireUninterruptibly(semaphore.availablePermits());

        while (true) {
            try {
                System.out.println(Instant.now() + "   " + counter.getAndIncrement());
                TimeUnit.SECONDS.sleep(1);
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
