package edu.cascadia.brianb.uithread;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

public class MainActivity extends Activity {
    private ProgressBar bar;
    private Handler handler;
    private Task task; // task object for running on separate threads

    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bar = (ProgressBar) findViewById(R.id.progressBar1);
        handler = new Handler();
        bar.setProgress(0);

    }

    public void startProgress(View view) {
        if (!(task != null && task.isRunning)) {
            bar.setProgress(0);
            new Thread(task = new Task()).start();
        }
    }

    public void takeSomeTime(int seconds){
        long endTime = System.currentTimeMillis() + seconds*1000;

        while (System.currentTimeMillis() < endTime) {
            synchronized (this) {
                try {
                    wait(endTime - System.currentTimeMillis());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Third way, prevents more than one extra thread from running
    class Task implements Runnable {
        private boolean isRunning;

        @Override
        public void run() {
            isRunning = true;

            for (int i = 0; i < 10; i++) {
                final int count = i + 1;
                takeSomeTime(1);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        bar.setProgress(count);
                    }
                });
                if (count == 10) // allows a new thread to be created
                    isRunning = false;
            }
        }
    }

    /* second implementation
    class Task implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                final int count = i + 1;
                takeSomeTime(5); //cause the current thread to delay for given seconds
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        bar.setProgress(count);
                    }
                });
            }
        }
    } */

    /* First implementation
    class Task implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                final int count = i + 1;
                takeSomeTime(3);
                bar.setProgress(count); // modifies a UI element outside of the UI thread.
            }
        }
     }
     */
}
