package sk.stuba.fei.uim.oop;

import java.util.ArrayList;

public class Player {

    private final int MAX_EXIT_JAIL_ATTEMPTS = 3;

    private String name;
    private int balance;
    private int position;
    private int amount;
    private boolean passed;

    private ArrayList<Property> properties = new ArrayList<>();

    private boolean inJail;
    private int exitJailAttempts;
    private ArrayList<Card> cards = new ArrayList<>();


    Player (String name){
        this.name = name;
        position = 0;
        balance = 0;
        passed = false;
        inJail = false;
    }


    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public boolean moreAttemptsFromJail(){
        return exitJailAttempts >= MAX_EXIT_JAIL_ATTEMPTS;
    }

    // Pohyb

    public void move(int squares){
        int check = (position + squares) / 24;
        position = (position + squares) % 24;

        passed = check == 1;
    }

    public void moveTo(int square){

        passed = square < position;
        position = square;
    }

    public boolean getPassed(){
        return passed;
    }

    //Vazanie

    public void goToJail(){

        inJail = true;
        exitJailAttempts = 0;
    }

    public void freeFromJail(){
        inJail = false;
    }

    public boolean isInJail(){
        return inJail;
    }

    public void failedJailAttempts(){
        exitJailAttempts++;
    }

    public boolean exceededExitJailAttempts(){
        return exitJailAttempts >= MAX_EXIT_JAIL_ATTEMPTS;
    }

    public boolean getOutOfJailCard(){
        boolean hasCard = false;
        if (cards.size() > 0){
            hasCard =cards.get(0).getActionID() == ChanceDeck.GET_OF_JAIL;
        }
        return hasCard;
    }
    public Card getCard(){
        Card card = cards.get(0);
        cards.remove(0);
        return card;
    }

    public void addCard(Card card) {
        cards.add(card);

    }
    // Transakcie

    public void doTransaction(int amount){
        balance = balance + amount;
        this.amount = amount;
    }

    public int getTransaction(){
        return amount;
    }

    public int getBalance(){
        return balance;
    }

}

