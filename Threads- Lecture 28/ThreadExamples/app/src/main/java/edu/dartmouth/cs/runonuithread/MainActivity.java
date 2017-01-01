package edu.dartmouth.cs.runonuithread;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btn_count;
    private Button btn_blank;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_count = (Button) findViewById(R.id.btn_count);
        btn_blank = (Button) findViewById(R.id.btn_blank);

    }


    public void startThread(View view) {
        runThread();
    }

    // create a thread that counts and to write on the UI thread
    // it coordinates via runOnUiThread()

    private void runThread() {

        new Thread() {
            public void run() {

                updateButton();
                while (i++ < 1000) {
                    try {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                btn_count.setText("#" + i);
                            }
                        });
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            // illustrates post(runnable) on a view (button) from a background thread
            // this is cool.

            private void updateButton() {

                btn_blank.post(new Runnable() {

                    @Override
                    public void run() {
                        btn_blank.setText("posting hello from background thread");
                    }
                });
            }
        }.start();
    }

    // freeze() means you can't interact with the button after the first click
    // until the delay or freeze is completed
    //
    // nonBlocking() creates a new thread for every click to delay.
    //
    // comment one out an observe the behavior


    public void startWaitThread(View view) {

        // this method will cause the button to block and be
        // no responsive. Try clicking on the button multiple
        // times in one shot -- notice it's blocked ANR

        //freeze();

        // this method creates a new thread for each click on the button.
        //
        // each time the button is clicked we create a separate thread
        // which delays but you can still click on the button.
        // might be an overkill but see it as an example, not programming practice
        nonBlocking();
    }

    private void freeze() {

        long endTime = System.currentTimeMillis() + 15 * 1000;

        while (System.currentTimeMillis() < endTime) {
            synchronized (this) {
                try {
                    wait(endTime -
                            System.currentTimeMillis());
                } catch (Exception e) {
                }
            }
        }

    }

        private void nonBlocking() {
            Runnable runnable = new Runnable() {
                public void run() {

                    long endTime = System.currentTimeMillis() + 20 * 1000;

                    while (System.currentTimeMillis() < endTime) {
                        synchronized (this) {
                            try {
                                wait(endTime -
                                        System.currentTimeMillis());
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            };
            Thread mythread = new Thread(runnable);
            mythread.start();
        }
}
