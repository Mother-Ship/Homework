package _20181028.ver9;

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
     * 方法九
     * 依然是Object.wait()和notifyAll()方法来控制
     * http://git.jiayuinc.com/projects/TX/repos/takeex-support/browse/takeex-ticket-printer/src/main/java/com/jiayu/takeex/support/ticketprinter/MainFrame.java
     * 借鉴打印客户端里的实现方式，当需要暂停时，打印线程获取对象锁后，调用对象锁的wait()方法后释放对象锁，阻塞住自身；
     * 点击按钮时获取对象锁，并且把因为对象锁而暂停的所有线程叫醒，再释放对象锁，这样打印线程就可以继续运行了。
     * ----
     * 当时没有想到这个，是因为写完方法二之后，觉得如果要在打印逻辑里判断是否暂停，就根本用不上对象锁。
     * 我写的打印方法是while，而实际代码是for，不过实际上for循环里这么运用……也不是不行
     * for(int i = 0; i < count; i++){
     *     if(paused){
     *         i--;
     *         continue;
     *     }
     *     //打印逻辑
     * }
     */
    @Override
    public void handle(ActionEvent event) {
        if (thread.isPaused()) {
            System.out.println("continue");
            thread.setPaused(false);
            thread.go();
            btn.setText("暂停");
        } else {
            System.out.println("pause");
            thread.setPaused(true);
            btn.setText("继续");
        }

    }
}
