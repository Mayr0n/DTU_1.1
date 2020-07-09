package xyz.nyroma.Welhfkmtm;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.nyroma.DTU.DTUCrafts;

import java.util.Arrays;
import java.util.List;

public class CraftsManager {
    private String difficulty;
    private List<Material> materials;
    private List<ItemStack> items;
    private JavaPlugin plugin;

    public CraftsManager(String difficulty, List<Material> materials, List<ItemStack> items, JavaPlugin plugin){
        this.difficulty = difficulty;
        this.materials = materials;
        this.items = items;
        this.plugin = plugin;
    }

    private static NamespacedKey getNamespacedkey(JavaPlugin plugin, String name){
        return new NamespacedKey(plugin, name);
    }


    public ShapelessRecipe getFinalStarRecipe() {
        ItemStack is = new ItemStack(Material.NETHER_STAR);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Final Star");
        im.addEnchant(Enchantment.BINDING_CURSE, 5, true);
        im.setLore(Arrays.asList("La dernière étape avant la win !", "Déposez cette étoile au spawn pour gagner la partie !"));
        is.setItemMeta(im);

        ShapelessRecipe sr = new ShapelessRecipe(getNamespacedkey(this.plugin, "fstar"), is);
        for (Material mat : materials) {
            sr.addIngredient(mat);
        }
        return sr;
    }

    public ShapedRecipe[] build(){
        return new DTUCrafts().build(plugin);
    }

}
