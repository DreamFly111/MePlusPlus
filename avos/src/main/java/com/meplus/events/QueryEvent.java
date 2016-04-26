package com.meplus.events;

import java.util.List;

/**
 * Created by dandanba on 3/11/16.
 * 查询
 */
public class QueryEvent<T> extends BaseEvent {
    public QueryEvent() {
        super();
    }

    public QueryEvent(List<T> list) {
        this();
        setList(list);
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    private List<T> list;
}
