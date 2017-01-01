package edu.dartmouth.cs.mythread;

import android.app.ListFragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class ItemFragment extends ListFragment {

    private ArrayList<String> model = null;
    private Handler handler = null;
    //private ArrayList<String> model = new ArrayList<>();
    private ArrayAdapter adapter = null;

    public ItemFragment() {
        this.handler = new Handler() {

            // must implement this to receive messages

            @Override
            public void handleMessage(Message msg) {
                String item = msg.getData().getString("item");
                adapter.add(item);
            }

        };
    }

    public Handler getHandler() {
        return handler;
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
         // set up adapter
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, model);
        getListView().setScrollbarFadingEnabled(false);
        setListAdapter(adapter);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public void setModel(ArrayList<String> model) {
        this.model = model;
    }

}
