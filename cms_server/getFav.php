<?php
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 
if (isset($_POST['user_id'])) {
 
    // receiving the post params
    $user_id = (int)$_POST['user_id'];
    // $user_id = 8;
 
    // get the user by email and password
    $fav = $db->getFavorites($user_id);
 
    if ($fav != false) {
        // use is found
        $response["error"] = FALSE;
        $response["id"] = $user_id;
        $response["favorites"] = $fav;
        // $response["user"]["username"] = $user["username"];
        // $response["user"]["email"] = $user["email"];
        // $response["user"]["dob"] = $user["dob"];
        // $response["user"]["created_at"] = $user["created_at"];
        echo json_encode($response);
    } else {
        // user is not found with the credentials
        $response["error"] = TRUE;
        $response["error_msg"] = "Error Occurred!";
        echo json_encode($response);
    }
} else {
    // required post params is missing
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters are missing!";
    echo json_encode($response);
}
?>