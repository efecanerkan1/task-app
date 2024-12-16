package com.example.taskapp3;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Task {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public long dueTime; // Store as timestamp in milliseconds
    public boolean isCompleted;

    // Constructor
    public Task(String title, long dueTime, boolean isCompleted) {
        this.title = title;
        this.dueTime = dueTime;
        this.isCompleted = isCompleted;
    }
}