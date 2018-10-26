package _20181028.ver2;

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
     * 在打印线程中设置一个volatile的paused变量，在run方法中根据变量值判断是否暂停。
     * 直接更改这个变量就可以了，线程间只用这个变量通信，不涉及锁。
     * （理论上可以用这办法写出各种看起来酷炫的东西，比如搞一个线程透明的锁，然后用是否锁上来代替这个boolean值）
     * @param event
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
