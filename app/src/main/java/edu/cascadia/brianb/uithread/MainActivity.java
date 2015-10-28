package edu.cascadia.brianb.uithread;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.os.Handler;

public class MainActivity extends Activity {
    private ProgressBar bar;
    private Handler handler;

    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bar = (ProgressBar) findViewById(R.id.progressBar1);
        handler = new Handler();
    }

    public void startProgress(View view) {
        bar.setProgress(0);
        new Thread(new Task()).start();
    }

    class Task implements Runnable {
        @Override
        public void run() {
            for(int i = 0; i < 10; i++) {
                final int count = i + 1;
                takeSomeTime(1);

                // old (incorrect) code-- violated "do not update UI outside of UI thread"
                // bar.setProgress(count);

                // correct code
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        bar.setProgress(count);
                    }
                });
            }
        }
    }

    public void takeSomeTime(int seconds){
        long endTime = System.currentTimeMillis() + seconds*1000;

        while (System.currentTimeMillis() < endTime) {
            synchronized (this) {
                try {
                    wait(endTime - System.currentTimeMillis());
                } catch (Exception e) {}
            }
        }
    }
}
