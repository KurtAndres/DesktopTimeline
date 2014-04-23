package gui;

import java.util.ArrayList;

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
	/**
	 * The list of Mementos. Used for undoing and redoing actions.
	 */
	private static ArrayList<TimelineMaker.Memento> Mementos;
	/**
	 * Keeps track of the current Memento.
	 */
	private static int currentMemento;

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

	/**
	 * Calls pop which initializes the username and password, then launches the application which calls start.
	 * @param args
	 */
	public static void main(String[] args) {
		currentMemento = 0;
		Mementos = new ArrayList<TimelineMaker.Memento>();
			
		pop();
		launch(args);
	}

	/**
	 * Initializes the username and password.
	 */
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
		if (result == JOptionPane.CANCEL_OPTION) {
			System.exit(0);
		}

	}

	/**
	 * Asks the timelineMaker to create a Memento of itself, then adds that Memento to the list of Mementos.
	 */
	public static void addMemento(TimelineMaker tm){
		Mementos.add(currentMemento, tm.createMemento());
		currentMemento++;
	}
	/**
	 * Returns the Memento for undoing one action.
	 * @return The Memento for undoing one action or null if there is nothing to undo.
	 */
	public static TimelineMaker.Memento undo(){
		if(currentMemento >= 1)
			return Mementos.get(--currentMemento);
		else
			return null;
	}
	/**
	 * Returns the Memento for redoing one action.
	 * @return The Memento for redoing one action or null if there is nothing to be redone.
	 */
	public static TimelineMaker.Memento redo(){
		if(currentMemento <= Mementos.size() - 1)
			return Mementos.get(currentMemento++);
		else
			return null;
	}



}
