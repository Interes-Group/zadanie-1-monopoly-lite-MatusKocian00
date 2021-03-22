package sk.stuba.fei.uim.oop;


import java.util.ArrayList;

public class Monopoly {

    private int NUM_OF_PLAYERS = 0;
    private int STARTING_MONEY = 750;
    private int START_MONEY = 200;
    private int doubleCount = 0;
    private int JAIL_FINE = 100;

    private Players players = new Players();
    private Player currPlayer;
    private Dice dice = new Dice();
    private Board board = new Board(dice);
    private ChanceDeck chanceDeck = new ChanceDeck();
    private ArrayList<String> names = new ArrayList<>();
    private int cmd;
    private int commandId;
    private boolean rollDone;
    private boolean turnFinished;
    private boolean gameOver;
    private boolean onlyOneNotBankrupt = false;



    public Monopoly() {
        game();
    }

    public void setNumOfPlayers() {
        while (true) {
            NUM_OF_PLAYERS = ZKlavesnice.readInt("Enter number of players. (2-6)");
            if (NUM_OF_PLAYERS >= 2 && NUM_OF_PLAYERS <= 6) {
                break;

            }
        }
    }


    public void inputName() {
        for (int i = 0; i < NUM_OF_PLAYERS; i++) {
            System.out.print("Enter name for " + (i + 1));
            names.add(ZKlavesnice.readString(" player."));
        }
    }

    public void inputNames() {
        inputName();
        for (int i = 0; i < NUM_OF_PLAYERS; i++) {
            players.add(new Player(names.get(i)));
        }
    }

    public int inputCommand(Player player) {
        boolean inputValid = false;
        do {
            cmd =  ZKlavesnice.readInt("Type your command ! [0] for help");
            switch (cmd) {
                case 0:
                    System.out.println("1 - DONE \n2 - ROLL\n3 - BUY\n4 - USE CARD\n5 - SHOW BALANCE\n6 - SHOW PROPERTIES\n7 - BANKRUPT/QUIT");
                    inputValid = true;
                    break;
                case 1 :
                    commandId = 1; // done
                    inputValid = true;
                    break;
                case 2:
                    commandId = 2; //roll
                    inputValid = true;
                    break;
                case 3:
                    commandId = 3; // buy
                    inputValid = true;
                    break;
                case 4:
                    commandId = 4; // card
                    inputValid = true;
                    break;
                case 5:
                    commandId = 5; // show balance
                    inputValid = true;
                    break;
                case 6:
                    commandId = 6; // show prop
                    inputValid = true;
                    break;
                case 7:
                    commandId = 7; //quit/bankrupt
                    inputValid = true;
                    break;
            }
        } while (!inputValid);
        return commandId;
    }


    private void rollCommand () {
        if (!rollDone) {
            if (currPlayer.getBalance() >= 0) {
                dice.diceRoll();
                displayDice(currPlayer, dice);
                if (!currPlayer.isInJail()) {
                    currPlayer.move(dice.numDice());
                    checkPassedGo();
                    squareArrival();
                    if (dice.numDice()==6) {
                        System.out.println("You go once again !");
                        doubleCount++;
                        if (doubleCount == 3) {
                            displayThreeDoubles(currPlayer);
                            currPlayer.goToJail();
                            rollDone = true;
                        }
                    } else {
                        rollDone = true;
                    }
                } else {
                    if (dice.numDice()==6) {
                        currPlayer.freeFromJail();
                        displayFreeFromJail(currPlayer);
                    } else {
                        currPlayer.failedJailAttempts();
                        if (currPlayer.exceededExitJailAttempts()) {
                            currPlayer.doTransaction(-JAIL_FINE);
                            displayJailFine(currPlayer);
                            currPlayer.freeFromJail();
                            displayFreeFromJail(currPlayer);
                        }
                    }
                    currPlayer.move(dice.numDice());
                    rollDone = true;
                }
            } else {
                displayError("Negative Balance");
            }
        } else {
            displayError("You cant roll again, press DONE");
        }

    }

    private void buyCommand () {
        if (board.getSquare(currPlayer.getPosition()) instanceof Property) {
            Property property = (Property) board.getSquare(currPlayer.getPosition());
            if (!property.isOwned()) {
                if (currPlayer.getBalance() >= property.getPrice()) {
                    currPlayer.doTransaction(-property.getPrice());
                    displayBankTransaction(currPlayer);
                    currPlayer.addProperty(property);
                    displayLatestProperty(currPlayer);
                } else {
                    displayError("Not Enough money");
                }
            } else {
                displayError("Property is owned");
            }
        } else {
            displayError("This is not a property");
        }
    }

    private void doneCommand () {
        if (rollDone) {
            if (currPlayer.getBalance() >= 0) {
                turnFinished = true;
            } else {
                displayError("negative balance");
            }
        } else {
            displayError("You didnt roll");
        }
    }

    public void displayProperty (Player player) {
        ArrayList<Property> propertyList = player.getProperties();
        if (propertyList.size() == 0) {
            System.out.println(player.getName() + " owns no property.");
        } else {
            System.out.println(player.getName() + " owns the following property...");
            for (Property p : propertyList) {
                System.out.println(p.getName());
                }
            }
        }

    public void nextPlayer () {
        currPlayer = players.getNextPlayer(currPlayer);
        System.out.println("\n\n");
    }

    public void decideWinner () {
        if (onlyOneNotBankrupt) {
            displayWinner(currPlayer);
        } else {
            ArrayList<Player> playersWithMostAssets = new ArrayList<>();
            int mostAssets = players.get(0).getAssets();
            for (Player player : players.get()) {
                displayAssets(player);
                if (player.getAssets() > mostAssets) {
                    playersWithMostAssets.clear();
                    playersWithMostAssets.add(player);
                } else if (player.getAssets() == mostAssets) {
                    playersWithMostAssets.add(player);
                }
            }
            if (playersWithMostAssets.size() == 1) {
                displayWinner(playersWithMostAssets.get(0));
            } else {
                displayDraw(playersWithMostAssets);
            }
        }
    }

    public boolean isGameOver () {
        return gameOver;
    }

    public void giveStartMoney() {
        for (Player p : players.get()) {
            p.doTransaction(STARTING_MONEY);
        }
    }

    public void startDecide() {
        Players inPlayers = new Players(players), selectedPlayers = new Players();
        boolean tie = false;
        do {
            int highestTotal = 0;
            for (Player p : inPlayers.get()) {
                dice.diceRoll();
                displayDice(p, dice);
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
        } while (tie);

        currPlayer = selectedPlayers.get(0);
        displayRollWinner(currPlayer);
    }

    private void squareArrival() {
        displaySquare(currPlayer);
        Square square = board.getSquare(currPlayer.getPosition());
        if (square instanceof Property && ((Property) square).isOwned() && !((Property) square).getOwner().equals(currPlayer)) {
            int rent = ((Property) square).getRent();
            Player owner = ((Property) square).getOwner();
            currPlayer.doTransaction(-rent);
            owner.doTransaction(+rent);
            displayTransaction(currPlayer, owner);
        } else if (square instanceof Chance) {
            Card card = chanceDeck.get();
            displayCard(card);
            cardAction(card);
            chanceDeck.addCard(card);
        } else if (square instanceof Tax) {
            int amount = ((Tax) square).getAmount();
            currPlayer.doTransaction(-amount);
            displayBankTransaction(currPlayer);
        } else if (square instanceof GoToJail) {
            currPlayer.goToJail();
            rollDone = true;
        }

    }


    private void cardAction (Card card) {
        switch (card.getActionID()) {
            case ChanceDeck.GO_FORWARD :
                currPlayer.move(card.getSpaces());
                checkPassedGo();
                squareArrival();
                break;
            case ChanceDeck.GO_BACKWARD:
                currPlayer.move(-card.getSpaces());
                squareArrival();
                break;
            case ChanceDeck.GO_TO_JAIL:
                currPlayer.goToJail();
                rollDone = true;
                break;
            case ChanceDeck.GET_OF_JAIL:
                currPlayer.addCard(card);
                break;
            case ChanceDeck.PAY_FINE:
                currPlayer.doTransaction(-card.getAmount());
                displayBankTransaction(currPlayer);
                break;
            case ChanceDeck.GET_MONEY:
                currPlayer.doTransaction(+card.getAmount());
                displayBankTransaction(currPlayer);
                break;
        }
    }
    private void checkPassedGo () {
        if (currPlayer.getPassed()) {
            currPlayer.doTransaction(+START_MONEY);
            displayPassed(currPlayer);
            displayBankTransaction(currPlayer);
        }
    }

    private void bankruptCommand () {
        displayBankrupt(currPlayer);
        Player tempPlayer = players.getNextPlayer(currPlayer);
        players.remove(currPlayer);
        currPlayer = tempPlayer;
        if (players.numOfPlayers()==1) {
            gameOver = true;
            onlyOneNotBankrupt = true;
        }
    }

    private void cardCommand () {
        if (currPlayer.isInJail()) {
            if (currPlayer.getOutOfJailCard()) {
                Card card = currPlayer.getCard();
                chanceDeck.addCard(card);
                currPlayer.freeFromJail();
                displayFreeFromJail(currPlayer);
            } else {
                displayError("You dont have get out of jail card");
            }
        } else {
            displayError("You are not in prison");
        }
    }




    public void processTurn(){
        turnFinished = false;
        rollDone = false;
        do{
            System.out.println(currPlayer.getName()+ "'s turn");
            switch(inputCommand(currPlayer)){
                case 1:
                    doneCommand();
                    break;
                case 2:
                    rollCommand();
                    break;
                case 3:
                    buyCommand();
                    break;
                case 4:
                    cardCommand();
                    break;
                case 5:
                    displayBalance(currPlayer);
                    break;
                case 6:
                    displayProperty(currPlayer);
                case 7:
                    bankruptCommand();
                    break;

            }
        } while(!turnFinished);

    }



// Display Methonds //

    public void displayBankTransaction(Player player) {
        if (player.getTransaction() > 0) {
        System.out.println(player.getName() + " receives " + player.getTransaction() + "from the bank.");
        }
        else if (player.getTransaction() == 0) {
        System.out.println("No money is transfered");
        }
        else {
        System.out.println(player.getName() + " pays " + (-player.getTransaction() + " to the bank."));

    }
}

    public void displayTransaction(Player fromPlayer, Player toPlayer) {
        System.out.println(fromPlayer.getName() + " pays " + toPlayer.getTransaction() + " to " + toPlayer.getName());
    }

    public void displayDice(Player player, Dice dice) {
        System.out.println(player.getName() + " rolls " + dice.numDice());
    }

    public void displayBalance(Player player) {
        System.out.println(player.getName() + "'s balance is " + player.getBalance());
    }

    public void displayDraw (ArrayList<Player> players) {
        System.out.println("The following players drew the game " + players + ".");
    }
    public void displayDraw() {
        System.out.println("Draw");
    }

    public void displayRollWinner(Player player) {
        System.out.println(player.getName() + " wins the roll and starts first !");
    }

    public void displayGameOver() {
        System.out.println("GAME OVER");
    }

    public void displaySquare(Player player) {
        Square square = board.getSquare(player.getPosition());
        System.out.println(player.getName() + " arrives at " + square.getName()+" (" + player.getPosition()+"/24)") ;
        if (square instanceof Property) {
            Property property = (Property) square;
            System.out.println("Price - [" +property.getPrice() + "]  Rent - [" + property.getRent()+"]");
            if (property.isOwned()) {
                System.out.println("The property is owned by " + property.getOwnerName() + ".");
            } else {
                System.out.println("The property is not owned.");
            }
        }
    }
    public void displayCard (Card card) {
        System.out.println("The card says: " + card.toString());
    }

    public void displayPassed(Player player) {
        System.out.print(player.getName() + " passed Start.");
    }

    public void displayFreeFromJail (Player player) {
        System.out.println(player.getName() + " is free from jail.");

    }

    public void displayJailFine (Player player) {
        System.out.println(player.getName() + " pays fine of " + JAIL_FINE + " to leave jail.");
    }
    public void displayError (String string) {
        System.out.println(string);
    }

    public void displayLatestProperty (Player player) {
        System.out.println(player.getName() + " bought " + player.getLatestProperty());
    }

    public void displayWinner (Player player) {
        System.out.println("The winner is " + player.getName() + ".");
    }

    public void displayAssets (Player player) {
        System.out.println(player.getName() + " has assets of " + player.getAssets());
    }
    public void displayBankrupt (Player player) {
        System.out.println(player.getName() + " is bankrupt.");
    }

    public void displayThreeDoubles (Player player) {
        System.out.println(player.getName() + " rolled three sixes. Go to Jail.");
        return;
    }

    public void game(){
        System.out.println("Welcome in MONOPOLY lite");
        System.out.println("When you finish round, press ROLL DONE command to proceed");
        setNumOfPlayers();
        inputNames();
        giveStartMoney();
        startDecide();
        do{
            processTurn();
            if(!isGameOver()){
                nextPlayer();
            }
        } while(!isGameOver());
        decideWinner();
        displayGameOver();
    }

}