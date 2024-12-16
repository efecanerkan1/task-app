//package com.example.taskapp3;
//
//import android.media.MediaPlayer;
//import android.os.Bundle;
//import android.os.Vibrator;
//import android.view.View;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;
//
//public class AlarmActivity extends AppCompatActivity {
//
//    private MediaPlayer mediaPlayer;
//    private Vibrator vibrator;
//    private Task task;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_alarm);
//
//        String taskTitle = getIntent().getStringExtra("taskTitle");
//        long taskDueTime = getIntent().getLongExtra("taskDueTime", 0);
//
//        // Görev bilgilerini ekranda gösterin
//        TextView textViewTaskTitle = findViewById(R.id.textViewAlarmTaskTitle);
//        textViewTaskTitle.setText("Task: " + taskTitle);
//
//        // İsterseniz, taskDueTime bilgisini da formatlayıp gösterebilirsiniz
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
//        String dueTimeFormatted = sdf.format(new Date(taskDueTime));
//
//        TextView textViewTaskDueTime = findViewById(R.id.textViewAlarmTaskDueTime);
//        textViewTaskDueTime.setText("Due Time: " + dueTimeFormatted);
//
//        // Görev bilgilerini alın
////        String taskTitle = getIntent().getStringExtra("taskTitle");
////        long taskDueTime = getIntent().getLongExtra("taskDueTime", 0);
//
//        task = new Task(taskTitle, taskDueTime, false);
//
//        // Görev bilgilerini ekranda gösterin
////        TextView textViewTaskTitle = findViewById(R.id.textViewAlarmTaskTitle);
//        textViewTaskTitle.setText("Task: " + task.title);
//
//        // Alarm sesini çal
//        mediaPlayer = MediaPlayer.create(this, android.provider.Settings.System.DEFAULT_ALARM_ALERT_URI);
//        mediaPlayer.setLooping(true);
//        mediaPlayer.start();
//
//        // Cihazı titreştir
//        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//        long[] pattern = {0, 1000, 1000}; // Titreşim deseni: Bekle, titreştir, bekle, titreştir...
//        vibrator.vibrate(pattern, 0); // 0 sonsuz döngü anlamına gelir
//
//        // Alarmı kapatmak için butona tıklama olayı
//        findViewById(R.id.buttonStopAlarm).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                stopAlarm();
//                finish();
//            }
//        });
//    }
//
//    private void stopAlarm() {
//        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//            mediaPlayer.stop();
//            mediaPlayer.release();
//        }
//
//        if (vibrator != null) {
//            vibrator.cancel();
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        stopAlarm();
//    }
//}

package com.example.taskapp3;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AlarmActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ensure the activity shows over the lock screen
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
        );

        setContentView(R.layout.activity_alarm);

        // Retrieve task information
        String taskTitle = getIntent().getStringExtra("taskTitle");
        long taskDueTime = getIntent().getLongExtra("taskDueTime", -1);

        // Display task information
        TextView textViewTaskTitle = findViewById(R.id.textViewAlarmTaskTitle);
        textViewTaskTitle.setText("Task: " + taskTitle);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String dueTimeFormatted = sdf.format(new Date(taskDueTime));

        TextView textViewTaskDueTime = findViewById(R.id.textViewAlarmTaskDueTime);
        textViewTaskDueTime.setText("Due Time: " + dueTimeFormatted);

        // Play alarm sound
        mediaPlayer = MediaPlayer.create(this, android.provider.Settings.System.DEFAULT_ALARM_ALERT_URI);
        if (mediaPlayer != null) {
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }

        // Vibrate the device
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (vibrator != null) {
            long[] pattern = {0, 1000, 1000}; // Wait, Vibrate, Wait, Vibrate...
            vibrator.vibrate(pattern, 0); // Repeat indefinitely
        }

        // Set up the stop alarm button
        findViewById(R.id.buttonStopAlarm).setOnClickListener(v -> {
            stopAlarm();
            finish();
        });
    }

    private void stopAlarm() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } finally {
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }

        if (vibrator != null) {
            vibrator.cancel();
            vibrator = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAlarm();
    }
}
