package com.varunmishra.listview_inclass;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by varun on 10/10/17.
 */

public class ListMobileActivity extends ListActivity {

    static final String[] MOBILE_OS = new String[] { "Android", "iOS",
            "WindowsMobile", "Blackberry"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new MobileArrayAdapter(this, MOBILE_OS));
        final ListView listview = getListView();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.d("TAGG", ""+l);
                Toast.makeText(getApplicationContext(), listview.getAdapter().getItem(position).toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
