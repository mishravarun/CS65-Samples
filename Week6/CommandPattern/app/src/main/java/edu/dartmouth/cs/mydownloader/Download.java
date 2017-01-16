package edu.dartmouth.cs.mydownloader;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class Download extends IntentService {

    //XD: Service vs IntentService - http://stackoverflow.com/questions/15524280/service-vs-intentservice
    //XD: http://javatechig.com/android/creating-a-background-service-in-android
    //XD: The Service can be used in tasks with no UI, but shouldn't be too long. If you need to
    // perform long tasks, you must use threads within Service.
    //XD: The IntentService can be used in long tasks usually with no communication to Main Thread.
    // If communication is required, can use Main Thread handler or broadcast intents.
    // Another case of use is when callbacks are needed (Intent triggered tasks).

    //XD: In this case here, download stops after the app window is minimized and
    // removed (as IntentService can not run without UI).
    // If using service, download will continue regardless if the main app is closed.

    //XD: An intent filter is an expression in an app's manifest file that specifies the type of intents that the component would like to receive. For instance, by declaring an intent filter for an activity, you make it possible for other apps to directly start your activity with a certain kind of intent. Likewise, if you do not declare any intent filters for an activity, then it can be started only with an explicit intent.
    //XD: see user defined actions: http://developer.android.com/intl/es/guide/topics/manifest/action-element.html

    public static final String ACTION_COMPLETE = "edu.dartmouth.cs.mydownloader.action.COMPLETE";

    // pass the name of the worker thread. Need this constructor
    public Download() {
        super("Download");
    }

    //XD: onHandleIntent() is the worker thread which is called up on the creation of an IntentService
    @Override
    public void onHandleIntent(Intent i) {
        // this is a background thread
        try {

            // create a downloads directory if doesn't exist
            File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            root.mkdirs();

            // getLastPathSegment() gets the filename potion of the URI. e.g., 2014_orc.pdf
            File output = new File(root, i.getData().getLastPathSegment());

            if (output.exists()) {
                output.delete();
            }

            URL url = new URL(i.getData().toString());
            HttpURLConnection c = (HttpURLConnection)url.openConnection();

            FileOutputStream fos = new FileOutputStream(output.getPath());
            BufferedOutputStream out = new BufferedOutputStream(fos);

            try {
                InputStream in = c.getInputStream();
                byte[] buffer = new byte[8192];
                int len = 0;

                // read until the end of the input stream
                while ((len=in.read(buffer)) >= 0) {
                    Log.d(getClass().getName(), "Background thread active: len is " + len + " bytes");
                    out.write(buffer, 0, len);
                }
                out.flush();
                Log.e("xd", "done download");
            }
            finally {

                fos.getFD().sync();
                out.close();
                c.disconnect();
            }
            //XD: download will still finish but the broadcast msg won't be sent.
            //XD: this sendBroadcast() is not the one in Context. It is in LocalBroadcastManager class
            //XD: LocalBroadcastManager - communication internal to this application
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(ACTION_COMPLETE));

        } catch (IOException e2) {
            Log.e("xd", "Exception in download", e2);
        }

    }
}
