package gui;

import java.net.URL;
import java.util.ResourceBundle;

import model.Category;
import model.TimelineMaker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Controls the window for delete category confirmation.
 * 
 * @author Leanne
 *
 */
public class DeleteCategoryConfirmationController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane deleteCategoryConfirmationAnchor;

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
    	Category selectedCategory = timelineMaker.getSelectedTimeline()
				.getSelectedCategory();
		//timelineMaker.getSelectedTimeline().deleteCategory(selectedCategory);
		timelineMaker.deleteCategory(selectedCategory);
		mainWindowController.populateListView();
    	
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
        assert deleteCategoryConfirmationAnchor != null : "fx:id=\"deleteCategoryConfirmationAnchor\" was not injected: check your FXML file 'DeleteCategoryConfirmation.fxml'.";
        assert noButton != null : "fx:id=\"noButton\" was not injected: check your FXML file 'DeleteCategoryConfirmation.fxml'.";
        assert yesButton != null : "fx:id=\"yesButton\" was not injected: check your FXML file 'DeleteCategoryConfirmation.fxml'.";


    }

}
