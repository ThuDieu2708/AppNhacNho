package com.example.ungdungnhacnho.Model;

import java.io.Serializable;

public class ModelNhacNho implements Serializable {


    private String id, date, time, content, tilte , date_future , tim_future;

    private  String hind;

    private String status;

    public ModelNhacNho(){

    }

    public ModelNhacNho(String id, String tilte, String content, String date, String time , String status) {
        this.id = id;
        this.tilte = tilte;
        this.content = content;
        this.date = date;
        this.time = time;
        this.status = status;
        this.date_future = "empty";
        this.tim_future = "empty";
    }


    public ModelNhacNho(String id, String tilte, String content, String date, String time , String status, String date_future , String tim_future) {
        this.id = id;
        this.tilte = tilte;
        this.content = content;
        this.date = date;
        this.time = time;
        this.status = status;
        this.date_future = date_future;
        this.tim_future = tim_future;
    }

    @Override
    public String toString() {
        return "ModelNhacNho{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", content='" + content + '\'' +
                ", tilte='" + tilte + '\'' +
                ", date_future='" + date_future + '\'' +
                ", tim_future='" + tim_future + '\'' +
                ", status=" + status +
                '}';
    }

    public String getHind() {
        return hind;
    }

    public void setHind(String hind) {
        this.hind = hind;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getTilte() {
        return tilte;
    }

    public void setTilte(String tilte) {
        this.tilte = tilte;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate_future() {
        return date_future;
    }

    public void setDate_future(String date_future) {
        this.date_future = date_future;
    }

    public String getTim_future() {
        return tim_future;
    }

    public void setTim_future(String tim_future) {
        this.tim_future = tim_future;
    }


    public void assgin(ModelNhacNho other){
        this.id = other.id;
        this.tilte = other.tilte;
        this.content = other.content;
        this.date = other.date;
        this.time = other.time;
        this.status = other.status;
        this.date_future = other.date_future;
        this.tim_future = other.tim_future;
    }
}
