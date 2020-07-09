package xyz.nyroma.main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.nyroma.DTU.DTUListeners;
import xyz.nyroma.DTU.DTUCrafts;
import xyz.nyroma.DTU.DTULauncher;
import xyz.nyroma.ManagersCommuns.BorderManager;
import xyz.nyroma.ManagersCommuns.TeamManager;
import xyz.nyroma.Welhfkmtm.WelhfLauncher;

import java.util.Arrays;

public class MainCommands implements CommandExecutor {
    private JavaPlugin plugin;
    private String[] commands = {"dstart","stuff","border","dteam","dlisteners", "pvp", "wstart", "items"};
    private DTULauncher DTULauncher;
    private WelhfLauncher welhfLauncherW;
    private Boolean launched = false;
    private TeamManager tm = new TeamManager();

    public MainCommands(JavaPlugin plugin){
        this.plugin = plugin;
    }

    public String[] getCommands(){
        return this.commands;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command c, String arg, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            String cmd = c.getName();

            if (cmd.equals(commands[0]) && p.getName().equals("Imperayser")) {
                try {
                    if(!this.launched) {
                        if (args[0].equals("SOLO") || args[0].equals("TEAM")) {
                            this.DTULauncher = new DTULauncher(tm, p.getWorld(), this.plugin);
                            this.launched = true;
                            if(args.length == 2){
                                this.DTULauncher.build(Arrays.asList(args[0], args[1]), p);
                            } else {
                                this.DTULauncher.build(Arrays.asList(args[0]), p);
                            }
                        } else {
                            p.sendMessage(ChatColor.DARK_RED + "Argument invalide ! Syntaxe : /dstart <SOLO:TEAM>");
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "Une partie est déjà lancée !");
                    }
                } catch(ArrayIndexOutOfBoundsException e){
                    p.sendMessage(ChatColor.DARK_RED + "Argument invalide ! Syntaxe : /dstart <SOLO:TEAM>");
                }
            }
            else if(cmd.equals(commands[1]) && p.isOp() && !this.launched){
                MainUtils.giveStuff(p);
            }
            else if(cmd.equals(commands[2]) && p.getName().equals("Imperayser")){
                new BorderManager(p.getWorld(), plugin).run();
            }
            else if(cmd.equals(commands[3]) && p.getName().equals("Imperayser")){
                try {
                    switch (args[0]) {
                        case "add":
                            int team = Integer.parseInt(args[2]);
                            Player play = MainUtils.getPlayerByName(p.getServer(), args[1]);
                            try {
                                if (tm.add(play, team)) {
                                    p.sendMessage(ChatColor.GREEN + "Joueur ajouté !");
                                } else {
                                    p.sendMessage(ChatColor.RED + "Le joueur n'a pas pu être ajouté.");
                                }
                            } catch(NullPointerException e){
                                p.sendMessage(ChatColor.RED + "Ce joueur n'est pas connecté.");
                            }
                            break;
                        case "remove":
                            Player pl = MainUtils.getPlayerByName(p.getServer(), args[1]);
                            try {
                                if (tm.remove(pl, Integer.parseInt(args[2]))) {
                                    p.sendMessage(ChatColor.GREEN + "Joueur retiré !");
                                } else {
                                    p.sendMessage(ChatColor.RED + "Le joueur n'a pas pu être retiré.");
                                }
                            } catch(NullPointerException e){
                                p.sendMessage(ChatColor.RED + "Ce joueur n'est pas connecté.");
                            }
                            break;
                        case "show":
                            tm.showTeam(p);
                            break;
                        default:
                            p.sendMessage(ChatColor.DARK_RED + "[Erreur] Syntaxe : /dteam <add:remove:show>");
                    }
                } catch(ArrayIndexOutOfBoundsException | NumberFormatException e){
                    p.sendMessage(ChatColor.DARK_RED + "[Erreur] Syntaxe : /dteam <add:remove:show>");
                }
            }
            else if(cmd.equals(commands[4]) && p.getName().equals("Imperayser")){
                try {
                    switch (args[0]) {
                        case "on":
                            Bukkit.getServer().getPluginManager().registerEvents(new DTUListeners(plugin), plugin);
                            for(ShapedRecipe recipe : new DTUCrafts().build(plugin)){
                                Bukkit.getServer().addRecipe(recipe);
                            }
                            p.sendMessage(ChatColor.GREEN + "Listeners du DTU ajoutés !");
                            break;
                        case "off":
                            p.sendMessage(ChatColor.GREEN + "Redémarrage...");
                            p.getServer().reload();
                            p.sendMessage(ChatColor.GREEN + "Redémarrage terminé.");
                            break;
                        default:
                            p.sendMessage(ChatColor.DARK_RED + "[Error] Syntaxe : /dtulisteners <on:off>");
                    }
                } catch(ArrayIndexOutOfBoundsException ignored){
                    p.sendMessage(ChatColor.DARK_RED + "[Error] Syntaxe : /dtulisteners <on:off>");
                }
            }
            else if(cmd.equals(commands[5]) && p.isOp()){
                switch (args[0]) {
                    case "on":
                        p.getWorld().setPVP(true);
                        Bukkit.broadcastMessage(ChatColor.GREEN + "Le pvp a été activé sur le serveur " + p.getWorld().getName());
                        return true;
                    case "off":
                        p.getWorld().setPVP(false);
                        Bukkit.broadcastMessage(ChatColor.GREEN + "Le pvp a été désactivé sur le serveur " + p.getWorld().getName());
                        return true;
                    default:
                        p.sendMessage(ChatColor.DARK_RED + "[Erreur] Syntaxe : /pvp <on:off>");
                        return false;
                }
            }
            else if(cmd.equals(commands[6]) && p.getName().equals("Imperayser")){
                try {
                    if(!this.launched) {
                        if ((args[0].equals("SOLO") || args[0].equals("TEAM")) && (args[1].equals("FLASH") || args[1].equals("NORMAL") || args[1].equals("HARD"))) {
                            this.welhfLauncherW = new WelhfLauncher(this.plugin, p.getWorld());
                            this.launched = true;
                            this.welhfLauncherW.build(Arrays.asList(args[0], args[1]), p);
                        } else {
                            p.sendMessage(ChatColor.DARK_RED + "Argument invalide ! Syntaxe : /wstart <SOLO:TEAM> <FLASH:NORMAL:HARD>");
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "Une partie est déjà lancée !");
                    }
                } catch(NullPointerException | ArrayIndexOutOfBoundsException e){
                    p.sendMessage(ChatColor.DARK_RED + "Argument invalide ! Syntaxe : /wstart <SOLO:TEAM> <FLASH:NORMAL:HARD>");
                }
            }
            else if(cmd.equals(commands[7])){
                if(!launched){
                    p.sendMessage(ChatColor.RED + "Aucune partie n'est lancée...");
                } else {
                    p.openInventory(welhfLauncherW.getInventory());
                }
            }
        }
        return false;
    }
}
