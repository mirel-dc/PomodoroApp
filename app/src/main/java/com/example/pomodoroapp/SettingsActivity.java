package com.example.pomodoroapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    private EditText ETFocusTime, ETRestTime, ETChillTime;
    private CheckBox CBSound;
    private RelativeLayout main;
    private LinearLayout menu;
    private ImageButton IBTodoList, IBPomodoro, IBSettings;

    private String state;
    private Resources resources;

    //KEYS
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    private static final String FILE_NAME = "PREF";
    private static final String IS_SOUND = "IS_SOUND";
    private static final String FOCUS_TIME = "FOCUS_TIME";
    private static final String CHILL_TIME = "CHILL_TIME";
    private static final String REST_TIME = "REST_TIME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent intent = getIntent();
        state = intent.getStringExtra("state");
        resources = getResources();

        settings = getSharedPreferences(FILE_NAME, MODE_PRIVATE);

        IBSettings = findViewById(R.id.IBSettings);
        IBPomodoro = findViewById(R.id.IBPomodoro);
        IBTodoList = findViewById(R.id.IBTodoList);
        ETFocusTime = findViewById(R.id.ETFocusTime);
        ETChillTime = findViewById(R.id.ETChillTime);
        ETRestTime = findViewById(R.id.ETRestTime);
        CBSound = findViewById(R.id.CBSound);
        main = findViewById(R.id.settings);
        menu = findViewById(R.id.Menu);

        changeColor();

        ETFocusTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!ETFocusTime.getText().toString().equals("")) {
                    editor = settings.edit();
                    editor.putLong(FOCUS_TIME, Long.parseLong(String.valueOf(ETFocusTime.getText())) * 60 * 1000);
                    editor.apply();
                }
            }
        });

        ETChillTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!ETChillTime.getText().toString().equals("")) {
                    editor = settings.edit();
                    editor.putLong(CHILL_TIME, Long.parseLong(String.valueOf(ETChillTime.getText())) * 60 * 1000);
                    editor.apply();
                }
            }
        });

        ETRestTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!ETRestTime.getText().toString().equals("")) {
                    editor = settings.edit();
                    editor.putLong(REST_TIME, Long.parseLong(String.valueOf(ETRestTime.getText())) * 60 * 1000);
                    editor.apply();
                }
            }
        });

        CBSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor = settings.edit();
            editor.putBoolean(IS_SOUND, isChecked);
            editor.apply();
        }
        );


        //If settings were changed already, its gonna set them like user did
        long focusTime = settings.getLong(FOCUS_TIME, Long.parseLong(String.valueOf(ETFocusTime.getText())));
        ETFocusTime.setText(String.valueOf(focusTime / 60 / 1000));
        long chillTime = settings.getLong(CHILL_TIME, Long.parseLong(String.valueOf(ETChillTime.getText())));
        ETChillTime.setText(String.valueOf(chillTime / 60 / 1000));
        long restTime = settings.getLong(REST_TIME, Long.parseLong(String.valueOf(ETRestTime.getText())));
        ETRestTime.setText(String.valueOf(restTime / 60 / 1000));
        CBSound.setChecked(settings.getBoolean(IS_SOUND,true));
    }


    public void goTodoList(View v) {
        Intent intent = new Intent(this, TodoList.class);
        intent.putExtra("state", state);
        startActivity(intent);
        finish();
    }

    public void goPomodoro(View v) {
        finish();
    }

    public void changeColor() {
        if (Objects.equals(state, "0")) {          //Work
            main.setBackgroundColor(resources.getColor(R.color.BackWork));
            menu.setBackgroundColor(resources.getColor(R.color.NavOffWork));
            IBSettings.setBackgroundColor(resources.getColor(R.color.NavOnWork));
            IBPomodoro.setColorFilter(resources.getColor(R.color.NavButtonsWork),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            IBSettings.setColorFilter(resources.getColor(R.color.NavButtonsWork),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            IBTodoList.setColorFilter(resources.getColor(R.color.NavButtonsWork),
                    android.graphics.PorterDuff.Mode.SRC_IN);

        } else if (Objects.equals(state, "1")) {   //Chill
            main.setBackgroundColor(resources.getColor(R.color.BackChill));
            menu.setBackgroundColor(resources.getColor(R.color.NavOffChill));
            IBSettings.setBackgroundColor(resources.getColor(R.color.NavOnChill));
            IBPomodoro.setColorFilter(resources.getColor(R.color.NavButtonsChill),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            IBSettings.setColorFilter(resources.getColor(R.color.NavButtonsChill),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            IBTodoList.setColorFilter(resources.getColor(R.color.NavButtonsChill),
                    android.graphics.PorterDuff.Mode.SRC_IN);

        } else if (Objects.equals(state, "2")) {   //Rest
            main.setBackgroundColor(resources.getColor(R.color.BackRest));
            menu.setBackgroundColor(resources.getColor(R.color.NavOffRest));
            IBSettings.setBackgroundColor(resources.getColor(R.color.NavOnRest));
            IBPomodoro.setColorFilter(resources.getColor(R.color.NavButtonsRest),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            IBSettings.setColorFilter(resources.getColor(R.color.NavButtonsRest),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            IBTodoList.setColorFilter(resources.getColor(R.color.NavButtonsRest),
                    android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }
}