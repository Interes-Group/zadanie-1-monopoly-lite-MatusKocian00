package sk.stuba.fei.uim.oop;

public class Assignment1 {
    public static void main(String[] args) {
       Monopoly game = new Monopoly();

       game.setNumOfPlayers();
       game.inputNames();
       game.giveStartMoney();
       game.startDecide();
       do{
           game.processTurn();
           if(!game.isGameOver()){
               game.nextPlayer();
           }
       } while(!game.isGameOver());
       game.decideWinner();
       game.displayGameOver();
    }
}
