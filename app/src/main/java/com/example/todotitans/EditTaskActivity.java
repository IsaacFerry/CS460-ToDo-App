package com.example.todotitans;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todotitans.database.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditTaskActivity extends AppCompatActivity {

    private EditText editTitle, editDescription, editDate;
    private Button saveButton;
    private Task taskToEdit;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_layout);  // Inflate the task_layout XML

        // Get the task passed from the previous activity
        taskToEdit = (Task) getIntent().getSerializableExtra("task");

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Tasks");

        // Initialize the views
        TextView textTitle = findViewById(R.id.textTitle);
        TextView descriptionTitle = findViewById(R.id.descriptionTitle);
        TextView dateText = findViewById(R.id.dateText);

        // Convert TextViews into EditTexts for editing
        editTitle = new EditText(this);
        editDescription = new EditText(this);
        editDate = new EditText(this);

        // Replace TextViews with EditTexts
        ((ViewGroup) textTitle.getParent()).removeView(textTitle);
        ((ViewGroup) descriptionTitle.getParent()).removeView(descriptionTitle);
        ((ViewGroup) dateText.getParent()).removeView(dateText);

        ((ViewGroup) findViewById(R.id.editAndDelete)).addView(editTitle);
        ((ViewGroup) findViewById(R.id.editAndDelete)).addView(editDescription);
        ((ViewGroup) findViewById(R.id.editAndDelete)).addView(editDate);

        // Set the initial text values to the EditTexts
        editTitle.setText(taskToEdit.getTitle());
        editDescription.setText(taskToEdit.getDescription());
        editDate.setText(taskToEdit.getDueDate());

        // Hide the "edit" icon and "delete" button
        ImageView editImage = findViewById(R.id.editImage);
        editImage.setVisibility(View.GONE);

        // Add a save button programmatically
        saveButton = new Button(this);
        saveButton.setText("Save");
        ((ViewGroup) findViewById(R.id.editAndDelete)).addView(saveButton);

        // Handle save button click
        saveButton.setOnClickListener(v -> {
            // Save the updated task
            String updatedTitle = editTitle.getText().toString();
            String updatedDescription = editDescription.getText().toString();
            String updatedDate = editDate.getText().toString();

            // Update task object
            taskToEdit.setTitle(updatedTitle);
            taskToEdit.setDescription(updatedDescription);
            taskToEdit.setDueDate(updatedDate);

            // Save the updated task to the database
            saveUpdatedTask(taskToEdit);
        });
    }

    // Method to save the updated task to Firebase
    private void saveUpdatedTask(Task task) {
        databaseReference.child(task.getTaskId()).setValue(task)
                .addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(EditTaskActivity.this, "Task updated successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Close the activity and return to the previous one
                    } else {
                        Toast.makeText(EditTaskActivity.this, "Failed to update task. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
