package prakhargupta.loginapp.activity;

/**
 * Created by SAHIL on 4/1/2017.
 */


import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import prakhargupta.loginapp.R;
import prakhargupta.loginapp.helper.FileListClass;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static prakhargupta.loginapp.app.AppConfig.DOWNLOAD_SOURCE_URL;


public class DownloadActivity extends Activity {
    private long enqueue;
    private DownloadManager dm;
    private String downloadUrl = DOWNLOAD_SOURCE_URL;
    private String sourceUrl = DOWNLOAD_SOURCE_URL;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_download);
        URL urls[] = new URL[1];

        List fileList=null;
        try {
            urls[0] = new URL(sourceUrl);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        Integer i=-1;
        try{

            fileList = new FileListClass().execute(urls).get();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(fileList);
        List<String> fileNameList= new ArrayList<String>();
        for(int j=0; j<fileList.size(); j++){
            String s = fileList.get(j).toString();
            s = s.substring(s.lastIndexOf('/')+1);
            System.out.println(s);
            fileNameList.add(s);
        }

        final ListView lv = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                fileNameList );

        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                downloadUrl = sourceUrl + lv.getItemAtPosition(position).toString();
                download(downloadUrl);
//                dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
//                Request request = new Request(
//                        Uri.parse(downloadUrl));
//                enqueue = dm.enqueue(request);
            }
        });


    }

    public void onClick(View view) {
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        Request request = new Request(
                Uri.parse(downloadUrl));
        enqueue = dm.enqueue(request);


    }

    public void showDownload(View view) {
        Intent i = new Intent();
        i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
        startActivity(i);
    }

    public void download(final String downloadUrl){
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        Request request = new Request(
                Uri.parse(downloadUrl));
        enqueue = dm.enqueue(request);

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    long downloadId = intent.getLongExtra(
                            DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    Query query = new Query();
                    query.setFilterById(enqueue);
                    Cursor c = dm.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c
                                .getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == c
                                .getInt(columnIndex)) {

                            ImageView view = (ImageView) findViewById(R.id.imageView1);
                            String uriString = c
                                    .getString(c
                                            .getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                            view.setImageURI(Uri.parse(uriString));
                        }
                    }
                }
            }
        };

        registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
}