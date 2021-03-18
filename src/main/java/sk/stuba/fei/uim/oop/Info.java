package sk.stuba.fei.uim.oop;

public class Info {

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
}
