package com.example.ungdungnhacnho.Helper;

import android.util.Pair;

import com.example.ungdungnhacnho.Model.ModelAlarm;
import com.example.ungdungnhacnho.Model.ModelNhacNho;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public  class MyHelper {

    public static final int TIME_DELAY = -2;
    public  static  final String Message_Notify = "Thông Báo Hiện Tại";
    public  static  final String Message_Notify_Before = "Nhắc Nhở Trước";

    public static class MyDate{

        public static String getDate(){
            return  new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        }

        public static String getTime(){
            return  new SimpleDateFormat("HH:mm").format(new Date());
        }


        public static ModelAlarm getSencondsFrom(ModelNhacNho model , int delay){

            String date = model.getDate(); // DD/MM/YYYY
            String time = model.getTime(); // housr:minute

            String date_future = model.getDate_future(); // DD/MM/YYYY
            String time_future = model.getTim_future(); // housr:minute

            Calendar calendarCurent = setCalendar(getListDateTime(date , time) , 0);
            Calendar calendarFuture = setCalendar(getListDateTime(date_future , time_future) , 0);
            Calendar calendarBeforeFuture = setCalendar(getListDateTime(date_future , time_future) , delay);

            long secondsFuture = -1L;
            long secondsBeforeFuture = -1L;

            if(calendarCurent.getTimeInMillis() < calendarFuture.getTimeInMillis())
                    secondsFuture = calendarFuture.getTimeInMillis();

            if(calendarCurent.getTimeInMillis() < calendarBeforeFuture.getTimeInMillis())
                secondsBeforeFuture = calendarBeforeFuture.getTimeInMillis();

            return  new ModelAlarm(secondsFuture , secondsBeforeFuture);
        }

        public static Calendar getCalCurent(){
            Calendar calendar = Calendar.getInstance();
            TimeZone zone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
            calendar.setTimeZone(zone);
            return calendar;
        }

        public static Calendar setCalendar(ArrayList<String> arr , int delay){
            Calendar calendar = Calendar.getInstance();
            TimeZone zone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
            calendar.setTimeZone(zone);

            calendar.set(Integer.parseInt(arr.get(2)) ,
                    Integer.parseInt(arr.get(1)) - 1,
                    Integer.parseInt(arr.get(0)) ,
                    Integer.parseInt(arr.get(3)) ,
                    Integer.parseInt(arr.get(4)) ,
                    0);

            calendar.add(Calendar.MINUTE , delay);

            return calendar;
        }

        private static String[] convertDate(String dateTime , String regex){
            return dateTime.split(regex);
        }

        public static ArrayList<String> getListDateTime(String date , String time){
            String[] arrDate = convertDate(date , "/");
            String day = arrDate[0];
            String month = arrDate[1];
            String year = arrDate[2];

            String[] arrTime = convertDate(time , ":");
            String hours = arrTime[0];
            String minute = arrTime[1];

            ArrayList<String> arr = new ArrayList<>();
            arr.add(day);
            arr.add(month);
            arr.add(year);
            arr.add(hours);
            arr.add(minute);



            return arr;
        }
    }


    public static class MyCheck{

        public enum STATUS{
            SUCCESS,
            EMPTY
        }

        public static boolean isEmpty(String other){
            return other.trim().equals("");
        }

    }

}
