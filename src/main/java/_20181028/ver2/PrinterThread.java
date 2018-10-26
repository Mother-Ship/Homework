package _20181028.ver2;

import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class PrinterThread extends Thread {
    private AtomicLong counter = new AtomicLong(0);
    private volatile boolean paused = false;

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }


    @Override
    public void run() {
        while (true) {
            if (paused) continue;
            System.out.println(Instant.now() + "   " + counter.getAndIncrement());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
