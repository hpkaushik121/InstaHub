package com.example.anitesh.photocon;

/**
 * Created by sourabh on 8/20/2017.
 */

public  class chatmodal {
    public String msg;
    public boolean Send;

    public chatmodal(String msg, boolean send) {
        this.msg = msg;
        Send = send;
    }

    public chatmodal() {
    }

    public String Msg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean Send1() {
        return Send;
    }

    public void Send(boolean send) {
        Send = send;
    }
}