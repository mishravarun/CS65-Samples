package edu.dartmouth.cs.mythread;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

import java.util.ArrayList;


public class ModelFragment extends Fragment {

    static final String[] items = new String[] { "Chris Bailey-Kellogg",
            "Devin Balkcom", "Andrew Campbell", "Michael Casey",
            "Amit Chakrabarti", "Thomas H. Cormen ",
            "Robert L. (Scot) Drysdale, III", "Hany Farid", "Lisa Fleischer",
            "Gevorg Grigoryan", "Prasad Jayanti", "David Kotz", "Lorie Loeb",
            "Fabio Pellacini", "Daniel Rockmore", "Sean Smith",
            "Lorenzo Torresani", "Peter Winkler","Emily Whiting","Xia Zhou" };


    public Handler handler = null;
    private ArrayList<String> model = new ArrayList<>();

    private Thread mUpdateThread = null;
    private boolean Started = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true); // retain the model in memory

        if (!Started) {
            mUpdateThread = new UpdateDisplay();
            mUpdateThread.start();
        }
    }

    public ArrayList<String> getModel() {
        return model;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    class UpdateDisplay extends Thread {

        @Override
        public void run() {
            super.run();

            for (String item : items) {
                Message msg = handler.obtainMessage();
                Bundle b = new Bundle();
                b.putString("item", item);
                msg.setData(b);

                //Pushes a message onto the end of the message queue
                handler.sendMessage(msg);
                SystemClock.sleep(1000);
            }

        }


    }
}
