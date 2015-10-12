package edu.cascadia.brianb.uithread;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.os.Handler;

//import java.util.logging.Handler;

public class MainActivity extends Activity {
    private ProgressBar bar;
    private Handler handler;

    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();
        bar = (ProgressBar) findViewById(R.id.progressBar1);
       // bar.setProgress(0); 3rd Exercise

    }

    public void startProgress(View view) {
       // bar.setProgress(0); // Commented for 3rd Exercise
        new Thread(new Task()).start();
        // Commented for 2nd Exercise
        /*for (int i = 0; i < 10; i++) {
            final int count = i+1;
            takeSomeTime(5); //cause the current thread to delay for given seconds
            bar.setProgress(count);
        }*/
    }

    class Task implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                final int count = i+1;
                takeSomeTime(5); //cause the current thread to delay for given seconds
                //bar.setProgress(count);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        bar.setProgress(count);
                    }

                } );
        }
    }
    public void takeSomeTime(int seconds){
        // Commented for 2nd Exercise
        //long endTime = System.currentTimeMillis() + seconds*1000;

        //while (System.currentTimeMillis() < endTime) {
            //synchronized (this) {
                try {
                    //wait(endTime - System.currentTimeMillis());  // Commented for 2nd Exercise
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                e.printStackTrace();}
            }
           // }
        //}
    }
}
