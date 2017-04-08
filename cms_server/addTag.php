<?php
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 
if (isset($_POST['object_id']) && isset($_POST['tag_names'])) {
 
    // receiving the post params
    $object_id = (int)($_POST['object_id']);
    $tag_names = explode(",",$_POST['tag_names']);
    
    // create a new tag
    $tags_stored = $db->addTags($object_id, $tag_names);
    
    if ($tags_stored) {
        // tag stored successfully
        $response["error"] = FALSE;
        echo json_encode($response);
    } else {
        // tag failed to store
        $response["error"] = TRUE;
        $response["error_msg"] = "Unknown error occurred in adding tags!";
        echo json_encode($response);
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters (object_id, tag_names) is missing!";
    echo json_encode($response);
}
?>