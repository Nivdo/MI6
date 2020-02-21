package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.MoneypennyAnswer;

import java.util.List;

/**
 * an event that wiil be sent by an M to a Monneypenny via the Message Broker to check ig a list of agents are available fo a mission
 */
public class AgentsAvailableEvent implements Event<MoneypennyAnswer> {
    private List<String> agentsSerialsNumbersRequired;

    /**
     * constructor
     * @param agentsNumbers the required agents for the mission
     */
    public AgentsAvailableEvent(List<String> agentsNumbers){
        agentsSerialsNumbersRequired=agentsNumbers;

    }

    /**
     *
     * @return ths serialNumbers list of the required agents
     */
    public List<String> getAgentsSerialsNumbersRequired(){
        return agentsSerialsNumbersRequired;
    }

}