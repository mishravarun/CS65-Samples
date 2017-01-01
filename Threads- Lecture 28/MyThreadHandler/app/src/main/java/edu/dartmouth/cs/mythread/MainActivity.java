package edu.dartmouth.cs.mythread;

import android.app.Activity;
import android.os.Bundle;

//This is a fairly cool program. We create a model fragment that we retain across
//reconfigurations just to make things easier. We then use a thread in the model
//to update the handler in the UI therad to update the UI. We pass a message with
//the item to update. So this is an example of a background thread coordinating
//using a msg/event handler.

// OK. what the frig are loopers!

public class MainActivity extends Activity {

    private static final String MODEL_FRAG = "Model Frag";
    private ItemFragment mUIFrag = null;
    private ModelFragment mModelFrag = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mModelFrag = (ModelFragment)getFragmentManager().findFragmentByTag(MODEL_FRAG);
        // create the model fragment if it does not already exist
        if (mModelFrag == null) {
            mModelFrag = new ModelFragment();
            getFragmentManager().beginTransaction().add(mModelFrag, MODEL_FRAG).commit();
        }

        // create the UI fragment
        mUIFrag = (ItemFragment)getFragmentManager().findFragmentById(android.R.id.content);
        if (mUIFrag == null) {
            mUIFrag = new ItemFragment();
            getFragmentManager().beginTransaction().add(android.R.id.content, mUIFrag).commit();
        }

        mUIFrag.setModel(mModelFrag.getModel());
        mModelFrag.setHandler(mUIFrag.getHandler());
    }

}
