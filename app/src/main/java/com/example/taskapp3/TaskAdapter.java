package com.example.taskapp3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
//
//public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
//
//    private List<Task> taskList;
//    private OnTaskCompleteListener listener;
//
//    public interface OnTaskCompleteListener {
//        void onTaskCompleted(Task task, boolean isChecked);
//        void onTaskDeleted(Task task);
//    }
//
//    public TaskAdapter(List<Task> taskList, OnTaskCompleteListener listener) {
//        this.taskList = taskList;
//        this.listener = listener;
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        public TextView textViewTaskTitle;
//        public TextView textViewTaskTime;
//        public CheckBox checkBoxTaskCompleted;
//
//        public ViewHolder(View view) {
//            super(view);
//            textViewTaskTitle = view.findViewById(R.id.textViewTaskTitle);
//            textViewTaskTime = view.findViewById(R.id.textViewTaskTime);
//            checkBoxTaskCompleted = view.findViewById(R.id.checkBoxTaskCompleted);
//        }
//    }
//
//    @Override
//    public TaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_task, parent, false);
//
//        return new ViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(TaskAdapter.ViewHolder holder, int position) {
//        final Task task = taskList.get(position);
//        holder.textViewTaskTitle.setText(task.title);
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
//        String dueTime = sdf.format(task.dueTime);
//        holder.textViewTaskTime.setText("Due Time: " + dueTime);
//
//        holder.checkBoxTaskCompleted.setChecked(task.isCompleted);
//        holder.checkBoxTaskCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            listener.onTaskCompleted(task, isChecked);
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return taskList.size();
//    }
//}

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<Task> taskList;
    private OnTaskActionListener listener;

    public interface OnTaskActionListener {
        void onTaskCompleted(Task task, boolean isChecked);
        void onTaskDeleted(Task task);
    }

    public TaskAdapter(List<Task> taskList, OnTaskActionListener listener) {
        this.taskList = taskList;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTaskTitle;
        public TextView textViewTaskTime;
        public CheckBox checkBoxTaskCompleted;
        public ImageButton buttonDeleteTask;

        public ViewHolder(View view) {
            super(view);
            textViewTaskTitle = view.findViewById(R.id.textViewTaskTitle);
            textViewTaskTime = view.findViewById(R.id.textViewTaskTime);
            checkBoxTaskCompleted = view.findViewById(R.id.checkBoxTaskCompleted);
            buttonDeleteTask = view.findViewById(R.id.buttonDeleteTask);
        }
    }

    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TaskAdapter.ViewHolder holder, int position) {
        final Task task = taskList.get(position);
        holder.textViewTaskTitle.setText(task.title);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String dueTime = sdf.format(task.dueTime);
        holder.textViewTaskTime.setText("Due Time: " + dueTime);

        holder.checkBoxTaskCompleted.setChecked(task.isCompleted);
        holder.checkBoxTaskCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            listener.onTaskCompleted(task, isChecked);
        });

        holder.buttonDeleteTask.setOnClickListener(v -> {
            listener.onTaskDeleted(task);
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}