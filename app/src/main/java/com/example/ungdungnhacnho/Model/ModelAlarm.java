package com.example.ungdungnhacnho.Model;

public class ModelAlarm {


    private long secondFuture;

    private long secondBeforeFuture;

    public ModelAlarm(long secondFuture, long secondBeforeFuture) {
        this.secondFuture = secondFuture;
        this.secondBeforeFuture = secondBeforeFuture;
    }


    public long getSecondFuture() {
        return secondFuture;
    }

    public void setSecondFuture(long secondFuture) {
        this.secondFuture = secondFuture;
    }

    public long getSecondBeforeFuture() {
        return secondBeforeFuture;
    }

    public void setSecondBeforeFuture(long secondBeforeFuture) {
        this.secondBeforeFuture = secondBeforeFuture;
    }


}
