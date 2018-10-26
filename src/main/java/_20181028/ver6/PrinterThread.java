package _20181028.ver6;

import java.time.Instant;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class PrinterThread extends Thread {
    private AtomicLong counter = new AtomicLong(0);
    private volatile boolean paused = false;
    private CountDownLatch latch = new CountDownLatch(1);

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
                            latch.countDown();
                        }
                    }
                }
        ).start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                latch = new CountDownLatch(1);
                latch.await();
                System.out.println(Instant.now() + "   " + counter.getAndIncrement());
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
