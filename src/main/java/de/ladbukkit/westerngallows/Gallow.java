package de.ladbukkit.westerngallows;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Bat;
import org.bukkit.entity.LeashHitch;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Contains a gallow.
 * @author LADBukkit (Robin Eschbach)
 */
public class Gallow implements ConfigurationSerializable {
    /**
     * The location of the criminal.
     */
    private Location criminal;

    /**
     * The location of the lead.
     */
    private Location lead;

    /**
     * The location of the lever.
     */
    private Location lever;

    /**
     * The currently hung player.
     */
    private Player hangman;

    /**
     * The leash hitch the player hangs on.
     */
    private LeashHitch leash;

    /**
     * The invisile bat the leash is connected to.
     */
    private Bat bat;

    /**
     * constructs this object without any set position.
     */
    public Gallow() {
        this(null, null, null);
    }

    /**
     * Constructs this object with set positions.
     * @param criminal The location of the criminal.
     * @param lead The location of the lead.
     * @param lever The location of the lever.
     */
    public Gallow(Location criminal, Location lead, Location lever) {
        this.criminal = criminal;
        this.lead = lead;
        this.lever = lever;
    }

    public Location getCriminal() {
        return criminal;
    }

    public void setCriminal(Location criminal) {
        this.criminal = criminal;
    }

    public Location getLead() {
        return lead;
    }

    public void setLead(Location lead) {
        this.lead = lead;
    }

    public Location getLever() {
        return lever;
    }

    public void setLever(Location lever) {
        this.lever = lever;
    }

    public Player getHangman() {
        return hangman;
    }

    public void setHangman(Player hangman) {
        this.hangman = hangman;
    }

    public LeashHitch getLeash() {
        return leash;
    }

    public void setLeash(LeashHitch leash) {
        this.leash = leash;
    }

    public Bat getBat() {
        return bat;
    }

    public void setBat(Bat bat) {
        this.bat = bat;
    }

    /**
     * Cleans up hangman, leash and bat
     */
    public void cleanUp() {
        hangman = null;
        bat.remove();
        leash.remove();
        bat = null;
        leash = null;
    }

    /**
     * @return Whether the configuration of this gallow is complete.
     */
    boolean isComplete() {
        return criminal != null && lead != null && lever != null;
    }

    /**
     * Serializes a Gallow to the config.
     * @return The serialized gallow.
     */
    @Override
    public Map<String, Object> serialize() {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("criminal", locationToString(criminal));
        result.put("lead", locationToString(lead));
        result.put("lever", locationToString(lever));
        return result;
    }

    /**
     * Deserializes a Gallow from the config.
     * @param args The incoming config section.
     * @return The deserialized gallow.
     */
    public static Gallow deserialize(Map<String, Object> args) {
        String criminal = args.containsKey("criminal") ? args.get("criminal").toString() : null;
        String lead = args.containsKey("lead") ? args.get("lead").toString() : null;
        String lever = args.containsKey("lever") ? args.get("lever").toString() : null;

        return new Gallow(
            stringToLocation(criminal),
            stringToLocation(lead),
            stringToLocation(lever)
        );
    }

    /**
     * Converts a string to a location.
     * @param str The string.
     * @return The location.
     */
    private static Location stringToLocation(String str) {
        String[] split = str.split(" ");
        return new Location(Bukkit.getWorld(split[0]),
                Double.parseDouble(split[1]),
                Double.parseDouble(split[2]),
                Double.parseDouble(split[3]),
                Float.parseFloat(split[4]),
                Float.parseFloat(split[5]));
    }

    /**
     * Converts a location to a string.
     * @param loc The location.
     * @return The string.
     */
    private static String locationToString(Location loc) {
        if(loc == null) return null;
        return loc.getWorld().getName() + " " + loc.getX() + " " + loc.getY() + " " + loc.getZ() + " " + loc.getYaw() + " " + loc.getPitch();
    }
}
