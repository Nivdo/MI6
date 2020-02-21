package bgu.spl.mics.application.publishers;

import bgu.spl.mics.Publisher;
import bgu.spl.mics.SimplePublisher;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;

import java.sql.Time;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {
	private SimplePublisher simPub;
	private int duration;
	private long currentTime;
	private int tickCounter;

	/**
	 * constrautor
	 * @param duration - the duration of the program. after it the program will be terminated.
	 */
	public TimeService( int duration) {
		super("TimeLord");
		this.duration=duration;
	}

	/**
	 * initializng the TimeService with time tick 0
	 */
	@Override
	protected void initialize() {
		simPub=new SimplePublisher();
		tickCounter=0;
	}

	/**
	 * run method calls initialize and do the timeservice thing
	 */
	@Override
	public void run() {
		initialize();
		while(tickCounter<duration) {
			try {
				Thread.sleep(100, 0);
				currentTime = currentTime + 100;
				tickCounter++;
				simPub.sendBroadcast(new TickBroadcast(tickCounter));
			}
			catch (InterruptedException e) {}
		}
		simPub.sendBroadcast(new TerminationBroadcast());
	}
}
