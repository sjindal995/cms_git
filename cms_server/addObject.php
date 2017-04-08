<?php
require 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 
if (isset($_POST['label']) && isset($_POST['type']) && isset($_POST['uri']) && isset($_POST['upload_time'])) {
 
    // receiving the post params
    $label = $_POST['label'];
    $type = $_POST['type'];
    $uri = $_POST['uri'];
    $upload_time = $_POST['upload_time'];

    // create a new object
    $object = $db->addObject($label, $type, $uri);
    
    if ($object) {
        // object stored successfully
        $response["error"] = FALSE;
        $response["object"]["id"] = $object["id"];
        $response["object"]["label"] = $object["label"];
        $response["object"]["type"] = $object["type"];
        $response["object"]["uri"] = $object["uri"];
        $response["object"]["upload_time"] = $object["upload_time"];
        echo json_encode($response);
    } else {
        // object failed to store
        $response["error"] = TRUE;
        $response["error_msg"] = "Unknown error occurred in upload!";
        echo json_encode($response);
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters (username, email, dob or password) is missing!";
    echo json_encode($response);
}
?>