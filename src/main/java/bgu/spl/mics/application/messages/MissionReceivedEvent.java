package bgu.spl.mics.application.messages;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Report;
import java.util.List;

/**
 * An event that contains information that is needed to accomplish the mission.
 * Intelligence publisher sends it and M handles it
 */
public class MissionReceivedEvent implements Event<Integer> {
    private String missionName;
    private List<String> necessaryAgentsNumbers;
    private String necessaryGadget;
    private int issuedTime;
    private int expiredTime;
    private int duration;

    /**
     * Constructor
     * @param missionName
     * @param AgentsNumbers required for the mission
     * @param gadget required for the mission
     * @param expiredTime of the mission
     * @param duration of the mission
     * @param issuedTime of the mission
     */
    public MissionReceivedEvent(String missionName , List<String> AgentsNumbers, String gadget , int expiredTime , int duration , int issuedTime) {
        this.missionName=missionName;
        necessaryAgentsNumbers=AgentsNumbers;
        necessaryGadget=gadget;
        this.expiredTime = expiredTime;
        this.duration=duration;
        this.issuedTime=issuedTime;
    }

    /**
     *
     * @return mission name
     */
    public String getMissionName (){
        return missionName;
    }

    /**
     *
     * @return required agents for the mission
     */
    public List<String> getNecessaryAgentsNumbers(){
        return necessaryAgentsNumbers;
    }

    /**
     *
     * @return required gadget for the mission
     */
    public String getNecessaryGadget(){
        return necessaryGadget;
    }

    /**
     *
     * @return the expiration time of the mission
     */
    public int getExpiredTime (){
        return expiredTime;
    }

    /**
     *
     * @return the duration of the mission
     */
    public int getDuration(){
        return duration;
    }

    /**
     *
     * @return the issued time of the mission
     */
    public int getIssuedTime(){return issuedTime;}
}