package xyz.nyroma.ManagersCommuns;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeamManager {
    List<Player> team1 = new ArrayList<>();
    List<Player> team2 = new ArrayList<>();
    List<Player> team3 = new ArrayList<>();
    List<Player> team4 = new ArrayList<>();
    List<Player> team5 = new ArrayList<>();

    List[] teams = new List[]{team1, team2, team3, team4, team5};

    public TeamManager(){

    }
    public boolean add(Player p, int team){
        if(team > 0 && team < 6) {
            teams[team - 1].add(p);
            return true;
        } else {
            return false;
        }
    }
    public boolean remove(Player p, int team){
        if(team > 0 && team < 5){
            List<Player> t = teams[team - 1];
            if(t.contains(p)){
                t.remove(p);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    public void showTeam(Player p) {
        int i = 1;
        try {
            for (List<Player> l : teams) {
                p.sendMessage(ChatColor.BLUE + "Team " + i + " :");
                for (Player play : l) {
                    p.sendMessage(play.getName());
                }
                i++;
            }
        } catch(NullPointerException e){
            p.sendMessage(ChatColor.DARK_RED + "oof.");
        }
    }
    public List[] getTeams(){
        return this.teams;
    }
    public int getTeamSize(int team){
        return teams[team].size();
    }


}
