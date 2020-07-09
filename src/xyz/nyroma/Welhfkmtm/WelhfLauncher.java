package xyz.nyroma.Welhfkmtm;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.nyroma.ManagersCommuns.TasksManager;
import xyz.nyroma.abstracts.Setup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WelhfLauncher extends Setup implements InventoryHolder {
    private List<Material> mats = new ArrayList<>();
    private List<ItemStack> items = new ArrayList<>();
    private JavaPlugin plugin;
    private World w;

    public WelhfLauncher(JavaPlugin plugin, World w){
        this.plugin = plugin;
        this.w = w;
    }

    @Override
    public void build(List<String> parameters, Player player) {
        String type = parameters.get(0);
        String difficulty = parameters.get(1);
        World w  = player.getWorld();

        mats.clear();
        items.clear();

        switch(difficulty){
            case "FLASH":
                setMat(9);
                break;
            case "NORMAL":
                break;
            case "HARD":
                break;
        }
        addCrafts(new CraftsManager(difficulty, mats, items, plugin).build());
        addCrafts(new ShapelessRecipe[]{new CraftsManager(difficulty, mats, items, plugin).getFinalStarRecipe()});

        setPresentation("Welhfkmtm", "Objectif : obtenez les neuf items.");
        setGamerules(w);
        setActivity(true);
        w.getWorldBorder().setDamageAmount(0.1);
        w.getWorldBorder().setCenter(0, 0);
        w.getWorldBorder().setSize(1000);
        addListeners(new Listener[]{new WelhfListeners(this, plugin, difficulty)}, plugin);
        sendPresentation();
        runTasks();

        for(Player p : player.getServer().getOnlinePlayers()){
            new BukkitRunnable() {
                @Override
                public void run() {
                    reset(p);
                    teleport(p, randomLoc(w, 1000));
                    p.getInventory().addItem(new ItemStack(Material.STONE_SWORD));
                    p.getInventory().addItem(new ItemStack(Material.STONE_PICKAXE));
                    p.getInventory().addItem(new ItemStack(Material.STONE_AXE));
                    p.getInventory().addItem(new ItemStack(Material.STONE_SHOVEL));
                    p.getInventory().addItem(new ItemStack(Material.STONE_HOE));
                }
            }.run();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        w.setSpawnLocation(0, 75, 0);
    }

    private void setMat(int nb){
        this.mats.clear();

        List<Material> dispos = Arrays.asList(
                Material.DIAMOND, Material.ENDER_EYE, Material.GRAVEL, Material.GRINDSTONE, Material.GOLDEN_APPLE, Material.IRON_HELMET, Material.LAVA_BUCKET,
                Material.WITHER_SKELETON_SKULL, Material.CLOCK, Material.DETECTOR_RAIL, Material.VINE, Material.LEATHER_BOOTS, Material.DIAMOND_BLOCK, Material.ANVIL,
                Material.ENCHANTING_TABLE, Material.GHAST_TEAR, Material.POTION, Material.BLAZE_ROD, Material.DIAMOND_CHESTPLATE, Material.HONEY_BOTTLE,
                Material.QUARTZ_BLOCK, Material.CHISELED_QUARTZ_BLOCK, Material.MAGMA_BLOCK, Material.NETHER_WART, Material.TNT, Material.PUFFERFISH, Material.FISHING_ROD
        );
        for(int i = 0 ; i < nb ; i++){
            Random r = new Random();
            int rand = r.nextInt(dispos.size());
            while(this.mats.contains(dispos.get(rand))){
                rand = r.nextInt(dispos.size());
            }
            this.mats.add(dispos.get(rand));
        }
    }

    @Override
    public void runTasks() {
        new BukkitRunnable() {
            @Override
            public void run() {
                TasksManager tsm = new TasksManager(plugin, w);
                tsm.setTimer(w);
                tsm.setPVPTask(20);
                tsm.setResistTask();
                tsm.setPausesTask(getLauncher());
            }
        }.run();
    }

    private WelhfLauncher getLauncher(){
        return this;
    }

    @Override
    public void sendPresentation() {
        for(Player p : plugin.getServer().getOnlinePlayers()){
            p.sendTitle(ChatColor.RED + presentation.get(0), ChatColor.RED + presentation.get(1), 10,40,10);
        }
    }

    @Override
    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, 9, "Items à obtenir");
        if(items.size() == 0){
            for(Material m : mats){
                inv.addItem(new ItemStack(m));
            }
        } else if(mats.size() == 0) {
            for(ItemStack item : items){
                inv.addItem(item);
            }
        } else if(mats.size() + items.size() <= 9){
            for(Material m : mats){
                inv.addItem(new ItemStack(m));
            }
            for(ItemStack item : items){
                inv.addItem(item);
            }
        } else {
            return null;
        }
        return inv;
    }
}
