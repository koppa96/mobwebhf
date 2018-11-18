package hu.bme.aut.mobwebhf.sudoku.model;

import android.provider.ContactsContract;
import android.widget.TextView;

public class Timer extends Thread {
    private int value;
    private boolean running;
    private TextView view;

    public Timer(TextView view) {
        this(view, 0);
    }

    public Timer(TextView view, int startCount) {
        value = startCount;
        running = true;
        this.view = view;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (!running) {
                break;
            }

            value++;
            view.setText(Integer.toString(value / 60) + ":"
                    + (value % 60 < 10 ? "0" : "") + Integer.toString(value % 60));
        }
    }

    public void stopTimer() {
        running = false;
    }

    public int getValue() {
        return value;
    }
}
