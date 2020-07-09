package xyz.nyroma.DTU;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class DTUListeners implements Listener {
    private JavaPlugin plugin;

    private boolean hasDiamondFire(ItemStack item) {
        try {
            if (item.getType().equals(Material.DIAMOND_SWORD) && item.hasItemMeta()) {
                if (item.getItemMeta().hasEnchants()) {
                    if (item.getItemMeta().hasEnchant(Enchantment.FIRE_ASPECT)) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (NullPointerException e) {
            return false;
        }
    }

    public DTUListeners(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        for (ItemStack is : p.getInventory().getContents()) {
            if (hasDiamondFire(is)) {
                try {
                    ItemMeta im = is.getItemMeta();
                    im.removeEnchant(Enchantment.FIRE_ASPECT);
                    is.setItemMeta(im);
                } catch (NullPointerException ignored) {

                }
            }
        }
    }

    public ItemStack getPlayerHead(Player p){
        ItemStack sk = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skull = (SkullMeta) sk.getItemMeta();
        if(skull != null){
            skull.setOwningPlayer(p.getPlayer());
        }
        sk.setItemMeta(skull);

        return sk;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        Location loc = p.getLocation();
        World w = p.getWorld();

        Objects.requireNonNull(loc.getWorld()).dropItem(loc, getPlayerHead(p));
        w.dropItem(loc, new ItemStack(Material.GOLDEN_APPLE));
        w.playSound(loc, Sound.ENTITY_WITHER_DEATH, 100, 1);
        p.setGameMode(GameMode.SPECTATOR);
        p.teleport(loc);
        e.setDeathMessage(ChatColor.DARK_RED + e.getDeathMessage());

        List<Player> vivants = new ArrayList<>();
        for (Player play : p.getServer().getOnlinePlayers()) {
            if (play.getGameMode().equals(GameMode.SURVIVAL)) {
                vivants.add(play);
            }
        }
        if(vivants.size() == 1){
            Player winner = vivants.get(0);
            for (Player play : p.getServer().getOnlinePlayers()) {
                if (play.getGameMode().equals(GameMode.SURVIVAL)) {
                    for (int i = 0; i <= 5; i++) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                Firework fw = (Firework) play.getWorld().spawnEntity(play.getLocation(), EntityType.FIREWORK);
                                FireworkMeta fwm = fw.getFireworkMeta();
                                fwm.setPower(5);
                                fwm.addEffect(FireworkEffect.builder().trail(true).withColor(Color.BLUE).flicker(true).build());
                                fw.setFireworkMeta(fwm);
                                fw.detonate();
                            }
                        }.runTaskLater(this.plugin, 20L);
                    }
                }
                play.teleport(winner);
                play.sendTitle(ChatColor.RED + winner.getName(), ChatColor.RED + "a gagné la partie !", 10, 40, 10);
                play.playSound(play.getLocation(), Sound.ENTITY_WITHER_SPAWN, 100, 1);
            }
            p.getWorld().getWorldBorder().setSize(6000000);
            p.getServer().reload();
        }

    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            try {
                if (item.getData().toString().equals("LEGACY_SKULL_ITEM(3)")) {
                    SkullMeta meta = (SkullMeta) item.getItemMeta();
                    if (meta.getOwningPlayer() != null) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 2 * 20, 4));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60 * 20, 1));
                        try {
                            p.getInventory().setItemInMainHand(new ItemStack(Material.REDSTONE));
                        } catch (AssertionError ignored) {
                        }
                    }
                }
            } catch (StringIndexOutOfBoundsException ignored) {
            }
        }
    }

    @EventHandler
    public void onEnKilled(EntityDeathEvent e) {
        LivingEntity entity = e.getEntity();
        Location loc = entity.getLocation();
        World w = entity.getWorld();
        Random r = new Random();
        int i = r.nextInt(5);
        int amount = r.nextInt(4);

        if (entity.getType().equals(EntityType.SHEEP)) {
            if (i == 4) {
                w.dropItem(loc, new ItemStack(Material.LEATHER, amount));
            }
            if (i == 2) {
                w.dropItem(loc, new ItemStack(Material.STRING, amount));
            }
        } else if (entity.getType().equals(EntityType.PIGLIN)) {
            System.out.println(i);
            if (i == 4) {
                w.dropItem(loc, new ItemStack(Material.GOLD_NUGGET, amount));
            }
        } else if (entity.getType().equals(EntityType.PIG)) {
            if (i == 4) {
                w.dropItem(loc, new ItemStack(Material.LEATHER, amount));
            }
        }
    }

    @EventHandler
    public void leavesDecay(LeavesDecayEvent e) {
        Block b = e.getBlock();
        Random r = new Random();
        int i = r.nextInt(50);
        if (i == 34) {
            b.getWorld().dropItem(b.getLocation(), new ItemStack(Material.APPLE));
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Block b = e.getBlock();
        ArrayList<Material> leaves = new ArrayList<>(Arrays.asList(Material.ACACIA_LEAVES, Material.BIRCH_LEAVES, Material.DARK_OAK_LEAVES,
                Material.JUNGLE_LEAVES, Material.OAK_LEAVES, Material.SPRUCE_LEAVES));
        if (b.getType() == Material.GRAVEL) {
            Random r = new Random();
            int i = r.nextInt(100);
            if (i < 30) {
                b.getWorld().dropItem(b.getLocation(), new ItemStack(Material.FLINT, 1));
                e.setDropItems(false);
            }
        } else if (leaves.contains(b.getType())) {
            Random r = new Random();
            int i = r.nextInt(50);
            if (i == 34) {
                b.getWorld().dropItem(b.getLocation(), new ItemStack(Material.APPLE));
            }
        }
    }
}
