package xyz.nyroma.ManagersCommuns;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TimerManager extends BukkitRunnable {
    private int seconds = 0;
    private int minutes = 0;
    private int hours = 0;
    private String min = "";
    private String sec = "";
    private World w;

    public TimerManager(World w){
        this.w = w;
    }

    @Override
    public void run() {
        seconds++;
        if(seconds == 60){
            seconds = 0;
            minutes++;
        }
        if(minutes == 60){
            minutes = 0;
            hours++;
        }

        if(minutes < 10){
            min = "0" + minutes;
        } else {
            min = String.valueOf(minutes);
        }
        if(seconds < 10){
            sec = "0" + seconds;
        } else {
            sec = String.valueOf(seconds);
        }
        String time = "0" + hours + " : " + min + " : " + sec;

        for(Player p : w.getPlayers()){
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(time));
        }
    }
}
