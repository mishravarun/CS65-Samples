package edu.dartmouth.cs.postdelayed;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PostDelayedDemo extends Activity implements Runnable {

    private static final long PERIOD = 5000;
    private View root = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        root = findViewById(android.R.id.content);
    }

    @Override
    protected void onResume() {
        super.onResume();

        run();
    }


    @Override
    protected void onPause() {
        super.onPause();

        //Remove any pending posts of Runnable r that are in the message queue.
        root.removeCallbacks(this);
    }

    @Override
    public void run() {
        Toast.makeText(PostDelayedDemo.this, "Hello class: Yours Runnable", Toast.LENGTH_LONG).show();

        // Causes the Runnable r to be added to the message queue, to be run after the specified
        // amount of time elapses.
        root.postDelayed(this, PERIOD);
    }


}
