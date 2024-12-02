package com.example.todotitans;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todotitans.database.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.HashMap;
import java.util.Map;

public class EditTaskActivity extends AppCompatActivity {

    private EditText editTitle, editDescription;
    private TextView textDate, textTime;
    private Button dateAndTimeButton, saveTaskButton;
    private Spinner prioritySpinner;
    private Task taskToEdit;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_task);

        // Get the task passed from the previous activity
        taskToEdit = (Task) getIntent().getSerializableExtra("task");

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Tasks");
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize views
        editTitle = findViewById(R.id.editTaskTitle);
        editDescription = findViewById(R.id.editTaskNotes);
        textDate = findViewById(R.id.showTextDate);
        textTime = findViewById(R.id.showTextTime);
        dateAndTimeButton = findViewById(R.id.dateAndTimeButton);
        saveTaskButton = findViewById(R.id.saveTaskButton);
        prioritySpinner = findViewById(R.id.priorityButton);

        // Set up priority dropdown
        ArrayAdapter<CharSequence> priorityAdapter = ArrayAdapter.createFromResource(this,
                R.array.priority, android.R.layout.simple_spinner_item);
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(priorityAdapter);

        // Pre-fill data
        editTitle.setText(taskToEdit.getTitle());
        editDescription.setText(taskToEdit.getDescription());
        prefillDateTime(taskToEdit.getDueDate());
        int priorityIndex = priorityAdapter.getPosition(taskToEdit.getPriorityLevel());
        prioritySpinner.setSelection(priorityIndex);

        // Handle Date and Time Button click
        dateAndTimeButton.setOnClickListener(v -> openDateTimeDialog());

        // Handle Save Task Button click
        saveTaskButton.setOnClickListener(v -> saveUpdatedTask());
    }

    private void openDateTimeDialog() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        // DatePicker Dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year, month, dayOfMonth) -> {
            textDate.setText(new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
                    .format(new Date(year - 1900, month, dayOfMonth)));
        }, currentYear, currentMonth, currentDay);

        // TimePicker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, hourOfDay, minute) -> {
            textTime.setText(String.format("%02d:%02d", hourOfDay, minute));
        }, currentHour, currentMinute, true);

        // Show the dialogs
        datePickerDialog.show();
        timePickerDialog.show();
    }

    private void saveUpdatedTask() {
        String updatedTitle = editTitle.getText().toString().trim();
        String updatedDescription = editDescription.getText().toString().trim();
        String updatedDate = textDate.getText().toString().trim();
        String updatedTime = textTime.getText().toString().trim();
        String updatedPriority = prioritySpinner.getSelectedItem().toString();

        if (TextUtils.isEmpty(updatedTitle)) {
            editTitle.setError("Task title is required!");
            return;
        }

        // Combine and format the date and time
        String formattedDueDate = updatedDate + " " + updatedTime;

        Map<String, Object> updatedTask = new HashMap<>();
        updatedTask.put("taskId", taskToEdit.getTaskId());
        updatedTask.put("userId", firebaseAuth.getCurrentUser().getUid());
        updatedTask.put("priorityLevel", updatedPriority);
        updatedTask.put("title", updatedTitle);
        updatedTask.put("description", updatedDescription);
        updatedTask.put("dueDate", formattedDueDate);

        databaseReference.child(taskToEdit.getTaskId()).setValue(updatedTask)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Task updated successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Failed to update task. Try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void prefillDateTime(String dueDate) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("MMMM dd, yyyy HH:mm", Locale.getDefault());
            Date date = inputFormat.parse(dueDate);

            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

            textDate.setText(dateFormat.format(date));
            textTime.setText(timeFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
            textDate.setText("");
            textTime.setText("");
        }
    }
}
