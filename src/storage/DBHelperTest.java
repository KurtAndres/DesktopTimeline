/**
 * 
 */
package storage;

import static org.junit.Assert.*;

import java.sql.Date;

import org.junit.Test;

import model.*;

/**
 * DBHelperTest.java
 * 
 * @author Josh Wright Created: Feb 14, 2014 Package: database
 * 
 *         The tests are a little on the simple side, but the tests are made to
 *         allow for straightforward implementation, and it is difficult to make
 *         sure the database is setup properly for each test (since the data in
 *         the database lasts after the tests are finished).
 * 
 *         At the time of writing this, all the tests pass. If for some reason
 *         they don't, try deleting the helpertest.db database in the databases
 *         directory.
 * 
 */
public class DBHelperTest {

	DBHelper db = new DBHelper("databases/helpertest.db");

	/**
	 * Test method for
	 * {@link database.DBHelper#changeTimeline(entities.Timeline, entities.Timeline)}
	 * . Make sure that the changeTimeline method swaps both the timelines
	 * (names and events).
	 */
	@Test
	public void testChangeTimeline() {
		Timeline oldTimeline = new Timeline("ding");
		Timeline newTimeline = new Timeline("dong");
		oldTimeline.addEvent(new Duration("the", new Category(""), new Date(0),
				new Date(0), -1, ""));
		newTimeline.addEvent(new Duration("witch", new Category(""),
				new Date(0), new Date(0), -1, ""));
		db.saveTimeline(oldTimeline);
		assertEquals(newTimeline.getName(), db.getTimelines()[0].getName());
		assertEquals(newTimeline.getEvents()[0].getName(),
				db.getTimelines()[0].getEvents()[0].getName());

		// Cleanup (JUnit tests with databases is sneaky)
		db.removeTimeline(newTimeline);
	}

	/**
	 * Test method for
	 * {@link database.DBHelper#writeTimeline(entities.Timeline)}. Make sure
	 * that the writeTimeline method writes both the names and events of the
	 * timeline.
	 */
	@Test
	public void testWriteTimeline() {
		Timeline timeline = new Timeline("mightbe"); // is causes problems in
														// the SQL
		timeline.addEvent(new Duration("dead", new Category(""), new Date(0),
				new Date(0), -1, ""));
		db.saveTimeline(timeline);
		assertEquals(timeline.getName(), db.getTimelines()[0].getName());
		assertEquals(timeline.getEvents()[0].getName(),
				db.getTimelines()[0].getEvents()[0].getName());

		// Cleanup
		db.removeTimeline(timeline);
	}

	/**
	 * Test method for
	 * {@link database.DBHelper#removeTimeline(entities.Timeline)}. Make sure
	 * that the removeTimeline method removes the timeline from the database.
	 */
	@Test
	public void testRemoveTimeline() {
		Timeline timeline = new Timeline("which");
		timeline.addEvent(new Duration("old", new Category(""), new Date(0),
				new Date(0), -1, ""));
		db.saveTimeline(timeline);
		db.removeTimeline(timeline);
		assertTrue(db.getTimelines().length == 0);
	}

	/**
	 * Test method for {@link database.DBHelper#getTimelines()}. Make sure that
	 * the getTimelines method gets all the timelines in the database, including
	 * their names and events.
	 */
	@Test
	public void testGetTimelines() {
		Timeline firstTimeline = new Timeline("which");
		Timeline secondTimeline = new Timeline("the");
		firstTimeline.addEvent(new Atomic("wicked", new Category(""), new Date(
				0), -1, ""));
		secondTimeline.addEvent(new Duration("witch", new Category(""),
				new Date(0), new Date(0), -1, ""));
		db.saveTimeline(firstTimeline);
		db.saveTimeline(secondTimeline);
		assertEquals(firstTimeline.getName(), db.getTimelines()[0].getName());
		assertEquals(firstTimeline.getEvents()[0].getName(),
				db.getTimelines()[0].getEvents()[0].getName());
		assertEquals(secondTimeline.getName(), db.getTimelines()[1].getName());
		assertEquals(secondTimeline.getEvents()[0].getName(),
				db.getTimelines()[1].getEvents()[0].getName());

		// Cleanup
		db.removeTimeline(firstTimeline);
		db.removeTimeline(secondTimeline);
	}

}
