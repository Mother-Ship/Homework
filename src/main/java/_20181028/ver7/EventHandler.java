package _20181028.ver7;

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
     * 方法七
     * 歪用冷却阀来控制打印线程，每打印一个数字实例化一个新的CountdownLatch，并且将当前线程await在上面
     * 另开一个线程，当持续运行时反复让这个latch执行countDown()方法，需要暂停时则进入一个死循环
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
