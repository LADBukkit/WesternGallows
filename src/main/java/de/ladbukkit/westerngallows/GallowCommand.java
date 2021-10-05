package de.ladbukkit.westerngallows;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the gallow command.
 * @author LADBukkit (Robin Eschbach)
 */
public class GallowCommand implements CommandExecutor, TabCompleter {

    /**
     * The plugin this object belongs to.
     */
    private final WesternGallows plugin;

    /**
     * Creates a gallow command handler with the plugin it belongs to.
     * @param plugin The plugin this object belongs to.
     */
    public GallowCommand(WesternGallows plugin) {
        this.plugin = plugin;
    }

    /**
     * Checks if a material is a fence.
     * @param material The material to check.
     * @return whether the material is a fence
     */
    private boolean isFence(Material material) {
        return  material == Material.OAK_FENCE ||
                material == Material.SPRUCE_FENCE ||
                material == Material.BIRCH_FENCE ||
                material == Material.JUNGLE_FENCE ||
                material == Material.ACACIA_FENCE ||
                material == Material.DARK_OAK_FENCE ||
                material == Material.CRIMSON_FENCE ||
                material == Material.WARPED_FENCE;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!sender.hasPermission("wg.admin")) {
            sender.sendMessage(plugin.getMessageConfig().get("nopermission"));
            return false;
        }
        if(!(sender instanceof Player p)) {
            sender.sendMessage(plugin.getMessageConfig().get("notaplayer"));
            return false;
        }

        if(args.length == 2) {
            String gallowName = args[1].toLowerCase();
            if(args[0].equalsIgnoreCase("create")) {
                if(plugin.getGallows().containsKey(gallowName)) {
                    p.sendMessage(plugin.getMessageConfig().get("gallow.exist"));
                } else {
                    plugin.getGallows().put(gallowName, new Gallow());
                    plugin.saveGallows();
                    p.sendMessage(plugin.getMessageConfig().get("gallow.create"));
                }
                return false;
            }
            if(args[0].equalsIgnoreCase("setpoint")) {
                if(!plugin.getGallows().containsKey(gallowName)) {
                    p.sendMessage(plugin.getMessageConfig().get("gallow.notexist"));
                } else {
                    Gallow gallow = plugin.getGallows().get(gallowName);
                    gallow.setCriminal(p.getLocation());
                    plugin.getGallows().put(gallowName, gallow);
                    plugin.saveGallows();
                    p.sendMessage(plugin.getMessageConfig().get("gallow.setcriminal"));
                }
                return false;
            }
            if(args[0].equalsIgnoreCase("setlead")) {
                if(!plugin.getGallows().containsKey(gallowName)) {
                    p.sendMessage(plugin.getMessageConfig().get("gallow.notexist"));
                } else {
                    Block block = p.getTargetBlock(null, 5);
                    if(!isFence(block.getType())) {
                        p.sendMessage(plugin.getMessageConfig().get("gallow.nofence"));
                        return false;
                    }
                    Gallow gallow = plugin.getGallows().get(gallowName);
                    gallow.setLead(block.getLocation());
                    plugin.getGallows().put(gallowName, gallow);
                    plugin.saveGallows();
                    p.sendMessage(plugin.getMessageConfig().get("gallow.setlead"));
                }
                return false;
            }
            if(args[0].equalsIgnoreCase("setlever")) {
                if(!plugin.getGallows().containsKey(gallowName)) {
                    p.sendMessage(plugin.getMessageConfig().get("gallow.notexist"));
                } else {
                    Block block = p.getTargetBlock(null, 5);
                    if(block.getType() != Material.LEVER) {
                        p.sendMessage(plugin.getMessageConfig().get("gallow.nolever"));
                        return false;
                    }
                    Gallow gallow = plugin.getGallows().get(gallowName);
                    gallow.setLever(block.getLocation());
                    plugin.getGallows().put(gallowName, gallow);
                    plugin.saveGallows();
                    p.sendMessage(plugin.getMessageConfig().get("gallow.setlever"));
                }
                return false;
            }
        }

        p.sendMessage(plugin.getMessageConfig().get("gallow.help"));
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> tab = new ArrayList<>();

        if(sender.hasPermission("wg.admin")) {
            if(args.length == 1) {
                tab.add("create");
                tab.add("setpoint");
                tab.add("setlead");
                tab.add("setlever");
            } else if(args.length == 2) {
                tab.addAll(plugin.getGallows().keySet());
            }
        }
        return tab;
    }
}
