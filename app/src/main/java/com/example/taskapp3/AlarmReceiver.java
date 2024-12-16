package com.example.taskapp3;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.media.RingtoneManager;
//import android.net.Uri;
//import android.os.Vibrator;
//
//public class AlarmReceiver extends BroadcastReceiver {
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        // Play default alarm sound
//        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//        RingtoneManager.getRingtone(context, alarmUri).play();
//
//        // Vibrate the device
//        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
//        vibrator.vibrate(2000); // Vibrate for 2 seconds
//
//        // Show notification
//        String taskTitle = intent.getStringExtra("taskTitle");
//        NotificationHelper.showNotification(context, "Task Reminder", "You have a pending task: " + taskTitle);
//    }
//}


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
//
//public class AlarmReceiver extends BroadcastReceiver {
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        // Görev bilgilerini alın
//        String taskTitle = intent.getStringExtra("taskTitle");
//        long taskDueTime = intent.getLongExtra("taskDueTime", 0);
//
//        // AlarmActivity'yi başlatın
//        Intent alarmIntent = new Intent(context, AlarmActivity.class);
//        alarmIntent.putExtra("taskTitle", taskTitle);
//        alarmIntent.putExtra("taskDueTime", taskDueTime);
//        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(alarmIntent);
//    }
//}

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Görev bilgilerini alın
        String taskTitle = intent.getStringExtra("taskTitle");
        long taskDueTime = intent.getLongExtra("taskDueTime", 0);

        // AlarmActivity'yi başlatın
        Intent alarmIntent = new Intent(context, AlarmActivity.class);
        alarmIntent.putExtra("taskTitle", taskTitle);
        alarmIntent.putExtra("taskDueTime", taskDueTime);
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(alarmIntent);
    }
}