<?php
//connecting to the db
$db = mysqli_connect('csdb.wheaton.edu', 'TimelineAdmin', 'kurtconner',"Timeline");

//get the name
$name = "'".$_GET['name']."'";
//echo 'Name: '.$name;

//get the password
$password = "'".$_GET['password']."'";
//echo ' Password: '.$password.' ';

//query for specific user and password
$result = mysqli_query($db, "SELECT * FROM userAdmin WHERE name=".$name);
        $found = false;
        while($row = mysqli_fetch_assoc($result)){
                if("'".$row['password']."'" == $password){
                //echo "Duplicate User Password Combo";
                $found = true;
                 }else{
                        echo $found;
                        //echo " Not Duplicate ";
                        }
                }
if($found==true | $name == "''" | $password == "''"){
        //echo "don't add anything";
        }else{
                $sql="INSERT INTO userAdmin(name, password) VALUES (".$name.",".$password.")";
                mysqli_query($db, $sql);
                }

$result = mysqli_query($db, "SELECT * FROM userAdmin WHERE name=".$name." AND password=".$password);
        while($row = mysqli_fetch_assoc($result)){
                $json[] = $row;
                }
        echo json_encode($json);

mysqli_close($db);
?>
