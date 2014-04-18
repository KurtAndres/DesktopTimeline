<?php
//connecting to the db
$db = mysqli_connect('csdb.wheaton.edu', 'TimelineAdmin', 'kurtconner',"Timeline");

//get uid
$uid = "'".$_GET['uid']."'";
//echo $uid;

//get timeline name
$name = "'".$_GET['name']."'";
//echo $name;

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
                        $sql="INSERT INTO timelines(uid, name) VALUES (".$uid.",".$name.")";
                        mysqli_query($db, $sql);
                        //echo "added";
                        }
                }

if($uid!= "''"){
        $result = mysqli_query($db, "SELECT * FROM timelines WHERE uid=".$uid);
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
