package storage;

import model.*;
import model.Timeline.AxisLabel;

import java.net.URL;
import java.sql.Date;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

import javafx.scene.paint.Color;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import model.*;
/**
 * phpDBHelper.java
 * 
 * Updates online database with newest data from the online database.
 * 
 * 
 * @author Conner Vick Wheaton College, CS 335, Spring 2014
 *         Project Phase 3 
 * 
 */
public class phpDBHelper {

	private static DBHelper database;
	private static HashMap<String,Timeline> tlMap;


	public void doit(){
		database = new DBHelper("timeline.db");
		tlMap = new HashMap<String,Timeline>();
		try{
		parse();
		} catch (ParseException e){
			System.out.println("json parsing fail");
		}
		Collection<Timeline> c = tlMap.values();
		for (Timeline t : c){
			
			if(!database.saveTimeline(t)){
				System.out.println("php save timeline fail");
			}
		}
		
	}
	public static void parse() throws ParseException{
		//parse example:

		JSONParser parser=new JSONParser(); //parser
		Object obj = parser.parse(getEvents()); //get json string and parse it
		JSONArray array = (JSONArray)obj; //turn parsed object into array

		Object objT = parser.parse(getTimelines()); //get json string and parse it
		JSONArray arrayT = (JSONArray)objT; //turn parsed object into array

		System.out.println("First Timeline");
		System.out.println(arrayT.get(0)); // events start at 0
		System.out.println();

		System.out.println("First Event");
		System.out.println(array.get(0)); // events start at 0
		System.out.println();

		JSONObject obj1 = (JSONObject)array.get(0);
		System.out.println("eid:"); //event id (unique)
		System.out.println(obj1.get("eid"));

		System.out.println("endDate:");
		System.out.println(obj1.get("endDate"));

		System.out.println("eventName:");
		System.out.println(obj1.get("eventName"));

		System.out.println("type:"); // atomic or duration
		System.out.println(obj1.get("type"));

		System.out.println("category:");
		System.out.println(obj1.get("category"));

		System.out.println("startDate:");
		System.out.println(obj1.get("startDate"));

		System.out.println("iconid");
		System.out.println(obj1.get("iconid"));

		System.out.println("description");
		System.out.println(obj1.get("description"));

		System.out.println("tid:"); // timeline ID all events for a timeline have this same id.
		System.out.println(obj1.get("tid"));

		System.out.println("First Event Again"); // this one has a different tid, so it has a different timeline.
		System.out.println(array.get(0)); 
		System.out.println();


		Iterator it = arrayT.iterator();
		JSONObject jobj;
		Timeline tl;
		String tid;

		while(it.hasNext()){
			jobj = (JSONObject)it.next();
			tid = (String) jobj.get("tid");
			tl = new Timeline((String) jobj.get("name"), AxisLabel.valueOf((String) jobj.get("axis_label")), 
					Color.web((String) jobj.get("axis_color")), Color.web((String) jobj.get("background_color")));
			tlMap.put((String) jobj.get("tid"), tl);
		}
		Iterator it2 = array.iterator();
		JSONObject jobj2;
		while(it2.hasNext()){
			jobj2 = (JSONObject)it2.next();
			TLEvent event;
			if (obj1.get("type").equals("duration") ) {
				event = new Duration((String)jobj2.get("eventName"), new Category((String)jobj2.get("category")),
						Date.valueOf((String) jobj2.get("startDate")), Date.valueOf((String) jobj2.get("EndDate")), Integer.parseInt((String) jobj2.get("iconid")), (String) jobj2.get("description"));
			} else {
				event = new Atomic((String)jobj2.get("eventName"), new Category((String)jobj2.get("category")),
						Date.valueOf((String) jobj2.get("startDate")), Integer.parseInt((String) jobj2.get("iconid")), (String) jobj2.get("description"));
			}
			tlMap.get((String) jobj2.get("tid")).addEvent(event);
		}
		
	}
		

	
		public static String getEvents(){ 
			try {

				//getting json string from database
				URL internet = new URL("http://cs.wheaton.edu/~kurt.andres/userTimelineEvent.php?name=kurt&password=password");

				Scanner sc = new Scanner(internet.openStream());
				return sc.nextLine();
			} catch (Exception e){
				return e.getMessage();
			}
		}
		public static String getUid(){ 
			try {

				//getting json string from database
				URL internet = new URL("http://cs.wheaton.edu/~kurt.andres/addUser.php?name=kurt&password=password");

				Scanner sc = new Scanner(internet.openStream());
				
				JSONParser parser=new JSONParser(); //parser
				Object obj = parser.parse(sc.nextLine()); //get json string and parse it
				JSONArray array = (JSONArray)obj; //turn parsed object into array
				JSONObject obj1 = (JSONObject)array.get(0);
				System.out.println(obj1.get("uid"));
				return (String)obj1.get("uid");
				
			} catch (Exception e){
				return e.getMessage();
			}
		}
		
		public static String getTimelines(){ 
			try {

				//getting json string from database
				URL internet = new URL("http://cs.wheaton.edu/~kurt.andres/addTimeline.php?uid="+getUid());

				Scanner sc = new Scanner(internet.openStream());
				return sc.nextLine();
			} catch (Exception e){
				return e.getMessage();
			}
		}
}