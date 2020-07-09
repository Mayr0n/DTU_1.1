package xyz.nyroma.main;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private MainCommands MainCommands = new MainCommands(this);

    public static void main(String[] args){

    }

    @Override
    public void onEnable(){
        System.out.println("Plugin DTU activé");
        for(String cmd : MainCommands.getCommands()){
            this.getCommand(cmd).setExecutor(MainCommands);
        }
        getServer().getWorld("DTU").setPVP(false);
    }
    @Override
    public void onDisable(){
        System.out.println("Plugin DTU désactivé");
    }

}
