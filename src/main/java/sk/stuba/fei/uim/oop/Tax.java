package sk.stuba.fei.uim.oop;

public class Tax extends Square {
    int amount;

    Tax(String name, int amount){
        super(name);
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}

