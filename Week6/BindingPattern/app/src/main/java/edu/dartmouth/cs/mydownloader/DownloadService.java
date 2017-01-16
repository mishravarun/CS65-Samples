package edu.dartmouth.cs.mydownloader;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;


// This variant does not use an Interface. Also does implement the ServiceConnection as an interface.
// Returns a service. Then directly calls methods on the service after binding. For example, download()
// and getRandomNumber()

public class DownloadService extends Service {

    // Binder given to clients
    private final IBinder mBinder = new DownloadBinder();
    // Random number generator
    private final Random mGenerator = new Random();

    //XD: This is a bound service, which is the server in a client-server interface.It is also an implementation
    // of the Service class that allows other applications to bind to it and interact with it. To provide binding
    // for a service,you must implement the onBind() callback method.
    // http://developer.android.com/intl/es/guide/components/bound-services.html
    //XD: onBind() returns the communication channel to the service - Return an IBinder through which clients
    // can call on to the service.

    @Override
    public IBinder onBind(Intent intent) {
        Log.e("XD", "onBind()");
        return (mBinder);
    }

    //XD: Binder implements the IBinder interface
    //XD: DownloadService - package-private scope.
    public class DownloadBinder extends Binder  {
        DownloadService getService() {
            // Return this instance of DownloadBinder so clients can call public methods
            return DownloadService.this;
        }
    }

    // method for clients to download
    public void download(String url) {
        // start the thread and pass the url
        Log.d(getClass().getName(), "DownloadBinder:download");
        new DownloadThread(url).start();
    }

    /** method for clients */
    public int getRandomNumber() {
        return mGenerator.nextInt(100);
    }

    // worker thread for the download
    private static class DownloadThread extends Thread {

        String url = null;

        public DownloadThread(String url) {
            this.url = url;
        }

        @Override
        public void run() {

            // this is a background thread

            try {

                // create a downloads directory if doesn't exist
                File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                root.mkdirs();

                File output = new File(root, Uri.parse(url).getLastPathSegment());

                if (output.exists()) {
                    output.delete();
                }

                URL url1 = new URL(url);
                // get a new URL object and open connection
                HttpURLConnection c = (HttpURLConnection) url1.openConnection();

                // added an a BufferedInputStream, probably not needed.
                FileOutputStream fos = new FileOutputStream(output.getPath());
                BufferedOutputStream out = new BufferedOutputStream(fos);
                InputStream in = c.getInputStream();
                InputStream is = new BufferedInputStream(in);

                try {

                    byte[] buffer = new byte[8192];
                    int len;
                    Log.e("xd", "~~~~~~~~~~~~~~~~~~~~~~Start download~~~~~~~~~~~~~~~~~~~~~~");
                    // read until the end of the input stream
                    while ((len = is.read(buffer, 0, buffer.length)) >= 0) {
                        Log.d(getClass().getName(), "len is " + len + " bytes");
                        out.write(buffer, 0, len);
                    }
                    out.flush();

                } finally {

                    /* In Java, the flush() method is used in output streams and writers to ensure
                     that buffered data is written out. However, according to the Javadocs:
                    If the intended destination of this stream is an abstraction provided by the
                    underlying operating system, for example a file, then flushing the stream guarantees
                    only that bytes previously written to the stream are passed to the operating
                    system for writing; it does not guarantee that they are actually written to a
                    physical device such as a disk drive.
                    On the other hand, FileDescriptor.sync() can be used to ensure that data buffered by
                    the OS is written to the physical device (disk). This is the same as the sync
                    call in Linux / POSIX.

                    If your Java application really needs to ensure that data is physically written
                    to disk, you may need to flush and sync, e.g.: */

                    // above we out.flush();

                    fos.getFD().sync();;

                    out.close();
                    c.disconnect();
                }

                Log.e("xd", "~~~~~~~~~~~~~~~~~~~~~~Download completed~~~~~~~~~~~~~~~~~~~~~~");

            } catch (IOException e2) {
                Log.e(getClass().getName(), "Exception in download", e2);
            }

        }

    }

}
