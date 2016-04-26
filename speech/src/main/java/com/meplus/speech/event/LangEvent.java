package com.meplus.speech.event;


import com.meplus.events.BaseEvent;

/**
 * Created by dandanba on 3/3/16.
 */
public class LangEvent extends BaseEvent {
    // lang
    public final static String ZH_LANG = "zh_cn";// "en_us"
    public final static String EN_LANG = "en_us";// "en_us"

    public LangEvent(String lan) {
        this.lan = lan;
    }

    private String lan;


    public String getLan() {
        return lan;
    }

    public void setLan(String lan) {
        this.lan = lan;
    }
}
