<?php
//connecting to the db
$db = mysqli_connect('csdb.wheaton.edu', 'TimelineAdmin', 'kurtconner',"Timeline");

//get uid
$uid = "'".$_GET['uid']."'";
//echo $uid;

//get timeline name
$name = "'".$_GET['name']."'";
//echo $name;

//get axis_label
$axis_label = "'".$_GET['axis_label']."'";

//get axis_color
$axis_color = "'".$_GET['axis_color']."'";

//get background color
$background_color = "'".$_GET['background_color']."'";

//detect if coming from web team
$webTeam = false;
$web = "'".$_GET['web']."'";
if($web != "''"){
	$webTeam = true;	
	}

if($uid!= "''" & $name != "''"){
	$result = mysqli_query($db, "SELECT * FROM timelines WHERE uid=".$uid);
	$found = false;
	while($row = mysqli_fetch_assoc($result)){
		if("'".$row['name']."'" == $name){
                //echo "Duplicate Timeline";
               	$found = true;
		 }else{
			//echo " Not Duplicate ";
			}
	}
	if($found){
		//echo "Not Added";		
		}else{
			if($webTeam & $axis_color!= "''"){
				$newAxis_color = substr_replace($axis_color, '0x', 1, -7);
				$axis_color = substr_replace($newAxis_color, 'ff', 9, 0);
				}
			if($webTeam & $background_color!= "''"){
				$newBackground_color = substr_replace($background_color, '0x', 1, -7);
				$background_color = substr_replace($newBackground_color, 'ff', 9, 0);
				}
				
			if($axis_label == "''"){
				$axis_label = "'YEARS'";
				}
			if($axis_color == "''"){
				$axis_color = "'0x000000ff'";
				}
			if($background_color == "''"){
				$background_color = "'0x999999ff'";
				}
				
		$sql="INSERT INTO timelines(uid, name, axis_label, background_color, axis_color) VALUES (".$uid.",".$name.",".$axis_label.",".$background_color.",".$axis_color.")";
		mysqli_query($db, $sql);
			//echo "added";			
			}
		}

if($uid!= "''"){
	$result = mysqli_query($db, "SELECT * FROM timelines WHERE uid=".$uid);
	while($row = mysqli_fetch_assoc($result)){
		if($webTeam){
			$background_color = $row['background_color'];
			$axis_color = $row['axis_color'];
			$temp_background_color= substr_replace($background_color, '', 0, -8);
			$temp_axis_color= substr_replace($axis_color, '', 0, -8);
			$temp_background_color= substr($temp_background_color, 0, 6)." ";
			$temp_axis_color= substr($temp_axis_color, 0, 6)." ";
			$row['background_color']=$temp_background_color;
			$row['axis_color']=$temp_axis_color;
			}
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
