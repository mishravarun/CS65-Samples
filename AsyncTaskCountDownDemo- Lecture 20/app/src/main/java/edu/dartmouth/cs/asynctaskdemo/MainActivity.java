package edu.dartmouth.cs.asynctaskdemo;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Starts the CountDownTask
        new CountDownTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    private class CountDownTask extends AsyncTask<Void, Integer, Void>{
    	
    	// A callback method executed on UI thread on starting the task
    	@Override
    	protected void onPreExecute() {
    		// Getting reference to the TextView tv_counter of the layout activity_main
    		TextView tvCounter = (TextView) findViewById(R.id.tv_counter);
    		tvCounter.setText("*START*");
    	}

    	// A callback method executed on non UI thread, invoked after 
    	// onPreExecute method if exists
    	
    	// Takes a set of parameters of the type defined in your class implementation. This method will be 
    	// executed on the background thread, so it must not attempt to interact with UI objects.
		@Override
		protected Void doInBackground(Void... params) {
			for(int i=15;i>=0;i--){
				try {
					Thread.sleep(1000);
					publishProgress(i); // Invokes onProgressUpdate()
				} catch (InterruptedException e) {
				}
			}
			return null;
		}
		
		// A callback method executed on UI thread, invoked by the publishProgress() 
		// from doInBackground() method
		
		// Overrider this handler to post interim updates to the UI thread. This handler receives the set of parameters
		// passed in publishProgress from within doInbackground. 
		@Override
		protected void onProgressUpdate(Integer... values) {
			// Getting reference to the TextView tv_counter of the layout activity_main
			TextView tvCounter = (TextView) findViewById(R.id.tv_counter);
			
			// Updating the TextView 
			tvCounter.setText( Integer.toString(values[0].intValue()));			
		}
		
		// A callback method executed on UI thread, invoked after the completion of the task
		
		// When doInbackground has completed, the return value from that method is passed into this event 
		// handler. 
		@Override
		protected void onPostExecute(Void result) {
			// Getting reference to the TextView tv_counter of the layout activity_main
			TextView tvCounter = (TextView) findViewById(R.id.tv_counter);
			tvCounter.setText("*DONE*");			
		}		
    }
}
