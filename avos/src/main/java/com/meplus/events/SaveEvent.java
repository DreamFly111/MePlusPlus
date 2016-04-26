package com.meplus.events;

/**
 * Created by dandanba on 3/11/16.
 * 增加和修改
 */
public class SaveEvent<T> extends BaseEvent {
    public SaveEvent() {
        super();
    }

    public SaveEvent(T data) {
        this();
        setData(data);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    private T data;
}
