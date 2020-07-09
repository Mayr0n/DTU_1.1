package xyz.nyroma.ManagersCommuns;

import xyz.nyroma.abstracts.Setup;
import xyz.nyroma.main.MainUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TasksManager {

    private JavaPlugin plugin;
    private World w;

    public TasksManager(JavaPlugin plugin, World w) {
        this.plugin = plugin;
        this.w = w;
    }

    public void setResistTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : w.getPlayers()) {
                    p.sendMessage( ChatColor.RED + "Les deux minutes sont écoulées ! Vous êtes maitenant sensibles aux dégâts !");
                }
            }
        }.runTaskLater(this.plugin, 120 * 20L);
    }

    public void setPausesTask(Setup launcher) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(launcher.getActivity()) {
                    for (Player p : w.getPlayers()) {
                        p.sendMessage(ChatColor.GREEN + "Une petite pause s'impose !");
                        p.playSound(p.getLocation(), Sound.ENTITY_CAT_EAT, 100,1);
                    }
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            for (Player p : w.getPlayers()) {
                                if (p.getGameMode() == GameMode.ADVENTURE) {
                                    p.sendMessage(ChatColor.GREEN + "La pause est terminée !");
                                    p.setGameMode(GameMode.SURVIVAL);
                                }
                                p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_SHOOT, 100,1);
                            }
                        }
                    }.runTaskLater(plugin, 5 * 60 * 20L);
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 3600 * 20L, 3600 * 20L);
    }

    public void setPVPTask(int minutes) {
        new BukkitRunnable() {
            @Override
            public void run() {
                w.setPVP(true);
                for(Player p : w.getPlayers()){
                    p.sendMessage( ChatColor.RED + "LE PVP A ETE ACTIVE");
                    p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 100,1);
                }
            }
        }.runTaskLater(plugin, minutes * 60 * 20L);
    }

    public void setTimer(World w){
        new TimerManager(w).runTaskTimer(this.plugin, 20L, 20L);
    }

    public void setBorderTimer(int minutes){
        new BorderManager(w, plugin).runTaskLater(this.plugin, (minutes-10) * 60 * 20L);
    }
}
