<?php
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 
if (isset($_POST['user_id']) && isset($_POST['playlist_name']) && isset($_POST['object_ids'])) {
 
    // receiving the post params
    $user_id = (int)($_POST['user_id']);
    $playlist_name = $_POST['playlist_name'];
    $object_ids = explode(",",$_POST['object_ids']);
    
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
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters are missing!";
    echo json_encode($response);
}
?>