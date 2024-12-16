package com.example.taskapp3;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Build;
import android.provider.Settings;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnTaskActionListener {
    private RecyclerView recyclerViewTasks;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;
    private AppDatabase db;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // EdgeToEdge.enable(this); // We'll discuss this later
//        setContentView(R.layout.activity_main);
//
//
//
//        db = Room.databaseBuilder(getApplicationContext(),
//                AppDatabase.class, "task-database").allowMainThreadQueries().build();
//
//        recyclerViewTasks = findViewById(R.id.recyclerViewTasks);
//        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));
//
//        taskList = db.taskDao().getAllTasks();
//        taskAdapter = new TaskAdapter(taskList, this);
//        recyclerViewTasks.setAdapter(taskAdapter);
//
//
//        FloatingActionButton fabAddTask = findViewById(R.id.fabAddTask);
//        fabAddTask.setOnClickListener(v -> showAddTaskDialog());
//
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Only one setContentView() call

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
        );

        setContentView(R.layout.activity_main);

        // Optionally, comment out EdgeToEdge code
        // EdgeToEdge.enable(this);
        // ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
        //     Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        //     v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
        //     return insets;
        // });

        // Initialize the database
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "task-database").allowMainThreadQueries().build();

        // Set up RecyclerView
        recyclerViewTasks = findViewById(R.id.recyclerViewTasks);
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));

        // Get tasks from database
        taskList = db.taskDao().getAllTasks();
        taskAdapter = new TaskAdapter(taskList, this);
        recyclerViewTasks.setAdapter(taskAdapter);

        // Set up Floating Action Button
        FloatingActionButton fabAddTask = findViewById(R.id.fabAddTask);
        fabAddTask.setOnClickListener(v -> showAddTaskDialog());
    }

    private void showAddTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_task, null);
        builder.setView(dialogView);

        EditText editTextTaskTitle = dialogView.findViewById(R.id.editTextTaskTitle);
        EditText editTextTaskDate = dialogView.findViewById(R.id.editTextTaskDate);
        EditText editTextTaskTime = dialogView.findViewById(R.id.editTextTaskTime);

        // Date Picker
        editTextTaskDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        editTextTaskDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        // Time Picker
        editTextTaskTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
                    (view, hourOfDay, minute) -> {
                        editTextTaskTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
            timePickerDialog.show();
        });

        builder.setTitle("Add New Task")
                .setPositiveButton("Add", (dialog, which) -> {
                    String title = editTextTaskTitle.getText().toString();
                    String date = editTextTaskDate.getText().toString();
                    String time = editTextTaskTime.getText().toString();

                    if (!title.isEmpty() && !date.isEmpty() && !time.isEmpty()) {
                        String dateTime = date + " " + time;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                        try {
                            Date dueDate = sdf.parse(dateTime);
                            Task newTask = new Task(title, dueDate.getTime(), false);
                            db.taskDao().insert(newTask);

                            // Refresh the list
                            taskList.clear();
                            taskList.addAll(db.taskDao().getAllTasks());
                            taskAdapter.notifyDataSetChanged();

                            // Set up alarm
                            scheduleTaskReminder(newTask);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

//    private void scheduleTaskReminder(Task task) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Android 12 and above
//            if (!getApplicationContext().getSystemService(AlarmManager.class).canScheduleExactAlarms()) {
//                // Prompt the user to grant the permission
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle("Permission Required")
//                        .setMessage("This app needs permission to schedule exact alarms. Please grant the permission in the next screen.")
//                        .setPositiveButton("OK", (dialog, which) -> {
//                            Intent intent = new Intent();
//                            intent.setAction(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
//                            startActivity(intent);
//                        })
//                        .setNegativeButton("Cancel", null)
//                        .show();
//                return;
//            }
//        }
//
//        Intent intent = new Intent(this, AlarmReceiver.class);
//        intent.putExtra("taskTitle", task.title);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, task.id, intent, PendingIntent.FLAG_IMMUTABLE);
//
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, task.dueTime, pendingIntent);
//    }

//    private void scheduleTaskReminder(Task task) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Android 12 ve üzeri
//            if (!getApplicationContext().getSystemService(AlarmManager.class).canScheduleExactAlarms()) {
//                // Kullanıcıdan izin isteme kodu
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle("Permission Required")
//                        .setMessage("This app needs permission to schedule exact alarms. Please grant the permission in the next screen.")
//                        .setPositiveButton("OK", (dialog, which) -> {
//                            Intent intent = new Intent();
//                            intent.setAction(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
//                            startActivity(intent);
//                        })
//                        .setNegativeButton("Cancel", null)
//                        .show();
//                return;
//            }
//        }
//
//        Intent intent = new Intent(this, AlarmReceiver.class);
//        intent.putExtra("taskTitle", task.title);
//        intent.putExtra("taskDueTime", task.dueTime);
//
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, task.id, intent, PendingIntent.FLAG_IMMUTABLE);
//
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, task.dueTime, pendingIntent);
//    }

    private void scheduleTaskReminder(Task task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Android 12 ve üzeri
            if (!getApplicationContext().getSystemService(AlarmManager.class).canScheduleExactAlarms()) {
                // Kullanıcıdan izin isteme kodu
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Permission Required")
                        .setMessage("This app needs permission to schedule exact alarms. Please grant the permission in the next screen.")
                        .setPositiveButton("OK", (dialog, which) -> {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                            startActivity(intent);
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                return;
            }
        }

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("taskTitle", task.title);
        intent.putExtra("taskDueTime", task.dueTime);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                task.id,
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, task.dueTime, pendingIntent);
    }



    private void cancelTaskReminder(Task task) {
        Intent intent = new Intent(this, AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                task.id,
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    @Override
    public void onTaskDeleted(Task task) {
        // Remove the task from the database
        db.taskDao().delete(task);

        // Cancel the alarm associated with the task
        cancelTaskReminder(task);

        // Remove the task from the list and notify the adapter
        taskList.remove(task);
        taskAdapter.notifyDataSetChanged();

        Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTaskCompleted(Task task, boolean isChecked) {
        task.isCompleted = isChecked;
        db.taskDao().update(task);

        if (isChecked) {
            cancelTaskReminder(task);
        } else {
            scheduleTaskReminder(task);
        }
    }
}