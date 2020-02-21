package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Callback;
import bgu.spl.mics.SimplePublisher;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.MoneypennyAnswer;
import bgu.spl.mics.application.passiveObjects.Squad;

import java.util.LinkedList;
import java.util.List;


/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {
	private Squad squad;

	public Moneypenny(String name) {
		super(name);
		squad = Squad.getInstance();
	}

	/**
	 * registers Moneypenny to the appropriate Event and Broadcasts at the MessageBroker
	 */
	@Override
	protected void initialize() {
		if(getName().equals("1")){  //only one Moneypenny will check the agents availability
			subscribeEvent(AgentsAvailableEvent.class, new AgentsAvailableCallback());
			subscribeBroadcast(TerminationBroadcast.class, new TerminateCallback());
		}
		else{  //these events will eventually release agents
			subscribeEvent(SendAgentsEvent.class , new SendAgentsCallBack());
			subscribeEvent(ReleaseAgentsEvent.class , new ReleaseAgentsCallBack());
			subscribeBroadcast(TerminationBroadcast.class, new TerminateCallback());
		}
	}

	/**
	 * gets called after receiving a terminationBroadcast and releases all the agents in the Squad
	 */
	private class TerminateCallback implements Callback {
		@Override
		public void call(Object c)
		{
			List<String> releaseAll = new LinkedList<>();
			releaseAll.add("releaseAll");
			squad.releaseAgents(releaseAll);
			terminate();
		}
	}

	private class AgentsAvailableCallback implements Callback<AgentsAvailableEvent> {

		/**
		 * gets called after receiving an AgentsAvailableEvent
		 * @param agentsRequest an AgentsAvailableEvent containing a list of the required agents
		 */
		@Override
		public void call(AgentsAvailableEvent agentsRequest) {
			boolean CheckResult = squad.getAgents(agentsRequest.getAgentsSerialsNumbersRequired());
			MoneypennyAnswer answer;
			if(CheckResult) {
				answer = new MoneypennyAnswer(getName(), squad.getAgentsNames(agentsRequest.getAgentsSerialsNumbersRequired()),CheckResult);
			}
			else {
				answer = new MoneypennyAnswer("-1", new LinkedList<String>(),CheckResult);
			}
			complete(agentsRequest,answer);
		}
	}

	private class SendAgentsCallBack implements Callback<SendAgentsEvent> {

		/**
		 * gets called after receiving a SendAgentsEvent and send all the agents in the param all
		 * @param e a SendAgentsEvent containing the agents that will be sent to the mission
		 */
		@Override
		public void call(SendAgentsEvent e) {
			squad.sendAgents(e.getAgentsSerialsNumbersRequired(),e.getMissionLength());
			complete(e,new Object());
		}
	}

	private class ReleaseAgentsCallBack implements Callback<ReleaseAgentsEvent> {

		/**
		 * gets called after receiving a ReleaseAgentsEvent and release all the agents in the list on the param
		 * @param e a ReleaseAgentsEvent
		 */
		@Override
		public void call(ReleaseAgentsEvent e) {
			squad.releaseAgents(e.getAgentsSerialsNumbersRequired());
			complete(e,new Object());
		}
	}
}
