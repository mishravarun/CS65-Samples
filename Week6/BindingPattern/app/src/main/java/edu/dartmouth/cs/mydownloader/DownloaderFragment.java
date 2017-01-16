package edu.dartmouth.cs.mydownloader;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class DownloaderFragment extends Fragment implements View.OnClickListener {

    private static final String DOCUMENT = "https://www.dartmouth.edu/~regarchive/documents/2014_orc.pdf";
    DownloadService mService;
    boolean mBound = false;
    private Button b = null;
    Application mAppContext = null;//XD: Application inherits Context

    // you can't call  bindService on a fragment. If you use the getActivity it maybe killed.
    // so use the application context which is always. Activity could be destroyed
    // across configuration changes.

    // Bind to the Application object, obtained via getApplicationContext(), with the
    // ServiceConnection managed by (or perhaps actually being) a retained Fragment.

    // The reason is configuration changes. A binding is state. You need to maintain that state across
    // configuration changes. While a retained Fragment can hold onto the ServiceConnection, there is an
    // implicit tie in the system between the ServiceConnection and the Context that registered it for
    // a binding. Since activities can be destroyed and recreated on configuration changes, the Activity
    // is not a good choice of Context here. Application, which is system-global, is a safer choice,
    // and one of the few places where choosing Application over another Context is a wise move IMHO.

    //XD: * In short, binding to a server can be destroyed after the rotation of the screen is changed.This is
    //      because binding is a state, and a state may be lost during the change of configurations if the state
    //      is created with an Activity as the Activity can be destroyed after the change of configurations. As a result
    //      connection to the server may be lost after the change of the orientation of the device.The solution
    //      is to create the binding state using Application, which won't be destroyed upon the change of screen orientation.
    //      So you want to use Application.bindService() not Context.bindService()

    //XD: onAttach() is called once the fragment is associated with its activity.
    //XD: onAttach() is called before onCreate()
    //XD: put the code in onCreate to see what will happen.

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("XD", "onAttach()");
//
//        Intent i;
//        if (mAppContext == null) {
////            Log.d("XD", "onAttach() " + getActivity().toString());//--> show android.app.Application
////            Log.d("XD", "onAttach() " + getActivity().getApplicationContext().toString()); //->shows edu.dartmouth.cs.mydownloader.DownloadBindingDemo
//            i = new Intent(getActivity(), DownloadService.class);
//            mAppContext = (Application) getActivity().getApplicationContext();
//            mAppContext.bindService(i, mConnection, Context.BIND_AUTO_CREATE);
//        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("XD", "onCreate()");
        //XD: setRetainInstance(true) -> onAttach() and onCreateView() will be called upon changeing the orientation
        //XD: setRetainInstance(false) -> onAttach(), onCreateView(), onCreate(), and onDestroy() will be called
        setRetainInstance(true);

        Intent i;
        if (mAppContext == null) {
            i = new Intent(getActivity(), DownloadService.class);
            mAppContext = (Application) getActivity().getApplicationContext();
            mAppContext.bindService(i, mConnection, Context.BIND_AUTO_CREATE);
        }

        //XD: when setRetainInstance(false); you will see an error message but
        // getActivity().bindService(i, mConnection, Context.BIND_AUTO_CREATE)
        // will still work because the reference to the DownloadService still work.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("XD", "onCreateView()");
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        b = (Button) v.findViewById(R.id.button);
        b.setOnClickListener(this);
        b.setEnabled(mBound);
        return (v);
    }

    @Override
    public void onDestroy() {
        Log.d("xd", "onDestroy:unbindService()");
        mAppContext.unbindService(mConnection);
        disconnect();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

        if (mBound) {
            // Call a method from the DownService.
            // However, if this call were something that might hang, then this request should
            // occur in a separate thread to avoid slowing down the activity performance.

            int num = mService.getRandomNumber();
            Toast.makeText(getActivity(), "number: " + num, Toast.LENGTH_SHORT).show();

            // call method to start the download
            mService.download(DOCUMENT);
        }
    }

    public ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DownloadService.DownloadBinder binder = (DownloadService.DownloadBinder) service; //XD: DownloadBinder is an inner class of DownloadService
            mService = binder.getService();
            mBound = true;
            b.setEnabled(mBound);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("xd", "onServiceDisconnected");
            disconnect();
        }

    };

    private void disconnect() {
        mBound = false;
        b.setEnabled(mBound);
    }
}
