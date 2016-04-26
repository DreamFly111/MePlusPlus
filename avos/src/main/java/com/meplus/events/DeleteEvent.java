package com.meplus.events;

/**
 * Created by dandanba on 3/11/16.
 * 删除
 */
public class DeleteEvent<T> extends BaseEvent {
    public DeleteEvent() {
        super();
    }

    public DeleteEvent(T data) {
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
