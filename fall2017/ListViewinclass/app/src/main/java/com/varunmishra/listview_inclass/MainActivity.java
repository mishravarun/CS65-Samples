package com.varunmishra.listview_inclass;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    static final String[] FRUITS = new String[] { "Apple", "Avocado", "Banana",
            "Blueberry", "Coconut", "Durian", "Guava", "Kiwifruit",
            "Jackfruit", "Mango", "Olive", "Pear", "Sugar-apple" };

    static final String[] MOBILE_OS = new String[] { "Android", "iOS",
            "WindowsMobile", "Blackberry"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btn1Click(View view) {
        Intent i = new Intent(MainActivity.this, ListFruitActivityDefault.class);
        startActivity(i);
    }

    public void btn2Click(View view) {
        Intent i = new Intent(MainActivity.this, FruitListActivity.class);
        startActivity(i);
    }

    public void btn3Click(View view) {
        Intent i = new Intent(MainActivity.this, ListMobileActivity.class);
        startActivity(i);
    }
}
