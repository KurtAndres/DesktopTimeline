package render;

import java.awt.MouseInfo;
import java.util.ArrayList;

import model.Category;
import model.TLEvent;
import model.TimelineMaker;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.WindowEvent;

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
	private boolean selected;
	
	/**
	 * The event associated with this label
	 */
	private TLEvent event;
	
	/**
	 * ArrayList of all other eventLabels, used for clearing previous selection
	 */
	private ArrayList<TLEventLabel> eventLabels;
	
	/**
	 * The model of the program to update selected event
	 */
	private TimelineMaker model;
	
	/**
	 * The x and y position of this event
	 */
	private int xPos;
	private int yPos;
        
        private Image icon;
	
	private ContextMenu contextMenu;
	
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
		contextMenu = new ContextMenu();
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
	 * Setter for selected, that updates the label in accordance with the selection value
	 * 
	 * @param selected
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
		updateDesign();
	}
	
	/**
	 * Initializes generic parts of TLEventLabel
	 */
	private void init(){
		initDesign();
		initContextMenu();
		initHandlers();
	}

	/**
	 * Initializes the ContextMenu which will display when the item is clicked
	 */
	private void initContextMenu() {
		contextMenu.setOnShowing(new EventHandler<WindowEvent>() {
		    public void handle(WindowEvent e) {
		        //System.out.println("showing");
		    }
		});
		
		Text t = new Text();
		t.setText(event.getName());
		t.setFont(Font.font("Verdana",20));
		t.setFill(Color.BLACK);
		CustomMenuItem name = new CustomMenuItem(t);
		
		TextArea test = new TextArea();
		test.setText(event.getDescription());
		test.setPrefWidth(200);
		test.setEditable(false);
		test.setWrapText(true);
		
		
		if(event.getCategory() != null){
			MenuItem category = new MenuItem("Category: "+event.getCategory().getName());
			CustomMenuItem text = new CustomMenuItem(test);
			contextMenu.getItems().addAll(name, category, text);
			setContextMenu(contextMenu);
		}
		
	}

	/**
	 * Sets up the "design" of the label. Border, position, etc.
	 */
	private void initDesign(){
		setLayoutX(xPos);
		setLayoutY(yPos);
		Category c = event.getCategory();
		Color clr = c.getColor();
		String color = clr.toString(); //This works fine, I kept textfill because it wont overwrite the stylesheet
		color = color.substring(2);
		setStyle("-fx-background-color: #" + color);
		//setTextFill(Color.web(event.getCategory().getColor().toString()));
		uniqueDesign();
	}

	/**
	 * Initialize generic handlers for the TLEventLabel
	 */
	private void initHandlers(){
		final Label label = this;
		setTooltip(new Tooltip("Double click to show info!"));
		setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				Platform.runLater(new Thread(new Runnable() {
					public void run() {
						setId("label-highlighted");
					}
				}));
			}
		});
		setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				new Thread(new Runnable() {
					public void run() {
						updateDesign();
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
								contextMenu.show(label, MouseInfo.getPointerInfo().getLocation().getX(), MouseInfo.getPointerInfo().getLocation().getY());	
							}
						}));
		            }
		        }
				for(TLEventLabel label : eventLabels){
					label.setSelected(false);
				}
				setSelected(true);
				model.selectEvent(event);
			}
		});
		uniqueHandlers();
	}
	
	/**
	 * Abstract method where unique design code can go. 
	 */
	public abstract void uniqueDesign();

	/**
	 * Initialize handlers unique to Duration or Atomic
	 */
	public abstract void uniqueHandlers();
	
	/**
	 * How the label will update itself
	 */
	public abstract void updateDesign();
}
