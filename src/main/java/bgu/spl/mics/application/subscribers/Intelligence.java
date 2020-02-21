package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * A Publisher\Subscriber.
 * Holds a list of Info objects and sends them
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {

    private List<MissionInfo> missionsList;
    private int lastTimeTick;

    public Intelligence(String name , MissionInfo[] newMissions) {
        super(name);
        missionsList=new LinkedList<>();
        setMissions(newMissions);

    }

    /**
     * initializing method - subscrinig the intell' to the relevant messages
     */
    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class ,new TickCallback());
        subscribeBroadcast(TerminationBroadcast.class ,new TerminateCallback());
    }

    /**
     * load the missions to the missions list.
     * @param newMissions - a list of missions to load to the list
     */
    private void setMissions(MissionInfo[] newMissions){
        for(MissionInfo mission : newMissions){
            missionsList.add(mission);
        }
        missionsList.sort(Comparator.comparingInt(MissionInfo::getTimeIssued));
    }

    private class TickCallback implements Callback<TickBroadcast> {
        private SimplePublisher simPub;

        public TickCallback(){
            simPub = new SimplePublisher();
        }

        /**
         * gets called after reciveing aTicBroadcast
         * @param b the tick broadcast recieved from the message broker.
         */
        @Override
        public void call(TickBroadcast b) {
            int tick =b.getTimeTick();
            sendMissionsRequests(tick);
        }

        /**
         * A method which send missions rewuest to the message broker if the timeissued of the mission match the time tick received
         * @param Timetick- timetick according to it the intelligence will check if he has a mission in this time which he can send
         */
        private void sendMissionsRequests(int Timetick) {
            if(missionsList.size()>0) {
                MissionInfo mission = missionsList.get(0);
                while (mission.getTimeIssued() == Timetick ) {
                    simPub.sendEvent(new MissionReceivedEvent(mission.getMissionName(), mission.getSerialAgentsNumbers(), mission.getGadget() , mission.getTimeExpired() ,mission.getDuration(), mission.getTimeIssued()));
                    missionsList.remove(0);
                    if (!missionsList.isEmpty())
                        mission = missionsList.get(0);
                    else
                        break;

                }
            }
        }
    }


    private class TerminateCallback implements Callback {
        @Override
        public void call(Object c) {
            terminate();
        }
    }
}