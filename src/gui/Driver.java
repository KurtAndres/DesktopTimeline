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

	/**
	 * Calls pop which initializes the username and password, then launches the application which calls start.
	 * @param args
	 */
	public static void main(String[] args) {
		currentMemento = 0;
		Mementos = new ArrayList<TimelineMaker.Memento>();
			
		launch(args);
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
