package com.example.ungdungnhacnho.View;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ungdungnhacnho.DataBase.DataBase;
import com.example.ungdungnhacnho.Helper.MyHelper;
import com.example.ungdungnhacnho.Helper.MyManager;
import com.example.ungdungnhacnho.Model.ModelAlarm;
import com.example.ungdungnhacnho.Model.ModelNhacNho;
import com.example.ungdungnhacnho.R;

import java.util.Calendar;

public class NhacNhoActivity extends AppCompatActivity {
    DatePickerDialog datePickerDialog;
    Calendar calendar;
    int year, month, dayOfMonth;
    private EditText editTime, editDate;
    private Button btnNhacNho, btnDate, btnTime;
    private int hour = -1;
    private int minute = -1;


    // intent
    private String id;
    private String title;
    private String content;
    private String date;
    private String time;
    private String status;
    private String date_future;
    private String time_future;
    private String _action_;
    private DataBase db;

    private MyManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhac_nho);

        manager = MyManager.getInstance();

        System.out.println("hashcode = " + manager.hashCode());
        db = new DataBase(NhacNhoActivity.this);
        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        date = getIntent().getStringExtra("date");
        time = getIntent().getStringExtra("time");
        status = getIntent().getStringExtra("status");
        date_future = getIntent().getStringExtra("date_future");
        time_future = getIntent().getStringExtra("time_future");
        _action_ = getIntent().getStringExtra("_action_");

        editTime = (EditText) findViewById(R.id.editTime);
        editDate = (EditText) findViewById(R.id.edit_Date);

        if(status != null && status.equals("ON")){
            editTime.setText(time_future);
            editDate.setText(date_future);
        }else{
            // nếu status not On -> có nghĩa là trống
            // nên để date , time hiện tại
            editDate.setText(MyHelper.MyDate.getDate());
            editTime.setText(MyHelper.MyDate.getTime());
        }

        btnTime = (Button) findViewById(R.id.btnTime);
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTime();
            }
        });
        btnNhacNho = (Button) findViewById(R.id.btnNhacNho);
        btnNhacNho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // save db
                String date_future = editDate.getText().toString();
                String time_future = editTime.getText().toString();

                if(!MyHelper.MyCheck.isEmpty(date_future) && !MyHelper.MyCheck.isEmpty(time_future)){

                    date = MyHelper.MyDate.getDate();
                    time = MyHelper.MyDate.getTime();

                    Calendar calendarCurent = MyHelper.MyDate.setCalendar(MyHelper.MyDate.getListDateTime(date , time) , 0);
                    Calendar calendarFuture = MyHelper.MyDate.setCalendar(MyHelper.MyDate.getListDateTime(date_future , time_future) , 0);

                    if(calendarFuture.getTimeInMillis() <= calendarCurent.getTimeInMillis()){
                        Toast.makeText(getApplicationContext(), "Ngày Và Giờ Tương Lai Phải Lớn Hơn hiện tại " , Toast.LENGTH_LONG).show();
                        return;
                    }


                    ModelNhacNho model = new ModelNhacNho(id , title , content , date , time , "ON" , date_future , time_future);
                    boolean resValue = false;

                    if(_action_.equals("add")) {
                        resValue= db.addNote(model);
                    }else if(_action_.equals("update")){
                        resValue= db.upDateItem(model);
                    }

                    if(!resValue)
                        Toast.makeText(getApplicationContext(), "Data Not " + _action_, Toast.LENGTH_SHORT).show();
                    else {
                        System.out.println(model.toString());
                        // cai dat time
                        if(_action_.equals("update")){
                            manager.cancel(Integer.parseInt(model.getId()));
                            manager.cancel(Integer.parseInt(model.getId()) + 1);
                        }


                        ModelAlarm alarm = MyHelper.MyDate.getSencondsFrom(model ,  MyHelper.TIME_DELAY);

                        if(alarm.getSecondFuture() != -1) {
                            model.setHind(MyHelper.Message_Notify);
                            manager.set(Integer.parseInt(model.getId()), alarm.getSecondFuture(), model);
                        }
                        if(alarm.getSecondBeforeFuture() != -1){
                            model.setHind(MyHelper.Message_Notify_Before);
                            manager.set(Integer.parseInt(model.getId()) + 1 , alarm.getSecondBeforeFuture() , model);
                        }

                        // intent
                        intentView();
                        Toast.makeText(getApplicationContext(), "Data "+_action_+" Successfully", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(), "Date or Time is Empty!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnDate = (Button) findViewById(R.id.btn_Date);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(NhacNhoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        editDate.setText(day + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });
    }


    private void intentView(){
        Intent i1 = new Intent(NhacNhoActivity.this, MainActivity.class);
        i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i1);
        finish();
    }

    public void showTime(){
        if(hour == -1){
            Calendar calendar = Calendar.getInstance();
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
        }

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int gio, int phut) {
                editTime.setText(gio + ":" + phut);
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }
}