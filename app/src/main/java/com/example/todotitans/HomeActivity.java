package com.example.todotitans;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todotitans.database.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class HomeActivity extends AppCompatActivity {

    private TextView currentDateTextView;
    private LinearLayout daysTimeline;
    private ImageButton addTaskButton;
    private ImageButton removeTaskButton;
    private ImageButton menuButton;
    private ArrayList<String> tasks;
    private ImageButton logoutButton;
    private TextView userNameTextView;
    private RecyclerView taskRecyclerView;
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
        logoutButton = findViewById(R.id.logout_button);
        userNameTextView = findViewById(R.id.user_name);

        // Set up RecyclerView
        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(this, taskList, databaseReference);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskRecyclerView.setAdapter(taskAdapter);

        // Set current date
        setCurrentDate();

        // Populate days of the week
        populateDaysOfWeek();

        // Show the users tasks
        fetchUserTasks();

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
            Intent intent = new Intent(HomeActivity.this, CadenActivity.class);
            startActivity(intent);
        });


        // Delete a task that is selected
        removeTaskButton.setOnClickListener(v -> deleteSelectedTasks());

        currentDateTextView.setOnClickListener(v -> {
            // Navigate to CalendarActivity when the calendar button is clicked
            Intent intent = new Intent(HomeActivity.this, CalendarActivity.class);
            startActivity(intent);
        });


        // Set up the Log Out button functionality
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log out the user
                FirebaseAuth.getInstance().signOut();

                // Navigate back to the sign-in page
                Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // Close HomeActivity
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (!powerManager.isIgnoringBatteryOptimizations(getPackageName())) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        }


        // Mark task as completed or uncheck to reset the status
        taskAdapter.setOnTaskCompleteListener(task -> {
            // Toggle the task status between "Completed" and "Pending"
            if ("Completed".equals(task.getStatus())) {
                task.setStatus("Pending");
            } else {
                task.setStatus("Completed");
            }

            // Save the updated task back to Firebase
            databaseReference.child(task.getTaskId()).setValue(task)
                    .addOnSuccessListener(aVoid -> taskAdapter.notifyDataSetChanged())
                    .addOnFailureListener(e -> Toast.makeText(HomeActivity.this, "Failed to update task", Toast.LENGTH_SHORT).show());
        });

        // New task
        taskAdapter.setOnTaskEditListener(task -> {
            Intent intent = new Intent(HomeActivity.this, CadenActivity.class);
            intent.putExtra("TASK_ID", task.getTaskId());
            startActivity(intent);
        });

        // Edit task
        taskAdapter.setOnTaskEditListener(task -> {
            Intent intent = new Intent(HomeActivity.this, EditTaskActivity.class);
            intent.putExtra("task", (Serializable) task);
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

        // Get the current date for comparison
        Calendar currentCalendar = Calendar.getInstance();
        int currentDayOfMonth = currentCalendar.get(Calendar.DAY_OF_MONTH);

        daysTimeline.removeAllViews();

        // Display the current week with the current day on the left
        for (int i = 0; i < 7; i++) {
            int dayOffset = (i - calendar.get(Calendar.DAY_OF_WEEK) + 1);
            calendar.add(Calendar.DAY_OF_MONTH, dayOffset);

            TextView dayView = new TextView(this);
            dayView.setText(String.format("%s %s", sdfDay.format(calendar.getTime()), sdfDate.format(calendar.getTime())));
            dayView.setTextSize(15);
            dayView.setPadding(12, 8, 12, 8);

            // Highlight the current date
            if (calendar.get(Calendar.DAY_OF_MONTH) == currentDayOfMonth) {
                dayView.setTypeface(Typeface.DEFAULT_BOLD);
            }

            daysTimeline.addView(dayView);

            // Reset the calendar for next iteration
            calendar.add(Calendar.DAY_OF_MONTH, -dayOffset);
        }
    }
    private void fetchUserTasks() {
        String userId = firebaseAuth.getCurrentUser().getUid();

        databaseReference.orderByChild("userId").equalTo(userId)
                .addValueEventListener(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot snapshot) {
                        taskList.clear(); // Clear the task list
                        for (com.google.firebase.database.DataSnapshot taskSnapshot : snapshot.getChildren()) {
                            Task task = taskSnapshot.getValue(Task.class);
                            if (task != null) {
                                taskList.add(task);
                            }
                        }

                        // Sort the taskList by dueDate
                        taskList.sort((task1, task2) -> {
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy HH:mm", Locale.getDefault());
                                Date date1 = sdf.parse(task1.getDueDate());
                                Date date2 = sdf.parse(task2.getDueDate());
                                return date1.compareTo(date2);
                            } catch (Exception e) {
                                e.printStackTrace();
                                return 0; // Keep the order unchanged if parsing fails
                            }
                        });

                        taskAdapter.notifyDataSetChanged(); // Refresh RecyclerView
                    }

                    @Override
                    public void onCancelled(@NonNull com.google.firebase.database.DatabaseError error) {
                        Toast.makeText(HomeActivity.this, "Failed to load tasks.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Delete a task functionality
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


    private void scheduleNotification(Task task) {
        if (task.getDueDate() == null || task.getDueDate().trim().isEmpty()) {
            Log.d("NotificationDebug", "No due date for task: " + task.getTitle());
            return;
        }

        Date dueDate = task.getDueDateAsDate();
        if (dueDate == null) {
            Log.d("NotificationDebug", "Invalid date format for task: " + task.getTitle());
            return;
        }

        long dueTimeMillis = dueDate.getTime();
        String priority = task.getPriorityLevel();

        if ("High".equalsIgnoreCase(priority)) {
            scheduleAlarm(dueTimeMillis - TimeUnit.MINUTES.toMillis(60), task, 60);
            scheduleAlarm(dueTimeMillis - TimeUnit.MINUTES.toMillis(30), task, 30);
            scheduleAlarm(dueTimeMillis - TimeUnit.MINUTES.toMillis(15), task, 15);
        } else if ("Medium".equalsIgnoreCase(priority)) {
            scheduleAlarm(dueTimeMillis - TimeUnit.MINUTES.toMillis(30), task, 30);
            scheduleAlarm(dueTimeMillis - TimeUnit.MINUTES.toMillis(15), task, 15);
        } else if ("Low".equalsIgnoreCase(priority)) {
            scheduleAlarm(dueTimeMillis - TimeUnit.MINUTES.toMillis(15), task, 15);
        }
    }


    private void scheduleAlarm(long time, Task task, int minutesBefore) {
        if (time <= System.currentTimeMillis()) {
            Log.d("NotificationDebug", "Skipping alarm for task: " + task.getTitle() + " as the time is in the past.");
            return;
        }

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
    notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, task.getTaskId());
    notificationIntent.putExtra(
    NotificationPublisher.NOTIFICATION,
            "Your task '" + task.getTitle() + "' is due in " + minutesBefore + " minutes!"
            );

    PendingIntent pendingIntent = PendingIntent.getBroadcast(
            this, (task.getTaskId() + "_" + minutesBefore).hashCode(), notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);

    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    Log.d("NotificationDebug", "Scheduling alarm for " + task.getTitle() + " in " + minutesBefore + " minutes at " + new Date(time));
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    } else {
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }
}



    private void cancelAlarm(PendingIntent pendingIntent) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        Log.d("NotificationDebug", "Canceled alarm for PendingIntent: " + pendingIntent);
    }

}
