<?php


$host = "localhost";
$user = "root";
$pass = "";
$db = "solarrelax";

try {
	$db = new PDO("mysql:host=$host;dbname=$db", $user, $pass);
        $db->query("SET CHARACTER SET utf8");
}catch(PDOException $e) {
	die( $e->getMessage());
}

 ?>
