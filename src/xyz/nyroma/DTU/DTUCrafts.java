package xyz.nyroma.DTU;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class DTUCrafts {

    public ShapedRecipe[] build(JavaPlugin plugin){

        ShapedRecipe totemOU = new ShapedRecipe(getNamespacedkey(plugin, "totem"), new ItemStack(Material.TOTEM_OF_UNDYING));
        totemOU.shape("QWQ","IEI",".Q.");
        totemOU.setIngredient('Q', Material.QUARTZ_BLOCK);
        totemOU.setIngredient('W', Material.WITHER_SKELETON_SKULL);
        totemOU.setIngredient('I', Material.IRON_BLOCK);
        totemOU.setIngredient('E', Material.EMERALD);

        ShapedRecipe xpBottls = new ShapedRecipe(getNamespacedkey(plugin, "xp"), new ItemStack(Material.EXPERIENCE_BOTTLE,8));
        xpBottls.shape("GCG","GIG","GGG");
        xpBottls.setIngredient('G', Material.GLASS);
        xpBottls.setIngredient('I', Material.IRON_BLOCK);
        xpBottls.setIngredient('C', Material.COAL_BLOCK);

        ShapedRecipe notchApple = new ShapedRecipe(getNamespacedkey(plugin, "apple"), new ItemStack(Material.ENCHANTED_GOLDEN_APPLE));
        notchApple.shape("GDG","GAG","GGG");
        notchApple.setIngredient('G', Material.GOLD_BLOCK);
        notchApple.setIngredient('A', Material.APPLE);
        notchApple.setIngredient('D', Material.DIAMOND_BLOCK);

        ShapedRecipe diam = new ShapedRecipe(getNamespacedkey(plugin, "diamant"), new ItemStack(Material.DIAMOND));
        diam.shape("CCC","CCC","CCC");
        diam.setIngredient('C', Material.COAL_BLOCK);

        ShapedRecipe lapis = new ShapedRecipe(getNamespacedkey(plugin, "lapis"), new ItemStack(Material.LAPIS_BLOCK));
        lapis.shape(".W.","WIW",".W.");
        lapis.setIngredient('W', Material.WATER_BUCKET);
        lapis.setIngredient('I', Material.IRON_INGOT);

        return new ShapedRecipe[]{totemOU, xpBottls, notchApple, diam, lapis};
    }

    private static NamespacedKey getNamespacedkey(JavaPlugin plugin, String name){
        return new NamespacedKey(plugin, name);
    }

}
