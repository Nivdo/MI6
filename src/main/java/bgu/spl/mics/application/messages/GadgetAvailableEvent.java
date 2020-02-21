package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.passiveObjects.QAnswer;

/**
 *An event that wiil be sent via the Message Broker to check if the required gadget for the mission exists
 */
public class GadgetAvailableEvent implements Event<QAnswer> {
    private String gadgetRequired;

    /**
     *
     * @param gadget - the required gadget for the mission
     */
    public GadgetAvailableEvent(String gadget){
        gadgetRequired=gadget;
    }

    /**
     *
     * @return the name of the required gadget
     */
    public String getGadgetRequiredName(){
        return gadgetRequired;
    }

}