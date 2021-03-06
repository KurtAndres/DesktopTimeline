package model;

import render.*;
import gui.*;
import gui.Driver;
import storage.*;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import model.Timeline.AxisLabel;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * TimelineMaker.java
 * 
 * The model of the timeline editor and viewer. Contains all necessary objects
 * to model the application.
 * 
 * @author Josh Wright and Andrew Thompson Wheaton College, CS 335, Spring 2014
 *         Project Phase 1 Feb 15, 2014
 * 
 */
public class TimelineMaker {
	/**
	 * A list of all the timelines in this application.
	 */
	private ArrayList<Timeline> timelines;
	/**
	 * A list of all the icons in this application.
	 */
	private ArrayList<Icon> icons;
	/**
	 * The timeline selected in this application.
	 */
	private Timeline selectedTimeline;
	/**
	 * The username of the application's user. Used for database interaction.
	 */
	public static String user;
	/**
	 * The password for the application's user. Used for database interaction.
	 */
	public static String pass;	
	/**
	 * The event selected in this application.
	 */
	private TLEvent selectedEvent;
	/**
	 * The database for storing timelines of this application.
	 */
	private DBHelper database;
	/**
	 * The main GUI window for this application.
	 */
	private MainWindowController mainWindow;

	/**
	 * The graphics object for displaying timelines in this application.
	 */
	public TimelineGraphics graphics;

	/**
	 * Constructor. Create a new TimelineMaker application model with database,
	 * graphics, and GUI components. 
	 */
	public TimelineMaker() {
			

		database = new DBHelper("timeline.db");
		graphics = new TimelineGraphics(this);
		timelines = new ArrayList<Timeline>();
		icons = new ArrayList<Icon>();
		icons.add(new Icon("None", null, null));
		phpDBHelper p = new phpDBHelper(user, pass);
		for (Timeline t : database.getTimelines())
			database.removeTimeline(t);

		p.doit();

		try {
			for (Timeline t : database.getTimelines())
				timelines.add(t);
			HashMap<Category, String> categories = database.getCategories();
			for (Timeline t : timelines) {
				for (Category c : categories.keySet()) {
					if (t.getName().equals(categories.get(c))) {
						t.addCategory(c);
					}
				}
			}
			for (Timeline t : timelines) { // Sets categories.
				if (t.getEvents() == null)
					continue;
				for (TLEvent e : t.getEvents()) {
					Category toSet = t.getCategory(e.getCategory().getName());
					if (toSet != null) {
						e.setCategory(toSet);
					}
				}
			}
			for (Icon icon : database.getIcons()) {
				icons.add(icon);
			}
			populateEventIcons();
			selectedTimeline = timelines.get(0);
			selectedEvent = null;
			Driver.addMemento(this);
		} catch (IndexOutOfBoundsException e) {
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error loading from Database.");
		}
	}

	/**
	 * 
	 * @param t
	 *            The icon to be returned
	 * @return The icon with a name matching String t
	 */
	public Icon getIcon(String t) {
		for (Icon i : icons) {
			if (i.getName().equals(t))
				return i;
		}
		return icons.get(0);
	}

	/**
	 * 
	 * @return An ArrayList representation of all the icon names.
	 */
	public ArrayList<String> getImageTitles() {
		ArrayList<String> iconTitles = new ArrayList<String>();
		for (Icon i : icons) {
			iconTitles.add(i.getName());
		}
		return iconTitles;
	}

	/**
	 * 
	 * @param i Icon to be added to the list of icons and to the database.
	 */
	public void addIcon(Icon i) {
		if (i != null) {
			icons.add(i);
			database.saveIcon(i);
			Driver.addMemento(this);
		}		
	}

	/**
	 * Deletes an icon from the list of icons and the database.
	 * 
	 * @param icon
	 *            the name of the icon to be deleted
	 * @return success
	 */
	public boolean deleteIcon(String icon) {

		if (icons.size() <= 1)
			return false;
		Icon ico = new Icon(null, null, null);
		for (Icon i : icons)
			if (i.getName().equals(icon)) {
				ico = i;
				break;
			}
		for (Timeline t : timelines) {
			Iterator<TLEvent> eventIterator = t.getEventIterator();
			TLEvent e;
			while (eventIterator.hasNext()) {
				e = eventIterator.next();
				if (e.getIcon() == ico)
					e.setIcon(null);
			}
		}
		boolean toReturn = icons.remove(ico);
		Driver.addMemento(this);

		return toReturn;
	}

	/**
	 * Retrieve a list of the names of all the timelines.
	 * 
	 * @return timelines
	 */
	public ArrayList<String> getTimelineTitles() {
		ArrayList<String> toReturn = new ArrayList<String>();
		for (Timeline t : timelines) {
			toReturn.add(t.getName());
		}
		return toReturn;
	}

	/**
	 * Retrieve the timeline with the parameterized name.
	 * 
	 * @param title
	 *            The name of the timeline to be found
	 * @return The timeline with the correct name; null otherwise.
	 */
	public Timeline getTimeline(String title) {
		for (Timeline t : timelines)
			if (t.getName().equals(title))
				return t;
		return null;
	}

	/**
	 * 
	 * @return the currently selected timeline.
	 */
	public Timeline getSelectedTimeline() {
		return selectedTimeline;
	}

	/**
	 * Set the selected timeline. Find the timeline of the parameterized title
	 * and set selectedTimeline to it. Update selectedTimeline, selectedTLEvent,
	 * and graphics.
	 * 
	 * @param title
	 *            of the timeline
	 */
	public void selectTimeline(String title) {
		selectedTimeline = getTimeline(title);
		selectedEvent = null;
		if (selectedTimeline != null)
			updateGraphics();
	}

	/**
	 * Selects the default timeline in the case that an event is deleted.
	 */
	public void selectDefaultTimeline() {
		if (timelines.size() > 0)
			selectedTimeline = timelines.get(0);
		else
			selectedTimeline = null;
	}

	/**
	 * Add a timeline to this model. Update selectedTimeline, selectedTLEvent,
	 * graphics, and database.
	 * 
	 * @param t
	 *            the timeline to be added
	 */
	public void addTimeline(String title, Color colorTL, Color colorBG,
			AxisLabel axisUnit, Font font) {
		Timeline t = new Timeline(title, axisUnit, colorTL, colorBG);
		selectedTimeline = t;
		selectedEvent = null;
		timelines.add(selectedTimeline);

		database.saveTimeline(selectedTimeline);
		mainWindow.populateListView();
		updateGraphics();
		Driver.addMemento(this);
	}

	/**
	 * Remove a timeline from this model. Update selectedTimeline,
	 * selectedTLEvent, graphics, and database.
	 * 
	 * @param t
	 *            the timeline to be removed
	 */
	public void deleteTimeline() {
		if (selectedTimeline != null) {
			timelines.remove(selectedTimeline);
			database.removeTimeline(selectedTimeline);
			selectedTimeline = null;
			selectedEvent = null;
			graphics.clearScreen();
			mainWindow.populateListView();
			Driver.addMemento(this);
		}
	}

	/**
	 * Edit the selected timeline. Remove the selected timeline and replace it
	 * with the parameterized one. Update selectedTimeline, selectedTLEvent,
	 * graphics, and database.
	 * 
	 * @param t The new timeline
	 */

	public void editTimeline(Timeline t, String title, Color colorTL,
			Color colorBG, AxisLabel axisUnit, Font font) {
		timelines.remove(selectedTimeline);
		Timeline newTimeline = new Timeline(title, t.getEvents(), t.getCategories(), colorTL,
				colorBG, axisUnit);
		newTimeline.setID(t.getID());
		timelines.add(newTimeline);
		database.editTimelineInfo(newTimeline);
		selectedTimeline = newTimeline;
		mainWindow.populateListView();
		updateGraphics();
		Driver.addMemento(this);
	}

	/**
	 * Populates the list view of the main window.
	 */
	public void populateView() {
		mainWindow.populateListView();
	}

	/**
	 * Retrieve the currently selected event.
	 * 
	 * @return selectedTLEvent
	 */
	public TLEvent getSelectedEvent() {
		return selectedEvent;
	}

	/**
	 * Set the selected event.
	 * 
	 * @param e
	 *            The event to be selected
	 */
	public void selectEvent(TLEvent e) {
		if (e != null)
			selectedEvent = e;
	}

	/**
	 * Add an event to the selected timeline. Update selectedTimeline,
	 * selectedTLEvent, graphics, and database.
	 * 
	 * @param e
	 *            the new event
	 */

	public void addEvent(String title, Date startDate, Date endDate,
			Category category, String description, Icon icon) {	
		TLEvent event;
		if (endDate != null) {
			event = new Duration(title, category,
					startDate, endDate, icon.getId(), description);
		} else {
			event = new Atomic(title, category,
					startDate, icon.getId(), description);
		}
		if (!icon.getName().equals("None") || event.getIcon() == null) {
			event.setIcon(icon);
		}
		if (selectedTimeline != null) {
			selectedTimeline.addEvent(event);
			selectedEvent = event;
			updateGraphics();
			database.saveEvent(event, selectedTimeline.getName());
			Driver.addMemento(this);
		}
	}

	/**
	 * Delete the selected event from the timeline. Update selectedTimeline,
	 * selectedTLEvent, graphics, and database.
	 */
	public void deleteEvent() {
		if (selectedEvent != null && selectedTimeline != null
				&& selectedTimeline.contains(selectedEvent)) {
			selectedTimeline.deleteEvent(selectedEvent);
			database.removeEvent(selectedEvent, selectedTimeline.getName());
			selectedEvent = null;
			updateGraphics();
			Driver.addMemento(this);
		}
	}

	/**
	 * Edit the selected event. Remove the currently selected event from the
	 * timeline and replace it with the parameter. Update selectedTimeline,
	 * selectedTLEvent, graphics, and database.
	 * 
	 * @param e
	 *            the new event
	 */
	public void editEvent(TLEvent oldEvent, String title, Date startDate,
			Date endDate, Category category, String description, Icon icon) {
		if (selectedEvent != null && selectedTimeline != null
				&& selectedTimeline.contains(selectedEvent)) {		
			selectedTimeline.deleteEvent(selectedEvent);
			TLEvent toAdd;
			if (endDate != null)
				toAdd = new Duration(title, category, startDate, endDate,
						icon.getId(), description);
			else
				toAdd = new Atomic(title, category, startDate, icon.getId(),
						description);
			if (!icon.getName().equals("None"))
				toAdd.setIcon(icon);
			toAdd.setID(oldEvent.getID());
			selectedEvent = toAdd;
			selectedTimeline.addEvent(toAdd);
			toAdd.setCategory(category);
			updateGraphics();
			database.editEvent(toAdd, selectedTimeline.getName());
			Driver.addMemento(this);
		}
	}


	/**
	 * Adds a category to the selected timeline and saves it.
	 * 
	 * @param category The category to add.
	 */
	public void addCategory(Category category) {
		selectedTimeline.addCategory(category);
		database.saveCategory(category, selectedTimeline.getName());
		Driver.addMemento(this);
	}

	/**
	 * Deletes a category and all events belonging to that category.
	 * 
	 * @param category The category to delete.
	 */
	public void deleteCategory(Category category) {	
		for(TLEvent e : selectedTimeline.getEvents()) {
			if(e.getCategory().getName().equals(category.getName())){
				selectedEvent = e;
				deleteEvent();
			}
		}
		selectedTimeline.deleteCategory(category);
		database.removeCategory(category, selectedTimeline.getName());
		Driver.addMemento(this);
	}

	/**
	 * Edits a category
	 * 
	 * @param category
	 *            edits this category in the database.
	 */
	public void editCategory(Category category) {
		database.editCategory(category, selectedTimeline.getName());
		Driver.addMemento(this);
	}

	/**
	 * Update the graphics for the display screen.
	 */
	public void updateGraphics() {
		graphics.clearScreen();
		if (selectedTimeline != null)
			graphics.renderTimeline(selectedTimeline);
	}

	/**
	 * Sets the mainWindow controller object
	 * 
	 * @param mainWindow
	 *            the mainWindow to set
	 */
	public void setMainWindow(MainWindowController mainWindow) {
		this.mainWindow = mainWindow;
	}

	/**
	 * Gets the size of the timeline array
	 * 
	 * @return the size of the timelines array.
	 */
	public int timeSize() {
		return timelines.size();
	}

	/**
	 * An attempt at associating the events with their icons on start-up.
	 * Intended to load the events with their icons from the database.
	 */
	private void populateEventIcons() {
		HashMap<Integer, Icon> iconMap = new HashMap<Integer, Icon>();
		for (Icon i : icons) {
			iconMap.put(i.getId(), i);
		}
		for (Timeline t : timelines) {
			if (t.getEvents() == null)
				continue;
			for (TLEvent e : t.getEvents()) {
				e.setIcon(iconMap.get(e.getIconIndex()));
				if (e.getIcon() == null)
					e.setIcon(icons.get(0));
			}
		}
	}

	/**
	 * Creates a Memento object storing the current state of the TimelineMaker.
	 */
	public TimelineMaker.Memento createMemento(){
		//Does not store the mainWindow or the graphics object, as the only state they store is the TimelineMaker
		
		Memento m = new Memento();

		//Deep copy the timelines
		m.timelines = new ArrayList<Timeline>();
		for (Timeline t : this.timelines){
			m.timelines.add(t.clone());
		}

		//Deep copy the icons
		m.icons = new ArrayList<Icon>();
		for (Icon icon : this.icons) {
			m.icons.add(new Icon(icon.getName(), icon.getImage(), icon.getPath()));
		}

		if(this.selectedTimeline != null)
			m.selectedTimeline = this.selectedTimeline.clone();
		else
			m.selectedTimeline = null;
		if(this.selectedEvent != null)		
			m.selectedEvent = this.selectedEvent.clone();
		else
			m.selectedEvent = null;

		return m;
	}

	/**
	 * Restores the state of the TimelineMaker to the given Memento.
	 * 
	 * @param m The Memento to load into the TimelineMaker.
	 * @return The updated TimelineMaker object
	 */
	public void loadMemento(Memento m){
		this.timelines = new ArrayList<Timeline>();
		for (Timeline t : m.timelines){
			this.timelines.add(t.clone());
			database.saveTimeline(t);
			if(t.getEvents() != null)
				for(TLEvent e : t.getEvents()){
					database.saveEvent(e, t.getName());
				}
		}

		for (Icon icon : m.icons) {
			if(icon.getPath() != null){
				icons.add(icon);
				database.saveIcon(icon);
			}
		}

		selectedTimeline = m.selectedTimeline;
		selectedEvent = m.selectedEvent;

		//populateEventIcons();
		mainWindow.populateListView();
		updateGraphics();
	}

	/**
	 * Memento class for the purpose of supporting undo and redo. Stores the state of the TimelineMaker.
	 * @author Leanne
	 *
	 */
	public class Memento{
		/**
		 * A list of all the timelines in this application.
		 */
		private ArrayList<Timeline> timelines;
		/**
		 * A list of all the icons in this application.
		 */
		private ArrayList<Icon> icons;
		/**
		 * The timeline selected in this application.
		 */
		private Timeline selectedTimeline;		
		/**
		 * The event selected in this application.
		 */
		private TLEvent selectedEvent;

	}

}



