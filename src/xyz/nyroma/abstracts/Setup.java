package xyz.nyroma.abstracts;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public abstract class Setup {
    protected List<String> presentation = new ArrayList<>();
    protected Sound soundP;
    protected Boolean activity;

    public abstract void build(List<String> parameters, Player player);
    public abstract void runTasks();
    public abstract void sendPresentation();

    public void setPresentation(String title, String subtitle){
        presentation = Arrays.asList(title, subtitle);
    }

    public Boolean getActivity(){
        return this.activity;
    }
    public void setActivity(Boolean b){
        this.activity = b;
    }

    protected void teleport(Player p, Location loc){
        loc.getChunk().load();
        p.teleport(loc);
    }

    protected void manageWorld(World w){
        w.setSpawnLocation(new Location(w, 0,150,0));
        w.setTime(0);
        w.setFullTime(0);
        w.setDifficulty(Difficulty.HARD);
        w.setPVP(true);
    }

    protected void setBorder(World w){
        WorldBorder wb = w.getWorldBorder();
        wb.setCenter(w.getSpawnLocation());
        wb.setSize(2000);
        wb.setDamageAmount(0.01);
        Bukkit.broadcastMessage(ChatColor.AQUA + "La bordure s'est réduite sur une zone de 2000x2000 blocks !");
    }

    protected void setGamerules(World w){
        w.setGameRule(GameRule.SPECTATORS_GENERATE_CHUNKS, false);
        w.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        w.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        w.setGameRule(GameRule.KEEP_INVENTORY, false);
        w.setGameRule(GameRule.DO_FIRE_TICK, false);
    }

    protected void reset(Player p){
        p.setGameMode(GameMode.ADVENTURE);
        for(PotionEffect pe : p.getActivePotionEffects()){
            p.removePotionEffect(pe.getType());
        }
        p.getInventory().clear();
        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 120 * 20, 250));
        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10, 10));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 10, 10));
        p.setGameMode(GameMode.SURVIVAL);
        p.setExp(0);
        p.setLevel(0);
        if(soundP == null) {
            p.playSound(p.getLocation(), Sound.BLOCK_END_PORTAL_SPAWN, 100, 1);
        } else {
            p.playSound(p.getLocation(), soundP, 100, 1);
        }
    }

    protected Location randomLoc(World w, int size){
        Random r = new Random();
        int x = r.nextInt(size) - size/2;
        int z = r.nextInt(size) - size/2;
        return new Location(w, x, 200, z);
    }

    protected void addCrafts(ShapedRecipe[] recipes){
        for(ShapedRecipe r : recipes) {
            Bukkit.getServer().addRecipe(r);
        }
    }
    protected void addCrafts(ShapelessRecipe[] recipes){
        for(ShapelessRecipe sr : recipes){
            Bukkit.getServer().addRecipe(sr);
        }
    }

    protected void addListeners(Listener[] listeners, JavaPlugin plugin){
        for(Listener l : listeners){
            Bukkit.getServer().getPluginManager().registerEvents(l, plugin);
        }
    }
}
