package _20181028.ver1;

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
     * 简单粗暴的使用resume和suspend方法，反正不涉及锁，也不担心是否会造成死锁。
     * 仍然需要设置paused成员变量（用于修改按钮文字）。
     * @param event
     */
    @Override
    public void handle(ActionEvent event) {
        if (thread.isPaused()) {
            System.out.println("continue");
            thread.resume();
            thread.setPaused(false);
            btn.setText("暂停");
        } else {
            System.out.println("pause");
            thread.setPaused(true);
            thread.suspend();
            btn.setText("继续");
        }
    }

}
