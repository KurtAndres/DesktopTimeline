package render;

import gui.EventPropertiesWindowController;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Category;
import model.TLEvent;
import model.TimelineMaker;

/**
 * An abstract class to create labels for Atomic and Duration events to render.
 * Currently the subclasses have a decent amount of repetition so some of that could be
 * moved up here.
 * 
 * @author Josh Wright
 * Created: February 15, 2014
 * Last Edited: March 7, 2014
 * 
 * Some ContextMenu code ripped from Oracle's documentation on ContextMenus
 */
public abstract class TLEventLabel extends Label {

	/**
	 * Whether this is the selected event or not
	 */
	private boolean selected, hovered;

	/**
	 * The event associated with this label
	 */
	protected TLEvent event;

	/**
	 * ArrayList of all other eventLabels, used for clearing previous selection
	 */
	private ArrayList<TLEventLabel> eventLabels;

	/**
	 * The model of the program to update selected event
	 */
	private TimelineMaker model;
	
	private Tooltip tooltip;

	/**
	 * The x and y position of this event
	 */
	private int xPos;
	private int yPos;

	private Image icon;

	/**
	 * Set the text of the label to text
	 * 
	 * @param text the label text
	 */
	TLEventLabel(int xPos, int yPos, TLEvent event, TimelineMaker model, ArrayList<TLEventLabel> eventLabels){
		super(event.getName());
		
		this.event = event;
		this.eventLabels = eventLabels;
		this.model = model;
		this.xPos = xPos;
		this.yPos = yPos;
		if(event.getIcon()!=null)
			this.icon = event.getIcon().getImage();
		init();
	}

	public Image getIcon(){
		return icon;
	}

	/**
	 * Getter for selected
	 * 
	 * @return selected
	 */
	public boolean isSelected() {
		return selected;
	}
	
	/**
	 * Getter for hovered
	 * 
	 * @return hovered
	 */
	public boolean isHovered() {
		return hovered;
	}

	/**
	 * Initializes generic parts of TLEventLabel
	 */
	private void init(){
		initDesign();
		initTooltip();
		initHandlers();
	}

	/**
	 * Initializes the Tooltip which will display when hovering over the label
	 */
	private void initTooltip() {
		tooltip = new Tooltip(tooltipText());
		tooltip.setGraphic(new ImageView(icon));
		tooltip.setPrefWidth(250);
		tooltip.setWrapText(true);
		setTooltip(tooltip);
	}

	/**
	 * Sets up the "design" of the label. Border, position, etc.
	 */
	private void initDesign(){
		setLayoutX(xPos);
		setLayoutY(yPos);
		Category c = event.getCategory();
		Color clr = c.getColor();
		String color = clr.toString();
		color = color.substring(2);
		setStyle("-fx-event-color: #" + color);
		initUniqueDesign();
	}

	/**
	 * Initialize generic handlers for the TLEventLabel
	 */
	private void initHandlers(){
		setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				Platform.runLater(new Thread(new Runnable() {
					public void run() {
						for(TLEventLabel label : eventLabels)
							label.setHovered(false);
						setHovered(true);
					}
				}));
			}
		});
		setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				new Thread(new Runnable() {
					public void run() {
						for(TLEventLabel label : eventLabels)
							label.setHovered(false);
						setHovered(false);
					}
				}).start();
			}
		});
		setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				if(e.getButton().equals(MouseButton.PRIMARY)){
					if(e.getClickCount() == 2){
						Platform.runLater(new Thread(new Runnable() {
							public void run() {
								try {
									FXMLLoader loader = new FXMLLoader(getClass().getResource(
											"../gui/EventPropertiesWindow.fxml"));
									Parent root = (Parent) loader.load();
									EventPropertiesWindowController controller = loader
											.<EventPropertiesWindowController> getController();
									controller.initData(model, event);
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
						}));
					}
				}
				for(TLEventLabel label : eventLabels)
					label.setSelected(false);
				setSelected(true);
				model.selectEvent(event);
			}
		});
	}
	
	public abstract void initUniqueDesign();
	
	/**
	 * Setter for selected, that updates the label in accordance with the selection value
	 * 
	 * @param selected
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
		updateDesign();
	}
	
	/**
	 * Setter for hovered, that updates the label in accordance with the hover value
	 * 
	 * @param selected
	 */
	protected void setHovered(boolean hovered) {
		this.hovered = hovered;
		updateDesign();
	}

	/**
	 * How the label will update itself
	 */
	public abstract void updateDesign();
	
	/**
	 * Retrieve the text to display when hovering over the event.
	 * @return the text
	 */
	public abstract String tooltipText();
}
