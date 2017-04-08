package prakhargupta.loginapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
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
import prakhargupta.loginapp.app.AppController;

import static prakhargupta.loginapp.app.AppConfig.ADD_OBJECT_URL;
import static prakhargupta.loginapp.app.AppConfig.SEARCH_URL;

/**
 * Created by SAHIL on 4/2/2017.
 */

public class SearchActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Button btnSearch;
    private EditText inputTags;
    private EditText inputTypes;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search);

        inputTags = (EditText) findViewById(R.id.et_tags);
        inputTypes = (EditText) findViewById(R.id.et_types);
        btnSearch = (Button) findViewById(R.id.btnSearch2);

        btnSearch.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String[] tag_names = (inputTags.getText().toString().trim()).split(" ");
                String[] types = (inputTypes.getText().toString().trim()).split(" ");

                search(tag_names, types);
            }

        });
    }

    public void search(final String[] tag_names, final String[] types){
        StringRequest strReq;
        strReq = new StringRequest(Request.Method.POST,
                SEARCH_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        List<String> labelList= new ArrayList<String>();
                        JSONArray jObjData = jObj.getJSONArray("data");
                        for(int j=0; j<jObjData.length(); j++){
                            JSONObject row = jObjData.getJSONObject(j);
                            String label = row.getString("label");
                            labelList.add(label);
                        }
                        final ListView lvSearch = (ListView) findViewById(R.id.searchList);
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                SearchActivity.this,
                                android.R.layout.simple_list_item_1,
                                labelList );

                        lvSearch.setAdapter(arrayAdapter);

                        System.out.println(jObj.toString());
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
                Log.e(TAG, "Database Search Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag_names", implode(",",tag_names));
                params.put("types", implode(",",types));

                return params;
            }

        };
        Log.d("a",strReq.toString());

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "abcd");
    }

    public static String implode(String separator, String[] data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length - 1; i++) {
            //data.length - 1 => to not add separator at the end
            if (!data[i].matches(" *")) {//empty string are ""; " "; "  "; and so on
                sb.append(data[i]);
                sb.append(separator);
            }
        }
        sb.append(data[data.length - 1].trim());
        return sb.toString();
    }
}
