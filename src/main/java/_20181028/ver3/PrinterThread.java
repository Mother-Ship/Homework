package _20181028.ver3;

import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class PrinterThread extends Thread {
    private AtomicLong counter = new AtomicLong(0);
    private volatile boolean paused = false;
    private Object lock = new Object();

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
                        synchronized (lock) {
                            while (paused) {
                            }
                        }
                    }
                }
        ).start();
    }


    @Override
    public void run() {
        while (true) {
            synchronized (lock) {
                System.out.println(Instant.now() + "   " + counter.getAndIncrement());
            }
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
