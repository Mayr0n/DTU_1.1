package xyz.nyroma.DTU;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.nyroma.ManagersCommuns.TasksManager;
import xyz.nyroma.abstracts.Setup;
import xyz.nyroma.ManagersCommuns.TeamManager;

import java.util.List;
import java.util.Random;

public class DTULauncher extends Setup {
    private JavaPlugin plugin;
    private TeamManager tm;
    private World w;

    @Override
    public void build(List<String> parameters, Player player) {
        String type = parameters.get(0);
        boolean special = false;
        if(parameters.size() == 2 && parameters.get(1).equals("SPECIAL")){
            special = true;
        }
        Player p = player;
        World mainWorld = p.getWorld();

        for(World w : Bukkit.getServer().getWorlds()) {
            manageWorld(w);
        }
        setGamerules(mainWorld);
        setBorder(mainWorld);

        addCrafts(new DTUCrafts().build(plugin));
        if(special){
            addListeners(new Listener[]{new DTUListeners(plugin), new SpecialDTUListeners()}, plugin);
            PotionEffectType[] effets = {
                    PotionEffectType.DAMAGE_RESISTANCE,
                    PotionEffectType.JUMP,
                    PotionEffectType.SPEED,
                    PotionEffectType.ABSORPTION,
                    PotionEffectType.FAST_DIGGING,
                    PotionEffectType.FIRE_RESISTANCE,
                    PotionEffectType.HEALTH_BOOST,
                    PotionEffectType.INCREASE_DAMAGE,
                    PotionEffectType.INVISIBILITY
            };
            for(Player play : Bukkit.getServer().getOnlinePlayers()){
                play.addPotionEffect(new PotionEffect(effets[new Random().nextInt(effets.length + 1)], 999999, 0));
            }
        } else {
            addListeners(new Listener[]{new DTUListeners(plugin)}, plugin);
        }
        runTasks();
        setActivity(true);
        setPresentation("DestroyTheUniverse", "Objectif : tuer tout le monde. Bonne chance.");
        sendPresentation();

        if(type.equals("TEAM")){
            List[] teams = this.tm.getTeams();
            for(List<Player> team : teams){
                Location loc = randomLoc(mainWorld, 2000);
                for(Player play : team){
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            reset(play);
                            teleport(p, loc);
                            play.sendMessage( ChatColor.DARK_GREEN + "Vous êtes invincible pendant deux minutes !");
                            play.sendMessage( ChatColor.DARK_GREEN + "Le pvp a été désactivé pour les 20 premières minutes !");
                        }
                    }.run();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if(type.equals("SOLO")){
            for(Player play : p.getServer().getOnlinePlayers()){
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        reset(play);
                        teleport(play, randomLoc(mainWorld, 2000));
                        play.sendMessage( ChatColor.DARK_GREEN + "Vous êtes invincible pendant deux minutes !");
                        play.sendMessage( ChatColor.DARK_GREEN + "Le pvp a été désactivé pour les 20 premières minutes !");
                    }
                }.run();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public void sendPresentation(){
        for(Player p : plugin.getServer().getOnlinePlayers()){
            p.sendTitle(ChatColor.RED + presentation.get(0), ChatColor.RED + presentation.get(1), 10,40,10);
        }
    }
    @Override
    public void runTasks() {
        final DTULauncher l = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                TasksManager tsm = new TasksManager(plugin, w);
                tsm.setBorderTimer(90);
                tsm.setPausesTask(l);
                tsm.setPVPTask(20);
                tsm.setResistTask();
                tsm.setTimer(w);
            }
        }.run();
    }

    public DTULauncher(TeamManager tm, World w, JavaPlugin plugin) {
        this.plugin = plugin;
        this.tm = tm;
        this.w = w;
    }
}
