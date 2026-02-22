package tom;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tom.MainWindow;
import tom.Tom;

/**
 * A GUI for Duke using FXML.
 */
public class Main extends Application {

    private Tom tom = new Tom();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setTom(tom);  // inject the Duke instance
            stage.setTitle("Tom Chatbot");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
