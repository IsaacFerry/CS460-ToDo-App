package com.example.todotitans;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todotitans.database.Task;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final DatabaseReference databaseReference;
    private Context context;
    private ArrayList<Task> tasks;
    private Set<Integer> selectedTasks;
    private OnTaskCompleteListener onTaskCompleteListener;
    private OnTaskEditListener onTaskEditListener;

    public TaskAdapter(Context context, ArrayList<Task> tasks, DatabaseReference databaseReference) {
        this.context = context;
        this.tasks = tasks;
        this.selectedTasks = new HashSet<>();
        this.databaseReference = databaseReference;
    }

    public interface OnTaskCompleteListener {
        void onTaskComplete(Task task);
    }

    public interface OnTaskEditListener {
        void onTaskEdit(Task task);
    }

    public void setOnTaskCompleteListener(OnTaskCompleteListener listener) {
        this.onTaskCompleteListener = listener;
    }

    public void setOnTaskEditListener(OnTaskEditListener listener) {
        this.onTaskEditListener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_layout, parent, false);
        return new TaskViewHolder(view);
    }

    // Helper method to determine the day suffix
    private String getDaySuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th"; // Special case for 11th, 12th, 13th
        }
        switch (day % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);


        holder.textTitle.setText(task.getTitle());
        holder.descriptionTitle.setText(task.getDescription());


        // Check if the dueDate is empty or null
        if (task.getDueDate() != null && !task.getDueDate().trim().isEmpty()) {
            holder.dateText.setText(task.getDueDate());
            holder.dateText.setVisibility(View.VISIBLE); // Show the TextView
        } else {
            holder.dateText.setVisibility(View.GONE); // Hide the TextView if no due date
        }
        // Highlight selected items
        if (selectedTasks.contains(position)) {
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
        }

        // Handle task completion
        // Set the checkbox state based on task status
        holder.taskCompleteCheckBox.setChecked("Completed".equals(task.getStatus()));

        // Handle task completion/uncompletion when the checkbox is clicked
        holder.taskCompleteCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                task.setStatus("Completed"); // Mark the task as completed
            } else {
                task.setStatus("Pending"); // Mark the task as pending
            }

            // Save the updated task to Firebase
            databaseReference.child(task.getTaskId()).setValue(task)
                    .addOnSuccessListener(aVoid -> {
                        notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Failed to update task", Toast.LENGTH_SHORT).show();
                    });
        });

        // Handle task editing
        holder.editTaskButton.setOnClickListener(v -> {
            if (onTaskEditListener != null) {
                onTaskEditListener.onTaskEdit(task);
            }
        });

        // Set click listener for selecting tasks
        holder.itemView.setOnClickListener(v -> {
            if (selectedTasks.contains(position)) {
                selectedTasks.remove(position);
                holder.itemView.setBackgroundColor(Color.WHITE);
            } else {
                selectedTasks.add(position);
                holder.itemView.setBackgroundColor(Color.LTGRAY);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public ArrayList<Task> getSelectedTasks() {
        ArrayList<Task> selected = new ArrayList<>();
        for (int index : selectedTasks) {
            selected.add(tasks.get(index));
        }
        return selected;
    }

    public void removeTasks(ArrayList<Task> tasksToRemove) {
        tasks.removeAll(tasksToRemove);
        selectedTasks.clear();
        notifyDataSetChanged();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView textTitle, descriptionTitle, dateText;
        CheckBox taskCompleteCheckBox;
        ImageView editTaskButton;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            descriptionTitle = itemView.findViewById(R.id.descriptionTitle);
            dateText = itemView.findViewById(R.id.dateText);
            taskCompleteCheckBox = itemView.findViewById(R.id.taskCompleteCheckBox);
            editTaskButton = itemView.findViewById(R.id.editImage);
        }
    }
}
