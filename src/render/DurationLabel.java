/**
 * 
 */
package render;

import java.util.ArrayList;

import model.TimelineMaker;
import model.Duration;
import javafx.geometry.Pos;

/**
 * @author josh
 * 
 */
public class DurationLabel extends TLEventLabel {

	/**
	 * The width in pixels of the label
	 */
	private int width;

	/**
	 * Constructor calls the super constructor with the event name, assigns
	 * instance variables, and then calls init
	 * 
	 * @param event
	 *            the event this label is associated with
	 * @param xPos
	 *            the xPosition on the screen
	 * @param yPos
	 *            the yPosition on the screen
	 * @param width
	 *            the width of the label
	 * @param model
	 *            the program model
	 * @param eventLabels
	 *            the list of TLEventLabels
	 */
	DurationLabel(Duration event, int xPos, int yPos, int width,
			TimelineMaker model, ArrayList<TLEventLabel> eventLabels) {
		super(xPos, yPos, event, model, eventLabels);
		this.width = width;
		uniqueDesign(); // yeah this is kludgy
	}

	@Override
	public void uniqueHandlers() {
	}

	@Override
	public void updateDesign() {
		if (isSelected()) {
			setId("event-selected");
		} else {
			setId("duration-label");
		}
	}

	@Override
	public void uniqueDesign() {
		setPrefWidth(width);
		setAlignment(Pos.CENTER);
		setId("duration-label");
	}

}
