package sk.stuba.fei.uim.oop;


import java.util.ArrayList;

public class Monopoly {

    private int NUM_OF_PLAYERS = 0;
    private int STARTING_MONEY = 500;
    private int START_MONEY = 200;
    private Players players = new Players();
    private Player currPlayer;
    private Dice dice = new Dice();
    private Board board = new Board(dice);
    private ChanceDeck chanceDeck = new ChanceDeck();
    private ArrayList<String> names = new ArrayList<>();
    private int[] cmds = {1,2,3,4,5,6};
    private int cmd;
    private String string;
    private int commandId;
    private boolean done;
    private boolean rollDone;
    private boolean turnFinished;
    private boolean gameOver;
    private boolean onlyOneNotBankrupt = false;


    public void setNumOfPlayers() {
        NUM_OF_PLAYERS = ZKlavesnice.readInt("Enter num of players");
    }

    public int getNumOfPlayers() {
        return NUM_OF_PLAYERS;
    }


    public void inputName() {
        for (int i = 0; i < NUM_OF_PLAYERS; i++) {
            System.out.print("Enter name for " + (i + 1));
            names.add(ZKlavesnice.readString(" player"));
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
                    System.out.println("1 - BUY COMMAND\n2 - ROLL DONE\n3 - ROLL DICE\n4 - SHOW MY PROPERTIES\n5 - SHOW BALANCE\n6 - BANKRUPT");
                    inputValid = true;
                    break;
                case 1 :
                    commandId = 1; // buy
                    inputValid = true;
                    break;
                case 2:
                    commandId = 2; //pass
                    inputValid = true;
                    break;
                case 3:
                    commandId = 3; // useCard
                    inputValid = true;
                    break;
                case 4:
                    commandId = 4; // show properties
                    inputValid = true;
                    break;
                case 5:
                    commandId = 5; // show balance
                    inputValid = true;
                    break;
                case 6:
                    commandId = 6; // bankrupt
                    inputValid = true;
                    break;
            }
        } while (!inputValid);
        return commandId;
    }


    private void rollCommand() {
        if (!rollDone) {
            if (currPlayer.getBalance() >= 0) {
                dice.diceRoll();
                displayDice(currPlayer, dice);
                if (!currPlayer.isInJail()) {
                    currPlayer.move(dice.numDice());
                    checkPassedGo();
                    squareArrival();
                    rollDone = true;
                }
            }
            else{
                currPlayer.failedJailAttempts();
                if(currPlayer.exceededExitJailAttempts()){
                    currPlayer.freeFromJail();
                    displayFreeFromJail(currPlayer);
                }
                currPlayer.move(dice.numDice());
                rollDone = true;
            }
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
                displayError("PROP_IS_OWNED");
            }
        } else {
            displayError("This is not a property");
        }
        return;
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
                System.out.print(p.getName() + " ");
                }
            }
        }

    public void nextPlayer () {
        currPlayer = players.getNextPlayer(currPlayer);
    }

    public void decideWinner () {
        if (onlyOneNotBankrupt) {
            displayWinner(currPlayer);
        } else {
            ArrayList<Player> playersWithMostAssets = new ArrayList<Player>();
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

    public void checkPassedGo() {
        if (currPlayer.getPassed()) {
            currPlayer.doTransaction(+START_MONEY);
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
    private void checkPassed () {
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




    public void processTurn(){
        turnFinished = false;
        rollDone = false;
        do{
            System.out.println(currPlayer.getName()+ "'s turn");
            switch(inputCommand(currPlayer)){
                case 1:
                    buyCommand();
                    break;
                case 2:
                    doneCommand();
                    break;
                case 3:
                    rollCommand();
                    break;
                case 4:
                    displayProperty(currPlayer);
                    break;
                case 5:
                    displayBalance(currPlayer);
                    break;
                case 6:
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
        return;
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
        System.out.println(player.getName() + " arrives at " + square.getName() ) ;
        if (square instanceof Property) {
            Property property = (Property) square;
            System.out.println("Price - [" +property.getPrice() + "]  Rent - [" + property.getRent()+"]");
            if (property.isOwned()) {
                System.out.println("The property is owned by " + property.getOwner() + ".");
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

}