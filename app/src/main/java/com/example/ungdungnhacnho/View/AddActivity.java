package com.example.ungdungnhacnho.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ungdungnhacnho.DataBase.DataBase;
import com.example.ungdungnhacnho.Helper.MyHelper;
import com.example.ungdungnhacnho.Model.ModelNhacNho;
import com.example.ungdungnhacnho.R;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {
    private EditText editTitle, editContent;
    private Button btnSave;
    private int hour = -1;
    private int minute = -1;
    private DataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        db = new DataBase(AddActivity.this);
        editTitle = (EditText) findViewById(R.id.ediTitle);
        editContent = (EditText) findViewById(R.id.editContent);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!TextUtils.isEmpty(editTitle.getText().toString()) && !TextUtils.isEmpty(editContent.getText().toString())){

                    String id = db.getMaxID() + "";
                    String title = editTitle.getText().toString();
                    String content = editContent.getText().toString();
                    String date = MyHelper.MyDate.getDate();
                    String time = MyHelper.MyDate.getTime();
                    String status = "OFF";

                    ModelNhacNho model = new ModelNhacNho(id , title , content , date , time , status );

                    boolean resValue = db.addNote(model);

                    if(!resValue)
                        Toast.makeText(getApplicationContext(), "Data Not Added", Toast.LENGTH_SHORT).show();
                    else {
                        System.out.println(model.toString());
                        intentView();

                        Toast.makeText(getApplicationContext(), "Data Added Successfully", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(AddActivity.this, "Both Fields Required", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void intentView(){
        Intent i1 = new Intent(AddActivity.this, MainActivity.class);
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
            case R.id.nhacnho:
                nhacNho();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void nhacNho() {
        Intent intent = new Intent(AddActivity.this, NhacNhoActivity.class);

        MyHelper.MyCheck.STATUS status = pass();

        if(status.equals(MyHelper.MyCheck.STATUS.SUCCESS)) {

            String id = db.getMaxID() + "";
            String title = editTitle.getText().toString();
            String content = editContent.getText().toString();
            String date = MyHelper.MyDate.getDate();
            String time = MyHelper.MyDate.getTime();

            intent.putExtra("_action_" , "add");
            intent.putExtra("id" , id);
            intent.putExtra("title" , title);
            intent.putExtra("content" , content);
            intent.putExtra("date" , date);
            intent.putExtra("time" , time);

            startActivity(intent);

        }else {
            if(status.equals(MyHelper.MyCheck.STATUS.EMPTY))
                Toast.makeText(AddActivity.this, "Title Or Content Is Empty!", Toast.LENGTH_SHORT).show();
            // code error here
        }

    }


    private MyHelper.MyCheck.STATUS pass(){
        if(isEmptyTilteAndContent())
            return MyHelper.MyCheck.STATUS.EMPTY;
        return MyHelper.MyCheck.STATUS.SUCCESS;
    }

    private boolean isEmptyTilteAndContent(){
        return MyHelper.MyCheck.isEmpty(editTitle.getText().toString()) || MyHelper.MyCheck.isEmpty(editContent.getText().toString());
    }

}