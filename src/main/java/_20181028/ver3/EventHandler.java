package _20181028.ver3;

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
     * 方法三
     * <p>
     * 一开始的想法是，暂停操作是【开一个线程，尝试获取lock对象的对象锁之后，
     * 调用对象锁的wait()方法将所有持有lock对象锁的线程阻塞（包括自己）】。
     * ----
     * 直接在pause方法内调用Object.wait()的问题是，由于pause方法是被UI线程调用，
     * 而UI线程获取不到锁，因此会被阻塞
     * ----
     * 仔细思考，完成这个操作，需要让打印的方法持有lock对象锁。
     * 假如sync代码块中写死循环，那这个新线程永远获取不到对象锁；
     * 假如在死循环中写sync块，那么对象锁的wait方法实际不起作用：
     * 每次打印完都会释放对象锁，下次循环重新获取lock的对象锁，
     * 而pause方法无法保证打印方法是否释放了lock对象锁，pause方法获取了对象锁之后，打印方法也无法再次获取对象锁。
     * ----
     * 那么干脆在pause方法中新开一个线程霸占这个lock对象的对象锁。
     * 但是这样会导致无法在其他地方获取lock的对象锁，进而对对象锁进行操作。
     * ----
     * 最后解决方案：在pause方法中新开线程，判断paused变量状态，如果暂停，则获取对象锁后 死循环霸占，如果继续，则退出sync代码块释放对象锁。
     * <p>
     * 打印方法则在死循环内写sync块，在每次打印前尝试获取对象锁，并且延迟的1秒必须放在sync块外，否则后台线程获取对象锁可能要等很久。
     * <p>
     * ……好复杂啊 可读性还贼鸡儿差
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
