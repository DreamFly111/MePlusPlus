package com.meplus.speech.event;


import com.meplus.events.BaseEvent;

/**
 * Created by dandanba on 3/3/16.
 */
public class SpeechEvent extends BaseEvent {
    public SpeechEvent(Speech speech) {
        this.speech = speech;
    }

    public Speech getSpeech() {
        return speech;
    }

    public void setSpeech(Speech speech) {
        this.speech = speech;
    }

    private Speech speech;


}
