package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

/**
 * An event that M sends to MennyPenny in order to release agents because a mission was aborted.
 */
public class ReleaseAgentsEvent implements Event<Object> {
    private List<String> agentsSerialsNumbersRequired;

    /**
     *constructor
     * @param agentsNumbers that are to be released
     */
    public ReleaseAgentsEvent(List<String> agentsNumbers){
        agentsSerialsNumbersRequired=agentsNumbers;
    }

    /**
     *
     * @return a list of serial numbers of the agents that will be released
     */
    public List<String> getAgentsSerialsNumbersRequired(){
        return agentsSerialsNumbersRequired;
    }
}
