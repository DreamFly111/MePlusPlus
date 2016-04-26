package com.meplus.punub;

/**
 * Created by GleasonK on 6/25/15.
 */
public class Command {

    public final static String ACTION_CALL = "call";

    public final static String ACTION_UP = "up";
    public final static String ACTION_DOWN = "down";
    public final static String ACTION_LEFT = "left";
    public final static String ACTION_RIGHT = "right";
    public final static String ACTION_STOP = "stop";

    public final static String ACTION_HOME = "home";

    public String sender;
    public String message;
    public long timeStamp;

    public Command() {// 必须要有无参数构造函数
    }

    public Command(String sender, String message) {
        this.sender = sender;
        this.message = message;
        this.timeStamp = System.currentTimeMillis();
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
