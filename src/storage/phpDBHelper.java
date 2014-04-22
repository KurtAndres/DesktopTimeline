package storage;

import model.*;
import model.Timeline.AxisLabel;

import java.net.URL;
import java.sql.Date;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

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
	private static HashMap<String,String> tlNameIdMap;
	private static HashMap<Category, String> catMap;
	private static String user, pass;

	public phpDBHelper(String u, String p){
		user = u;
		pass = p;
		
	}
	public void doit(){
		database = new DBHelper("timeline.db");
		tlMap = new HashMap<String,Timeline>();
		tlNameIdMap = new HashMap<String,String>();
		catMap = new HashMap<Category, String>();
		System.out.println("u:"+user+" p:"+pass);
		try{
			
		parse();
		} catch (ParseException e){
			System.out.println("json parsing fail");
		} 
		
		
		Set<Category> s = catMap.keySet();
		for(Category c: s){
			database.saveCategory(c,tlNameIdMap.get(catMap.get(c)));
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
/*
		System.out.println("First Timeline");
		System.out.println(arrayT.get(0)); // events start at 0
		System.out.println();
		System.out.println("2nd Timeline");
		System.out.println(arrayT.get(1)); // events start at 0
		System.out.println();

		System.out.println("First Event");
		System.out.println(array.get(0)); // events start at 0
		System.out.println();
		
		System.out.println("2nd Event");
		System.out.println(array.get(1)); // events start at 0
		System.out.println();
		
		System.out.println("3rd Event");
		System.out.println(array.get(2)); // events start at 0
		System.out.println();

		System.out.println("4th Event");
		System.out.println(array.get(3)); // events start at 0
		System.out.println();

		System.out.println("5th Event");
		System.out.println(array.get(4)); // events start at 0
		System.out.println();
*/

/*
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
*/

		Iterator it = arrayT.iterator();
		JSONObject jobj;
		Timeline tl;
		String tid;

		do{
			jobj = (JSONObject)it.next();
			tid = (String) jobj.get("tid");
			tl = new Timeline((String) jobj.get("name"), AxisLabel.valueOf((String) jobj.get("axis_label")), 
					Color.web((String) jobj.get("axis_color")), Color.web((String) jobj.get("background_color")));
			tlMap.put((String) jobj.get("tid"), tl);
			tlNameIdMap.put((String) jobj.get("tid"),(String) jobj.get("name"));
		}while(it.hasNext());
		
		Iterator it2 = array.iterator();
		JSONObject jobj2;
		do{
			jobj2 = (JSONObject)it2.next();
			TLEvent event;
			Category cat = new Category((String)jobj2.get("category"));
			cat.setColor(Color.web("0xffffffff"));
			catMap.put(cat,(String)jobj2.get("tid"));
			if (jobj2.get("type").equals("duration") ) {
				event = new Duration((String)jobj2.get("eventName"),  cat,
						Date.valueOf((String) jobj2.get("startDate")), Date.valueOf((String) jobj2.get("endDate")), Integer.parseInt((String) jobj2.get("iconid")), (String) jobj2.get("description"));
			} else {
				event = new Atomic((String)jobj2.get("eventName"), cat,
						Date.valueOf((String) jobj2.get("startDate")), Integer.parseInt((String) jobj2.get("iconid")), (String) jobj2.get("description"));
			}
			tlMap.get((String) jobj2.get("tid")).addEvent(event);
			
		}while(it2.hasNext());
		
	}
		

	
		public static String getEvents(){ 
			try {

				//getting json string from database
				URL internet = new URL("http://cs.wheaton.edu/~kurt.andres/userTimelineEvent.php?name="+user+"&password="+pass);

				Scanner sc = new Scanner(internet.openStream());
				return sc.nextLine();
			} catch (Exception e){
				return e.getMessage();
			}
		}
		public static String getUid(){ 
			try {

				//getting json string from database
				URL internet = new URL("http://cs.wheaton.edu/~kurt.andres/addUser.php?name="+user+"&password="+pass);

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
