package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * This is the driver for the program. Main launches start.
 */
public class Driver extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("SignInWindow.fxml"));
			Parent root = (Parent) loader.load();
			SignInWindowController controller = loader.<SignInWindowController> getController();
			controller.initValidators();
			primaryStage.setTitle("Welcome");
			Scene scene = new Scene(root);
			scene.getStylesheets().add("gui/MainWindow.css");
			primaryStage.setScene(scene);
			primaryStage.setMinWidth(300);
			primaryStage.setMinHeight(150);
			primaryStage.show();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
