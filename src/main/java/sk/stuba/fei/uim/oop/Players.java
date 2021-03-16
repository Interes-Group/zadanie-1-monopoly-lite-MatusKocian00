package sk.stuba.fei.uim.oop;

import java.util.ArrayList;

public class Players {
    public final int MAX_PLAYERS = 4;
    private ArrayList<Player> players = new ArrayList<>();
    private boolean playerWasRemoved = false;

    Players (){
    }

    Players (Players players){
        for (Player p: players.get()){
            this.players.add(p);
        }
    }

    public void add(Player player){
        players.add(player);
    }

    public ArrayList<Player> get(){
        return players;
    }
}
