package edu.cascadia.brianb.uithread;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class MainActivity extends Activity {
    private ProgressBar bar;

    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bar = (ProgressBar) findViewById(R.id.progressBar1);
        bar.setProgress(0);

    }

    public void startProgress(View view) {
        bar.setProgress(0);
        /*for (int i = 0; i < 10; i++) {
            final int count = i+1;
            takeSomeTime(20); //cause the current thread to delay for given seconds
            bar.setProgress(count);*/

            new Thread(new Task()).start();
        }
    }

    class Task implements Runnable {
        @Override
         public void run() {
            for (int i = 0; i <= 10; i++) {
                final int value = i;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                  e.printStackTrace();
                 }
                bar.setProgress(value);

             }
        }
    }


   /* public void takeSomeTime(int seconds){
        long endTime = System.currentTimeMillis() + seconds*1000;

        while (System.currentTimeMillis() < endTime) {
            synchronized (this) {
                try {
                    wait(endTime - System.currentTimeMillis());
                } catch (Exception e) {}
            }
        }
    }*/
}
