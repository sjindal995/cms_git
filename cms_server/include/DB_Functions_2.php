<?php
 
/**
 * @author Ravi Tamada
 * @link http://www.androidhive.info/2012/01/android-login-and-registration-with-php-mysql-and-sqlite/ Complete tutorial
 */


class DB_Functions {
 
    private $conn;
 
    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // connecting to database
        $db = new Db_Connect();
        $this->conn = $db->connect();
    }
 	
    // destructor
    function __destruct() {
         
    }
 
    /**
     * Storing new user
     * returns user details
     */
    public function storeUser($username, $dob, $email, $password) {
    // public function storeUser($username, $email, $password) {
        // $uuid = uniqid('', true);
        $hash = $this->hashSSHA($password);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt

 
        $stmt = $this->conn->prepare("INSERT INTO users(username, dob, email, encrypted_password, salt, created_at) VALUES(?, ?, ?, ?, ?, NOW())");
        // $stmt->bind_param("ssss", $username, $email, $encrypted_password, $salt);
        $stmt->bind_param("sssss", $username, $dob, $email, $encrypted_password, $salt);
        $result = $stmt->execute();
        $stmt->close();
 
        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM users WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $user;
        } else {
            return false;
        }
    }
 
    /**
     * Get user by email and password
     */
    public function getUserByEmailAndPassword($email, $password) {
 
        $stmt = $this->conn->prepare("SELECT * FROM users WHERE email = ?");
 
        $stmt->bind_param("s", $email);
 
        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            // verifying user password
            $salt = $user['salt'];
            $encrypted_password = $user['encrypted_password'];
            $hash = $this->checkhashSSHA($salt, $password);
            // check for password equality
            if ($encrypted_password == $hash) {
                // user authentication details are correct
                return $user;
            }
        } else {
            return NULL;
        }
    }
 
    /**
     * Check user is existed or not
     */
    public function isUserExisted($email) {
        $stmt = $this->conn->prepare("SELECT email from users WHERE email = ?");
 
        $stmt->bind_param("s", $email);
 
        $stmt->execute();
 
        $stmt->store_result();
 
        if ($stmt->num_rows > 0) {
            // user existed 
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
    }
 
    /**
     * Encrypting password
     * @param password
     * returns salt and encrypted password
     */
    public function hashSSHA($password) {
 
        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }
 
    /**
     * Decrypting password
     * @param salt, password
     * returns hash string
     */
    public function checkhashSSHA($salt, $password) {
 
        $hash = base64_encode(sha1($password . $salt, true) . $salt);
 
        return $hash;
    }

    public function addObject($label, $type, $uri){

        $stmt = $this->conn->prepare("INSERT INTO object (label, type, uri, upload_time) VALUES(?, ?, ?, NOW())");
        // $stmt->bind_param("ssss", $username, $email, $encrypted_password, $salt);
        $stmt->bind_param("sss", $label, $type, $uri);
        $result = $stmt->execute();
        $stmt->close();
 
        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM object WHERE label=? and type=? and uri=?");
            $stmt->bind_param("sss", $label, $type, $uri);
            $stmt->execute();
            $object = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $object;
        } else {
            return false;
        }
    }

    public function addTags($obj_id, $tag_names){
        $tags = array();
        for($i=0;$i<count($tag_names);$i++){
            $stmt = $this->conn->prepare("INSERT INTO tag (object_id, tag_name) VALUES(?, ?)");
            // $stmt->bind_param("ssss", $username, $email, $encrypted_password, $salt);
            $stmt->bind_param("is", $obj_id, $tag_names[$i]);
            $result = $stmt->execute();
            $stmt->close();
     
            // check for successful store
            if ($result) {
            }
            else{
                return false;
            }
        }
        return true;
    }

    public function addtoPlaylist($objs, $user_id, $playlist_name){
        $objs = array();
        for($i=0;$i<count($objs);$i++){
            $stmt = $this->conn->prepare("INSERT INTO playlists (object_id, user_id, playlist_id) VALUES(?, ?, ?)");
            // $stmt->bind_param("ssss", $username, $email, $encrypted_password, $salt);
            $stmt->bind_param("iis", $objs[$i], $user_id, $playlist_name);
            $result = $stmt->execute();
            $stmt->close();
     
            // check for successful store
            if ($result) {
            }
            else{
                return false;
            }
        }
        return true;
    }

    public function getPlaylists($user_id) {
 
        $stmt = $this->conn->prepare("SELECT * FROM playlist INNER JOIN object ON object.id = playlist.object_id WHERE user_id=?");
 
        $stmt->bind_param("i", $user_id);
 
        $stmt->execute();
        $result = $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        return $result;
        
    }

    public function addFavorite($obj_id, $user_id){
        $stmt = $this->conn->prepare("INSERT INTO favorites (user_id, object_id, timestamp) VALUES(?, ?, NOW())");
        // $stmt->bind_param("ssss", $username, $email, $encrypted_password, $salt);
        $stmt->bind_param("ii", $user_id, $obj_id);
        $result = $stmt->execute();
        $stmt->close();
 
        // check for successful store
        if ($result) {
        }
        else{
            return false;
        }
        return true;
    }

    public function getFavorites($user_id) {
 
        $stmt = $this->conn->prepare("SELECT * FROM favorites INNER JOIN object ON object.id = favorites.object_id WHERE user_id=?");
 
        $stmt->bind_param("i", $user_id);
 
        $stmt->execute();
        $result = $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        return $result;
        
    }
 
    public function search($tag_names, $types){
       $sql_query = "SELECT * FROM object INNER JOIN tag ON object.id = tag.object_id WHERE tag.tag_name IN (";
        for($i=0;$i<count($tag_names)-1;$i++){
            $sql_query = $sql_query."'".$tag_names[$i]."',";
        }
        $sql_query = $sql_query."'".$tag_names[count($tag_names)-1]."') AND object.type IN (";
        for($i=0;$i<count($types)-1;$i++){
            $sql_query = $sql_query."'".$types[$i]."',";
        }
        $sql_query = $sql_query."'".$tag_names[count($tag_names)-1]."');";
        
        $stmt = $this->conn->prepare($sql_query);
        $stmt->execute();
        $result = $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
 
        return $result;
        
    }


}
 
?>