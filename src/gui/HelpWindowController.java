package gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;


public class HelpWindowController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane helpWindowAnchor;


    @FXML
    void initialize() {
        assert helpWindowAnchor != null : "fx:id=\"helpWindowAnchor\" was not injected: check your FXML file 'HelpWindow.fxml'.";


    }

}
