package gui;

import model.TimelineMaker;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * This is the driver for the program. Main launches start.
 */
public class Driver extends Application {

	/**
	 * The model of the program
	 */
	TimelineMaker timelineMaker;

	@Override
	public void start(Stage primaryStage) {
		TimelineMaker timelineMaker = new TimelineMaker();
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(
					"MainWindow.fxml"));
			Parent root = (Parent) loader.load();
			MainWindowController controller = loader
					.<MainWindowController> getController();
			controller.initData(timelineMaker);
			primaryStage.setTitle("Wow. This Timeline Maker Is So Cool!");
			Scene scene = new Scene(root);
			scene.getStylesheets().add("gui/MainWindow.css");
			primaryStage.setScene(scene);
			primaryStage.setMinWidth(326);
			primaryStage.setMinHeight(580);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
