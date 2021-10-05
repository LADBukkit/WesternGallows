package de.ladbukkit.westerngallows;

import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.Optional;

/**
 * Handles the gallow events.
 * @author LADBukkit (Robin Eschbach)
 */
public class GallowListener implements Listener {

    /**
     * The plugin this object belongs to.
     */
    private final WesternGallows plugin;

    /**
     * Creates a gallow listener with the plugin it belongs to.
     * @param plugin The plugin this object belongs to.
     */
    public GallowListener(WesternGallows plugin) {
        this.plugin = plugin;
    }

    /**
     * Cancel movement of a hangmen.
     * @param e The PlayerMoveEvent.
     */
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if(plugin.getHangmen().contains(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    /**
     * Cleans up gallows of leaving players.
     * @param e The PlayerQuitEvent.
     */
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if(plugin.getHangmen().contains(e.getPlayer())) {
            plugin.getGallows().entrySet().stream().filter(k -> k.getValue().getHangman() == e.getPlayer()).forEach(k -> k.getValue().cleanUp());
            e.getPlayer().setHealth(0);
            plugin.getHangmen().remove(e.getPlayer());
        }
    }

    /**
     * Cleans up gallows when the player dies.
     * @param e The PlayeDeathEvent.
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if(plugin.getHangmen().contains(e.getEntity())) {
            plugin.getGallows().entrySet().stream().filter(k -> k.getValue().getHangman() == e.getEntity()).forEach(k -> k.getValue().cleanUp());
            plugin.getHangmen().remove(e.getEntity());
        }
    }

    /**
     * Handle when a player clicks a lever.
     * @param e The PlayerInteractEvent
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.LEVER) {
            Optional<Map.Entry<String, Gallow>> g = plugin.getGallows().entrySet().stream()
                    .filter(k -> k.getValue().getLever() != null && k.getValue().getLever().distance(e.getClickedBlock().getLocation()) < 1)
                    .findFirst();

            if(g.isPresent()) {
                Gallow gallow = g.get().getValue();
                if(!p.hasPermission("wg.executioner")) {
                    p.sendMessage(plugin.getMessageConfig().get("nopermission"));
                    e.setCancelled(true);
                    return;
                }
                if(gallow.getHangman() == null) {
                    p.sendMessage(plugin.getMessageConfig().get("hang.gallownotinuse"));
                    e.setCancelled(true);
                    return;
                }

                // Death anitmation
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        Player hang = gallow.getHangman();
                        if(hang == null) {
                            this.cancel();
                            return;
                        }

                        hang.setHealth(Math.max(0, gallow.getHangman().getHealth() - 2.5));
                        hang.playEffect(EntityEffect.HURT);
                    }
                }.runTaskTimer(plugin, 0, 10);
            }
        }
    }
}
