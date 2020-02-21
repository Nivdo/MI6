package bgu.spl.mics;
import bgu.spl.mics.application.*;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.QAnswer;
import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;
import org.junit.jupiter.api.BeforeEach;
import bgu.spl.mics.MessageBroker;
import bgu.spl.mics.MessageBrokerImpl;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


public class MessageBrokerTest {

    public class intEvent implements Event {}
    public class intBroadcast implements Broadcast{}

    MessageBroker theBroker;
    GadgetAvailableEvent event;
    TickBroadcast broadcast;
    Subscriber sub;


    @BeforeEach
    public void setUp(){
        theBroker=MessageBrokerImpl.getInstance();
        event = new GadgetAvailableEvent("Gadget");
        broadcast = new TickBroadcast(1);
        sub = new Subscriber("testSub") {
            @Override
            protected void initialize() {
            }
        };
    }

    /**
     * <p>
     *  testing instance is not null
     */
    @Test
    public void testInstanceInstanceIsNotNull(){
        assertNotNull(theBroker);
    }

    /**
     * <p>
     *  testing getInstance method doesn't retrieve null
     */
    @Test
    public void testGetInstance(){
        assertNotNull(MessageBrokerImpl.getInstance());
    }


    /**
     * <p>
     *  testing  IllegalStateException is thrown when the subscriber was not registered.
     */
    @Test
    public void testIllegalStateException(){
        theBroker.unregister(sub); //making sure that sub won't be registered
        try {
            theBroker.subscribeEvent(event.getClass(), sub);
        }
        catch (Exception exc){
            assertEquals(exc, "IllegalStateException");
        }
    }

    /**
     * <p>
     *  testing  sendEvent flow works as expected-a sent subscribed event is delivered to one of the registered subscribers.
     *  then, when calling the awaitMessage method with the subscriber that got the event the next message in the queue is returned.
     */
    @Test
    public void testSendEventFlow(){
        Subscriber s = new Subscriber("testS") {
            @Override
            protected void initialize() {
            }
        };
        theBroker.unregister(sub);//unregistering the only other sub inorder to make sure the event will be added to s q.
        theBroker.register(s); //registering the subscriber which means a que is created for s.
        theBroker.subscribeEvent(event.getClass(), s);
        theBroker.sendEvent(event);
        try {
            assertEquals(theBroker.awaitMessage(s), event); //testing the next message in s queue is equal to the event that was sent to him.
        }
        catch (InterruptedException i) {}
    }

    /**
     * <p>
     *  testing subscribeBroadcast and sendBroadcast methods really works by subscribing 2 subscribers to the same broadcast and verifying the broadcast gets to both their queues.
     */
    @Test
    public void testBroadcast(){
        theBroker.register(sub);
        theBroker.subscribeBroadcast(broadcast.getClass(), sub);
        //creating another sub to subscribe to the broadcast
        Subscriber s = new Subscriber("testS") {
            @Override
            protected void initialize() {}
        };
        theBroker.register(s);
        theBroker.subscribeBroadcast(broadcast.getClass(), s);
        theBroker.sendBroadcast(broadcast); //Adds the broadcast to the message queues of sub and s
        //now we'll check both s and sub have the broadcast message waiting for them in the queue
        try {
            assertEquals(theBroker.awaitMessage(s), broadcast);
            assertEquals(theBroker.awaitMessage(sub), broadcast);
        }
        catch (InterruptedException i) {}
    }

    /**
     * <p>
     *  testing complete method
     */
    @Test
    public void testComplete(){
        QAnswer qa = new QAnswer(true,10);
        theBroker.complete(event,qa);
        assertEquals(10,qa.getQtime());
        assertEquals(true,qa.getIsGadgetExists());
    }
}