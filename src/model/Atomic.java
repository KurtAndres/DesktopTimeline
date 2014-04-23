package model;

import java.sql.Date;

/**
 * Extension of class TLEvent to represent atomic (single date) events
 * 
 * @author Kayley Lane
 * 
 */
public class Atomic extends TLEvent {
	
	public Atomic(String name){
		super(name);
	}

	/**
	 * 
	 * @param name
	 *            the name of this event
	 * @param category
	 *            the category of this event
	 * @param startDate
	 *            this event's start date
	 * @param iconIndex
	 *            the integer of this icon's index for the database
	 * @param description
	 *            this event's description
	 */
	public Atomic(String name, Category category, Date startDate,
			int iconIndex, String description) {
		super(name, startDate, category, iconIndex, description);
	}

	public Atomic clone(){
		Atomic toReturn = new Atomic(this.name, this.getCategory().clone(), 
				(Date) this.getStartDate().clone(), this.getIconIndex(), this.getDescription());	

		return toReturn;
	}
}
