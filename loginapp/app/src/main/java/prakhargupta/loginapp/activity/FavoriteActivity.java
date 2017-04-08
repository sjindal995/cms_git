package prakhargupta.loginapp.activity;

/**
 * Created by Prakhar Gupta on 3/29/2017.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import prakhargupta.loginapp.R;
import prakhargupta.loginapp.app.AppConfig;
import prakhargupta.loginapp.app.AppController;
import prakhargupta.loginapp.helper.SQLiteHandler;
import prakhargupta.loginapp.helper.SessionManager;

public class FavoriteActivity extends Activity {
    private static final String TAG = FavoriteActivity.class.getSimpleName();
    private SessionManager session;
    private SQLiteHandler db;
    private String user_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        if(!session.isLoggedIn()){
      	 	logoutUser();
        }

        //fetch user details
        HashMap<String, String> user = db.getUserDetails();

        String username = user.get("username");
        user_id = user.get("dbid");
        Log.d("id: ", user_id);
        Log.d("usernmae: ", username);
//        addFavorite(user_id, "11");
        getFavorites(user_id);


    }

   private void logoutUser() {
       session.setLogin(false);

       db.deleteUsers();

       // Launching the login activity
       Intent intent = new Intent(FavoriteActivity.this, LoginActivity.class);
       startActivity(intent);
       finish();
   }

    private void getFavorites(final String user_id){

    	String tag_string_req = "req_getfav";

    	StringRequest strReq;
    	        strReq = new StringRequest(Method.POST,
                AppConfig.URL_GETFAV, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
//                        String uid = jObj.getString("uid");
                        List<String> labelList= new ArrayList<String>();
                        JSONArray favs = jObj.getJSONArray("favorites");
                        for(int i=0; i<favs.length(); i++){
                            JSONObject f = favs.getJSONObject(i);
                            String label = f.getString("label");
                            labelList.add(label);
                            Log.d("ff", f.toString());
                        }
                        // Inserting row in users table
                        final ListView lvFav = (ListView) findViewById(R.id.favList);
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                FavoriteActivity.this,
                                android.R.layout.simple_list_item_1,
                                labelList );

                        lvFav.setAdapter(arrayAdapter);
                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);



    }


    private void addFavorite(final String user_id, final String object_id) {
        // Tag used to cancel the request
        String tag_string_req = "req_addfav";

        // pDialog.setMessage("Registering ...");
        // showDialog();

        StringRequest strReq;
        strReq = new StringRequest(Method.POST,
                AppConfig.URL_ADDFAV, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
//                        String uid = jObj.getString("uid");

//                        JSONObject user = jObj.getJSONObject("user");
//                        String user_id = user.getString("user_id");
//                        String object_id = user.getString("object_id");
//                        String timestamp = user.getString("timestamp");



                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                params.put("object_id", object_id);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }




    /**
     * function to verify login details in mysql db
     * */
//     private void checkLogin(final String email, final String password) {
//         // Tag used to cancel the request
//         String tag_string_req = "req_login";

//         pDialog.setMessage("Logging in ...");
//         showDialog();

//         StringRequest strReq = new StringRequest(Method.POST,
//                 AppConfig.URL_LOGIN, new Response.Listener<String>() {

//             @Override
//             public void onResponse(String response) {
//                 Log.d(TAG, "Login Response: " + response.toString());
//                 hideDialog();

//                 try {
//                     JSONObject jObj = new JSONObject(response);
//                     boolean error = jObj.getBoolean("error");

//                     // Check for error node in json
//                     if (!error) {
//                         // user successfully logged in
//                         // Create login session
//                         session.setLogin(true);

//                         // Now store the user in SQLite
// //                        String uid = jObj.getString("uid");

//                         JSONObject user = jObj.getJSONObject("user");
//                         String username = user.getString("username");
//                         String email = user.getString("email");
//                         String dob = user.getString("dob");
//                         String created_at = user
//                                 .getString("created_at");

//                         // Inserting row in users table
//                         db.addUser(username, dob, email, created_at);

//                         // Launch main activity
//                         Intent intent = new Intent(LoginActivity.this,
//                                 MainActivity.class);
//                         startActivity(intent);
//                         finish();
//                     } else {
//                         // Error in login. Get the error message
//                         String errorMsg = jObj.getString("error_msg");
//                         Toast.makeText(getApplicationContext(),
//                                 errorMsg, Toast.LENGTH_LONG).show();
//                     }
//                 } catch (JSONException e) {
//                     // JSON error
//                     e.printStackTrace();
//                     Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                 }

//             }
//         }, new Response.ErrorListener() {

//             @Override
//             public void onErrorResponse(VolleyError error) {
//                 Log.e(TAG, "Login Error: " + error.getMessage());
//                 Toast.makeText(getApplicationContext(),
//                         error.getMessage(), Toast.LENGTH_LONG).show();
//                 hideDialog();
//             }
//         }) {

//             @Override
//             protected Map<String, String> getParams() {
//                 // Posting parameters to login url
//                 Map<String, String> params = new HashMap<String, String>();
//                 params.put("email", email);
//                 params.put("password", password);

//                 return params;
//             }

//         };

//         // Adding request to request queue
//         AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
//     }


}