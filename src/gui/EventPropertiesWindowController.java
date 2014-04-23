package gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Date;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Category;
import model.Duration;
import model.Icon;
import model.TLEvent;
import model.TimelineMaker;

/**
 * This class is the controller of the EventPropertiesWindow. This handles all
 * events and has references for all objects in that window.
 * 
 */
public class EventPropertiesWindowController {

	/**
	 * The model of the entire program
	 */
	private TimelineMaker timelineMaker;

	/**
	 * The event this window is associated with
	 */
	private TLEvent oldEvent;

	@FXML
	// ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML
	// URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML
	// fx:id="buttonSeparator"
	private Separator buttonSeparator; // Value injected by FXMLLoader

	@FXML
	// fx:id="cancelButton"
	private Button cancelButton; // Value injected by FXMLLoader

	@FXML
	// fx:id="categoryComboBox"
	private ComboBox<String> categoryComboBox; // Value injected by FXMLLoader

	@FXML
	// fx:id="categoryLabel"
	private Label categoryLabel; // Value injected by FXMLLoader

	@FXML
	// fx:id="createButton"
	private Button createButton; // Value injected by FXMLLoader

	@FXML
	// fx:id="dateLabel"
	private Label dateLabel; // Value injected by FXMLLoader

	@FXML
	// fx:id="dateToLabel"
	private Label dateToLabel; // Value injected by FXMLLoader

	@FXML
	// fx:id="descriptionLabel"
	private Label descriptionLabel; // Value injected by FXMLLoader

	@FXML
	// fx:id="descriptionTextArea"
	private TextArea descriptionTextArea; // Value injected by FXMLLoader

	@FXML
	// fx:id="durationCheckBox"
	private CheckBox durationCheckBox; // Value injected by FXMLLoader

	@FXML
	// fx:id="endDateTextField"
	private TextField endDateTextField; // Value injected by FXMLLoader

	@FXML
	// fx:id="eventPropertiesWindowAnchor"
	private AnchorPane eventPropertiesWindowAnchor; // Value injected by
	// FXMLLoader

	@FXML
	// fx:id="iconComboBox"
	private ComboBox<String> iconComboBox; // Value injected by FXMLLoader

	@FXML
	// fx:id="newIconButton"
	private Button newIconButton; // Value injected by FXMLLoader

	/**
	 * The file chooser for adding new icons.
	 */
	private FileChooser fileChooser;

	@FXML
	// fx:id="iconLabel"
	private Label iconLabel; // Value injected by FXMLLoader

	@FXML
	// fx:id="startDateTextField"
	private TextField startDateTextField; // Value injected by FXMLLoader

	@FXML
	// fx:id="titleLabel"
	private Label titleLabel; // Value injected by FXMLLoader

	@FXML
	// fx:id="titleTextField"
	private TextField titleTextField; // Value injected by FXMLLoader

	@FXML
	// fx:id="typeLabel"
	private Label typeLabel; // Value injected by FXMLLoader

	private TextFieldValidator titleValidator;

	private TextFieldValidator startDateValidator, endDateValidator;

	// Handler for Button[fx:id="newIconButton"] onAction
	@FXML
	void newIconPressed(ActionEvent event) throws FileNotFoundException {
		fileChooser.setTitle("Open Icon");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Images", "*.png", "*.jpg", "*.gif"),
				new FileChooser.ExtensionFilter("JPG", "*.jpg"),
				new FileChooser.ExtensionFilter("PNG", "*.png"),
				new FileChooser.ExtensionFilter("GIF", "*.gif"));
		File file = fileChooser.showOpenDialog(new Stage());
		if (file != null) {
			InputStream is = new FileInputStream(file.getPath());
			timelineMaker.addIcon(new Icon(file.getName(), new Image(is, 20, 20, true, true), file.getPath()));
			iconComboBox.setItems(FXCollections.observableList(timelineMaker.getImageTitles()));
			iconComboBox.getSelectionModel().select(file.getName());
		}		
	}

	// Handler for Button[fx:id="cancelButton"] onAction
	@FXML
	void cancelPressed(ActionEvent event) {
		Node source = (Node) event.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		stage.close();
	}

	// Handler for Button[fx:id="createButton"] onAction
	@FXML
	void createPressed(ActionEvent event) {
		Category selectedCategory = timelineMaker.getSelectedTimeline()
				.getCategory(
						categoryComboBox.getSelectionModel().getSelectedItem());
		Icon icon = timelineMaker.getIcon(iconComboBox.getSelectionModel()
				.getSelectedItem());
		String title = titleTextField.getText();
		if (titleValidator.isValid() && (startDateValidator.isValid()) && (!endDateTextField.isVisible() || endDateValidator.isValid())) {
			Date startDate = processDate(startDateTextField.getText());
			Date endDate = null;
			String description = descriptionTextArea.getText();
			if (durationCheckBox.isSelected()) {
				endDate = processDate(endDateTextField.getText());
			}
			if (oldEvent != null)
				timelineMaker.editEvent(oldEvent, title, startDate, endDate,
						selectedCategory, description, icon);
			else
				timelineMaker.addEvent(title, startDate, endDate, selectedCategory,
						description, icon);
			Node source = (Node) event.getSource();
			Stage stage = (Stage) source.getScene().getWindow();
			stage.close();
		}
	}

	// Handler for CheckBox[fx:id="durationCheckBox"] onAction
	@FXML
	void durationPressed(ActionEvent event) {
		endDateTextField.setVisible(!endDateTextField.isVisible());
		dateToLabel.setVisible(!dateToLabel.isVisible());
	}

	@FXML
	// This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		endDateTextField.setVisible(false);
		dateToLabel.setVisible(false);
		oldEvent = null;
	}

	/**
	 * Populates the combo box with categories and icons.
	 */
	public void initComboBox() {
		categoryComboBox.setItems(FXCollections.observableList(timelineMaker
				.getSelectedTimeline().getCategoryTitles()));
		if (timelineMaker.getSelectedTimeline() != null) {
			if (oldEvent != null
					&& !oldEvent.getCategory().getName().equals(""))
				categoryComboBox.getSelectionModel().select(
						oldEvent.getCategory().getName());
			else
				categoryComboBox.getSelectionModel().select(
						timelineMaker.getSelectedTimeline()
						.getFirstCategory().getName());
		}

		iconComboBox.setItems(FXCollections.observableList(timelineMaker
				.getImageTitles()));
		if (timelineMaker.getSelectedTimeline() != null) {
			if (oldEvent != null && oldEvent.getIcon() != null)
				iconComboBox.getSelectionModel().select(
						oldEvent.getIcon().getName());
			else if (timelineMaker.getImageTitles().get(0) != null)
				iconComboBox.getSelectionModel().select("None");
		}
	}

	/**
	 * Initializes the data for this Controller. As close as we can get to a
	 * constructor
	 * 
	 * @param timelineMaker
	 *            The timeline maker to set
	 * @param event
	 *            The event to set
	 */
	public void initData(TimelineMaker timelineMaker, TLEvent event) {
		this.timelineMaker = timelineMaker;

		HashMap<String, String> errorStrings = new HashMap<String, String>();
		errorStrings.put("", "Event title cannot be blank.");
		fileChooser = new FileChooser();
		this.oldEvent = event;
		if (timelineMaker.getSelectedTimeline() != null) {
			if (event != null) {
				loadEventInfo(event);
				for (TLEvent e : timelineMaker.getSelectedTimeline().getEvents())
					if (!oldEvent.getName().equals(e.getName()))
						errorStrings.put(e.getName(), "Event already exists.");
			} else if (timelineMaker.getSelectedTimeline().getEvents() != null)
				for (TLEvent e : timelineMaker.getSelectedTimeline().getEvents())
					errorStrings.put(e.getName(), "Event already exists.");
		}

		titleValidator = new TextFieldValidator(titleTextField, "Enter a title.", errorStrings, "[! \\w]*$", "Only alphanumeric characters.");
		titleTextField.focusedProperty().addListener(titleValidator);

		initDateCheckers();
		initComboBox();
	}

	private void initDateCheckers() {
		HashMap<String, String> errorStrings = new HashMap<String, String>();
		errorStrings.put("", "Cannot be blank.");
		startDateValidator = new TextFieldValidator(startDateTextField, "mm/dd/yyyy", errorStrings, "(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/((19|20)\\d\\d)", "mm/dd/yyyy");
		endDateValidator = new TextFieldValidator(endDateTextField, "mm/dd/yyyy", errorStrings, "(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/((19|20)\\d\\d)", "mm/dd/yyyy");
		startDateTextField.focusedProperty().addListener(startDateValidator);
		endDateTextField.focusedProperty().addListener(endDateValidator);

	}

	/**
	 * Loads the data from an event into the fields of this model
	 * 
	 * @param event
	 *            The event to load info from
	 */
	private void loadEventInfo(TLEvent event) {
		titleTextField.setText(event.getName());
		if (event instanceof Duration) {
			durationCheckBox.setSelected(true);
			endDateTextField.setVisible(!endDateTextField.isVisible());
			dateToLabel.setVisible(!dateToLabel.isVisible());
			endDateTextField
			.setText(TLEvent.getFormattedDate(((Duration) event).getEndDate()));
		}
		startDateTextField.setText(TLEvent.getFormattedDate(event.getStartDate()));
		categoryComboBox.setValue(event.getCategory().getName());
		descriptionTextArea.setText(event.getDescription());
	}

	private Date processDate(String date) {
		String month = date.substring(0,2);
		String day = date.substring(3,5);
		String year = date.substring(6, date.length());
		return Date.valueOf(year + "-" + month + "-" + day);
	}

}

