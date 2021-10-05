package de.ladbukkit.westerngallows;

import net.minecraft.world.entity.decoration.EntityLeash;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Bat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LeashHitch;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles the hang command.
 * @author LADBukkit (Robin Eschbach)
 */
public class HangCommand implements CommandExecutor, TabCompleter {
    /**
     * The plugin this object belongs to.
     */
    private final WesternGallows plugin;

    /**
     * Creates a hang command handler with the plugin it belongs to.
     * @param plugin The plugin this object belongs to.
     */
    public HangCommand(WesternGallows plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!sender.hasPermission("wg.executioner ")) {
            sender.sendMessage(plugin.getMessageConfig().get("nopermission"));
            return false;
        }

        if(args.length == 2) {
            Gallow gallow = plugin.getGallows().get(args[1].toLowerCase());

            if(gallow == null) {
                sender.sendMessage(plugin.getMessageConfig().get("gallow.notexist"));
                return false;
            }
            if(!gallow.isComplete()) {
                sender.sendMessage(plugin.getMessageConfig().get("hang.gallowincomplete"));
                return false;
            }
            if(gallow.getHangman() != null) {
                sender.sendMessage(plugin.getMessageConfig().get("hang.gallowinuse"));
                return false;
            }

            Player p = Bukkit.getPlayer(args[0]);
            if(p == null) {
                sender.sendMessage(plugin.getMessageConfig().get("hang.playernotfound"));
                return false;
            }
            if(p.hasPermission("wg.ignorehang")) {
                sender.sendMessage(plugin.getMessageConfig().get("hang.nothangable"));
                return false;
            }
            if(plugin.getHangmen().contains(p)) {
                sender.sendMessage(plugin.getMessageConfig().get("hang.playeralreadyhung"));
                return false;
            }

            gallow.setHangman(p);
            plugin.getHangmen().add(p);
            p.teleport(gallow.getCriminal());
            LeashHitch leash = gallow.getLead().getWorld().spawn(gallow.getLead(), LeashHitch.class);
            Bat bat = gallow.getCriminal().getWorld().spawn(gallow.getCriminal().clone().add(0, 1.5, 0), Bat.class);
            bat.setInvisible(true);
            bat.setAI(false);
            bat.setLeashHolder(leash);
            gallow.setBat(bat);
            gallow.setLeash(leash);

            Bukkit.broadcastMessage(plugin.getMessageConfig().get("hang.hangbroadcast").replace("%player%", p.getName()).replace("%gallow%", args[1].toLowerCase()));
        } else {
            sender.sendMessage(plugin.getMessageConfig().get("hang.help"));
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> tab = new ArrayList<>();

        if(sender.hasPermission("wg.executioner")) {
            if(args.length == 1) {
                tab.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toSet()));
            } else if(args.length == 2) {
                tab.addAll(plugin.getGallows().keySet());
            }
        }
        return tab;
    }
}
