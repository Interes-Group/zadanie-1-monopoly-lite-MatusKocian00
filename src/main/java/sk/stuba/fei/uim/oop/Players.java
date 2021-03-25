package sk.stuba.fei.uim.oop;

import java.util.ArrayList;

public class Players {
    public final int MAX_PLAYERS = 6;
    private ArrayList<Player> players = new ArrayList<>();
    private boolean playerWasRemoved = false;

    Players() {
    }

    Players(Players players) {
        for (Player p : players.get()) {
            this.players.add(p);
        }
    }

    public void add(Player player) {
        players.add(player);
    }


    public ArrayList<Player> get() {
        return players;
    }

    public ArrayList<Player> getOtherPlayers(Player player) {
        ArrayList<Player> otherPlayers = new ArrayList<>(players);
        otherPlayers.remove(player);
        return otherPlayers;
    }

    public void remove(Player player) {
        for (Property p : player.getProperties()) {
            p.releaseOwnership();
        }
        players.remove(player);
        playerWasRemoved = true;
    }

    public Player getNextPlayer(Player currPlayer) {
        Player nextPlayer;
        if (playerWasRemoved) {
            nextPlayer = currPlayer;
            playerWasRemoved = false;
        } else
            {
            nextPlayer = get((players.indexOf(currPlayer) + 1) % players.size());
        }
        return nextPlayer;
    }


     public void clear(){
        players.clear();
     }

     public int indexOf(Player player){
        return players.indexOf(player);
    }

    public Player get(int playerId){
        return players.get(playerId);
    }
    public Player get(String name){
        Player player = null;
        for (Player p : players){
            if(p.equals(name)){
                player = p;
            }
        }
        return player;
    }
    public int numOfPlayers(){return players.size();}

}
