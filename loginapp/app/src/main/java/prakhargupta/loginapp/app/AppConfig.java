package prakhargupta.loginapp.app;

/**
 * Created by Prakhar Gupta on 3/28/2017.
 */
public class AppConfig {
    // Server user login url
//    public static String URL_LOGIN = "http://192.168.0.102/android_login_api/login.php";
    public static String URL_LOGIN = "http://10.0.2.2/cms/login.php";

    // Server user register url
    public static String URL_REGISTER = "http://10.0.2.2/cms/register.php";
    public static String URL_GETFAV = "http://10.0.2.2/cms/getFav.php";
    public static String URL_ADDFAV = "http://10.0.2.2/cms/addFav.php";
    public static String URL_GETPLAY = "http://10.0.2.2/cms/getPlaylists.php";
    public static String URL_ADDPLAY = "http://10.0.2.2/cms/addtoPlaylist.php";

    public static String UPLOAD_URL = "http://10.0.2.2/cms/uploader/UploadToServer.php";
    public static String ADD_OBJECT_URL = "http://10.0.2.2/cms/addObject.php";
    public static String ADD_TAG_URL = "http://10.0.2.2/cms/addTag.php";
    public static String SEARCH_URL = "http://10.0.2.2/cms/search.php";
    public static String DOWNLOAD_SOURCE_URL = "http://10.0.2.2/cms/uploader/uploads/";
}