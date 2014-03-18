package gui;

import java.net.URL;
import java.util.ResourceBundle;

import model.Timeline;
import model.Timeline.AxisLabel;
import model.TimelineMaker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * This class is the controller of the TimelinePropertiesWindow. This handles
 * all events and has references for all objects in that window.
 * 
 */
public class TimelinePropertiesWindowController {

	/**
	 * The model of the entire program
	 */
	private TimelineMaker timelineMaker;

	/**
	 * The timeline this window is associated with
	 */
	private Timeline timeline;

	@FXML
	// ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML
	// URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML
	// fx:id="appearanceLabel"
	private Label appearanceLabel; // Value injected by FXMLLoader

	@FXML
	// fx:id="appearanceSeparator"
	private Separator appearanceSeparator; // Value injected by FXMLLoader

	@FXML
	// fx:id="axisUnitComboBox"
	private ComboBox<AxisLabel> axisUnitComboBox; // Value injected by
													// FXMLLoader

	@FXML
	// fx:id="axisUnitLabel"
	private Label axisUnitLabel; // Value injected by FXMLLoader

	@FXML
	// fx:id="buttonSeparator"
	private Separator buttonSeparator; // Value injected by FXMLLoader

	@FXML
	// fx:id="cancelButton"
	private Button cancelButton; // Value injected by FXMLLoader

	@FXML
	// fx:id="colorBackgroundChooser"
	private ColorPicker colorBackgroundChooser; // Value injected by FXMLLoader

	@FXML
	// fx:id="colorLabelBackground"
	private Label colorLabelBackground; // Value injected by FXMLLoader

	@FXML
	// fx:id="colorTimelineChooser"
	private ColorPicker colorTimelineChooser; // Value injected by FXMLLoader

	@FXML
	// fx:id="colorLabelTimeline"
	private Label colorLabelTimeline; // Value injected by FXMLLoader

	@FXML
	// fx:id="createButton"
	private Button createButton; // Value injected by FXMLLoader

	@FXML
	// fx:id="fontComboBox"
	private ComboBox<?> fontComboBox; // Value injected by FXMLLoader

	@FXML
	// fx:id="fontLabel"
	private Label fontLabel; // Value injected by FXMLLoader

	@FXML
	// fx:id="informationLabel"
	private Label informationLabel; // Value injected by FXMLLoader

	@FXML
	// fx:id="timelinePropertiesWindowAnchor"
	private AnchorPane timelinePropertiesWindowAnchor; // Value injected by
														// FXMLLoader

	@FXML
	// fx:id="titleLabel"
	private Label titleLabel; // Value injected by FXMLLoader

	@FXML
	// fx:id="titleTextField"
	private TextField titleTextField; // Value injected by FXMLLoader

	// Handler for Button[fx:id="cancelButton"] onAction
	@FXML
	void cancelButtonPressed(ActionEvent event) {
		Node source = (Node) event.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		stage.close();
	}

	// Handler for Button[fx:id="createButton"] onAction
	@FXML
	void createButtonPressed(ActionEvent event) {
		String title = titleTextField.getText();
		if (title.equals("")) {
			// TODO title missing alert
			return;
		}
		Color backgroundColor = colorBackgroundChooser.getValue();
		Color timelineColor = colorTimelineChooser.getValue();
		AxisLabel axisUnit = axisUnitComboBox.getValue();
		Font font = null; // TODO set font?
		if (timeline != null)
			timelineMaker.editTimeline(timeline, title, timelineColor,
					backgroundColor, axisUnit, font);
		else
			timelineMaker.addTimeline(title, timelineColor, backgroundColor,
					axisUnit, font);

		Node source = (Node) event.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		stage.close();
	}

	@FXML
	// This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		timeline = null;
		initComboBox();
	}

	/**
	 * Initializes all the labels to the Combo box
	 */
	private void initComboBox() {
		AxisLabel[] labels = Timeline.AxisLabel.values();
		for (AxisLabel label : labels)
			axisUnitComboBox.getItems().addAll(label);
		axisUnitComboBox.setValue(labels[0]);
	}

	/**
	 * Initializes the timeline
	 * 
	 * @param timelineMaker
	 *            The timelinemaker stored here.
	 * @param timeline
	 *            The timeline stored here.
	 */
	public void initData(TimelineMaker timelineMaker, Timeline timeline) {
		this.timelineMaker = timelineMaker;
		if (timeline != null) {
			loadTimelineInfo(timeline);
			this.timeline = timeline;
		}
	}

	/**
	 * Loads the information into the timeline.
	 * 
	 * @param timeline
	 *            The timeline that information is loaded to.
	 */
	private void loadTimelineInfo(Timeline timeline) {
		titleTextField.setText(timeline.getName());
		axisUnitComboBox.setValue(timeline.getAxisLabel());
		colorBackgroundChooser.setValue(timeline.getColorBG());
		colorTimelineChooser.setValue(timeline.getColorTL());
	}

}
