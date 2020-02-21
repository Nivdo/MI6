package bgu.spl.mics.application.passiveObjects;

import java.util.LinkedList;
import java.util.List;
/**
 * Passive data-object representing Moneypenny answer regarding Agents availability
 */
public class MoneypennyAnswer {
    private String name;
    private List<String> AgentsNames ;
    private boolean isAgentsAvailable;

    /**
     * <p>
     *  constructor
     * </p>
     *
     * @param name - the Moneypenny name handling the event
     * @param AgentsNames - the names of the Agents required for the mission
     *
     */
    public MoneypennyAnswer(String name , List<String>AgentsNames , boolean isAgentsAvailable){
        this.name=name;
        this.AgentsNames = new LinkedList();
        this.AgentsNames.addAll(AgentsNames);
        this.isAgentsAvailable=isAgentsAvailable;
    }
    /**
     * <p>
     *  returns the name of the Moneypenny
     * </p>
     * @return  name - the names of the Moneypenny
     */
    public String getName() {
        return name;
    }

    /**
     * <p>
     *  returns the name of the agents on the mission
     * </p>
     * @return  AgentsNames - a list containing the names of the agents required for the mission
     */
    public List<String> getAgentsNames() {
        return AgentsNames;
    }

    public boolean isAgentsAvailable() { return isAgentsAvailable;}
}
