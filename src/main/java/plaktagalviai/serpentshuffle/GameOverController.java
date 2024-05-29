package plaktagalviai.serpentshuffle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

public class GameOverController {

    @FXML
    protected void exitGame() {
        Platform.exit();
    }

    @FXML
    private void restartGame(ActionEvent event) {
        try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            GameApplication gameApp = new GameApplication();
            gameApp.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
