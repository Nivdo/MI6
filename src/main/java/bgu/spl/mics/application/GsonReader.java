package bgu.spl.mics.application;
import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Services;
import com.google.gson.annotations.*;

public class GsonReader {
    @SerializedName("services")
    public Services services;

    @SerializedName("inventory")
    public String[] inventory;

    @SerializedName("squad")
    public Agent[] squad;
}
