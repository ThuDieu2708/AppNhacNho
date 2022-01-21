package com.example.ungdungnhacnho.Helper;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.ungdungnhacnho.DataBase.DataBase;
import com.example.ungdungnhacnho.Model.ModelNhacNho;
import com.example.ungdungnhacnho.R;
import com.example.ungdungnhacnho.View.MainActivity;
import com.example.ungdungnhacnho.View.UpdateActivity;

public class MyBoardCast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent intentIn = new Intent(context , UpdateActivity.class);

        int id = intent.getIntExtra("id" , 0);
        String hind = intent.getStringExtra("hind");
        String time = intent.getStringExtra("time");
        String title = intent.getStringExtra("title");
        Bundle bundle = intent.getBundleExtra("bundle");
        ModelNhacNho model = (ModelNhacNho)bundle.getSerializable("model");

        intentIn.putExtra("id", model.getId());
        intentIn.putExtra("title", model.getTilte());
        intentIn.putExtra("content", model.getContent());
        intentIn.putExtra("date", model.getDate());
        intentIn.putExtra("time", model.getTime());
        intentIn.putExtra("status",model.getStatus());
        intentIn.putExtra("date_future", model.getDate_future());
        intentIn.putExtra("time_future", model.getTim_future());

        intentIn.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, id , intentIn , PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap image = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher_background);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context , "notify-alram")
                        .setContentTitle(hind) // title notify
                        .setSmallIcon(R.drawable.bell)
                        .setContentText(title) // content text
                        .setLargeIcon(image) // image right
                        .setAutoCancel(true) // remove notify when click notìy
                        .setContentIntent(pendingIntent) // intent view
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(id, builder.build());

        if(model.getHind().equals(MyHelper.Message_Notify)) {
            model.setStatus("OFF");
            model.setDate_future("empty");
            model.setTim_future("empty");
            DataBase db = new DataBase(context);
            boolean succes = db.upDateItem(model);
            if (!succes)
                Log.d("Update", "Update Model At BoardCard not Working!");
            else
                MainActivity.table.load();
        }
    }
}
