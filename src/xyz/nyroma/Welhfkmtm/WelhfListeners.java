package xyz.nyroma.Welhfkmtm;

import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class WelhfListeners implements Listener {
    private WelhfLauncher WelhfLauncher;
    private List<Player> winners = new ArrayList<>();
    private boolean counterLaunched = false;
    private JavaPlugin plugin;
    private int counter;
    private List<ItemStack> bounded = new ArrayList<>();
    private String difficulty;

    public WelhfListeners(WelhfLauncher WelhfLauncher, JavaPlugin plugin, String difficulty) {
        this.WelhfLauncher = WelhfLauncher;
        this.plugin = plugin;
        this.difficulty = difficulty;
    }

    private ItemStack getStar(){
        ItemStack is = new ItemStack(Material.NETHER_STAR);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Final Star");
        im.addEnchant(Enchantment.BINDING_CURSE, 5, true);
        im.setLore(Arrays.asList("La dernière étape avant la win !", "Déposez cette étoile au spawn pour gagner la partie !"));
        is.setItemMeta(im);

        return is;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        Player p = e.getEntity();
        for(ItemStack is : p.getInventory().getContents()){
            try {
                if (is.hasItemMeta()) {
                    ItemMeta im = is.getItemMeta();
                    if (im.hasEnchants()) {
                        Map<Enchantment, Integer> ench = im.getEnchants();
                        if (ench.containsKey(Enchantment.BINDING_CURSE)) {
                            if (ench.get(Enchantment.BINDING_CURSE) == 5) {
                                bounded.add(is);
                            }
                        }
                    }
                }
            } catch(NullPointerException ee){
                ee.printStackTrace();
            }
        }
        e.setDeathMessage(ChatColor.DARK_RED + e.getDeathMessage());
        p.spigot().respawn();
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e){
        Player p = e.getPlayer();
        if(bounded.size() > 0){
            for(ItemStack it : bounded){
                p.getInventory().addItem(it);
            }
            bounded.clear();
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Inventory inv = e.getInventory();
        if (inv.getSize() == 9 && inv.getHolder() instanceof WelhfLauncher) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        List<Boolean> bools = new ArrayList<>();
        if(difficulty.equals("FLASH")){
            if(p.getInventory().contains(getStar())){
                winners.add(p);
                Bukkit.broadcastMessage(ChatColor.DARK_GREEN + p.getName() + " a obtenu tous les items. Tuez le avant qu'il atteigne le spawn !");
            }
        } else {
            for (ItemStack item : WelhfLauncher.getInventory().getContents()) {
                if (p.getInventory().contains(item)) {
                    bools.add(true);
                } else {
                    bools.add(false);
                }
            }
            if (!bools.contains(false)) {
                winners.add(p);
            }
        }

        for(Player winner : winners){
            if (!counterLaunched && !isInSpawn(winner)) {
                counterLaunched = true;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        winner.sendMessage(ChatColor.DARK_RED + "Il faut te rendre au spawn !");
                        counterLaunched = false;
                    }
                }.runTaskTimer(plugin, 60 * 20L, 60 * 20L);
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (isInSpawn(winner)) {
                        if(counter == 0){
                            Bukkit.broadcastMessage(ChatColor.DARK_RED + winner.getName() + " est entré dans la zone du spawn avec tous les items. Vous avez 5 minutes pour l'en éjecter !");
                            for(Player play : p.getServer().getOnlinePlayers()){
                                play.sendTitle(ChatColor .RED + "ATTENTION", ChatColor.RED + winner.getName() + " est en train de gagner !", 10, 40, 10);
                                play.playSound(play.getLocation(), Sound.ENTITY_GHAST_SCREAM, 100, 1);
                            }
                        }
                        counter++;
                        if (counter == 10) {
                            for (Player play : winner.getServer().getOnlinePlayers()) {
                                play.teleport(winner);
                                play.sendTitle(ChatColor.RED + winner.getName(), ChatColor.RED + "a gagné la partie !", 10, 40, 10);
                                play.playSound(play.getLocation(), Sound.ENTITY_WITHER_SPAWN, 100, 1);
                            }
                            winner.getWorld().getWorldBorder().setSize(6000000);
                            winner.getServer().reload();
                        }
                    } else {
                        if(counter != 0) {
                            counter = 0;
                            Bukkit.broadcastMessage(ChatColor.DARK_GREEN + winner.getName() + " est sorti de la zone du spawn.");
                            for (Player play : p.getServer().getOnlinePlayers()) {
                                play.playSound(play.getLocation(), Sound.ENTITY_GHAST_DEATH, 100, 1);
                            }
                        }
                    }
                }
            }.runTaskTimer(plugin, 30 * 20L, 30 * 20L);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        Item is = e.getItemDrop();
        List<Entity> entities = is.getNearbyEntities(2,1,2);
        boolean diam = false;
        boolean or = false;
        boolean fer = false;
        boolean red = false;
        for(Entity ent : entities){
            if(ent instanceof Item){
                Item it = (Item) ent;
                ItemStack ist = it.getItemStack();
                if(ist.getType().equals(Material.DIAMOND)){
                    diam = true;
                } else if(ist.getType().equals(Material.GOLD_INGOT)){
                    or = true;
                } else if(ist.getType().equals(Material.IRON_INGOT)){
                    fer = true;
                } else if(ist.getType().equals(Material.REDSTONE)){
                    red = true;
                }
            }
        }
        if(diam && or && fer && red){
            is.remove();
            ItemStack newItem = is.getItemStack();
            ItemMeta im = newItem.getItemMeta();
            im.setLore(Arrays.asList("Cet item vous appartiendra pour toujours !"));
            im.setDisplayName("[Bound] " + im.getDisplayName());
            im.addEnchant(Enchantment.BINDING_CURSE, 5, true);
            newItem.setItemMeta(im);
            is.getLocation().getWorld().dropItem(is.getLocation(), newItem);
        }
    }

    private boolean isInSpawn(Player p) {
        Location loc = p.getLocation();
        return true;//loc.getX() >= -25 && loc.getX() <= 25 && loc.getZ() >= -25 && loc.getZ() <= 25 && !loc.getBlock().getBiome().equals(Biome.NETHER);
    }

}
