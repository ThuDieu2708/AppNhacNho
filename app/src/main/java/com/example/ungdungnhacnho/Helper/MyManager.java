package com.example.ungdungnhacnho.Helper;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ungdungnhacnho.Model.ModelNhacNho;

import java.io.Serializable;

public class MyManager {

    private AppCompatActivity app;
    private AlarmManager alarmManager;
    private static MyManager MY_MANAGER = null;

    public MyManager(AppCompatActivity app){
        this.app = app;
        if(MY_MANAGER == null)
            MY_MANAGER = this;
    }

    public static MyManager getInstance(){
        return MY_MANAGER;
    }

    public  void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Tên Đăng Kí";
            String description = "Nội Dung của Notify";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notify-alram", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = app.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void set(int idNotify , long secondsDelay , ModelNhacNho model){
        alarmManager = (AlarmManager) app.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(app , MyBoardCast.class);
        Bundle b = new Bundle();
        b.putSerializable("model" , model);
        intent.putExtra("bundle" , b);
        intent.putExtra("id" , idNotify);
        intent.putExtra("hind" , model.getHind());
        intent.putExtra("time" , model.getTime());
        intent.putExtra("title" , model.getTilte());
        PendingIntent pendingIntent  = PendingIntent.getBroadcast(app , idNotify, intent , PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP , secondsDelay, pendingIntent);
    }
    public void cancel(int idNotify){
        Intent intent = new Intent( app , MyBoardCast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(app, idNotify , intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (alarmManager == null){
            alarmManager = (AlarmManager) app.getSystemService(Context.ALARM_SERVICE);
        }
        alarmManager.cancel(pendingIntent) ;
        Toast.makeText(app,"Alarm Cancelled", Toast.LENGTH_SHORT).show();
    }
}
