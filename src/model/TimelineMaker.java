package model;

import render.*;
import gui.*;
import storage.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Timeline.AxisLabel;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.awt.GridLayout;

import javax.swing.*;

import java.awt.event.*;
import java.io.IOException;

import org.json.simple.parser.ParseException;

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

	private final String help_text = "\tHow to use this Timeline Maker:  \n"
			+ "*Use the buttons on the left to create, edit, or delete timelines. Timelines may have titles and background colors, and they may be displayed in a number of different units.\n"
			+ "*IMPORTANT: Timeline dates are in year-month-day form. You have to include the dashes.\n"
			+ "*Each timeline has a set of events. Create events with the \"add\" button.\n"
			+ "*To edit and delete events, select them on the rendered timeline and then proceed to edit or delete them.\"\n"
			+ "*Each timeline also has a set of categories. There must be at least one category, the default category, which may be edited. Each category has a name and a color associated with it.\"\n"
			+ "*Image icons may be added to timeline events. Upload images using the right side-bar and set them in the event editing window.\n"
			+ "*Double click events for surprises!";

	private final String about_text = "\tCredits: \n\n"
			+ "@Authors Andrew Sutton, Josh Wright, Kayley Lane, Conner Vick, Brian Williamson\n\n"
			+ "\tSoftware Dev 2014";

	/**
	 * Constructor. Create a new TimelineMaker application model with database,
	 * graphics, and GUI components. 
	 */
	public TimelineMaker() {
		System.out.println(pass+" "+user);		
		
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
			for (Timeline t : timelines) { // Very lame. Should have better implementation but don't have time.
				for (Category c : categories.keySet()) {
					if (t.getName().equals(categories.get(c))) {
						t.addCategory(c);
					}
				}
			}
			for (Timeline t : timelines) { // sets categories.
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
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Your database is empty.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error loading from Database.");
		}
	}
	
	/**
	 * Second constructor for constructing a timelineMaker from a Memento.
	 * @param m The Memento to construct the TimelineMaker from.
	 * @throws IOException 
	 */
	public TimelineMaker(Memento m) throws IOException{
		database = new DBHelper("timeline.db");
		graphics = m.graphics;
		mainWindow = m.mainWindow;
		timelines = new ArrayList<Timeline>();
		icons = new ArrayList<Icon>();
		icons.add(new Icon("None", null, null));
		
		for (Timeline t : m.timelines)
			timelines.add(t);
		HashMap<Category, String> categories = m.categories;
		for (Timeline t : timelines) {
			for (Category c : categories.keySet()) {
				if (t.getName().equals(categories.get(c))) {
					t.addCategory(c);
				}
			}
		}
		for (Timeline t : timelines) { // sets categories.
			if (t.getEvents() == null)
				continue;
			for (TLEvent e : t.getEvents()) {
				Category toSet = t.getCategory(e.getCategory().getName());
				if (toSet != null) {
					e.setCategory(toSet);
				}
			}
		}
		for (Icon icon : m.icons) {
			icons.add(icon);
		}
		populateEventIcons();
		selectedTimeline = m.selectedTimeline;
		selectedEvent = m.selectedEvent;
		
		try {
			phpPushHelper.send(this);
		} catch (ParseException e) {
			e.printStackTrace();
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
	 * @param i
	 *            Icon to be added to the list of icons and to the database.
	 */
	public void addIcon(Icon i) {
		if (i != null) {
			icons.add(i);
			database.saveIcon(i);
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
		// TODO GET THIS WORKING
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
		return icons.remove(ico);
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
		// gui.updateTimelines(getTimelineTitles(), selectedTimeline.getName());
		updateGraphics();
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
		}
	}

	/**
	 * Edit the selected timeline. Remove the selected timeline and replace it
	 * with the parameterized one. Update selectedTimeline, selectedTLEvent,
	 * graphics, and database.
	 * 
	 * @param t
	 *            the new timeline
	 */

	public void editTimeline(Timeline t, String title, Color colorTL,
			Color colorBG, AxisLabel axisUnit, Font font) {
		timelines.remove(selectedTimeline);
		Timeline newTimeline = new Timeline(title, t.getEvents(), colorTL,
				colorBG, axisUnit);
		newTimeline.setID(t.getID());
		timelines.add(newTimeline);
		database.editTimelineInfo(newTimeline);
		selectedTimeline = newTimeline;
		mainWindow.populateListView();
		updateGraphics();
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
		}
	}
	

	/**
	 * Adds a category to the selected timeline and saves it.
	 * 
	 * @param category
	 *            saves this category to the database.
	 */
	public void addCategory(Category category) {
		selectedTimeline.addCategory(category);
		database.saveCategory(category, selectedTimeline.getName());
	}

	/**
	 * Deletes a category
	 * 
	 * @param category
	 *            removes this category from the database.
	 */
	public void deleteCategory(Category category) {
		//All events which belong to the category are also deleted.
		for(TLEvent e : selectedTimeline.getEvents()){
			if(e.getCategory().getName().equals(category.getName())){
				selectedEvent = e;
				deleteEvent();
			}
		}
		selectedTimeline.deleteCategory(category);
		database.removeCategory(category, selectedTimeline.getName());
	}

	/**
	 * Edits a category
	 * 
	 * @param category
	 *            edits this category in the database.
	 */
	public void editCategory(Category category) {
		database.editCategory(category, selectedTimeline.getName());
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
	 * Gets the help text
	 * 
	 * @return the final string help_text.
	 */
	public String getHelpText() {
		return help_text;
	}

	/**
	 * Gets the about text
	 * 
	 * @return the final string about_text.
	 */
	public String getAboutText() {
		return about_text;
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
		System.out.println("Creating Memento");
		Memento m = new Memento();
		//m.timelines = this.timelines;
		//m.icons = this.icons;
		//m.selectedTimeline = this.selectedTimeline;
		//m.selectedEvent = this.selectedEvent;
		m.database = this.database;
		m.mainWindow = this.mainWindow;
		m.graphics = this.graphics;
		
		//Deep copy the timelines
		for (Timeline t : this.timelines)
			m.timelines.add(t.clone()); //Performs a deep copy each timeline
		
		/*HashMap<Category, String> categories = new HashMap<Category, String>();
		for(Timeline t : timelines){
			Iterator<Category> it = t.getCategoryIterator();
			while(it.hasNext())
				categories.put(it.next(), t.getName());
		}
		m.categories = categories;*/
		
		//Deep copy the icons
		for (Icon icon : this.icons) {
			m.icons.add(new Icon(icon.getName(), icon.getImage(), icon.getPath()));
		}
		
		m.selectedTimeline = this.selectedTimeline.clone();
		m.selectedEvent = this.selectedEvent.clone();
		
		m.mainWindow = this.mainWindow.clone();
		
		return m;
	}
	
	/**
	 * Restores the state of the TimelineMaker to the given Memento.
	 * 
	 * @param m The Memento to load into the TimelineMaker.
	 * @return The updated TimelineMaker object
	 */
	public void loadMemento(Memento m){
		/*try {
			return new TimelineMaker(m);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return this;
		}*/
		
		/*this.timelines = m.timelines;
		this.icons = m.icons;
		this.selectedTimeline = m.selectedTimeline;
		this.selectedEvent = m.selectedEvent;
		this.database = m.database;
		this.mainWindow = m.mainWindow;
		this.graphics = m.graphics;
		populateEventIcons();*/
		
		for (Timeline t : m.timelines)
			this.timelines.add(t.clone());
		
		for (Timeline t : this.timelines) {
			for (Category c : m.categories.keySet()) {
				if (t.getName().equals(m.categories.get(c))) {
					t.addCategory(c);
				}
			}
		}
		for (Timeline t : this.timelines) { // Sets categories to events.
			if (t.getEvents() == null)
				continue;
			for (TLEvent e : t.getEvents()) {
				Category toSet = t.getCategory(e.getCategory().getName());
				if (toSet != null) {
					e.setCategory(toSet);
				}
			}
		}
		for (Icon icon : m.icons) {
			icons.add(icon);
		}
		populateEventIcons();
		selectedTimeline = m.selectedTimeline;
		selectedEvent = m.selectedEvent;
		
		//return this;
		
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
		private TimelineGraphics graphics;
		
		private HashMap<Category, String> categories;

		
	}

}



