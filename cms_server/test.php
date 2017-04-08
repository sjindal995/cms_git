<?php
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);

 

    // receiving the post params
    $user_id = 8;
    $playlist_name = "p1";
    $object_ids = explode(",","10,11");
    
    // create a new tag
    $objects_added = $db->addtoPlaylist($object_ids, $user_id, $playlist_name);
    
    if ($objects_added) {
        // tag stored successfully
        $response["error"] = FALSE;
        echo json_encode($response);
    } else {
        // tag failed to store
        $response["error"] = TRUE;
        $response["error_msg"] = "Error occurred!";
        echo json_encode($response);
    }
?>