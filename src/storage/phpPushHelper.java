package storage;


import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Scanner;

import model.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
/**
 * phpPushHelper.java
 * 
 * Updates online database with newest data from the offline database.
 * 
 * 
 * @author Conner Vick Wheaton College, CS 335, Spring 2014
 *         Project Phase 3 
 * 
 */
public class phpPushHelper {
	
	public static void send(TimelineMaker timelinemaker) throws IOException, ParseException{
		ArrayList<String> timelines = timelinemaker.getTimelineTitles();
		JSONParser parser=new JSONParser();
		Timeline tl;
		String uid = phpDBHelper.uid;
		String tName, axis_label, background_color;
		String axis_color;
		String tid, type, endDate, startDate, eName, category, description, iconid; 
		
		URL internet = new URL("http://cs.wheaton.edu/~kurt.andres/deleteTimelineEvents.php?uid="+uid);
		Scanner sc = new Scanner(internet.openStream());
		
		for(String t : timelines){
			tl = timelinemaker.getTimeline(t);
			tName = tl.getName();
			axis_label = tl.getAxisLabel().name();
			axis_color = tl.getColorTL().toString();
			background_color = tl.getColorBG().toString();
			
			internet = new URL("http://cs.wheaton.edu/~kurt.andres/addTimeline.php?uid="+uid+"&name="+tName+"&axis_label="
					+axis_label+"&axis_color="+axis_color+"&background_color="+background_color);
			sc = new Scanner(internet.openStream());
			
			Object obj = parser.parse(sc.nextLine()); 
			JSONArray array = (JSONArray)obj;
			JSONObject obj1 = (JSONObject)array.get(array.size()-1);
			System.out.println(obj1.get("tid"));
			tid = (String)obj1.get("tid");
			TLEvent[] tla = tl.getEvents();
			for(TLEvent someEvent: tla){
				if(someEvent instanceof Duration){
					type= "duration";
					endDate = ((Duration) someEvent).getEndDate().toString();
				}else{
					endDate = (new Date(0)).toString();
					type = "atomic";
				}
				
				eName = someEvent.getName();
				category = someEvent.getCategory().getName();
				startDate = someEvent.getStartDate().toString();
				description = someEvent.getDescription();
				description.replaceAll(" ", "%20");
				
				iconid = Integer.toString(someEvent.getIconIndex());
				internet = new URL("http://cs.wheaton.edu/~kurt.andres/addEvent.php?tid="+tid+"&name="+eName+"&type="
						+type+"&startdate="+startDate+"&enddate="+endDate+"&category="+category+"&iconid="+iconid+"&description="+description);
				System.out.println(internet);
				sc = new Scanner(internet.openStream());
				
			}
			
		}
		sc.close();
		
		
	
	}
}
