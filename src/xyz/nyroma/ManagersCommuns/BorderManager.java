package xyz.nyroma.ManagersCommuns;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class BorderManager extends BukkitRunnable {
    private static int counter = 5;
    private JavaPlugin plugin;
    private World w;
    private int sizeBorder;

    public BorderManager(World w, JavaPlugin plugin) {
        this.plugin = plugin;
        this.w = w;
        sizeBorder = (int) w.getWorldBorder().getSize();
    }

    @Override
    public void run() {
        for (Player p : w.getPlayers()) {
            p.sendMessage(ChatColor.RED + "La bordure va se réduire de "+ sizeBorder + " blocks à "+ sizeBorder/2 +" blocks dans 10 minutes ! Commencez à avancer vers le 0 0 !");
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : w.getPlayers()) {
                    p.sendMessage(ChatColor.RED + "La bordure va se réduire de "+ sizeBorder + " blocks à "+ sizeBorder/2 +" blocks dans 5 minutes ! Commencez à avancer vers le 0 0 !");
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (Player p : w.getPlayers()) {
                            p.sendMessage(ChatColor.RED + "La bordure va se réduire de "+ sizeBorder + " blocks à "+ sizeBorder/2 +" blocks dans 1 minute ! Commencez à avancer vers le 0 0 !");
                        }
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (BorderManager.counter != 0) {
                                    for (Player p : w.getPlayers()) {
                                        p.sendMessage(ChatColor.RED + "La bordure va se réduire dans " + BorderManager.counter + "...");
                                    }
                                    BorderManager.counter--;
                                } else {
                                    for (Player p : w.getPlayers()) {
                                        p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 100, 1);
                                        p.sendMessage("La bordure a été réduite à une zone de " + (w.getWorldBorder().getSize() / 2) + " blocks de côté");
                                    }
                                    w.getWorldBorder().setSize(w.getWorldBorder().getSize() / 2);
                                    this.cancel();
                                }
                            }
                        }.runTaskTimer(plugin, 55 * 20L, 20L);
                    }
                }.runTaskLater(plugin, 4 * 60 * 20L);
            }
        }.runTaskLater(plugin, 5 * 60 * 20L);
    }
}
