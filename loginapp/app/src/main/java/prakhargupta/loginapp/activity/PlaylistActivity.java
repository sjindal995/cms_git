package prakhargupta.loginapp.activity;

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

public class PlaylistActivity extends Activity {
    private static final String TAG = PlaylistActivity.class.getSimpleName();
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
        Log.d("id", user_id);
        addtoPlaylist(user_id, "p1", "11,12");
        getPlaylists(user_id);


    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(PlaylistActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void getPlaylists(final String user_id){

        String tag_string_req = "req_getplay";

        StringRequest strReq;
        strReq = new StringRequest(Method.POST,
                AppConfig.URL_GETPLAY, new Response.Listener<String>() {

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
                        JSONArray favs = jObj.getJSONArray("playlists");
                        for(int i=0; i<favs.length(); i++){
                            JSONObject f = favs.getJSONObject(i);
                            String label = f.getString("playlist_name") + ": " + f.getString("label");
                            labelList.add(label);
                            Log.d("ff", f.toString());

                        }
                        // Inserting row in users table
                        final ListView lvFav = (ListView) findViewById(R.id.favList);
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                PlaylistActivity.this,
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


    private void addtoPlaylist(final String user_id, final String playlist_name, final String object_ids) {
        // Tag used to cancel the request
        String tag_string_req = "req_addplay";

        // pDialog.setMessage("Registering ...");
        // showDialog();

        StringRequest strReq;
        strReq = new StringRequest(Method.POST,
                AppConfig.URL_ADDPLAY, new Response.Listener<String>() {

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
                params.put("playlist_name", playlist_name);
                params.put("object_ids", object_ids);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }






}