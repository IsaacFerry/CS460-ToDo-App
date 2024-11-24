package com.example.todotitans;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todotitans.database.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private TextView currentDateTextView;
    private LinearLayout daysTimeline;
    private RecyclerView taskRecyclerView;
    private ImageButton addTaskButton, removeTaskButton;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private TaskAdapter taskAdapter;
    private ArrayList<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize Firebase Auth and Database Reference
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Tasks");

        // Initialize views
        currentDateTextView = findViewById(R.id.current_date);
        daysTimeline = findViewById(R.id.days_timeline);
        taskRecyclerView = findViewById(R.id.task_list);
        addTaskButton = findViewById(R.id.add_task_button);
        removeTaskButton = findViewById(R.id.remove_task_button);

        // Set up RecyclerView
        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(this, taskList);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskRecyclerView.setAdapter(taskAdapter);

        // Set current date
        setCurrentDate();

        // Populate days of the week
        populateDaysOfWeek();

        // Fetch tasks for the current user
        fetchUserTasks();

        // Add Task Button functionality
        addTaskButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CadenActivity.class);
            startActivity(intent);
        });

        // Remove Task Button functionality
        removeTaskButton.setOnClickListener(v -> deleteSelectedTasks());
    }

    private void setCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        currentDateTextView.setText(currentDate);
    }

    private void populateDaysOfWeek() {
        SimpleDateFormat sdfDay = new SimpleDateFormat("EEE", Locale.getDefault());
        SimpleDateFormat sdfDate = new SimpleDateFormat("d", Locale.getDefault());
        daysTimeline.removeAllViews();

        for (int i = 0; i < 7; i++) {
            TextView dayView = new TextView(this);
            dayView.setText(String.format("%s %s", sdfDay.format(new Date()), sdfDate.format(new Date())));
            dayView.setTextSize(18);
            dayView.setPadding(18, 8, 18, 8);

            if (i == 0) {
                dayView.setTypeface(Typeface.DEFAULT_BOLD);
            }

            daysTimeline.addView(dayView);
        }
    }
    private void fetchUserTasks() {
        String userId = firebaseAuth.getCurrentUser().getUid();

        databaseReference.orderByChild("userId").equalTo(userId)
                .addValueEventListener(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot snapshot) {
                        taskList.clear();
                        for (com.google.firebase.database.DataSnapshot taskSnapshot : snapshot.getChildren()) {
                            Task task = taskSnapshot.getValue(Task.class);
                            if (task != null) {
                                taskList.add(task);
                            }
                        }
                        taskAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull com.google.firebase.database.DatabaseError error) {
                        Toast.makeText(HomeActivity.this, "Failed to load tasks.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteSelectedTasks() {
        ArrayList<Task> selectedTasks = taskAdapter.getSelectedTasks();

        if (selectedTasks.isEmpty()) {
            Toast.makeText(this, "No tasks selected for deletion", Toast.LENGTH_SHORT).show();
            return;
        }

        for (Task task : selectedTasks) {
            databaseReference.child(task.getTaskId()).removeValue();
        }

        taskAdapter.removeTasks(selectedTasks);
        Toast.makeText(this, "Selected tasks deleted", Toast.LENGTH_SHORT).show();
    }
}
