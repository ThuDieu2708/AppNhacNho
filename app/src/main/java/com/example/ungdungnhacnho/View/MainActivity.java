package com.example.ungdungnhacnho.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ungdungnhacnho.DataBase.Adapter;
import com.example.ungdungnhacnho.DataBase.DataBase;
import com.example.ungdungnhacnho.Helper.MyHelper;
import com.example.ungdungnhacnho.Helper.MyManager;
import com.example.ungdungnhacnho.Model.ModelAlarm;
import com.example.ungdungnhacnho.Model.ModelNhacNho;
import com.example.ungdungnhacnho.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CoordinatorLayout coordinatorLayout;
    FloatingActionButton actionButton;
    List<ModelNhacNho> noteList;
    DataBase DB;
    Adapter adapter;

    private static MyManager manager;

    private static boolean NEW_CREATE = true;
    
    public static MainActivity table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.list_item);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.layoutMain);
        actionButton = (FloatingActionButton) findViewById(R.id.add);

        table = this;

        if(manager == null)
            manager = new MyManager(this);

        manager.createNotificationChannel();
        System.out.println("hashcode Main = " + manager.hashCode());

        // khi nhấn vô 1 floatingActionButton nào sẽ chuyển tới trang để viết nội dung
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        DB = new DataBase(this);


       // load();
    }

    public void load(){
        noteList = new ArrayList<>();
        findAllFromDB();
        System.out.println("Alarm Set " + (NEW_CREATE ? "ON" : "OFF"));
       // setUpAlram();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter( this, MainActivity.this, noteList);
        recyclerView.setAdapter(adapter);

        // VUỐT TỪ TRÁI SANG PHẢI ĐỂ XÓA
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        load();
    }

    private void findAllFromDB() {
        Cursor cursor = DB.AllData();
        if(cursor.getCount() == 0){
            Toast.makeText(this, "No Data to show", Toast.LENGTH_SHORT).show();
        }
        else {
            while (cursor.moveToNext()){
                noteList.add(
                        new ModelNhacNho(cursor.getString(0),
                                cursor.getString(1),
                                cursor.getString(2),
                                cursor.getString(3),
                                cursor.getString(4) ,
                                cursor.getString(5),
                                cursor.getString(6),
                                cursor.getString(7)));
                int t = noteList.size();
                String date = MyHelper.MyDate.getDate();
                String time = MyHelper.MyDate.getTime();
                String curentday = noteList.get(t - 1).getDate();
                String curenttime = noteList.get(t - 1).getTime();
                if(noteList.get(t - 1).getStatus().equals("ON")){
                    ModelNhacNho note = new ModelNhacNho();
                    note.assgin(noteList.get(t - 1));
                    note.setDate(date);
                    note.setTime(time);
                    ModelAlarm alarm = MyHelper.MyDate.getSencondsFrom(note ,  MyHelper.TIME_DELAY);

                    // A-  <  A < B
                    if(alarm.getSecondFuture() == -1){
                        note.setStatus("OFF");
                        note.setDate_future("empty");
                        note.setTim_future("empty");
                        boolean success = DB.upDateItem(note);
                        if(!success)
                            System.out.println("in Main update not working!");
                        else {
                            noteList.get(t - 1).assgin(note);
                            noteList.get(t - 1).setDate(curentday);
                            noteList.get(t - 1).setTime(curenttime);
                        }
                    }

                    alarmModel(noteList.get(t - 1));

                }
            }
            System.out.println(cursor.getCount());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.searchNote);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search Here...");
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if(item.getItemId() == R.id.deleteNote){
//            deleteAll();
//        }
        switch (item.getItemId()) {
            case R.id.deleteNote:
                deleteAll();
                break;
            case R.id.backgroudNote:
                changBackground();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void changBackground(){
        Random ran = new Random();
        int[] color = {Color.parseColor("#87CEEB"), Color.parseColor("#FFB6C1"), Color.parseColor("#6A5ACD")};
        coordinatorLayout.setBackgroundColor(color[ran.nextInt(3)]);
    }
    public void deleteAll(){
        if(noteList.size() > 0) {
            for(ModelNhacNho e : noteList){
                manager.cancel(Integer.parseInt(e.getId()));
                manager.cancel(Integer.parseInt(e.getId()) + 1);
            }
            DataBase db = new DataBase(MainActivity.this);
            db.deleAllItem();
            recreate();
        }
    }

    private void alarmModel(ModelNhacNho model){
        if(model.getStatus().equals("ON")){
            ModelNhacNho nNote = new ModelNhacNho();
            nNote.assgin(model);
            nNote.setDate(MyHelper.MyDate.getDate());
            nNote.setTime(MyHelper.MyDate.getTime());
            ModelAlarm alarm = MyHelper.MyDate.getSencondsFrom(nNote ,  MyHelper.TIME_DELAY);
            Calendar cal = MyHelper.MyDate.getCalCurent();
            if(cal.getTimeInMillis() <= alarm.getSecondFuture() &&  alarm.getSecondFuture() != -1) {
                model.setHind(MyHelper.Message_Notify);
                manager.set(Integer.parseInt(nNote.getId()), alarm.getSecondFuture(), model);
            }
            if(cal.getTimeInMillis() < alarm.getSecondBeforeFuture() && alarm.getSecondBeforeFuture() != -1){
                model.setHind(MyHelper.Message_Notify_Before);
                manager.set(Integer.parseInt(nNote.getId()) + 1 , alarm.getSecondBeforeFuture() , model);
            }
        }
    }



    private void setUpAlram(){
        for (ModelNhacNho model:
             noteList) {
           alarmModel(model);
        }
    }

    // vuốt để xóa
    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }


        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int index = viewHolder.getAdapterPosition();
            ModelNhacNho noteItem = adapter.getList().get(index);
            adapter.removeItem(viewHolder.getAdapterPosition());

            Snackbar snackbar = Snackbar.make(coordinatorLayout,"Đã Xóa", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapter.undoItem(noteItem, index);
                    recyclerView.scrollToPosition(index);
                    alarmModel(noteItem);
                }
            }).addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    super.onDismissed(transientBottomBar, event);
                    if(!(event == DISMISS_EVENT_ACTION)){
                        DataBase db = new DataBase(MainActivity.this);
                        db.deleteItem(noteItem.getId());
                        manager.cancel(Integer.parseInt(noteItem.getId()));
                        manager.cancel(Integer.parseInt(noteItem.getId()) + 1);
                    }
                }
            });
            snackbar.setActionTextColor(Color.rgb(50,205,50));
            snackbar.show();
        }
    };


}