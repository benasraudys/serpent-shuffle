package plaktagalviai.serpentshuffle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.IOException;

public class MenuApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MenuApplication.class.getResource("menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 540, 405);
        stage.setTitle("Serpent Shuffle");
        Image icon = new Image(getClass().getResourceAsStream("icon.png"));
        stage.getIcons().add(icon);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}