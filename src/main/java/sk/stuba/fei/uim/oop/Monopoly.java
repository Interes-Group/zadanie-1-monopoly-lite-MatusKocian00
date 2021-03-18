package sk.stuba.fei.uim.oop;


import java.util.ArrayList;

public class Monopoly{

    private Info info = new Info();
    private int NUM_OF_PLAYERS = 0;
    private int STARTING_MONEY = 500;
    private int START_MONEY = 200;
    private Players players = new Players();
    private Player currPlayer;
    private Dice dice = new Dice();
    private Board board = new Board(dice);
    private ChanceDeck chanceDeck = new ChanceDeck();
    private ArrayList<String> names = new ArrayList<>();
    private String string;
    private boolean done;



    public void inputName() {
        for (int i = 0; i < NUM_OF_PLAYERS; i++) {
            System.out.print("Enter name for " + (i+1) );
            names.add(ZKlavesnice.readString(" player"));
        }
    }

    public void inputNames(){
        inputName();
        for (int i = 0; i < NUM_OF_PLAYERS; i++){
            players.add(new Player(names.get(i)));
        }
    }


    public void giveStartMoney(){
        for (Player p : players.get()){
            p.doTransaction(STARTING_MONEY);
        }
    }

    public void checkPassedGo(){
        if (currPlayer.getPassed()){
            currPlayer.doTransaction(+START_MONEY);
        }
    }


    public void startDecide(){
        Players inPlayers = new Players(players), selectedPlayers = new Players();
        boolean tie = false;
        do {
            int highestTotal = 0;
            for (Player p : inPlayers.get()) {
                dice.diceRoll();
                info.displayDice(p,dice);
                if (dice.numDice() > highestTotal) {
                    tie = false;
                    highestTotal = dice.numDice();
                    selectedPlayers.clear();
                    selectedPlayers.add(p);
                } else if (dice.numDice() == highestTotal) {
                    tie = true;
                    selectedPlayers.add(p);
                }

            }
            if (tie) {
                info.displayDraw();
                inPlayers = new Players(selectedPlayers);
                selectedPlayers.clear();
            }
        }while (tie);

        currPlayer = selectedPlayers.get(0);
        info.displayRollWinner(currPlayer);

    }






}
