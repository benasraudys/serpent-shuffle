package plaktagalviai.serpentshuffle;

import javafx.application.Platform;
import javafx.fxml.FXML;

public class GameController {
    @FXML
    protected void exitGame() {
        Platform.exit();
    }

}