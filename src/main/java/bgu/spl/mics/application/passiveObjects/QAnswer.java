package bgu.spl.mics.application.passiveObjects;

public class QAnswer {
    private boolean isGadgetExists;
    private int Qtime;

    /**
     * <p>
     *  constructor
     * </p>
     *
     * @param isGadgetExists - a boolean indicating if the Gadget required exist in the inventory
     * @param Qtime - the time tick in which Q received the Gadget Event
     *
     */
    public QAnswer(boolean isGadgetExists , int Qtime){
        this.isGadgetExists=isGadgetExists;
        this.Qtime=Qtime;
    }

    /**
     * Retrieves if the gadget is available
     */
    public boolean getIsGadgetExists(){
        return isGadgetExists;
    }

    /**
     * Retrieves the time tick in which Q received the Gadget Event
     */
    public int getQtime(){
        return Qtime;
    }
}
