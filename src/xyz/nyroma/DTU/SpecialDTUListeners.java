package xyz.nyroma.DTU;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class SpecialDTUListeners implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        Player p = e.getPlayer();
        Block b = e.getBlock();
        List<Material> minerais = Arrays.asList(Material.GOLD_ORE, Material.IRON_ORE);
        if(minerais.contains(b.getType())){
            e.setDropItems(false);
            Material drop = null;
            if(b.getType() == Material.GOLD_ORE){
                drop = Material.GOLD_INGOT;
            } else if(b.getType() == Material.IRON_ORE){
                drop = Material.IRON_INGOT;
            }
            if(drop != null){
                p.getWorld().dropItemNaturally(b.getLocation(), new ItemStack(drop));
            }
        }
    }

    @EventHandler
    public void onFall(EntityDamageEvent e){
        if(e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            if(e.getCause() == EntityDamageEvent.DamageCause.FALL && p.hasPotionEffect(PotionEffectType.JUMP)){
                e.setCancelled(true);
            }
        }
    }

}
