package bgu.spl.mics;

import bgu.spl.mics.application.GsonReader;
import bgu.spl.mics.application.passiveObjects.intell;
import bgu.spl.mics.application.passiveObjects.Agent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

public class jsonParser {
    private final String filename;
    private String[] inventory;
    private Agent[] squad;
    private int Moneypenny;
    private int M;
    private int time;
    private intell[] intelligence;

    /**
     * parses the json file to all the pasive objects
     * @param filename the path of the Json file
     */
    public jsonParser(String filename) {
        this.filename = filename;
        BufferedReader reader = null;
        try {
            reader=new BufferedReader(new FileReader(filename));
            Gson gson = new GsonBuilder().create();
            GsonReader gsonR = gson.fromJson(reader, GsonReader.class);
            inventory =gsonR.inventory;
            squad = gsonR.squad;
            intelligence = gsonR.services.intelligence;
            M = gsonR.services.M;
            Moneypenny = gsonR.services.Moneypenny;
            time = gsonR.services.time;
        }
        catch (IOException IOE){}
    }

    public String[] getInventory() {
        return inventory;
    }

    public Agent[] getAgents() {
        return squad;
    }

    public intell[] getIntelligence () {
        return intelligence;
    }

    public int getMoneyPenny() {
        return Moneypenny;
    }

    public int getM() {
        return M;
    }

    public int getTime() {
        return time;
    }

}
