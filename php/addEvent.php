<?php
//connecting to the db
$db = mysqli_connect('csdb.wheaton.edu', 'TimelineAdmin', 'kurtconner',"Timeline");

//eid - event id
//if we are passed an eid this event already occurs so we are just updating it
$eid = "'".$_GET['eid']."'";

//tid
//which timeline
$tid = "'".$_GET['tid']."'";

//event name
$name = "'".$_GET['name']."'";

//event type
$type = "'".$_GET['type']."'";

//event startDate
$startdate = "'".$_GET['startdate']."'";

//event endDate
$enddate = "'".$_GET['enddate']."'";

//event category
$category = "'".$_GET['category']."'";

//event description
$description = "'".$_GET['description']."'";

//iconid - to keep track of icon on the local sql lite database
$iconid = "'".$_GET['iconid']."'";

//catcolor- for category colors
$catcolor = "'".$_GET['catcolor']."'";

if($eid!="''"){
	$result = mysqli_query($db, "SELECT * FROM events WHERE eid=".$eid);
	if(mysqli_num_rows($result)>0){
			if($name!="''"){
			$sql="UPDATE events SET eventName=".$name."WHERE eid=".$eid;
			$result=mysqli_query($db, $sql);
			}
			if($type!="''"){
			$sql="UPDATE events SET type=".$type."WHERE eid=".$eid;
                        $result=mysqli_query($db, $sql);
			}
			if($startdate!="''"){
                        $sql="UPDATE events SET startDate=".$startdate."WHERE eid=".$eid;
                        $result=mysqli_query($db, $sql);
                        }
			if($enddate!="''"){
                        $sql="UPDATE events SET endDate=".$enddate."WHERE eid=".$eid;
                        $result=mysqli_query($db, $sql);
                        }
			if($category!="''"){
                        $sql="UPDATE events SET category=".$category."WHERE eid=".$eid;
                        $result=mysqli_query($db, $sql);
                        }

			if($description!="''"){
                        $sql="UPDATE events SET description=".$description."WHERE eid=".$eid;
                        $result=mysqli_query($db, $sql);
                        }
         if($iconid!="''"){
                        $sql="UPDATE events SET iconid=".$iconid."WHERE eid=".$eid;
                        $result=mysqli_query($db, $sql);
                        }
         if($catcolor!="''"){
                        $sql="UPDATE events SET categoryColor=".$catcolor."WHERE eid=".$eid;
                        $result=mysqli_query($db, $sql);
                        }
			}
	}
//NOTICE a time-line is only added if the below conditions are met
//ALSO notice you can add the same exact thing multiple times which obviously i dont know why youd want to add the same event but we allow it
if($tid!= "''" & $name != "''" & $eid == "''"){
			if($type == "''"){
				$type = "'"."atomic"."'";
				}
			if($startdate == "''"){
				$startdate = "'". date('Y-m-d')."'";
				}
			if($enddate == "''"){
				$enddate = "'" . date('Y-m-d')."'";
				}
			if($category == "''"){
				$category = "'"."DEFAULT"."'";				
				}
			if($description == "''"){
				$description = "'"."No Description"."'";				
				}
			if($iconid == "''"){
				$iconid = "'"."0"."'";
				}
			if($catcolor == "''"){
				$catcolor = "'"."0xffffffff"."'";
				}
	$sql="INSERT INTO events(tid, eventName, type, startDate, endDate, category, description, iconid, categoryColor) VALUES (".$tid.",".$name.",".$type.",".$startdate.",".$enddate.",".$category.",".$description.",".$iconid.",".$catcolor.")";
        mysqli_query($db, $sql);
        }

if($tid!= "''"){
        $result = mysqli_query($db, "SELECT * FROM events WHERE tid=".$tid);
        while($row = mysqli_fetch_assoc($result)){
                $json[] = $row;
                }
        echo json_encode($json);
        //$row = mysqli_fetch_assoc($result);
        //echo json_encode($row);
        //$row = mysqli_fetch_array($result);
        //$tid = $row[tid];
        //echo $tid;
        }
mysqli_close($db);

?>
