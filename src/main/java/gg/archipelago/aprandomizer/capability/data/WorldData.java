package gg.archipelago.aprandomizer.capability.data;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class WorldData {

    private String seedName = "";

    private int dragonState = DRAGON_ASLEEP;

    private boolean jailPlayers = true;
    private int informMode = BOTH;
    private Set<Long> locations = new HashSet<>();

    public static final int DRAGON_KILLED = 30;
    public static final int DRAGON_SPAWNED = 20;
    public static final int DRAGON_WAITING = 15;
    public static final int DRAGON_ASLEEP = 10;

    public static final int NONE = 0;
    public static final int TITLE = 1;
    public static final int SIDEBAR = 2;
    public static final int BOTH = 3;
     
    public void setSeedName(String seedName) {
        this.seedName = seedName;
    }

    public String getSeedName() {
        return seedName;
    }

    public void setDragonState(int dragonState) {
        this.dragonState = dragonState;
    }

    public int getDragonState() {
        return dragonState;
    }

    public boolean getJailPlayers() {
        return jailPlayers;
    }

    public void setJailPlayers(boolean jailPlayers) {
        this.jailPlayers = jailPlayers;
    }

    public int getInformMode() {
    	return informMode;
    }
    
    public void setInformMode(int mode) {
    	informMode = mode;
    }
    
    public void addLocation(Long location) {
        this.locations.add(location);
    }

    public void setLocations(Long[] locations) {
        this.locations.addAll(Lists.newArrayList(Arrays.stream(locations).iterator()));
    }

    public Set<Long> getLocations() {
        return locations;
    }
}
