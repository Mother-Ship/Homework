package _20181028.ver4;

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
     * 方法四
     * 让两个线程竞争一个可重入锁（这里似乎不需要是否公平的概念，也不必引入读写锁）。
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
