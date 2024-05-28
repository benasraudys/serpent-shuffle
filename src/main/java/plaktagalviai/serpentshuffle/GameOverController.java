package plaktagalviai.serpentshuffle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

public class GameOverController {

    @FXML
    private void startTheGame(ActionEvent event) {
        try {
            System.out.println("start game");
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            GameApplication gameApp = new GameApplication();
            gameApp.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void exitGame() {
        System.out.println("exit game");
        Platform.exit();
    }

    @FXML
    private void restartGame(ActionEvent event) {
        try {
            System.out.println("restart game");
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            GameApplication gameApp = new GameApplication();
            gameApp.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
