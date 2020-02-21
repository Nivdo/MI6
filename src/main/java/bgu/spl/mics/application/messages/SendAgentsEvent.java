package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import java.util.List;

/**
 * An event sent by M to moneyPenny via the Message Broker in order to send agents on a mission after all condition of the mission were met
 */
public class SendAgentsEvent implements Event <Object>{
    private List<String> agentsSerialsNumbersRequired;
    private int MissionLength;

    /**
     * constructor
     * @param agentsNumbers - agents numbers of the agents that are sent to the mission
     * @param MissionLength - the lenth of the mission the agents are required for.
     */
    public SendAgentsEvent(List<String> agentsNumbers , int MissionLength){
        agentsSerialsNumbersRequired=agentsNumbers;
        this.MissionLength=MissionLength;
    }

    /**
     *
     * @returnlist of serial numbers of the agents required for the mission
     */
    public List<String> getAgentsSerialsNumbersRequired(){
        return agentsSerialsNumbersRequired;
    }

    /**
     *
     * @return the mission length
     */
    public int getMissionLength(){
        return MissionLength;
    }
}