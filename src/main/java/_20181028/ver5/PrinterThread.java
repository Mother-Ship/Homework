package _20181028.ver5;

import java.time.Instant;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

public class PrinterThread extends Thread {
    private AtomicLong counter = new AtomicLong(0);
    private volatile boolean paused = false;
    private Object object = new Object();

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
                        synchronized (object) {
                            if (paused) {

                            } else {
                                object.notifyAll();
                                continue;
                            }
                        }
                    }
                }
        ).start();
    }

    @Override
    public void run() {
        while (true) {
            synchronized (object) {
                try {
                    System.out.println(Instant.now() + "   " + counter.getAndIncrement());
                    TimeUnit.SECONDS.sleep(1);
                    object.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
