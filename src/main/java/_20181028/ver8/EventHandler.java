package _20181028.ver8;

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
     * 方法八
     * 歪用信号量来控制打印线程，管理1个信号量的Semaphore其实就是互斥锁
     * 信号量好坑啊1111
     * 似乎Exchanger Future和ForkJoinTask帮不上忙，就先这样吧
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
