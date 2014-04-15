<?php
//connecting to the db
$db = mysqli_connect('csdb.wheaton.edu', 'TimelineAdmin', 'kurtconner',"Timeline");

//get uid
$uid = "'".$_GET['uid']."'";
//echo $uid;

//get timeline name
$name = "'".$_GET['name']."'";
//echo $name;

if($uid!= "''" | $name != "''"){
	$result = mysqli_query($db, "SELECT * FROM timelines WHERE uid=".$uid);
	$row = mysqli_fetch_array($result);
	if("'".$row['name']."'" == $name){
		//echo "Duplicate Timeline";		
		}else{
			$sql="INSERT INTO timelines(uid, name) VALUES (".$uid.",".$name.")";
			mysqli_query($db, $sql);
			//echo "added";			
			}
		}
	
if($uid!= "''"){
	$result = mysqli_query($db, "SELECT * FROM timelines WHERE uid=".$uid);
	$row = mysqli_fetch_array($result);
	echo json_encode($row);	
	//$row = mysqli_fetch_array($result);
	//$tid = $row[tid];
	//echo $tid;
$sql="INSERT INTO userAdmin(name, password) VALUES (".$name.",".$password.")";
		mysqli_query($db, $sql);
		$result = mysqli_query($db, "SELECT * FROM userAdmin WHERE name=".$name);
		$row = mysqli_fetch_array($result);
		$uid = $row[uid];
		echo $uid;	
	}
mysqli_close($db);

?>
