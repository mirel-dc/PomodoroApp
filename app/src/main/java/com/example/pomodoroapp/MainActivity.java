package com.example.pomodoroapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private CountDownTimer CDTimer;
    private ImageButton IBTodoList, IBPomodoro, IBSettings;
    private RelativeLayout main;
    private ProgressBar progressBar;
    private Resources resources;
    private LinearLayout menu;
    private ImageView dot11, dot12, dot13, dot14, dot21, dot22, dot23, dot24;
    private TextView textTime, textState, textStateCounter, textTask;

    Ringtone ringtone;

    //KEYS
    SharedPreferences settings;

    private long focusTime, chillTime, restTime;

    private static final String FILE_NAME = "PREF";
    private static final String IS_SOUND = "IS_SOUND";
    private static final String FOCUS_TIME = "FOCUS_TIME";
    private static final String CHILL_TIME = "CHILL_TIME";
    private static final String REST_TIME = "REST_TIME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = getSharedPreferences(FILE_NAME, MODE_PRIVATE);

        IBSettings = findViewById(R.id.IBSettings);
        IBPomodoro = findViewById(R.id.IBPomodoro);
        IBTodoList = findViewById(R.id.IBTodoList);
        textTime = findViewById(R.id.textTime);
        textState = findViewById(R.id.textState);
        textTask = findViewById(R.id.textTask);
        textStateCounter = findViewById(R.id.textStateCounter);
        main = findViewById(R.id.Main);
        progressBar = findViewById(R.id.progressBar);
        menu = findViewById(R.id.Menu);
        dot11 = findViewById(R.id.dot11);
        dot12 = findViewById(R.id.dot12);
        dot13 = findViewById(R.id.dot13);
        dot14 = findViewById(R.id.dot14);
        dot21 = findViewById(R.id.dot21);
        dot22 = findViewById(R.id.dot22);
        dot23 = findViewById(R.id.dot23);
        dot24 = findViewById(R.id.dot24);
        resources = getResources();

        changeColor();

        focusTime = settings.getLong(FOCUS_TIME, 25 * 60 * 1000);
        chillTime = settings.getLong(CHILL_TIME, 5 * 60 * 1000);
        restTime = settings.getLong(REST_TIME, 15 * 60 * 1000);

        setAlarm();
        setCurrentTask();

        updateTextTimer(getTimeMin(focusTime), getTimeSec(focusTime));

        resetStateCounter();
    }

    public void goSettings(View v) {
        Intent intent = new Intent(this, SettingsActivity.class);
        String state = String.valueOf(getState());
        intent.putExtra("state", state);
        startActivity(intent);
    }

    public void goTodoList(View v) {
        Intent intent = new Intent(this, TodoList.class);
        String state = String.valueOf(getState());
        intent.putExtra("state", state);
        startActivity(intent);
    }

    public void setAlarm() {
        if (settings.getBoolean(IS_SOUND, true)) {
            Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            ringtone = RingtoneManager.getRingtone(this, notificationUri);
        } else {
            Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            ringtone = RingtoneManager.getRingtone(this, notificationUri);
        }
    }

    public void resetAll(View v) {
        resetState();
        resetDotes();
        timerRunning = false;
        resetStateCounter();
        incCounter();
        changeColor();
        resetTimer();
    }

    //Skip Button
    public void skip(View v) {
        nextState();
        resetTimer();
    }

    //Timer block
    private boolean timerRunning = false;

    //Will be inited later, cz of SharedPref
    private long timeLeft;

    //Timer Button
    public void timerBtn(View v) {
        if (!timerRunning) {
            startTimer();
        } else {
            pauseTimer();
        }
    }

    public long getTime() {
        try {
            focusTime = settings.getLong(FOCUS_TIME, 25 * 60 * 1000);
        } catch (Exception e) {
            focusTime = 25 * 1000 * 60;
        }
        try {
            chillTime = settings.getLong(CHILL_TIME, 5 * 60 * 1000);
        } catch (Exception e) {
            chillTime = 5 * 60 * 1000;
        }
        try {
            restTime = settings.getLong(REST_TIME, 15 * 60 * 1000);
        } catch (Exception e) {
            restTime = 15 * 60 * 1000;
        }

        if (getState() == 0) {
            return focusTime;
        } else if (getState() == 1) {
            return chillTime;
        } else if (getState() == 2) {
            return restTime;
        } else return 0;
    }

    public String getTimeMin(long ms) {
        return Long.toString(ms / 1000 / 60);
    }

    public String getTimeSec(long ms) {
        return Long.toString(ms / 1000 % 60);
    }

    public void updateTextTimer(String s1, String s2) {
        textTime.setText(s1 + " : " + s2);
    }

    boolean firstEntryTimer = true;

    public void startTimer() {

        if (firstEntryTimer) {
            timeLeft = focusTime;
            firstEntryTimer = false;
        }

        CDTimer = new CountDownTimer(timeLeft, 500) {
            @Override
            public void onTick(long l) {
                timeLeft = l;
                progressBar.setProgress(100 - Long.valueOf(l * 100 / getTime()).intValue());
                updateTextTimer(getTimeMin(timeLeft), getTimeSec(timeLeft));
            }

            @Override
            public void onFinish() {
                setAlarm();
                startAlarm();

                nextState();
                timeLeft = getTime();
                startTimer();
                updateTextTimer(getTimeMin(getTime()), getTimeSec(getTime()));

                String info;
                if (getState() == 0) {          //Work
                    info = "Time for Work";
                } else if (getState() == 1) {   //Chill
                    info = "Time for Chill";
                } else if (getState() == 2) {   //Rest
                    info = "Time for Rest";
                } else
                    info = "Good day";
                Toast.makeText(MainActivity.this, info, Toast.LENGTH_SHORT).show();
            }
        };

        CDTimer.start();
        timerRunning = true;
    }

    public void startAlarm() {
        ringtone.play();
        long alarmTime = 3000;
        CountDownTimer ringtoneTimer = new CountDownTimer(alarmTime, alarmTime) {

            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                ringtone.stop();
            }
        };
        ringtoneTimer.start();
    }

    public void pauseTimer() {
        CDTimer.cancel();
        timerRunning = false;
    }


    public void resetTimer() {
        if (!firstEntryTimer) {
            CDTimer.cancel();
        }
        timeLeft = getTime();
        updateTextTimer(getTimeMin(timeLeft), getTimeSec(timeLeft));
    }

    //Text State Counter (1/4 thing)
    private int numerator = 1, denominator = getDenominator();

    public int getDenominator() {
        return 4;
    }

    public void updateStateCounter() {
        textStateCounter.setText(numerator + "/" + denominator);
    }

    public void incNumerator() {
        numerator++;
    }

    boolean firstEntryStateCounter = true;

    public void resetStateCounter() {
        if (firstEntryStateCounter) {
            numerator = 1;
            firstEntryStateCounter = false;
        } else
            numerator = 0;
        denominator = getDenominator();
        updateStateCounter();
    }


    //Task block
    private boolean isNoTask=true;

    public void setCurrentTask() {
        try {
            textTask.setText(getString(R.string.task) + TodoList.toDoList.get(TodoList.taskIdQueue.peek()));
            isNoTask=false;
        } catch (Exception e) {
            textTask.setText(R.string.no_task);
            isNoTask=true;
        }
    }

    public void completeTask(View view) {
        if (isNoTask) {
            setCurrentTask();
        } else
            try {
                TodoList.taskIdQueue.remove();
            }finally {
                setCurrentTask();
            }
    }

    //Dotes block
    private int dotCounter = 0;

    public ImageView rotateDot() {
        dotCounter++;
        if (dotCounter == 9) {
            resetDotes();
            dotCounter++;
        }
        switch (dotCounter) {
            case 1:
                return dot11;
            case 2:
                return dot12;
            case 3:
                return dot13;
            case 4:
                return dot14;
            case 5:
                return dot21;
            case 6:
                return dot22;
            case 7:
                return dot23;
            case 8:
                return dot24;
            default:
                return null;
        }
    }

    public void resetDotes() {
        dot11.setColorFilter(resources.getColor(R.color.text_dark),
                android.graphics.PorterDuff.Mode.SRC_IN);
        dot12.setColorFilter(resources.getColor(R.color.text_dark),
                android.graphics.PorterDuff.Mode.SRC_IN);
        dot13.setColorFilter(resources.getColor(R.color.text_dark),
                android.graphics.PorterDuff.Mode.SRC_IN);
        dot14.setColorFilter(resources.getColor(R.color.text_dark),
                android.graphics.PorterDuff.Mode.SRC_IN);
        dot21.setColorFilter(resources.getColor(R.color.text_dark),
                android.graphics.PorterDuff.Mode.SRC_IN);
        dot22.setColorFilter(resources.getColor(R.color.text_dark),
                android.graphics.PorterDuff.Mode.SRC_IN);
        dot23.setColorFilter(resources.getColor(R.color.text_dark),
                android.graphics.PorterDuff.Mode.SRC_IN);
        dot24.setColorFilter(resources.getColor(R.color.text_dark),
                android.graphics.PorterDuff.Mode.SRC_IN);
        dotCounter = 0;
    }

    //State switch block
    private int counter = 1;

    public void nextState() {
        incCounter();
        changeColor();
    }

    public int getCounter() {
        return counter;
    }

    public void incCounter() {
        counter++;
    }

    public int getState() {
        if ((getCounter() % 8 == 0)) {
            return 2;   //Rest
        } else if ((getCounter() % 2 == 0)) {
            return 1;   //Chill
        } else if (getCounter() % 2 == 1) {
            return 0;   //Work
        }
        return 0;
    }

    public void resetState() {
        counter = 0;
        changeColor();
    }

    //Changing colors
    public void changeColor() {
        if (getState() == 0) {          //Work
            main.setBackgroundColor(resources.getColor(R.color.BackWork));
            progressBar.setProgressDrawable(resources.getDrawable(R.drawable.circle_work));
            menu.setBackgroundColor(resources.getColor(R.color.NavOffWork));
            IBPomodoro.setBackgroundColor(resources.getColor(R.color.NavOnWork));
            IBPomodoro.setColorFilter(resources.getColor(R.color.NavButtonsWork),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            IBSettings.setColorFilter(resources.getColor(R.color.NavButtonsWork),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            IBTodoList.setColorFilter(resources.getColor(R.color.NavButtonsWork),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            textState.setText(resources.getText(R.string.Work));
            incNumerator();
            updateStateCounter();
        } else if (getState() == 1) {   //Chill
            main.setBackgroundColor(resources.getColor(R.color.BackChill));
            progressBar.setProgressDrawable(resources.getDrawable(R.drawable.circle_chill));
            menu.setBackgroundColor(resources.getColor(R.color.NavOffChill));
            IBPomodoro.setBackgroundColor(resources.getColor(R.color.NavOnChill));
            IBPomodoro.setColorFilter(resources.getColor(R.color.NavButtonsChill),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            IBSettings.setColorFilter(resources.getColor(R.color.NavButtonsChill),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            IBTodoList.setColorFilter(resources.getColor(R.color.NavButtonsChill),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            textState.setText(resources.getText(R.string.Chill));
            rotateDot().setColorFilter(resources.getColor(R.color.text_white),
                    android.graphics.PorterDuff.Mode.SRC_IN);
        } else if (getState() == 2) {   //Rest
            main.setBackgroundColor(resources.getColor(R.color.BackRest));
            progressBar.setProgressDrawable(resources.getDrawable(R.drawable.circle_rest));
            menu.setBackgroundColor(resources.getColor(R.color.NavOffRest));
            IBPomodoro.setBackgroundColor(resources.getColor(R.color.NavOnRest));
            IBPomodoro.setColorFilter(resources.getColor(R.color.NavButtonsRest),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            IBSettings.setColorFilter(resources.getColor(R.color.NavButtonsRest),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            IBTodoList.setColorFilter(resources.getColor(R.color.NavButtonsRest),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            textState.setText(resources.getText(R.string.Rest));
            rotateDot().setColorFilter(resources.getColor(R.color.text_white),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            resetStateCounter();
        }
    }
}