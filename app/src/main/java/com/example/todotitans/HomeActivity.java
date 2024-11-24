package com.example.todotitans;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private TextView userNameTextView;

    @SuppressLint("MissingInflatedId")
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
        userNameTextView = findViewById(R.id.user_name);

        // Initialize task list and adapter
        tasks = new ArrayList<>();
        taskAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tasks);
        taskList.setAdapter(taskAdapter);

        // Set current date
        setCurrentDate();

        // Populate days of the week
        populateDaysOfWeek();

        // Get the user ID passed from the SignInActivity
        String userId = getIntent().getStringExtra("USER_ID");

        if (userId != null) {
            // Fetch user details from Firebase Database
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String firstName = dataSnapshot.child("firstName").getValue(String.class);
                    String lastName = dataSnapshot.child("lastName").getValue(String.class);
                    String fullName = firstName + " " + lastName;
                    userNameTextView.setText(fullName);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(HomeActivity.this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Add Task Button functionality
        addTaskButton.setOnClickListener(v -> {
            addTask("New Task");
            // Show Toast notification after adding task
            Toast.makeText(HomeActivity.this, "Task Added", Toast.LENGTH_SHORT).show();
        });

        // Remove Task Button functionality
        removeTaskButton.setOnClickListener(v -> {
            // Show Toast notification after removing task
            if (!tasks.isEmpty()) {
                Toast.makeText(HomeActivity.this, "Task Removed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(HomeActivity.this, "No tasks to remove", Toast.LENGTH_SHORT).show();
            }
            removeTask();
        });

        currentDateTextView.setOnClickListener(v -> {
            // Navigate to CalendarActivity when the calendar button is clicked
            Intent intent = new Intent(HomeActivity.this, CalendarActivity.class);
            startActivity(intent);
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
