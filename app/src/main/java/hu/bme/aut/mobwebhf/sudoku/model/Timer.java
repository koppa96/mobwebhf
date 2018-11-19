package hu.bme.aut.mobwebhf.sudoku.model;

import android.app.Activity;
import android.provider.ContactsContract;
import android.widget.TextView;

import hu.bme.aut.mobwebhf.sudoku.R;

public class Timer extends Thread {
    private int value;
    private boolean running;
    private TextView view;
    private Activity activity;
    private long t1;

    public Timer(TextView view, Activity activity) {
        this(view, activity, 0);
    }

    public Timer(TextView view, Activity activity, int startCount) {
        value = startCount;
        running = true;
        this.view = view;
        this.activity = activity;
    }

    @Override
    public void run() {
        t1 = System.currentTimeMillis();
        while (true) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    view.setText(Integer.toString(value / 60) + ":"
                            + (value % 60 < 10 ? "0" : "") + Integer.toString(value % 60));
                }
            });

            t1 = System.currentTimeMillis();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (!running) {
                break;
            }

            value++;
        }
    }

    public void stopTimer() {
        running = false;
    }

    public int getValue() {
        return value;
    }
}
