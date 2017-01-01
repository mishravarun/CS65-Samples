package edu.dartmouth.cs.asynctask;

import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */

public class MainActivityFragment extends ListFragment {

    private static final String TAG = MainActivityFragment.class.getSimpleName();

    static final String[] items = new String[] { "Chris Bailey-Kellogg",
            "Devin Balkcom", "Andrew Campbell", "Michael Casey",
            "Amit Chakrabarti", "Thomas H. Cormen ",
            "Robert L. (Scot) Drysdale, III", "Hany Farid", "Lisa Fleischer",
            "Gevorg Grigoryan", "Prasad Jayanti", "David Kotz", "Lorie Loeb",
            "Fabio Pellacini", "Daniel Rockmore", "Sean Smith",
            "Lorenzo Torresani", "Peter Winkler","Emily Whiting","Xia Zhou" };


    private TextView mSelection = null;
    private ArrayAdapter<String> mAdapter = null;
    private ArrayList<String> model = new ArrayList<>();
    private AddStringTask task = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, this + ": onCreate()");

        // retains the fragment between configuration changes.
        // a configuration changes does not call onCreate but does call on
        // onCreateView() and onActivityCreated(), hence the asyncTask
        // is not recreated. Also, the model is retained, that is
        // ArrayList

        setRetainInstance(true);


        task = new AddStringTask();
        task.execute();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, this + ": onCreateView()");

        View v = inflater.inflate(R.layout.fragment_main, container, false);

        mSelection = (TextView) v.findViewById(R.id.selection);
        mSelection.setText(items[1]);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(TAG, this + ": onActivityCreated()");

        mAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                model);

        setListAdapter(mAdapter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, this + ": onDestroy()");

        if (task != null) {
            task.cancel(false);
        }

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        super.onListItemClick(l, v, position, id);

        mSelection.setText(items[position]);

        Toast.makeText(getActivity(),
                ((TextView) v).getText() + " is an awesome prof!",
                Toast.LENGTH_SHORT).show();

    }

    class AddStringTask extends AsyncTask<Void, String, Void> {


        @Override
        protected Void doInBackground(Void... unused) {
            for (String name : items) {
                if (isCancelled())
                    break;

                publishProgress(name);
                SystemClock.sleep(1000);
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... name) {

            if (!isCancelled()) {
                mAdapter.add(name[0]);
            }
        }

        @Override
        protected void onPostExecute(Void unused) {
            Toast.makeText(getActivity(), R.string.done, Toast.LENGTH_SHORT).show();
            task = null;
        }


    }
}
