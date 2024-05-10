package plaktagalviai.serpentshuffle;

import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;

public class MenuController {
    @FXML
    private void startTheGame(javafx.event.ActionEvent event) {
        try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("game.fxml")));
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void exitGame() {
        Platform.exit();
    }
}