package com.example.ungdungnhacnho.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.ungdungnhacnho.Model.ModelNhacNho;

public class DataBase extends SQLiteOpenHelper {

    Context context;

    // Tạo CSDL
    private static final String DATABASE_NAME = "SoTayDH";
    private static final int DATABASE_VERSION = 3;

    // Tạo bảng
    private static final String TABLE_NAME = "NoteBook";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_SWICH = "status";
    private static final String KEY_DATE_FURURE = "date_furure";
    private static final String KEY_TIME_FURURE  = "time_future";

    public DataBase(@Nullable Context context) {
        super(context, DATABASE_NAME, null , DATABASE_VERSION);
        this.context = context;
    }

    private String QUERY_ADDTABLECOL1 = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + KEY_DATE_FURURE + " TEXT;";
    private String QUERY_ADDTABLECOL2 = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + KEY_TIME_FURURE + " TEXT;";

    // onCreate : viết những câu lệnh tạo bảng, được gọi khi database đã được tạo.
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
                + KEY_CONTENT + " TEXT," + KEY_DATE + " TEXT," + KEY_TIME + " TEXT," + KEY_SWICH + " TEXT," + KEY_DATE_FURURE + " TEXT," + KEY_TIME_FURURE + " TEXT" + ")";
        sqLiteDatabase.execSQL(query);

    }


    // onUpgrade : được gọi khi database được nâng cấp, ví dụ như chỉnh sửa cấu trúc các bảng, thêm những thay đổi cho database,..
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        if(i < 4){
            sqLiteDatabase.execSQL(QUERY_ADDTABLECOL1);
            sqLiteDatabase.execSQL(QUERY_ADDTABLECOL2);
        }
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
//        onCreate(sqLiteDatabase);
    }
    public boolean addNote(ModelNhacNho nhacNho){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_ID , nhacNho.getId());
        cv.put(KEY_TITLE, nhacNho.getTilte());
        cv.put(KEY_CONTENT, nhacNho.getContent());
        cv.put(KEY_DATE, nhacNho.getDate());
        cv.put(KEY_TIME, nhacNho.getTime());
        cv.put(KEY_SWICH, nhacNho.getStatus());
        cv.put(KEY_DATE_FURURE, nhacNho.getDate_future());
        cv.put(KEY_TIME_FURURE, nhacNho.getTim_future());
        return db.insert(TABLE_NAME, null, cv) != -1;

    }

//    public void addNote(String title, String content, String date, String time){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//        cv.put(KEY_TITLE, title);
//        cv.put(KEY_CONTENT, content);
//        cv.put(KEY_DATE, date);
//        cv.put(KEY_TIME, time);
////        String sw = !nhacNho.getStatus() ? "OFF" : "ON";
////        cv.put(KEY_SWICH, sw);
//        long resValue = db.insert(TABLE_NAME, null, cv);
//        if(resValue == -1){
//            Toast.makeText(context, "Data Not Added", Toast.LENGTH_SHORT).show();
//        }
//        else {
//            Toast.makeText(context, "Data Added Successfully", Toast.LENGTH_SHORT).show();
//        }
//    }

    public Cursor AllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        System.out.println("có vô đây");
        // tạo 1 con trỏ cursor ban đầu bằng null
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null); //Chạy SQL được cung cấp và trả về Con trỏ trên tập kết quả.
        }
        System.out.println("có vô đây không??");
        return cursor;
    }

    // update
    public boolean upDateItem(ModelNhacNho nhacNho){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, nhacNho.getTilte());
        values.put(KEY_CONTENT, nhacNho.getContent());
        values.put(KEY_DATE, nhacNho.getDate());
        values.put(KEY_TIME, nhacNho.getTime());
        values.put(KEY_SWICH, nhacNho.getStatus());
        values.put(KEY_DATE_FURURE, nhacNho.getDate_future());
        values.put(KEY_TIME_FURURE, nhacNho.getTim_future());
        return db.update(TABLE_NAME, values, "id=?", new String[]{nhacNho.getId()}) != -1;
    }

    // xóa từng item bằng cách vuốt sang phải
    public void deleteItem(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        long res = db.delete(TABLE_NAME, "id=?", new String[]{id});
        if(res == -1) Toast.makeText(context, "Item not Delete", Toast.LENGTH_SHORT).show();
        else Toast.makeText(context, "Delete item Successful", Toast.LENGTH_SHORT).show();
    }

    // Xóa all các item
    public void deleAllItem(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME;
        db.execSQL(query);
    }

    public int getMaxID()
    {
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT MAX("+KEY_ID+") FROM "+TABLE_NAME, null);
        int maxid = (cursor.moveToFirst() ? cursor.getInt(0) : 0);
        if(!cursor.moveToFirst())
            return 0;
        return maxid + 2;
    }


}
