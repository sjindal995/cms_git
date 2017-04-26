<?php
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 
$objects = $db->getAllObjects();

if ($objects != false) {
    $response["error"] = FALSE;
    $response["objects"] = $objects;
    echo json_encode($response);
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Error Occurred in  retrieving objects: getObjects.php!";
    echo json_encode($response);
}

?>