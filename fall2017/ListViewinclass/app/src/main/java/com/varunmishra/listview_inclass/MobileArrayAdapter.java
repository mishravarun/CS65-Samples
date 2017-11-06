package com.varunmishra.listview_inclass;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by varun on 10/10/17.
 */

public class MobileArrayAdapter extends BaseAdapter{
    private final Context context;
    private final String[] values;
    public MobileArrayAdapter(Context context, String[] values){
//        super(context, R.layout.list_mobile, values);

        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount() {
        return values.length;
    }

    @Override
    public Object getItem(int i) {
        return values[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_mobile,parent,false);

            TextView tv = (TextView) rowView.findViewById(R.id.label);
            ImageView im = (ImageView) rowView.findViewById(R.id.logo);

            tv.setText(values[position]);
            String s = values[position];

            if (s.equals("WindowsMobile")) {
                im.setImageResource(R.drawable.windowsmobile_logo);
            } else if (s.equals("iOS")) {
                im.setImageResource(R.drawable.ios_logo);
            } else if (s.equals("Blackberry")) {
                im.setImageResource(R.drawable.blackberry_logo);
            } else {
                im.setImageResource(R.drawable.android_logo);
            }


        return rowView;
//        return super.getView(position, convertView, parent);

    }
}
