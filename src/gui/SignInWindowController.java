package gui;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

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
import model.TimelineMaker;


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

	private TextFieldValidator usernameValidator, passwordValidator;


	@FXML
	void signInPressed(ActionEvent event) {
		if (usernameValidator.isValid() && passwordValidator.isValid()) {
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
	}

	@FXML
	void initialize() {
		assert passwordField != null : "fx:id=\"passwordField\" was not injected: check your FXML file 'SignInWindow.fxml'.";
		assert signInAnchor != null : "fx:id=\"signInAnchor\" was not injected: check your FXML file 'SignInWindow.fxml'.";
		assert signInButton != null : "fx:id=\"signInButton\" was not injected: check your FXML file 'SignInWindow.fxml'.";
		assert usernameField != null : "fx:id=\"usernameField\" was not injected: check your FXML file 'SignInWindow.fxml'.";
	}

	public void initValidators() {
		HashMap<String, String> invalidStrings = new HashMap<String, String>();
		invalidStrings.put("", "Cannot be blank.");
		usernameValidator = new TextFieldValidator(usernameField, "Enter username.", invalidStrings, "[! \\w]*$", "Only alphanumeric characters.");
		usernameField.focusedProperty().addListener(usernameValidator);
		passwordValidator = new TextFieldValidator(passwordField, "Enter password.", invalidStrings, "[! \\w]*$", "Only alphanumeric characters.");
		passwordField.focusedProperty().addListener(passwordValidator);
	}

}
