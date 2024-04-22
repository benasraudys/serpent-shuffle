module plaktagalviai.serpentshuffle {
    requires javafx.controls;
    requires javafx.fxml;


    opens plaktagalviai.serpentshuffle to javafx.fxml;
    exports plaktagalviai.serpentshuffle;
}