package _20181028.ver7;

import java.time.Instant;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class PrinterThread extends Thread {
    private AtomicLong counter = new AtomicLong(0);
    private volatile boolean paused = false;
    private CyclicBarrier barrier = new CyclicBarrier(2);

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

                        } else {
                            try {
                                //在守护线程里，让栅栏的两个线程中有一个已经准备好
                                barrier.await();
                            } catch (InterruptedException | BrokenBarrierException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        ).start();
    }

    @Override
    public void run() {
        while (true) {
            try {

                System.out.println(Instant.now() + "   " + counter.getAndIncrement());
                TimeUnit.SECONDS.sleep(1);

                //对栅栏的操作要放在暂停一秒之前，如果先执行await()方法，再中断线程 会导致 守护线程 中的barrier 抛出BrokenBarrierException异常
                //捕获后会照常运行，可以不打出堆栈而忽略（强迫症表示还是先暂停再操作栅栏好）
                barrier.await();
                //终于来了一个可以重用的
                barrier.reset();
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

}
