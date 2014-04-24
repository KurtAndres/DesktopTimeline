<?php
//connecting to the db
$db = mysqli_connect('csdb.wheaton.edu', 'TimelineAdmin', 'kurtconner',"Timeline");
//get the name
$name = "'".$_GET['name']."'";
//echo 'Name: '.$name;

//get the password
$password = " '".$_GET['password']."'";
//echo ' Password: '.$password.' ';

//query for specific user and password
$result = mysqli_query($db, "SELECT * FROM userAdmin WHERE name=".$name);
$row = mysqli_fetch_array($result);
if(" '".$row['password']."'" == $password){
	//user and password in db so we return uid
	$uid = $row[uid];
	}else {
		//user password combo not in db so we add them
		$sql="INSERT INTO userAdmin(name, password) VALUES (".$name.",".$password.")";
		mysqli_query($db, $sql);
		$result = mysqli_query($db, "SELECT * FROM userAdmin WHERE name=".$name);
		$row = mysqli_fetch_array($result);
		$uid = $row[uid];
		}
if($uid){
        $result = mysqli_query($db, "SELECT * FROM timelines WHERE uid=".$uid);
        while($row = mysqli_fetch_assoc($result)){
		$tid = $row[tid];               
		if($tid){
        		$tidResult = mysqli_query($db, "SELECT * FROM events WHERE tid=".$tid);
        		while($row = mysqli_fetch_assoc($tidResult)){
                	$json[] = $row;
			}			
                }
	}
}
if(is_null($json)){
	$tid = "'1'";
	$name = "'Kurt Born'";
	$type = "'atomic'";
	$startdate = "'1992-11-22'";
	$enddate = "'1992-11-22'";
	$category = "'Kurts Life'";
	$description = "'You forgot to add an event to your timeline so we added Kurts birthday'";
	$iconid = "'-1'";
	$catcolor = "'0xff000000'";
	$sql = "INSERT INTO events(tid, eventName, type, startDate, endDate, category, description, iconid, categoryColor) VALUES (".$tid.",".$name.",".$type.",".$startdate.",".$enddate.",".$category.",".$description.",".$iconid.",".$catcolor.")";
        mysqli_query($db, $sql);
        $tidResult = mysqli_query($db, "SELECT * FROM events WHERE tid=".$tid);
        		while($row = mysqli_fetch_assoc($tidResult)){
                	$json2[] = $row;
               echo json_encode($json2);
               } 	
	}else{
		echo json_encode($json);
		}
mysqli_close($db);
?>
