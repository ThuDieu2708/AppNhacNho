package com.example.ungdungnhacnho.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ungdungnhacnho.DataBase.DataBase;
import com.example.ungdungnhacnho.Helper.MyHelper;
import com.example.ungdungnhacnho.Helper.MyManager;
import com.example.ungdungnhacnho.Model.ModelNhacNho;
import com.example.ungdungnhacnho.R;

import java.util.Calendar;

public class UpdateActivity extends AppCompatActivity {

    private EditText editTitle, editContent;
    private Button btnUpdate;
    DataBase db;


    private String id;
    private String title;
    private String content;
    private String date;
    private String time;
    private String status;
    private String date_future;
    private String time_future;
    private MyManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        db = new DataBase(UpdateActivity.this);
        editTitle = (EditText) findViewById(R.id.ediTitle);
        editContent = (EditText) findViewById(R.id.editContent);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        manager = MyManager.getInstance();


        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        date = getIntent().getStringExtra("date");
        time = getIntent().getStringExtra("time");
        status = getIntent().getStringExtra("status");
        date_future = getIntent().getStringExtra("date_future");
        time_future = getIntent().getStringExtra("time_future");

        editTitle.setText(title);
        editContent.setText(content);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!MyHelper.MyCheck.isEmpty(editTitle.getText().toString()) && !MyHelper.MyCheck.isEmpty(editContent.getText().toString())){

                    title = editTitle.getText().toString();
                    content = editContent.getText().toString();
                    date = MyHelper.MyDate.getDate();
                    time = MyHelper.MyDate.getTime();

                    ModelNhacNho model = new ModelNhacNho(id , title , content , date , time , status , date_future , time_future);

                    boolean resValue = db.upDateItem(model);

                    if(!resValue)
                        Toast.makeText(getApplicationContext(), "Data Not Update", Toast.LENGTH_SHORT).show();
                    else {
                        System.out.println(model.toString());
                        intentView();
                        Toast.makeText(getApplicationContext(), "Data Update Successfully", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(UpdateActivity.this, "Both Fields Required", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void intentView(){
        Intent i1 = new Intent(UpdateActivity.this, MainActivity.class);
        i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i1);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nhacnho, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.huyNN:
                huyNN();
                break;
            case R.id.nhacnho:
                nhacnho();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void nhacnho(){
        if(!MyHelper.MyCheck.isEmpty(editTitle.getText().toString()) && !MyHelper.MyCheck.isEmpty(editContent.getText().toString())) {
            Intent intent = new Intent(UpdateActivity.this, NhacNhoActivity.class);

            intent.putExtra("_action_", "update");
            intent.putExtra("id", id);
            intent.putExtra("title", editTitle.getText().toString());
            intent.putExtra("content", editContent.getText().toString());
            intent.putExtra("date", date);
            intent.putExtra("time", time);
            intent.putExtra("status", status);
            intent.putExtra("date_future", date_future);
            intent.putExtra("time_future", time_future);

            startActivity(intent);
        }else{
            Toast.makeText(UpdateActivity.this, "Both Fields Required", Toast.LENGTH_SHORT).show();
        }
    }
    public void huyNN(){
        if(status.equals("OFF")){
            Toast.makeText(UpdateActivity.this, "Chưa Cài Nhắc Nhở", Toast.LENGTH_SHORT).show();
        }else {
            status = "OFF";
            date_future = "empty";
            time_future = "empty";
            manager.cancel(Integer.parseInt(id));
            manager.cancel(Integer.parseInt(id) + 1);
            Toast.makeText(UpdateActivity.this, "ĐÃ HỦY NHẮC NHỞ", Toast.LENGTH_LONG).show();
        }
    }
}
