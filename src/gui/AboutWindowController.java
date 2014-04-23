package gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;


public class AboutWindowController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane aboutAnchor;


    @FXML
    void initialize() {
        assert aboutAnchor != null : "fx:id=\"aboutAnchor\" was not injected: check your FXML file 'AboutWindow.fxml'.";
    }

}
