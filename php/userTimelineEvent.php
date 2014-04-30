+<?php
 +//connecting to the db
 +$db = mysqli_connect('DB-LOCATION', 'DB-UserName', 'DB-Password',"DB-TableName");
 +
 +//get the name
 +$name = "'".$_GET['name']."'";
 +//echo 'Name: '.$name;
 +
 +//get the password
 +$password = " '".$_GET['password']."'";
 +//echo ' Password: '.$password.' ';
 +
 +//query for specific user and password
 +$result = mysqli_query($db, "SELECT * FROM userAdmin WHERE name=".$name);
 +$row = mysqli_fetch_array($result);
 +if(" '".$row['password']."'" == $password){
 +        //user and password in db so we return uid
 +        $uid = $row[uid];
 +        }else {
 +                //user password combo not in db so we add them
 +                $sql="INSERT INTO userAdmin(name, password) VALUES (".$name.",".$password.")";
 +                mysqli_query($db, $sql);
 +                $result = mysqli_query($db, "SELECT * FROM userAdmin WHERE name=".$name);
 +                $row = mysqli_fetch_array($result);
 +                $uid = $row[uid];
 +                }
 +if($uid){
 +        $result = mysqli_query($db, "SELECT * FROM timelines WHERE uid=".$uid);
 +        while($row = mysqli_fetch_assoc($result)){
 +                $tid = $row[tid];
 +                if($tid){
 +                        $tidResult = mysqli_query($db, "SELECT * FROM events WHERE tid=".$tid);
 +                        while($row = mysqli_fetch_assoc($tidResult)){
 +                        $json[] = $row;
 +                        }
 +                }
 +        }
 +}
 +echo json_encode($json);
 +mysqli_close($db);
 +?>
