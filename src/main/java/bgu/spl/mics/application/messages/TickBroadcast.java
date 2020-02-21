package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

import java.sql.Time;

/**
 * Briascast letting all subscribers know what the time is
 */
public class TickBroadcast implements Broadcast {
    int TimeTick;

    /**
     * constructor
     * @param TimeTick
     */
    public TickBroadcast(int TimeTick){
        this.TimeTick=TimeTick;
    }

    /**
     *
     * @return the time tick
     */
    public int getTimeTick(){
        return TimeTick;
    }
}
