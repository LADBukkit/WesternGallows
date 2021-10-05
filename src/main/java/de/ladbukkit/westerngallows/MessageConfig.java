package de.ladbukkit.westerngallows;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * A config using a resource as a basis for messages.
 * @author LADBukkit (Robin Eschbach)
 */
public class MessageConfig {
    /**
     * The file of the config.
     */
    private final File file;

    /**
     * The name of the resource.
     */
    private final String resource;

    /**
     * The underlying config.
     */
    private final FileConfiguration config;

    /**
     * Creates the message config in a file with a resource name.
     * @param file The file where to create.
     * @param resource The resource to use when the file does not exist.
     * @throws IOException If the file could not be created.
     */
    public MessageConfig(File file, String resource) throws IOException {
        this.file = file;
        this.resource = resource;

        if(!this.file.exists()) {
            Files.copy(getClass().getResourceAsStream(this.resource), this.file.toPath());
        }

        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    /**
     * Gets the message at the path and parses the color codes.
     * @param path The path in the config.
     * @return The message.
     */
    public String get(String path) {
        return ChatColor.translateAlternateColorCodes('&', this.config.getString(path, ""));
    }
}
