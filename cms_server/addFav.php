<?php
require 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 
if (isset($_POST['user_id']) && isset($_POST['object_id'])) {
 
    // receiving the post params
    $user_id = $_POST['user_id'];
    $object_id = $_POST['object_id'];

    // create a new object
    $fav = $db->addFavorite($object_id, $user_id);
    
    if ($fav) {
        // object stored successfully
        $response["error"] = FALSE;
        $response["object_id"] = $fav["object_id"];
        $response["user_id"] = $fav["user_id"];
        $response["timestamp"] = $fav["timestamp"];

        echo json_encode($response);
    } else {
        // object failed to store
        $response["error"] = TRUE;
        $response["error_msg"] = "Error Occurred!";
        echo json_encode($response);
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters are missing!";
    echo json_encode($response);
}
?>