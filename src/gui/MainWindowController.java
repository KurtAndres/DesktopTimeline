package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Category;
import model.TLEvent;
import model.Timeline;
import model.TimelineMaker;


public class MainWindowController {
	
	/**
	 * The model of the program
	 */
	private TimelineMaker timelineMaker;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    
    @FXML 
    private MenuItem aboutMenuItem;

    @FXML
    private Button addCategoryButton;

    @FXML
    private Button addEventButton;

    @FXML
    private Button addTimelineButton;

    @FXML
    private Label categoriesLabel;

    @FXML
    private ListView<String> categoriesListView;

    @FXML
    private Button deleteCategoryButton;

    @FXML
    private Button deleteEventButton;
    
    @FXML
    private MenuItem deleteMenuItem;

    @FXML
    private Button deleteTimelineButton;

    @FXML
    private Button editCategoryButton;

    @FXML
    private Button editEventButton;
    
    @FXML
    private Menu editMenu;

    @FXML
    private Button editTimelineButton;

    @FXML
    private Label eventsLabel;
    
    @FXML
	private MenuItem exitMenuItem; 
    
	@FXML
	private Menu fileMenu;

	@FXML
	private MenuItem helpMenuItem;
	
	@FXML
	private Menu infoMenu;
	
	@FXML
	private Menu insertMenu;

    @FXML
    private AnchorPane mainWindowAnchor;

    @FXML
    private MenuBar menuBar;
    
    @FXML
    private MenuItem addCategoryMenuItem;
    
    @FXML
    private MenuItem addEventMenuItem;
    
    @FXML
    private MenuItem addTimelineMenuItem;
    
    @FXML
    private ScrollPane renderScrollPane;
    
    @FXML
    private MenuItem saveMenuItem;

    @FXML
    private Label timelinesLabel;

    @FXML
    private ListView<String> timelinesListView;

    /**
	 * Brings up a new unresizable JavaFX stage with the text in
	 * 
	 * @param String
	 *            the string to show
	 */
	private void showDialog(String show) {
		Stage dialog = new Stage();
		dialog.initStyle(StageStyle.UTILITY);
		TextArea text = new TextArea(show);
		text.setMaxWidth(300);
		text.setWrapText(true);
		text.setEditable(false);
		Scene scene = new Scene(new Group(text));
		dialog.setScene(scene);
		dialog.initStyle(StageStyle.UTILITY);
		dialog.setResizable(false);
		dialog.show();
	}
    
    @FXML
    void aboutPressed(ActionEvent event) {
    	showDialog(timelineMaker.getAboutText());
    }

    @FXML
    void addCategoryPressed(ActionEvent event) {
    	try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(
					"CategoryPropertiesWindow.fxml"));
			Parent root = (Parent) loader.load();
			CategoryPropertiesWindowController controller = loader
					.<CategoryPropertiesWindowController> getController();
			controller.initData(timelineMaker, new Category("DEFAULT"));
			Stage stage = new Stage();
			stage.setTitle("Edit Category");
			Scene scene = new Scene(root);
			scene.getStylesheets().add("gui/CategoryPropertiesWindow.css");
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    @FXML
    void deleteCategoryPressed(ActionEvent event) {
    	if (timelineMaker.getSelectedTimeline() == null)
			return;
		Category selectedCategory = timelineMaker.getSelectedTimeline()
				.getSelectedCategory();
		timelineMaker.getSelectedTimeline().deleteCategory(selectedCategory);
		timelineMaker.deleteCategory(selectedCategory);
		populateListView();
    }

    @FXML
    void deleteEventPressed(ActionEvent event) {
    	timelineMaker.deleteEvent();
    }

    @FXML
    void deletePressed(ActionEvent event) {
    	// TODO: Add delete functionality.
    }
    
    @FXML
	void deleteTimelinePressed(ActionEvent event) {
		if (timelineMaker == null)
			return;
		timelineMaker.deleteTimeline();
		timelineMaker.selectDefaultTimeline();
		timelineMaker.updateGraphics();
		populateListView();
		if (timelineMaker.getSelectedTimeline() == null) {
			ArrayList<String> x = new ArrayList<String>();
			categoriesListView.setItems(FXCollections.observableList(x));
			return;
		}

	}

    @FXML
    void editCategoryPressed(ActionEvent event) {
    	if (timelineMaker.getSelectedTimeline() == null)
			return;
		Category selectedCategory = timelineMaker.getSelectedTimeline()
				.getSelectedCategory();
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(
					"CategoryPropertiesWindow.fxml"));
			Parent root = (Parent) loader.load();
			CategoryPropertiesWindowController controller = loader
					.<CategoryPropertiesWindowController> getController();
			controller.initData(timelineMaker, selectedCategory);
			Stage stage = new Stage();
			stage.setTitle("Edit Category");
			Scene scene = new Scene(root);
			scene.getStylesheets().add("gui/CategoryPropertiesWindow.css");
			stage.setScene(scene);
			stage.setMinWidth(292);
			stage.setMinHeight(144);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    @FXML
    void editEventPressed(ActionEvent event) {
    	TLEvent selectedEvent = timelineMaker.getSelectedEvent();
		if (selectedEvent == null)
			return;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(
					"EventPropertiesWindow.fxml"));
			Parent root = (Parent) loader.load();
			EventPropertiesWindowController controller = loader
					.<EventPropertiesWindowController> getController();
			controller.initData(timelineMaker, selectedEvent);
			Stage stage = new Stage();
			stage.setTitle("Edit Event");
			Scene scene = new Scene(root);
			scene.getStylesheets().add("gui/EventPropertiesWindow.css");
			stage.setScene(scene);
			stage.setMinWidth(311);
			stage.setMinHeight(376);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    @FXML
    void editTimelinePressed(ActionEvent event) {
    	Timeline selectedTimeline = timelineMaker.getSelectedTimeline();
		if (selectedTimeline == null)
			return;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(
					"TimelinePropertiesWindow.fxml"));
			Parent root = (Parent) loader.load();
			TimelinePropertiesWindowController controller = loader
					.<TimelinePropertiesWindowController> getController();
			controller.initData(timelineMaker, selectedTimeline);
			Stage stage = new Stage();
			stage.setTitle("Edit Timeline");
			Scene scene = new Scene(root);
			scene.getStylesheets().add("gui/TimelinePropertiesWindow.css");
			stage.setScene(scene);
			stage.setMinWidth(426);
			stage.setMinHeight(270);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    @FXML
    void exitPressed(ActionEvent event) {
    	System.exit(0);
    }

    @FXML
    void helpPressed(ActionEvent event) {
    	showDialog(timelineMaker.getHelpText());
    }

    @FXML
    void addEventPressed(ActionEvent event) {
    	if (timelineMaker.getSelectedTimeline() == null)
			return;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(
					"EventPropertiesWindow.fxml"));
			Parent root = (Parent) loader.load();
			EventPropertiesWindowController controller = loader
					.<EventPropertiesWindowController> getController();
			controller.initData(timelineMaker, null);
			Stage stage = new Stage();
			stage.setTitle("Add Event");
			Scene scene = new Scene(root);
			scene.getStylesheets().add("gui/EventPropertiesWindow.css");
			stage.setScene(scene);
			stage.setMinWidth(311);
			stage.setMinHeight(376);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    @FXML
    void addTimelinePressed(ActionEvent event) {
    	try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(
					"TimelinePropertiesWindow.fxml"));
			Parent root = (Parent) loader.load();
			TimelinePropertiesWindowController controller = loader
					.<TimelinePropertiesWindowController> getController();
			controller.initData(timelineMaker, null);
			Stage stage = new Stage();
			stage.setTitle("Add Timeline");
			Scene scene = new Scene(root);
			scene.getStylesheets().add("gui/EventPropertiesWindow.css");
			stage.setScene(scene);
			stage.setMinWidth(426);
			stage.setMinHeight(270);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    @FXML
    void savePressed(ActionEvent event) {
    }

    @FXML
    void initialize() {
    	timelinesListView.addEventHandler(MouseEvent.MOUSE_CLICKED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent arg0) {
						timelineListViewClicked();
					}
				});
		categoriesListView.addEventHandler(MouseEvent.MOUSE_CLICKED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent arg0) {
						categoriesListViewClicked();
					}
				});
    }
    
    /**
	 * What happens when the timelineListView is clicked. It renders that
	 * timeline.
	 */
	private void timelineListViewClicked() {
		timelineMaker.selectTimeline(timelinesListView.getSelectionModel()
				.getSelectedItem());
		populateListView();
	}

	/**
	 * What happens when the categoriesListView is clicked. It sets that
	 * category as the selected category
	 */
	private void categoriesListViewClicked() {
		if (timelineMaker.getSelectedTimeline() == null)
			return;
		timelineMaker.getSelectedTimeline().selectCategory(
				categoriesListView.getSelectionModel().getSelectedItem());
	}
    
    /**
	 * Populates the list view with the timelines and categories.
	 */
	public void populateListView() {
		timelinesListView.setItems(FXCollections.observableList(timelineMaker
				.getTimelineTitles()));
		Timeline t = timelineMaker.getSelectedTimeline();
		if (t != null) {
			timelinesListView.getSelectionModel().select(t.getName());
			categoriesListView.setItems(FXCollections.observableList(t
					.getCategoryTitles()));
			categoriesListView.getSelectionModel().select(
					t.getSelectedCategory().getName());
		}

	}
    
    /**
	 * Initializes the Main window.
	 * 
	 * @param timelineMaker
	 *            The timelinemaker that makes the timeline.
	 */
	public void initData(TimelineMaker timelineMaker) {
		this.timelineMaker = timelineMaker;
		timelineMaker.setMainWindow(this);
		populateListView();
		timelineMaker.graphics.setPanel(renderScrollPane);

	}

}