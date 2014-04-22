package gui;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
			primaryStage.setTitle("Timelord");
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
		pop();
		launch(args);
	}
	public static void pop() {
	      JTextField xField = new JTextField(5);
	      JTextField yField = new JTextField(5);

	      JPanel myPanel = new JPanel();
	      myPanel.add(new JLabel("User:"));
	      myPanel.add(xField);
	      myPanel.add(Box.createHorizontalStrut(15)); // a spacer
	      myPanel.add(new JLabel("Password:"));
	      myPanel.add(yField);

	      int result = JOptionPane.showConfirmDialog(null, myPanel, 
	               "Please Enter User and Password", JOptionPane.OK_CANCEL_OPTION);
	      if (result == JOptionPane.OK_OPTION) {
	         TimelineMaker.user = xField.getText();
	         TimelineMaker.pass = yField.getText();
	      }

	   }
}
