package com.meplus.punub;


import com.meplus.events.BaseEvent;

/**
 * Created by dandanba on 3/3/16.
 */
public class StateEvent extends BaseEvent {
    private Command mCommand;

    public StateEvent() {
        super();
    }

    public Command getCommand() {
        return mCommand;
    }

    public void setCommand(Command command) {
        mCommand = command;
    }
}
