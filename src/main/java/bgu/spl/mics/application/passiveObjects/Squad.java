package bgu.spl.mics.application.passiveObjects;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {
	private static class SingeltonHolder {
		private static Squad theSquad = new Squad();
	}
	private Map<String, Agent> agentsMap;
	private AtomicBoolean releasedAll;

	/**
	 * Retrieves the single instance of this class.
	 */
	private Squad(){
		agentsMap=new HashMap<>();
		releasedAll = new AtomicBoolean(false);
	}
	public static Squad getInstance() {
		return SingeltonHolder.theSquad;
	}

	/**
	 * Initializes the squad. This method adds all the agents to the squad.
	 * <p>
	 * @param agents 	Data structure containing all data necessary for initialization
	 * 						of the squad.
	 */
	public void load (Agent[] agents) {
		Arrays.sort(agents, Comparator.comparing(Agent::getName));
		for (Agent agent : agents) {
			synchronized (agent) {
				agentsMap.putIfAbsent(agent.getSerialNumber(), agent);
			}
		}
	}


	/**
	 * Releases agents.
	 */
	public void releaseAgents(List<String> serials){
		if(serials.get(0)=="releaseAll"){
			releaseAllAgents();
		}
		else {
			Collections.sort(serials, String::compareTo);
			for (String serial : serials) {
				Agent agent = agentsMap.get(serial);
				synchronized (agent) {
					agent.release();
					agent.notifyAll();
				}
			}
		}
	}

	/**
	 * Release all agents after termination
	 */
	private void releaseAllAgents(){
		if(releasedAll.compareAndSet(false,true)) {
			for (Map.Entry<String, Agent> t : agentsMap.entrySet()) {
				if (!t.getValue().isAvailable())
					t.getValue().release();
			}
		}
	}

	/**
	 * simulates executing a mission by calling sleep.
	 * @param time   milliseconds to sleep
	 */
	public synchronized void sendAgents(List<String> serials, int time){
		Collections.sort(serials,String::compareTo);
			try {Thread.currentThread().sleep(time*100);}
			catch (InterruptedException e){}
		for(String serial : serials){
				Agent agent =agentsMap.get(serial);
				synchronized (agent) {
					agent.release();
					agent.notifyAll();
				}
			}
//		}
	}

	/**
	 * acquires an agent, i.e. holds the agent until the caller is done with it
	 * @param serials   the serial numbers of the agents
	 * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
	 */
	public boolean getAgents(List<String> serials){
		Collections.sort(serials,String::compareTo);
			for (String serial : serials) {
				if (!agentsMap.containsKey(serial))
					return false;
			}
			for (String serial : serials) {
				Agent agent = agentsMap.get(serial);
				synchronized (agent) {
					while (!agent.isAvailable()) {  //if one of the agents is not available
						try {agent.wait();}
						catch (InterruptedException e) {}
					}
					agent.acquire();
				}
			}
		return true;  //if all the agents were available
	}

	/**
	 * gets the agents names
	 * @param serials the serial numbers of the agents
	 * @return a list of the names of the agents with the specified serials.
	 */
	public List<String> getAgentsNames(List<String> serials){
		Collections.sort(serials,String::compareTo);
		List<String> names = new LinkedList<String>();
		for (String serial : serials) {
			names.add(agentsMap.get(serial).getName());
		}
		return names;
	}
}