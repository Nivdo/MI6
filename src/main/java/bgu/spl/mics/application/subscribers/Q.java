package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.QAnswer;

import java.util.LinkedList;
import java.util.List;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {
	private Inventory theInventory;
	private int TimeTick;

	public Q() {
		super("GadgetMaster");
		theInventory=Inventory.getInstance();
	}

	/**
	 * registers Q to the appropriate Event and Broadcasts at the MessageBroker
	 */
	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, new TickBroadcastCallback());
		subscribeBroadcast(TerminationBroadcast.class ,new TerminateCallback());
		subscribeEvent(GadgetAvailableEvent.class , new GadgetAvailableCallback());
	}

	private class GadgetAvailableCallback implements Callback<GadgetAvailableEvent> {
		/**
		 * gets called after receiving a GadgetAvailableEvent and checks the inventory for the required gadget
		 * @param e GadgetAvailableEvent
		 */
		@Override
		public void call(GadgetAvailableEvent e) {
			QAnswer QAnswer;
			if (theInventory.getItem(e.getGadgetRequiredName()))
				QAnswer=new QAnswer(true,TimeTick);    // the time Q received the request, and it received it at timeTick
			else
				QAnswer=new QAnswer(false,-1);			// means the gadget was not found
			complete(e, QAnswer);
		}
	}

	private class TickBroadcastCallback implements Callback<TickBroadcast> {

		/**
		 * gets called aftet receiving a TickBroadcast and update the TimeTick field accordingly
		 * @param b a TickBroadcast
		 */
		@Override
		public void call(TickBroadcast b) {
			TimeTick=b.getTimeTick();
		}
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
}