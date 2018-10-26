package _20181028.ver1;

import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class PrinterThread extends Thread {
    private AtomicLong counter = new AtomicLong(0);
    private boolean paused = false;

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void run() {
        while (true) {
            System.out.println(Instant.now() + "   " + counter.getAndIncrement());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
