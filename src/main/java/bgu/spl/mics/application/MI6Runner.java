package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.subscribers.Q;
import bgu.spl.mics.jsonParser;

import java.util.LinkedList;
import java.util.List;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) {
            jsonParser parser = new jsonParser(args[0]);
            Squad theSquad = Squad.getInstance();
            theSquad.load(parser.getAgents());
            Inventory theInventory = Inventory.getInstance();
            theInventory.load(parser.getInventory());
            intell[] sources = parser.getIntelligence();
            List<Thread> ThreadList = new LinkedList<>();
            Q q = new Q();

            List<Intelligence> intellList = new LinkedList<>();
            Integer name = 1;
            for (intell source : sources) {
                intellList.add(new Intelligence(name.toString(), source.missions));
                name++;
            }
            List<Moneypenny> MPList = new LinkedList<>();
            int numOfMoneyPenny = parser.getMoneyPenny();
            for (int i = 1; i <= numOfMoneyPenny; i++) {
                MPList.add(new Moneypenny(((Integer) i).toString()));
            }
            List<M> MList = new LinkedList<>();
            int numOfM = parser.getM();
            for (int i = 1; i <= numOfM; i++) {
                MList.add(new M(((Integer) i).toString()));
            }
            ThreadList.add(new Thread(q));
            for (Intelligence intell : intellList) {
                ThreadList.add(new Thread(intell));
            }
            for (M m : MList) {
                ThreadList.add(new Thread(m));
            }
            for (Moneypenny mp : MPList) {
                ThreadList.add(new Thread(mp));
            }
            for (Thread thread : ThreadList) {
                thread.start();
            }

            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
            }

            TimeService timeLord = new TimeService(parser.getTime());
            Thread timeThread = new Thread(timeLord);
            timeThread.start();
            ThreadList.add(timeThread);
            try {
                for (Thread thread : ThreadList) {
                    thread.join();
                }
            } catch (InterruptedException ex) {
            }
            Inventory.getInstance().printToFile(args[1]);
            Diary.getInstance().printToFile(args[2]);
    }
}
