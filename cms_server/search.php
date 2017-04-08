<?php
require_once 'include/DB_Functions.php';
$db = new DB_Functions();

// json response array
$response = array("error" => FALSE);
 
if (isset($_POST['tag_names']) && isset($_POST['types'])) {
 
    // receiving the post params
    $tag_names = explode(",",$_POST['tag_names']);
    $types = explode(",",$_POST['types']);
    
    $result = $db->search($tag_names, $types);
    
    if ($result) {
        $response["error"] = FALSE;
        $response["data"] = $result;
        echo json_encode($response);
    } else {
        $response["error"] = TRUE;
        $response["error_msg"] = "Unknown error occurred in searching!";
        echo json_encode($response);
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters (tag_names, types) is missing!";
    echo json_encode($response);
}
?>