package sk.stuba.fei.uim.oop;

public class Card {

    private String message;
    private int actionID;
    private int parameter;

    Card(){
        message = "";
    }

    Card (String message, int actionID){
        this.message = message;
        this.actionID = actionID;
    }
    Card (String message, int actionID, int parameter){
        this.message = message;
        this.actionID = actionID;
        this.parameter = parameter;
    }

    public int getActionID() {
        return actionID;
    }

    public int getAmount(){
        return parameter;
    }

    public String toString(){
        return message;
    }

}
