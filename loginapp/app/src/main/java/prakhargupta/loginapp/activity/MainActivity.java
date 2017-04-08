package prakhargupta.loginapp.activity;

/**
 * Created by Prakhar Gupta on 3/29/2017.
 */

import prakhargupta.loginapp.R;
import prakhargupta.loginapp.helper.SQLiteHandler;
import prakhargupta.loginapp.helper.SessionManager;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView txtName;
    private TextView txtEmail;
    private Button btnLogout;
    private Button btnUpload;
    private Button btnDownload;
    private Button btnFav;
    private Button btnPlay;
    private Button btnSearch;

    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        btnDownload = (Button) findViewById(R.id.btnDownload);
        btnFav = (Button) findViewById(R.id.btnFav);
        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());
        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String username = user.get("username");
        String email = user.get("email");

        // Displaying the user details on the screen
        txtName.setText(username);
        txtEmail.setText(email);

        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                upload();
            }
        });
        btnDownload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                download();
            }
        });
        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fav();
            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playlists();
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void upload() {
        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, UploadActivity.class);
        startActivity(intent);
        finish();
    }

    private void download() {
        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, DownloadActivity.class);
        startActivity(intent);
        finish();
    }
    private void fav() {
        Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
        startActivity(intent);
        finish();
    }
    private void playlists() {
        Intent intent = new Intent(MainActivity.this, PlaylistActivity.class);
        startActivity(intent);
        finish();
    }
    private void search() {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(intent);
        finish();
    }
}