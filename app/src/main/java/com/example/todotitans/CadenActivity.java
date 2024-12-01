package com.example.todotitans;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CadenActivity extends AppCompatActivity {

    private TextView textDate;
    private TextView textTime;
    private Button dateAndTimeButton;
    private Button saveTaskButton;
    private Spinner spinnerPriority;
    private TextInputEditText editTaskTitle;
    private TextInputEditText editTaskDescription;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_task);

        // Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Tasks");
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize views
        spinnerPriority = findViewById(R.id.priorityButton);
        textDate = findViewById(R.id.showTextDate);
        textTime = findViewById(R.id.showTextTime);
        dateAndTimeButton = findViewById(R.id.dateAndTimeButton);
        saveTaskButton = findViewById(R.id.saveTaskButton);
        editTaskTitle = findViewById(R.id.editTaskTitle);
        editTaskDescription = findViewById(R.id.editTaskNotes);

        // Setup Priority Spinner
        ArrayAdapter<CharSequence> priorityAdapter = ArrayAdapter.createFromResource(this,
                R.array.priority, android.R.layout.simple_spinner_item);
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(priorityAdapter);

        // Set Date and Time Picker
        dateAndTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        // Save Task Button Listener
        saveTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTaskToDatabase();
            }
        });
    }

    private void openDialog() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        // Create DatePickerDialog with current date as default
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year, month, dayOfMonth) -> {
            textDate.setText(String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth));
        }, currentYear, currentMonth, currentDay);

        // Create TimePickerDialog with a default time of 15:00
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, hourOfDay, minute) -> {
            textTime.setText(String.format("%02d:%02d", hourOfDay, minute));
        }, 15, 0, true);

        // Show the dialogs
        datePickerDialog.show();
        timePickerDialog.show();
    }

    private void saveTaskToDatabase() {
        // Get input values
        String taskTitle = editTaskTitle.getText().toString().trim();
        String taskDescription = editTaskDescription.getText().toString().trim();
        String taskDate = textDate.getText().toString().trim();
        String taskTime = textTime.getText().toString().trim();
        String priorityLevel = spinnerPriority.getSelectedItem().toString();

        // Validate inputs
        if (TextUtils.isEmpty(taskTitle)) {
            editTaskTitle.setError("Task title is required!");
            return;
        }

        // Get the current user ID from Firebase Authentication
        String userId = firebaseAuth.getCurrentUser().getUid();
        String status = "Pending"; // Default status

        // Create a unique ID for the task
        String taskId = databaseReference.push().getKey();

        // Create a task map
        Map<String, Object> task = new HashMap<>();
        task.put("taskId", taskId);
        task.put("userId", userId);
        task.put("priorityLevel", priorityLevel);
        task.put("title", taskTitle);
        task.put("description", taskDescription);
        task.put("status", status);

        // Only set dueDate if both date and time are provided
        if (!TextUtils.isEmpty(taskDate) && !TextUtils.isEmpty(taskTime)) {
            task.put("dueDate", taskDate + " " + taskTime);
        } else {
            task.put("dueDate", ""); // Save as an empty string
        }

        // Save task to Firebase
        databaseReference.child(taskId).setValue(task)
                .addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(CadenActivity.this, "Task saved successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Close activity
                    } else {
                        Toast.makeText(CadenActivity.this, "Failed to save task. Try again!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
