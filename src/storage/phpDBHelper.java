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
 * Updates offline database with newest data from the online database.
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
	public static String uid;

	public phpDBHelper(String u, String p){
		user = u;
		pass = p;

	}
	public void doit(){
		database = new DBHelper("timeline.db");
		tlMap = new HashMap<String,Timeline>();
		tlNameIdMap = new HashMap<String,String>();
		catMap = new HashMap<Category, String>();
		
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
	private static void parse() throws ParseException{
		

		JSONParser parser=new JSONParser(); //parser

		Object obj = parser.parse(getEvents()); //get json string and parse it
		JSONArray array = (JSONArray)obj; //turn parsed object into array

		Object objT = parser.parse(getTimelines()); //get json string and parse it
		JSONArray arrayT = (JSONArray)objT; //turn parsed object into array
		if(arrayT==null) return;
		if(array==null) return;

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
			cat.setColor(Color.web((String) jobj2.get("categoryColor")));
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



	private static String getEvents(){ 
		try {

			//getting json string from database
			URL internet = new URL("http://cs.wheaton.edu/~kurt.andres/userTimelineEvent.php?name="+user+"&password="+pass);

			Scanner sc = new Scanner(internet.openStream());
			return sc.nextLine();
		} catch (Exception e){
			return e.getMessage();
		}
	}
	private static String getUid(){ 
		try {

			//getting json string from database
			URL internet = new URL("http://cs.wheaton.edu/~kurt.andres/addUser.php?name="+user+"&password="+pass);

			Scanner sc = new Scanner(internet.openStream());

			JSONParser parser=new JSONParser(); //parser
			Object obj = parser.parse(sc.nextLine()); //get json string and parse it
			JSONArray array = (JSONArray)obj; //turn parsed object into array
			JSONObject obj1 = (JSONObject)array.get(0);
			sc.close();
			
			uid = (String)obj1.get("uid");
			return (String)obj1.get("uid");

		} catch (Exception e){
			
			System.out.println(e.getMessage());
			return null;
		}
	}

	private static String getTimelines(){ 
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
