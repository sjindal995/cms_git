<?php
 
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 
if (isset($_POST['username']) && isset($_POST['email']) && isset($_POST['dob']) && isset($_POST['password'])) {
// if (isset($_POST['username']) && isset($_POST['email']) && isset($_POST['password'])) {
 
    // receiving the post params
    $username = $_POST['username'];
    $email = $_POST['email'];
    $dob = $_POST['dob'];
    $password = $_POST['password'];
 
    // check if user is already existed with the same email
    if ($db->isUserExisted($email)) {
        // user already existed
        $response["error"] = TRUE;
        $response["error_msg"] = "User already existed with " . $email;
        echo json_encode($response);
    } else {
        // create a new user
        $user = $db->storeUser($username, $dob, $email, $password);
        // $user = $db->storeUser($username, $email, $password);
        if ($user) {
            // user stored successfully
            $response["error"] = FALSE;
            $response["user"]["id"] = $user["id"];
            $response["user"]["username"] = $user["username"];
            $response["user"]["email"] = $user["email"];
            $response["user"]["dob"] = $user["dob"];
            $response["user"]["created_at"] = $user["created_at"];
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in registration!";
            echo json_encode($response);
        }
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters (username, email, dob or password) is missing!";
    echo json_encode($response);
}
?>