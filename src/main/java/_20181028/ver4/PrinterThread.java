package _20181028.ver4;

import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

public class PrinterThread extends Thread {
    private AtomicLong counter = new AtomicLong(0);
    private volatile boolean paused = false;
    private ReentrantLock lock = new ReentrantLock();

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
                        lock.lock();
                        while (paused) {
                        }
                        lock.unlock();
                    }
                }
        ).start();
    }


    @Override
    public void run() {
        while (true) {
            lock.lock();
            System.out.println(Instant.now() + "   " + counter.getAndIncrement());
            lock.unlock();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
