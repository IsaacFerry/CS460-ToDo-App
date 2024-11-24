package com.example.todotitans;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todotitans.database.Task;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private Context context;
    private ArrayList<Task> tasks;
    private Set<Integer> selectedTasks;

    public TaskAdapter(Context context, ArrayList<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
        this.selectedTasks = new HashSet<>();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_layout, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.textTitle.setText(task.getTitle());
        holder.descriptionTitle.setText(task.getDescription());
        holder.dateText.setText(task.getDueDate());

        // Highlight selected items
        if (selectedTasks.contains(position)) {
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
        }

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

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            descriptionTitle = itemView.findViewById(R.id.descriptionTitle);
            dateText = itemView.findViewById(R.id.dateText);
        }
    }
}
