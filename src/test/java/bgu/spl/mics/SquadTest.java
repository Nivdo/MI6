package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.Squad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SquadTest {
    private Squad squad;

    @BeforeEach
    public void setUp(){
        squad=Squad.getInstance();
    }


    /**
     * <p>
     *
     */
    @Test
    public void testGetInstance(){
        assertNotNull(squad);
    }


    /**
     * <p>
     *  test loading agents really adds them to the squad
     *  test Getting agents from the squad list really works
     */
    @Test
    public void testLoadAndGetAgents(){
        Agent[] newAgents = new Agent[2];
        Agent Ag1 =new Agent();
        Agent Ag2 =new Agent();
        Ag1.setName("Bond");
        Ag1.setSerialNumber("007");
        Ag2.setName("Cohen");
        Ag2.setSerialNumber("001");
        newAgents[0]=Ag1;
        newAgents[1]=Ag2;
        squad.load(newAgents);
        List<String> Serials=new LinkedList<String>();
        Serials.add("001");
        Serials.add("007");
        assertTrue(squad.getAgents(Serials));
        Serials.add("not a number");
        assertFalse(squad.getAgents(Serials));
    }


    /**
     * <p>
     *
     */
    @Test
    public void testGetAgentsNames(){
        Agent[] newAgents = new Agent[2];
        Agent Ag3 =new Agent();
        Agent Ag4 =new Agent();
        Ag3.setName("Niv");
        Ag3.setSerialNumber("009");
        Ag4.setName("Yaely");
        Ag4.setSerialNumber("008");
        newAgents[0]=Ag3;
        newAgents[1]=Ag4;
        squad.load(newAgents);
        List<String> Serials=new LinkedList<String>();
        Serials.add("008");
        Serials.add("009");
        List<String> Names = new LinkedList<String>();
        Names.add("Yaely");
        Names.add("Niv");
        List<String> squadNames =squad.getAgentsNames(Serials);
        for(String name : squadNames){
            assertTrue(squadNames.contains(name));
        }
        assertFalse(squadNames.contains("008"));
        assertFalse(squadNames.contains("009"));
    }


    /**
     * <p>
     *  test Squad is a singletone
     */
    @Test
    public void testSingelton(){
        assertSame(squad,Squad.getInstance());
    }

    /**
     * <p>
     *  testing agents on mission really changes their status
     */
    @Test
    public void testSendAndReleaseAgents(){
        Agent[] newAgents = new Agent[2];
        Agent Ag5 =new Agent();
        Agent Ag6 =new Agent();
        Ag5.setName("Ni");
        Ag5.setSerialNumber("002");
        Ag6.setName("Ya");
        Ag6.setSerialNumber("003");
        newAgents[0]=Ag5;
        newAgents[1]=Ag6;
        squad.load(newAgents);
        List<String> Serials=new LinkedList<String>();
        Serials.add("002");
        Serials.add("003");
        assertTrue(squad.getAgents(Serials)); //the agents are available
        squad.sendAgents(Serials, Integer.MAX_VALUE);
        assertFalse(squad.getAgents(Serials));      // the agent should be missing because we sent him on a mission for the maximum time possible
        squad.releaseAgents(Serials);
        assertTrue(squad.getAgents(Serials)); //the agents should be available after being released
    }
}