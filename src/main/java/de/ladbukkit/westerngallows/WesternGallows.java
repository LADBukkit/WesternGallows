package de.ladbukkit.westerngallows;

import com.mojang.brigadier.Message;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The main class of the Plugin.
 *
 * @author LADBukkit (Robin Eschbach)
 */
public class WesternGallows extends JavaPlugin {

    static {
        ConfigurationSerialization.registerClass(Gallow.class, "Gallow");
    }

    /**
     * The MessageConfig of this plugin.
     */
    private MessageConfig messageConfig;

    /**
     * Hashmap containing all the gallows.
     */
    private final Map<String, Gallow> gallows = new HashMap<>();

    /**
     * Set of currently hung players.
     */
    private final Set<Player> hangmen = new HashSet<>();

    /**
     * The file containing the gallows.
     */
    private final File gallowFile = new File(getDataFolder(), "gallows.yml");

    /**
     * The gallow config.
     */
    private final FileConfiguration gallowConfig = YamlConfiguration.loadConfiguration(gallowFile);

    /**
     * Creates a message config, registers the listeners and the command executors.
     */
    @Override
    public void onEnable() {
        this.getDataFolder().mkdirs();

        try {
            this.messageConfig = new MessageConfig(new File(this.getDataFolder(), "messages.yml"), "/messages.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!gallowFile.exists()) {
            try {
                gallowFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        loadGallows();

        GallowCommand gallowCommand = new GallowCommand(this);
        HangCommand hangCommand = new HangCommand(this);

        getCommand("gallow").setExecutor(gallowCommand);
        getCommand("gallow").setTabCompleter(gallowCommand);
        getCommand("hang").setExecutor(hangCommand);
        getCommand("hang").setTabCompleter(hangCommand);

        Bukkit.getPluginManager().registerEvents(new GallowListener(this), this);
    }

    /**
     * @return The message config of this plugin.
     */
    public MessageConfig getMessageConfig() {
        return messageConfig;
    }

    /**
     * @return The hashmap of the gallows.
     */
    public Map<String, Gallow> getGallows() {
        return gallows;
    }

    /**
     * @return A set of currently hung players.
     */
    public Set<Player> getHangmen() {
        return hangmen;
    }

    /**
     * Loads all the gallows from the config.
     */
    public void loadGallows() {
        try {
            gallowConfig.load(gallowFile);
            for(String key : gallowConfig.getKeys(false)) {
                gallows.put(key, (Gallow) gallowConfig.get(key));
            }
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the gallows to the config.
     */
    public void saveGallows() {
        gallows.forEach(gallowConfig::set);
        try {
            gallowConfig.save(gallowFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
