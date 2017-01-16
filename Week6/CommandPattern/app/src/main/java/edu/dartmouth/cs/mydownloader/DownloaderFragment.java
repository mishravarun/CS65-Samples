package edu.dartmouth.cs.mydownloader;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 *
 */
// if you implements View.OnClickListener then you do not need to implement the onClick for each individual
// clickable view (e.g. button). Instead, you can have a single onClick() method, in which you do
// switch (view.getId()){} to perform certain action for different clickable views
public class DownloaderFragment extends Fragment implements View.OnClickListener {

    private Button b = null;
    private static final String DOCUMENT = "https://www.dartmouth.edu/~regarchive/documents/2014_orc.pdf";

    private BroadcastReceiver onEvent = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent i) {
            b.setEnabled(true);
            Toast.makeText(getActivity(), "We are done!", Toast.LENGTH_LONG).show();

        }
    };


    //XD: http://developer.android.com/intl/es/reference/android/app/Fragment.html#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
    //XD: This will be called between onCreate(Bundle) and onActivityCreated(Bundle).
    //XD: This is how you should use them - onCreate is called on initial creation of the fragment. You do your
    //non graphical initializations here. It finishes even before the layout is inflated and the fragment is visible.
    //The onCreate() method in a Fragment is called after the Activity's onAttachFragment()
    //onCreateView is called to inflate the layout of the fragment i.e graphical initialization usually takes place here.
    //XD: http://stackoverflow.com/questions/28929637/difference-and-uses-of-oncreate-oncreateview-and-onactivitycreated-in-fra

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        b = (Button) v.findViewById(R.id.button);
        b.setOnClickListener(this);
        return (v);
    }

    //XD: onResume() is called after onCreate()
    // http://stackoverflow.com/questions/12116096/android-is-onresume-always-called-after-oncreate

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter f = new IntentFilter(Download.ACTION_COMPLETE);
        //XD: LocalBroadcastManager - register for and send broadcasts of Intents to local objects

        //XD: this fragment (DownloaderFragment.java) is added to MainActivity.java so getActivity() should return MainActivity
        //Log.d("XD", getActivity().toString()); // ->shows edu.dartmouth.cs.mydownloader.MainActivity
        //XD: why LocalBroadcastManager? Using the LocalBroadastManager is more efficient than
        // sending a global broadcaset. It also ensures that the Intent you broadcast can not be
        // received by any components outside your application, ensuring that there is no risk
        // of leaking private or sensitive data, such as location information.
        // Similarly, other applications can not transmit broadcasts to your receivers, negating .
        // the risk of these receivers becomeing vectors for security exploits
        //XD:getActivity() - Return the Activity this fragment is currently associated with
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(onEvent, f);
    }

    @Override
    public void onPause() {
        //XD: when the app window is minimized, it will not receive the message (Intent) from the download.java when download is complete.
        //so if this line is commented out, we will still receive the download completion message.
        //Note: unlike Service, if I kill the app, we will never receive the download completion message.
        // also down will not finish at all.
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(onEvent);
        super.onPause();
    }

    @Override
    public void onClick(View v) {

        b.setEnabled(false); // disable button
        Intent i = new Intent(getActivity(), Download.class);

        //XD: Intent.setData vs Intent.putExtra - http://stackoverflow.com/questions/18794504/intent-setdata-vs-intent-putextra
        //XD: setData() is used to point to the location of a data object (like a file for example),
        // while putExtra() adds simple data types (such as an SMS text string for example).
        i.setData(Uri.parse(DOCUMENT));
        //XD: startService() does not exit in Fragment, so needs to trigger it from the hosting Activity.
        //XD: http://stackoverflow.com/questions/15524280/service-vs-intentservice
        getActivity().startService(i);
    }

}
