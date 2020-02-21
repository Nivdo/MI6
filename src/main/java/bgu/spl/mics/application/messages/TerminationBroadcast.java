package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Event;
import bgu.spl.mics.Message;

import java.util.LinkedList;
import java.util.List;

/**
 * A broadcast send via the messageBroker to all of the subscribers and causes the program termination
 */
public class TerminationBroadcast implements Broadcast { }
