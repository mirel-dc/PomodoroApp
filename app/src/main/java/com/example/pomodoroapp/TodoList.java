package com.example.pomodoroapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;

public class TodoList extends AppCompatActivity {

    private ListView listView;
    private EditText ETAddItem;
    private ImageButton IBTodoList, IBPomodoro, IBSettings;
    private RelativeLayout main;
    private LinearLayout menu;

    public static List<String> toDoList = new ArrayList<>();
    public static Queue<Integer> taskIdQueue = new PriorityQueue<>();
    private static int currentTaskId = 0;

    ArrayAdapter<String> adapter;

    private String state;
    private Resources resources;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        listView = findViewById(R.id.ListView);
        ETAddItem = findViewById(R.id.ET_add_item);
        IBSettings = findViewById(R.id.IBSettings);
        IBPomodoro = findViewById(R.id.IBPomodoro);
        IBTodoList = findViewById(R.id.IBTodoList);
        main = findViewById(R.id.TODOList);
        menu = findViewById(R.id.Menu);

        Intent intent = getIntent();
        state = intent.getStringExtra("state");
        resources = getResources();

        adapter = new ArrayAdapter<>(this, R.layout.todo_item, toDoList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            TextView textView = (TextView) view;
            textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            try {
                //Removing object itself from queue, cz taskIdQueue have all Ids of toDoList
                taskIdQueue.remove(i);

            } catch (Exception e) {
                System.out.println("Error - cannot remove");
            }
        });

        changeColor();
    }

    public void goSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra("state", state);
        startActivity(intent);
        finish();
    }

    public void goPomodoro(View view) {finish();
    }


    public void addItemToList(View v) {
        toDoList.add(ETAddItem.getText().toString());
        taskIdQueue.add(currentTaskId);
        currentTaskId++;

        adapter.notifyDataSetChanged();

        ETAddItem.setText("");
    }

    public void changeColor() {
        if (Objects.equals(state, "0")) {          //Work
            main.setBackgroundColor(resources.getColor(R.color.BackWork));
            menu.setBackgroundColor(resources.getColor(R.color.NavOffWork));
            IBTodoList.setBackgroundColor(resources.getColor(R.color.NavOnWork));
            IBPomodoro.setColorFilter(resources.getColor(R.color.NavButtonsWork),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            IBSettings.setColorFilter(resources.getColor(R.color.NavButtonsWork),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            IBTodoList.setColorFilter(resources.getColor(R.color.NavButtonsWork),
                    android.graphics.PorterDuff.Mode.SRC_IN);

        } else if (Objects.equals(state, "1")) {   //Chill
            main.setBackgroundColor(resources.getColor(R.color.BackChill));
            menu.setBackgroundColor(resources.getColor(R.color.NavOffChill));
            IBTodoList.setBackgroundColor(resources.getColor(R.color.NavOnChill));
            IBPomodoro.setColorFilter(resources.getColor(R.color.NavButtonsChill),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            IBSettings.setColorFilter(resources.getColor(R.color.NavButtonsChill),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            IBTodoList.setColorFilter(resources.getColor(R.color.NavButtonsChill),
                    android.graphics.PorterDuff.Mode.SRC_IN);

        } else if (Objects.equals(state, "2")) {   //Rest
            main.setBackgroundColor(resources.getColor(R.color.BackRest));
            menu.setBackgroundColor(resources.getColor(R.color.NavOffRest));
            IBTodoList.setBackgroundColor(resources.getColor(R.color.NavOnRest));
            IBPomodoro.setColorFilter(resources.getColor(R.color.NavButtonsRest),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            IBSettings.setColorFilter(resources.getColor(R.color.NavButtonsRest),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            IBTodoList.setColorFilter(resources.getColor(R.color.NavButtonsRest),
                    android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }
}