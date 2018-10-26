package _20181028.ver1;

import _20181028.Console;
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

public class App extends Application {
    private PrinterThread thread = new PrinterThread();
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

        btn.setOnAction(new EventHandler(thread,btn));



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
        thread.start();
    }

}
