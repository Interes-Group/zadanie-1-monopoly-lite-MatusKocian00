package sk.stuba.fei.uim.oop;
import java.util.Random;

public class Dice {

    private int dice;

    public void diceRoll(){
        Random random = new Random();
        dice = random.nextInt(6)+ 1;
    }

    public int numDice(){return dice;}



}

