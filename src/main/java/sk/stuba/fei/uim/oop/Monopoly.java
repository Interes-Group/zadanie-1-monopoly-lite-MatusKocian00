package sk.stuba.fei.uim.oop;


import java.util.ArrayList;

public class Monopoly{

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


    public void displayBankTransaction(Player player){
        if (player.getTransaction() > 0){
            System.out.println(player.getName() + " receives " + player.getTransaction() + "from the bank.");
        }
        else if (player.getTransaction() == 0){
            System.out.println("No money is transfered");

        }
        else {
            System.out.println(player.getName() + " pays " + (-player.getTransaction() +" to the bank."));

        }
    }

    public void displayTransaction(Player fromPlayer, Player toPlayer){
        System.out.println(fromPlayer.getName() + " pays " + toPlayer.getTransaction() + " to " + toPlayer.getName() );
    }

    public void displayDice(Player player, Dice dice){
        System.out.println(player.getName() + " rolls " + dice.numDice());
    }

    public void displayBalance(Player player){
        System.out.println(player.getName() + "'s balance is " + player.getBalance());
    }
    public void displayDraw(){
        System.out.println("Draw");
    }
    public void displayRollWinner(Player player){
        System.out.println(player.getName() + " wins the roll and starts first !");
    }
    public void displayGameOver(){
        System.out.println("GAME OVER");
    }


    public void inputNumOfPlayers(){
        NUM_OF_PLAYERS = ZKlavesnice.readInt("Enter number of players ( =< 6)");
    }

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
                displayDice(p,dice);
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
                displayDraw();
                inPlayers = new Players(selectedPlayers);
                selectedPlayers.clear();
            }
        }while (tie);

        currPlayer = selectedPlayers.get(0);
        displayRollWinner(currPlayer);

    }






}
