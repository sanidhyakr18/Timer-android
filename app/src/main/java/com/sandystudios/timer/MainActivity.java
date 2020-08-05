package com.sandystudios.timer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText etSec;
    TextView tvDisplay;
    Button btnStart, btnPause, btnReset;

    int pauseTime;
    Boolean isPaused;
    TimerTask tTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etSec = findViewById(R.id.etSec);
        tvDisplay = findViewById(R.id.tvDisplay);
        btnStart = findViewById(R.id.btnStart);
        btnPause = findViewById(R.id.btnPause);
        btnReset = findViewById(R.id.btnReset);
        btnPause.setVisibility(View.INVISIBLE);
        btnReset.setVisibility(View.INVISIBLE);

        etSec.requestFocus();
        btnStart.setTextColor(Color.GRAY);

        etSec.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                processButtonByTextLength();
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int totalSec = Integer.parseInt(etSec.getText().toString());
                btnPause.setVisibility(View.VISIBLE);
                btnReset.setVisibility(View.VISIBLE);
                btnStart.setVisibility(View.INVISIBLE);
                etSec.setEnabled(false);
                isPaused = false;
                pauseTime = 0;
                tTask = new TimerTask();
                tTask.execute(totalSec);
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnPause.getText() == "PAUSE") {
                    isPaused = true;
                    String text = "RESUME";
                    btnPause.setText(text);
                } else {
                    isPaused = false;
                    String text = "PAUSE";
                    btnPause.setText(text);
                    tTask = new TimerTask();
                    tTask.execute(pauseTime);
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPaused = true;
                reset();
                String text = "00:00";
                tvDisplay.setText(text);
            }
        });
    }

    class TimerTask extends AsyncTask<Integer, String, Void> {

        @Override
        protected Void doInBackground(Integer... integers) {
            for (int i = integers[0]; i >= 0; i--) {
                if(!isPaused) {
                    String displayTimer = timeLeftToString(i);
                    pauseTime = i;
                    publishProgress(displayTimer);
                    wait1Sec();
                } else {
                    break;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(!isPaused) {
                Toast.makeText(MainActivity.this, "TIME UP!", Toast.LENGTH_SHORT).show();
                reset();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            tvDisplay.setText(values[0]);
        }
    }

    private void processButtonByTextLength() {
        if (etSec.getText().toString().length() == 0) {
            btnStart.setTextColor(Color.GRAY);
            btnStart.setEnabled(false);
        } else {
            btnStart.setTextColor(getColor(R.color.colorPrimary));
            btnStart.setEnabled(true);
        }
    }

    private String timeLeftToString(int timeLeft) {
        int min = timeLeft / 60;
        int sec = timeLeft % 60;
        String time = "";
        if (min < 10) {
            time = time + "0" + min + ":";
        } else {
            time = time + min + ":";
        }
        if (sec < 10) {
            time = time + "0" + sec;
        } else {
            time = time + sec;
        }
        return time;
    }

    private void wait1Sec() {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() < startTime + 1000) ;
    }

    private void reset() {
        btnStart.setVisibility(View.VISIBLE);
        etSec.setEnabled(true);
        btnPause.setVisibility(View.INVISIBLE);
        btnReset.setVisibility(View.INVISIBLE);
        etSec.setText("");
        etSec.requestFocus();
        String text = "PAUSE";
        btnPause.setText(text);
    }
}