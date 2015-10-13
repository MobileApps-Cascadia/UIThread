package edu.cascadia.brianb.uithread;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MainActivity extends Activity {
    private ProgressBar bar;
    private Handler handler;

    private int clickCount = 0;
    private int threadCount = 0;

    private TextView clickCountTextView;
    private TextView threadCountTextView;
    private TextView threadBlockingTextView;

    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bar = (ProgressBar) findViewById(R.id.progressBar1);
        bar.setProgress(0);
        handler = new Handler();

        clickCountTextView = (TextView) findViewById(R.id.clickCountTextView);
        threadCountTextView = (TextView) findViewById(R.id.threadCountTextView);
        threadBlockingTextView = (TextView) findViewById(R.id.threadBlockingTextView);
    }

    public void startProgress(View view) {
        System.out.println("*** in startProgress() ***");
        clickCount++;
        clickCountTextView.setText("Click count: " + clickCount);

        Thread thread = new Thread(new BackgroundTask(threadCount));
        thread.start();
    }

    private synchronized void incrementThreadRunningCount() {
        threadCount++;
        threadCountTextView.setText("Number of active threads:" + threadCount);
    }

    private synchronized void decrementThreadRunningCount() {
        threadCount--;
        threadCountTextView.setText("Number of active threads:" + threadCount);

        // if no more threads are running, set
        // threadRunningTextView to NONE
        if (threadCount <= 0) {
            threadBlockingTextView.setText("");
        }
    }

    private void updateCurrentBlockingTextView(int id) {

        threadBlockingTextView.setText("Thread #" + id + " currently blocking");
    }

    private void doWork(final int taskID) {
        System.out.println("***doWork START #" + taskID + "***");

        bar.setProgress(0);

        for (int i = 0; i < 10; i++) {
            final int count = i + 1;
            System.out.println("***takeSomeTime for #:" + taskID + "***");
            takeSomeTime(1); //cause the current thread to delay for given seconds
            handler.post(new Runnable() {
                @Override
                public void run() {
                    bar.setProgress(count);
                    updateCurrentBlockingTextView(taskID);
                }
            });
        }
        System.out.println("***doWork DONE for #" + taskID + "***");

        // thread completes, update the number of thread count
        handler.post(new Runnable() {
            @Override
            public void run() {
                decrementThreadRunningCount();
            }
        });
    }


    public void takeSomeTime(int seconds) {
        long endTime = System.currentTimeMillis() + seconds * 1000;

        while (System.currentTimeMillis() < endTime) {
            synchronized (this) {
                try {
                    wait(endTime - System.currentTimeMillis());
                } catch (Exception e) {
                    System.out.println(">>>Exception e:" + e.getMessage());
                }
            }
        }
    }

    /*
      BackgroundTask
     */
    private class BackgroundTask implements Runnable {
        private int taskID;

        public BackgroundTask(int cnt) {
            super();

            // use the thread  count as the task ID,
            // so we can display which thread is running
            taskID = cnt + 1;

            // update the number of thread in main thread and update the
            // display
            handler.post(new Runnable() {
                @Override
                public void run() {
                    incrementThreadRunningCount();
                }
            });

        }

        @Override
        public void run() {
            synchronized (this) {
                doWork(taskID);
            }
        }
    }
}


