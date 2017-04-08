package prakhargupta.loginapp.helper;

/**
 * Created by SAHIL on 4/1/2017.
 */


import android.os.AsyncTask;

import org.apache.ivy.util.url.ApacheURLLister;

import java.net.URL;
import java.util.List;

/**
 * Created by Prakhar Gupta on 2/19/2017.
 */

public class FileListClass extends AsyncTask<URL, Void, List> {
    @Override
    protected List doInBackground(URL... urls) {
        List serverDir = null;
        URL url = urls[0];
//        try {
//            url1 = new URL("http://10.251.217.131/uploader");
        ApacheURLLister lister1 = new ApacheURLLister();
        try {

            serverDir = lister1.listAll(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println(serverDir);
//        }
        return serverDir;
    }
}
