package gui;

import java.util.ArrayList;

import org.json.simple.parser.ParseException;

import storage.phpPushHelper;
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
	private static TimelineMaker timelineMaker;
	/**
	 * The list of Mementos. Used for undoing and redoing actions.
	 */
	private static ArrayList<TimelineMaker.Memento> Mementos;
	/**
	 * Where the next Memento created is to be written.
	 */
	private static int nextMemento;


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
	 * Initializes the memento mechanism, then launches the application which calls start.
	 * @param args
	 */
	public static void main(String[] args) {
		nextMemento = 0;
		Mementos = new ArrayList<TimelineMaker.Memento>();
			
		launch(args);
	}

	/**
	 * Asks the timelineMaker to create a Memento of itself, then adds that Memento to the list of Mementos.
	 */
	public static void addMemento(TimelineMaker tm){
		timelineMaker = tm;
		Mementos.add(nextMemento, tm.createMemento());
		System.out.println("Added mem at: " + nextMemento);
		nextMemento++;
	}
	/**
	 * Returns the Memento for undoing one action.
	 * @return The Memento for undoing one action or null if there is nothing to undo.
	 */
	public static TimelineMaker.Memento undo(TimelineMaker tm){
		if(nextMemento >= 2){
			nextMemento--;
			System.out.println("Retrieving: (undo) " + (nextMemento-1));
			return Mementos.get(nextMemento-1);
		}
		else
			return null;
	}
	/**
	 * Returns the Memento for redoing one action.
	 * @return The Memento for redoing one action or null if there is nothing to be redone.
	 */
	public static TimelineMaker.Memento redo(){	
		if(nextMemento <= Mementos.size() - 1){
			System.out.println("Retrieving: (redo) " + nextMemento);
			TimelineMaker.Memento toReturn = Mementos.get(nextMemento);
			nextMemento++;
			return toReturn;
		}
		else
			return null;
	}
	
	@Override
	public void stop() {
		try {
			phpPushHelper.send(timelineMaker);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
