package com.meplus.events;

/**
 * Created by dandanba on 3/11/16.
 * 错误
 */
public class ErrorEvent extends BaseEvent {
    public ErrorEvent() {
        super();
    }

    public ErrorEvent(Throwable throwable) {
        this();
        setThrowable(throwable);
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    private Throwable throwable;
}
