package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Future;
import bgu.spl.mics.SimplePublisher;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.*;

import java.util.*;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
	private int TimeTick;
	private Diary diary = Diary.getInstance();

	public M(String name) {
		super(name);
		TimeTick=0;
	}

	/**
	 * registers M to the appropriate Event and Broadcasts at the MessageBroker
	 */
	@Override
	protected void initialize() {
		subscribeBroadcast(TerminationBroadcast.class ,new TerminateCallback());
		subscribeBroadcast(TickBroadcast.class ,new TickBroadcastCallback());
		subscribeEvent(MissionReceivedEvent.class , new MissionReceivedCallback());
	}



	private class TerminateCallback implements Callback {


		/**
		 * gets called after receiving a termination Broadcast
		 * @param c a nothing object
		 */
		@Override
		public void call(Object c) {
			terminate();
		}
	}


	private class MissionReceivedCallback implements Callback<MissionReceivedEvent> {
		private SimplePublisher simPub;

		public MissionReceivedCallback(){
			simPub=new SimplePublisher();
		}

		/**
		 *  gets called after M receives an MissionReceivedEvent
		 * @param e the MissionReceivedEvent M got from Intelligence via the MessageBroker
		 */
		@Override
		public void call(MissionReceivedEvent e) {
			QAnswer QAnswer;
			List<String> agentsNames;
			List<String> agentsSerials = e.getNecessaryAgentsNumbers();
			String gadget = e.getNecessaryGadget();
			MoneypennyAnswer MPAnswer = checkAgents(e,agentsSerials); //sends Monneypenny an event and receives answer saying whether the agents are available
			if(MPAnswer!=null && MPAnswer.isAgentsAvailable()) {
				agentsNames = MPAnswer.getAgentsNames();
				QAnswer =askQForGadget(gadget);
				if (QAnswer!=null && QAnswer.getIsGadgetExists() && TimeTick<=e.getExpiredTime()) {
					sendEventAndCreateReport(e,agentsSerials,gadget,QAnswer.getQtime(),agentsNames,MPAnswer);
				}
				else {
					releaseAgentsAndUpdateDiaryTotal(agentsSerials);
				}
				complete(e,diary.getTotal());
			}
			else {
				diary.addReport(null);
				complete(e,diary.getTotal());
			}
		}

		/**
		 * gets called if the mission could not be executed for some reason and releases the agents allready acquired for it
		 * @param agentsSerials a list of the agent serials than needs to be released
		 */
		private void releaseAgentsAndUpdateDiaryTotal (List<String> agentsSerials){
			ReleaseAgentsEvent eve = new ReleaseAgentsEvent(agentsSerials);
			simPub.sendEvent(eve);
			diary.addReport(null);
		}

		/**
		 * gets called if the all the specification of the mission were met and the mission can be execute.
		 * it will send an Event to Moneypenny asking her to send the mission
		 * afterwards it will create a report and log it in the diary
		 * @param e the MissionReceivedEvent executed
		 * @param agentsSerials a list of the agents serials that went on the mission
		 * @param gadget the gadget needed for the mission
		 * @param Qtime the Time Q received the Gadget request
		 * @param agentsNames a list of the names of the agents that went on the mission
		 * @param MPAnswer an object containing the name of the moneypenny that aquired the agents
		 */
		private void sendEventAndCreateReport (MissionReceivedEvent e, List<String> agentsSerials, String gadget, int Qtime, List<String> agentsNames,MoneypennyAnswer MPAnswer){
			int duration = e.getDuration();
			int issuedTime = e.getIssuedTime();
			SendAgentsEvent MissionEvent = new SendAgentsEvent(agentsSerials, duration);
			simPub.sendEvent(MissionEvent);
			Report report = CreateReport(agentsNames, gadget, e.getMissionName(), Qtime,agentsSerials , MPAnswer.getName(),issuedTime) ;
			diary.addReport(report);
		}

		/**
		 * creates an AgentsAvailableEvent and send it to Moneypenny via the MessageBroker
		 * @param e the MissionReceivedEvent that required the agents
		 * @param agentsSerials a list with the serials of the agents required for the mission
		 * @return an Object containing the time Moneypenny received the event and her answer if the agents are available
		 */
		private MoneypennyAnswer checkAgents(MissionReceivedEvent e , List<String> agentsSerials){
			AgentsAvailableEvent AgentsEvent= new AgentsAvailableEvent(agentsSerials);
			Future AgentsF= simPub.sendEvent(AgentsEvent);
			return  (MoneypennyAnswer)AgentsF.get();
		}

		/**
		 * creates an GadgetAvailableEvent and send it to Q via the MessageBroker
		 * @param gadget the gadget required
		 * @return an Object containing the time Q received the event and his answer if the gadget was available
		 */
		private QAnswer askQForGadget (String gadget){
			GadgetAvailableEvent GadgetEvent = new GadgetAvailableEvent(gadget);
			Future GadgetF = simPub.sendEvent(GadgetEvent);
			Object  output = GadgetF.get();
			return (QAnswer) output;
		}

		/**
		 * create a report with the necessary details
		 * @param agentNames a list of the names of the agents that went on the mission
		 * @param gadget the gadget needed for the mission
		 * @param MissionName the name of the mission reported
		 * @param Qtime the time Q received the event and his answer if the gadget was available
		 * @param agentsSerials a list of the agents serials that went on the mission
		 * @param MoneypennyName the name of the Moneypenny that aquired the agents
		 * @param issuedTime
		 * @return
		 */
		private Report CreateReport(List<String> agentNames ,String gadget,String MissionName ,int Qtime, List<String> agentsSerials, String MoneypennyName , int issuedTime){
			Report report= new Report();
			report.setAgentsNames(agentNames);
			report.setGadgetName(gadget);
			report.setM(Integer.parseInt(getName()));
			report.setMissionName(MissionName);
			report.setAgentsSerialNumbersNumber(agentsSerials);
			report.setMoneypenny(Integer.parseInt(MoneypennyName));
			report.setQTime(Qtime);
			report.setTimeCreated(TimeTick);
			report.setTimeIssued(issuedTime);
			return report;
		}
	}

	private class TickBroadcastCallback implements Callback<TickBroadcast> {
		@Override

		/**
		 * gets called after receiving a TickBroadcast and updates the field accordingly
		 */
		public void call(TickBroadcast b) {
			TimeTick=b.getTimeTick();
		}
	}
}
