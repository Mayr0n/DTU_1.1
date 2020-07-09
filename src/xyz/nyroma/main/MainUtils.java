package xyz.nyroma.main;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class MainUtils {
    public static void shutdown(Player p){
        for(PotionEffect pe : p.getActivePotionEffects()){
            p.removePotionEffect(pe.getType());
        }
        p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 300*20,255));
        p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 300*20,255));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 300*20,255));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 300*20,255));
        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 300*20,255));
        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 300*20,255));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 300*20,255));
    }
    public static ItemStack getUnbreakable(ItemStack item){
        ItemMeta m = item.getItemMeta();
        m.setUnbreakable(true);
        item.setItemMeta(m);
        return item;
    }
    public static Player getPlayerByName(Server server, String pseudo){
        Player p = null;
        for (Player play : server.getOnlinePlayers()) {
            if (play.getName().equals(pseudo)) {
                p = play;
            }
        }
        try {
            return p;
        } catch (NullPointerException e){
            return null;
        }
    }
    public static void giveStuff(Player p){
        p.getInventory().clear();

        ItemStack[] armor = {
                getUnbreakable(new ItemStack(Material.IRON_BOOTS)), getUnbreakable(new ItemStack(Material.IRON_LEGGINGS)),
                getUnbreakable(new ItemStack(Material.DIAMOND_CHESTPLATE)), getUnbreakable(new ItemStack(Material.IRON_HELMET))
        };

        ItemStack bow = getUnbreakable(new ItemStack(Material.BOW));
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);

        ItemStack[] bonusStuff = {
                bow, getUnbreakable(new ItemStack(Material.DIAMOND_AXE)),
                new ItemStack(Material.LAVA_BUCKET),
                new ItemStack(Material.WATER_BUCKET), new ItemStack(Material.GOLDEN_APPLE, 5),
                new ItemStack(Material.COOKED_BEEF, 64), new ItemStack(Material.ARROW)
        };

        p.getInventory().setArmorContents(armor);
        p.getInventory().setItemInMainHand(getUnbreakable(new ItemStack(Material.DIAMOND_SWORD)));
        p.getInventory().setItemInOffHand(getUnbreakable(new ItemStack(Material.SHIELD)));
        for (ItemStack item : bonusStuff) {
            p.getInventory().addItem(item);
        }
        for (int i = 0; i <= 6; i++) {
            p.getInventory().addItem(new ItemStack(Material.OAK_LEAVES, 64));
        }
        p.setGameMode(GameMode.SURVIVAL);
    }
}
