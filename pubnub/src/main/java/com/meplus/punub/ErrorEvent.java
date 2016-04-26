package com.meplus.punub;


import com.meplus.events.BaseEvent;

/**
 * Created by dandanba on 3/3/16.
 */
public class ErrorEvent extends BaseEvent {
    private Command mCommand;
    private String errorString;

    public ErrorEvent() {
        super();
    }

    public Command getCommand() {
        return mCommand;
    }

    public void setCommand(Command command) {
        mCommand = command;
    }

    public String getErrorString() {
        return errorString;
    }

    public void setErrorString(String errorString) {
        this.errorString = errorString;
    }
}
