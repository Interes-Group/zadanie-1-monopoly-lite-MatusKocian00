package sk.stuba.fei.uim.oop;

public class Property extends Square{

    private boolean isOwned;
    private int price;
    private Player owner;


    Property(String name, int price) {
        super(name);
        this.price = price;
        isOwned = false;
        owner = null;
    }

    public boolean isOwned() {
        return isOwned;
    }

    public int getPrice() {
        return price;
    }


    public void setOwner(Player player){
        owner = player;
        isOwned = true;
    }

    public Player getOwner() {
        return owner;
    }

    public void releaseOwnership(){
        isOwned = false;
        owner = null;
    }
}

