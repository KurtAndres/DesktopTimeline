package gui;

import java.net.URL;
import java.util.ResourceBundle;

import model.TimelineMaker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class SignInWindowController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private PasswordField passwordField;

    @FXML
    private AnchorPane signInAnchor;

    @FXML
    private Button signInButton;

    @FXML
    private TextField usernameField;


    @FXML
    void signInPressed(ActionEvent event) {
    	 TimelineMaker.user = usernameField.getText();
         TimelineMaker.pass = passwordField.getText();
         
         TimelineMaker timelineMaker = new TimelineMaker();
 		
 		try {
 			FXMLLoader loader = new FXMLLoader(getClass().getResource(
 					"MainWindow.fxml"));
 			Parent root = (Parent) loader.load();
 			MainWindowController controller = loader.<MainWindowController> getController();
 			controller.initData(timelineMaker);
 			Stage stage = new Stage();
 			stage.setTitle("Timelord");
 			Scene scene = new Scene(root);
 			scene.getStylesheets().add("gui/MainWindow.css");
 			stage.setScene(scene);
 			stage.setMinWidth(326);
 			stage.setMinHeight(580);
 			stage.show();
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
 		
 		Node source = (Node) event.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		stage.close();
    }

    @FXML
    void initialize() {
        assert passwordField != null : "fx:id=\"passwordField\" was not injected: check your FXML file 'SignInWindow.fxml'.";
        assert signInAnchor != null : "fx:id=\"signInAnchor\" was not injected: check your FXML file 'SignInWindow.fxml'.";
        assert signInButton != null : "fx:id=\"signInButton\" was not injected: check your FXML file 'SignInWindow.fxml'.";
        assert usernameField != null : "fx:id=\"usernameField\" was not injected: check your FXML file 'SignInWindow.fxml'.";
    }

}
