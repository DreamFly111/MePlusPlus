package com.meplus.fancy.events;

/**
 * Created by dandanba on 3/3/16.
 */
public class ScannerEvent {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ScannerEvent(String content) {
        this.content = content;
    }

}
