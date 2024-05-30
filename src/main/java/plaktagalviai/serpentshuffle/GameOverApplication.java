package plaktagalviai.serpentshuffle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.IOException;

public class GameOverApplication extends Application {


    private int gameScore = 0;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MenuApplication.class.getResource("gameOver.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 720, 720);

        SavedScore savedScore = new SavedScore();
        gameScore = savedScore.getScore();

        GameOverController controller = fxmlLoader.getController();


        controller.setGameScore(gameScore);

        stage.setTitle("Serpent Shuffle");
        Image icon = new Image(getClass().getResourceAsStream("icon.png"));
        stage.getIcons().add(icon);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}