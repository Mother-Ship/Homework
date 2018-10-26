package _20181028;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextAreaBuilder;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.PrintStream;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class App extends Application {
    private static AtomicLong counter = new AtomicLong(0);
    private static Thread thread;
    private static boolean paused = false;

    private final Thread lockThread = new Thread(
            () -> {
                while (true){
                    if (paused){
                        synchronized (this){
                            try {
                                this.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
    );

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Create console textarea
        TextArea ta = TextAreaBuilder.create().prefWidth(800).prefHeight(400).wrapText(true).editable(false).build();
        ta.setFont(new Font(15));
        //This _20181028.Console class is A class extend the OutputStream, so it can receive the System.out.println() request,IT IS NOT java.io.Console!!!
        PrintStream ps = new PrintStream(new Console(ta));
        //Redirect system console IOStream
        System.setOut(ps);
        System.setErr(ps);

        //Create window to place console textarea
        Stage newWindow = new Stage();
        newWindow.setTitle("_20181028.Console");
        VBox vbox = new VBox();
        vbox.getChildren().addAll(ta);
        Scene scene2 = new Scene(vbox);
        newWindow.setScene(scene2);
        // Set position of console window, related to primary window.
        newWindow.setX(primaryStage.getX() + 200);
        newWindow.setY(primaryStage.getY() + 100);
        newWindow.show();
        // Set On Close Event
        newWindow.setOnCloseRequest(event -> System.exit(0));
        newWindow.setResizable(false);


        //Set style of main window
        primaryStage.setTitle("Homework");
        Group root = new Group();
        Scene scene = new Scene(root, 300, 200, Color.WHITE);

        GridPane gridpane = new GridPane();
        gridpane.setPadding(new Insets(5));
        gridpane.setHgap(10);
        gridpane.setVgap(10);
        Button btn = new Button("暂停");

        btn.setOnAction(
                event -> {
                    if (paused) {
                        //调用被废弃的、不释放锁的暂停/继续方法勉强可以实现，但是不优雅
                        //而使用Object.wait()方式，又会导致UI线程也被阻塞（UI线程必须先拿到对象锁后，才能阻塞所有拿到这个锁的线程）
                        //新思路，写一个会卡死的线程，如果点继续就让那个线程继续跑（也会让当前线程卡死）
//                        thread.resume();
                        paused = false;
                        synchronized (lockThread){
                            lockThread.notifyAll();
                        }
                        btn.setText("暂停");
                    } else {
//                        thread.suspend();
                        paused = true;
                        try {
                            lockThread.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        btn.setText("继续");
                    }
                }
        );
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        gridpane.add(hbBtn, 1, 4);
        root.getChildren().add(gridpane);

        primaryStage.setScene(scene);
        primaryStage.show();
        //set On Close event
        primaryStage.setOnCloseRequest(event -> System.exit(0));
        primaryStage.setResizable(false);

        System.out.println(Instant.now() + " Starting...");
        thread = new Thread(
                () -> {
                    synchronized (lockThread) {
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
        );
        thread.start();
    }

}
