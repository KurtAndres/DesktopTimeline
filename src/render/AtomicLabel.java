package render;

import java.util.ArrayList;

import model.TimelineMaker;
import model.Atomic;

/**
 * Atomic version of TLEventLabel
 * 
 * @author Josh Wright February 15, 2014
 */
public class AtomicLabel extends TLEventLabel {

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
	 * @param model
	 *            the program model
	 * @param eventLabels
	 *            the list of TLEventLabels
	 */
	AtomicLabel(Atomic event, int xPos, int yPos, TimelineMaker model,
			ArrayList<TLEventLabel> eventLabels) {
		super(xPos, yPos, event, model, eventLabels);
	}

	@Override
	public void uniqueHandlers() {
	}

	@Override
	public void updateDesign() {
		if (isSelected()) {
			setId("event-selected");
		} else {
			setId("atomic-label");
		}
	}

	@Override
	public void uniqueDesign() {
		setId("atomic-label");
	}

}
