package bgu.spl.mics;
//import java.util.concurrent;

import bgu.spl.mics.application.messages.TerminationBroadcast;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {
	private static class forSingelton {
		private static MessageBrokerImpl theBroker = new MessageBrokerImpl();
	}
	private Map<Class<? extends Message>, LinkedBlockingQueue<Subscriber>> topicToSubscribersMap; // this map holds the different types of messages (topics) and the matching subscribers queue.
	private Map<Subscriber, LinkedBlockingQueue<Message>> SubscriberToMessageMap;//this map holds each subscriber and his messages queue
	private Map<Event,Future> eventToFutureMap;//this map holds each evemt and its future object

	private MessageBrokerImpl(){
		topicToSubscribersMap= new ConcurrentHashMap<>();
		SubscriberToMessageMap= new ConcurrentHashMap<>();
		eventToFutureMap= new HashMap();

	}
	/**
	 * Retrieves the single instance of this class.
	 */
	public static MessageBroker getInstance() {
		return forSingelton.theBroker;
	}

	/**
	 * adds the subscribeEvent to the matching queue in the MessageMap. if queue doesn't exist yet, first creating it.
	 */
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
		synchronized (m){
			register(m);
		}
		synchronized (type) {
			topicToSubscribersMap.putIfAbsent(type, new LinkedBlockingQueue());
			topicToSubscribersMap.get(type).add(m);
		}
	}

	/**
	 * adds the subscribeBroadcast to the matching list in the MessageMap. if list doesn't exist yet, first creating it.
	 */
	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		synchronized (m){
			register(m);
		}
		synchronized (type) {
			topicToSubscribersMap.putIfAbsent(type, new LinkedBlockingQueue());
			topicToSubscribersMap.get(type).add(m);
		}
	}

	/**
	 * this method resolve the event
 	 * @param e      The completed event.
	 * @param result The resolved result of the completed event.
	 * @param <T>
	 */
	@Override
	public <T> void complete(Event<T> e, T result) {
			eventToFutureMap.get(e).resolve(result);
	}

	/**
	 * this method sends the broadcast by inserting it to the subscribers queue.
	 * @param b 	The message to added to the queues.
	 */
	@Override
	public void sendBroadcast(Broadcast b) {
		//add b to each message queue of the the subscribed subscribers
		synchronized (b.getClass()) {  // we want to maintain order between different broadcasts of the same type, but don't want to block a broadcast of a different type to be sent simultaneously
			LinkedBlockingQueue<Subscriber> q = topicToSubscribersMap.get(b.getClass());
			if (q != null & !q.isEmpty()) {
				if(b.getClass().equals(TerminationBroadcast.class)){
					for (Subscriber s : q) {
						synchronized (s) {
							Message msg = b;
							SubscriberToMessageMap.get(s).clear();
							SubscriberToMessageMap.get(s).add(msg);
						}
					}
				}
				else {
					for (Subscriber s : q) {
						synchronized (s) {
							Message msg = b;
							SubscriberToMessageMap.get(s).add(msg);
						}
					}
				}

				b.getClass().notifyAll();
			}
		}
	}

	/**
	 * this method sends the event by inserting it to the subscriber queue in a round robin manner.
	 * @param e     	The event to add to the queue.
	 * @param <T>
	 * @return a future object
	 */
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		Future<T> f;
		Subscriber s;
			synchronized (e.getClass()) {
				Queue<Subscriber> q = topicToSubscribersMap.get(e.getClass());
				if (q == null || q.isEmpty())
					return null;
				s = q.poll();
				synchronized (s) {
					f = new Future<T>();
					synchronized (eventToFutureMap) {
						eventToFutureMap.put(e, f);
					}
					SubscriberToMessageMap.get(s).add(e);
					topicToSubscribersMap.get(e.getClass()).add(s);
					s.notifyAll();
				}
				e.getClass().notifyAll();
			}
		return f;
	}

	/**
	 * this method register a subscriber m
	 * @param m
	 */
	@Override
	public void register(Subscriber m) {
		synchronized (m) {
			SubscriberToMessageMap.putIfAbsent(m, new LinkedBlockingQueue());
		}
	}

	@Override
	public synchronized void unregister(Subscriber m) {
		if (SubscriberToMessageMap.containsKey(m)){//otherwise m is not subscribed and no action is required
			for (Map.Entry<Class<? extends Message>, LinkedBlockingQueue<Subscriber>> t : topicToSubscribersMap.entrySet()) {
				synchronized (t.getKey()) {
					t.getValue().remove(m);
				}
			}
			//for(Message msg : SubscriberToMessageMap.get(m)){
			//	eventToFutureMap.get(msg).resolve(null);
			//}
			for (Map.Entry<Event,Future> t : eventToFutureMap.entrySet()){
				complete(t.getKey(),null);
			}
			SubscriberToMessageMap.remove(m);
		}
	}

	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		if (!SubscriberToMessageMap.containsKey(m))
			throw new IllegalStateException();
		Message msg = SubscriberToMessageMap.get(m).take(); //returning the first message in the subscriber queue
		return msg;
	}
}