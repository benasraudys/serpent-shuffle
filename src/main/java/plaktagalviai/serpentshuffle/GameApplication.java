package plaktagalviai.serpentshuffle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GameApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("game.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 540, 405);
        stage.setTitle("Serpent Shuffle");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}