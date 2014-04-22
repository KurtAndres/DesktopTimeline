package gui;


import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import model.TimelineMaker;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Controls the window for delete timeline confirmation.
 * 
 * @author Leanne
 *
 */
public class DeleteTimelineConfirmationController  {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private AnchorPane deleteTimelineConfirmAnchor;

	@FXML
	private Label confirmLabel;

	@FXML
	private Button noButton;

	@FXML
	private Button yesButton;
	
	private TimelineMaker timelineMaker;
	
	private MainWindowController mainWindowController;


	@FXML
	void noPressed(ActionEvent event) {
		Node source = (Node) event.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		stage.close();
	}

	@FXML
	void yesPressed(ActionEvent event) {
		timelineMaker.deleteTimeline();
		timelineMaker.selectDefaultTimeline();
		mainWindowController.populateListView();
		mainWindowController.updateCategoryView();
		
		Node source = (Node) event.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		stage.close();
	}

	void initData(TimelineMaker tlm, MainWindowController mwc){
    	timelineMaker = tlm;
    	mainWindowController = mwc;
    }
	
	@FXML
	void initialize() {
		assert confirmLabel != null : "fx:id=\"confirmLabel\" was not injected: check your FXML file 'Untitled'.";
		assert noButton != null : "fx:id=\"noButton\" was not injected: check your FXML file 'Untitled'.";
		assert yesButton != null : "fx:id=\"yesButton\" was not injected: check your FXML file 'Untitled'.";


	}

}



