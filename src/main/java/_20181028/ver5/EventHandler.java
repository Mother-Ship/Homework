package _20181028.ver5;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class EventHandler implements javafx.event.EventHandler<ActionEvent> {
    private PrinterThread thread;
    private Button btn;

    public EventHandler(PrinterThread thread, Button btn) {
        this.thread = thread;
        this.btn = btn;
    }

    /**
     * 方法五
     * 使用较为原始的Object.wait()和notifyAll()方法来控制
     * 打印线程每次开始打印时尝试获取对象锁，打印完后让所有持有锁的线程阻塞，释放对象锁
     * 另开一个线程，当持续运行时反复尝试获取对象锁，获取后如果不需要暂停，立即唤醒所有获得该对象锁的线程，如果需要暂停则什么也不做
     * 看起来和方法3很像，但其实利用了Sync关键字的不同语义，方法3 是两个线程竞争对象锁，方法5是使用这个对象的通知机制，获取锁只不过是通知机制的前置条件
     *
     * 好想要一个共享锁版本的Sync关键字啊，这样我就能让两个线程都拿到这个对象的读锁，然后使用wait和notifyAll了，不必每次都释放对象锁
     */
    @Override
    public void handle(ActionEvent event) {
        if (thread.isPaused()) {
            System.out.println("continue");
            thread.setPaused(false);
            btn.setText("暂停");
        } else {
            System.out.println("pause");
            thread.setPaused(true);
            btn.setText("继续");
        }

    }
}
