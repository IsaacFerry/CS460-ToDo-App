package com.example.todotitans;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private TextView currentDateTextView;
    private LinearLayout daysTimeline;
    private ListView taskList;
    private ImageButton addTaskButton;
    private ImageButton removeTaskButton;
    private ImageButton menuButton;
    private ArrayAdapter<String> taskAdapter;
    private ArrayList<String> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        currentDateTextView = findViewById(R.id.current_date);
        daysTimeline = findViewById(R.id.days_timeline);
        taskList = findViewById(R.id.task_list);
        addTaskButton = findViewById(R.id.add_task_button);
        removeTaskButton = findViewById(R.id.remove_task_button);
        menuButton = findViewById(R.id.menu_button);

        // Initialize task list and adapter
        tasks = new ArrayList<>();
        taskAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tasks);
        taskList.setAdapter(taskAdapter);

        // Set current date
        setCurrentDate();

        // Populate days of the week
        populateDaysOfWeek();

        // Add Task Button functionality
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CadenActivity.class);

                startActivity(intent);
            }
        });

        // Remove Task Button functionality
        removeTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeTask();
            }
        });
    }

    private void setCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        currentDateTextView.setText(currentDate);
    }

    private void populateDaysOfWeek() {
        SimpleDateFormat sdfDay = new SimpleDateFormat("EEE", Locale.getDefault());
        SimpleDateFormat sdfDate = new SimpleDateFormat("d", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        daysTimeline.removeAllViews();

        // Display the current week with the current day on the left
        for (int i = 0; i < 5; i++) {
            int dayOffset = (i - calendar.get(Calendar.DAY_OF_WEEK) + 1);
            calendar.add(Calendar.DAY_OF_MONTH, dayOffset);

            TextView dayView = new TextView(this);
            dayView.setText(String.format("%s %s", sdfDay.format(calendar.getTime()), sdfDate.format(calendar.getTime())));
            dayView.setTextSize(18);
            dayView.setPadding(18, 8, 18, 8);

            if (i == 0) {
                dayView.setTypeface(Typeface.DEFAULT_BOLD);
            }

            daysTimeline.addView(dayView);

            // Reset the calendar for next iteration
            calendar.add(Calendar.DAY_OF_MONTH, -dayOffset);
        }
    }

    private void addTask(String task) {
        tasks.add(task);
        taskAdapter.notifyDataSetChanged();
    }

    private void removeTask() {
        if (!tasks.isEmpty()) {
            tasks.remove(tasks.size() - 1);
            taskAdapter.notifyDataSetChanged();
        }
    }
}
