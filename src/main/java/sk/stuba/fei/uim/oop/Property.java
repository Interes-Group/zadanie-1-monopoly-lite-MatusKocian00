package sk.stuba.fei.uim.oop;

public class Property extends Square{

    private boolean isOwned;
    private int price;
    private Player owner;
    private int rent;


    Property(String name, int price, int rent) {
        super(name);
        this.price = price;
        this.rent = rent;
        isOwned = false;
        owner = null;

    }

    public boolean isOwned() {
        return isOwned;
    }

    public int getRent () { // this method is overloaded by the subclasses
        return rent;
    }

    public int getPrice() {
        return price;
    }


    public void setOwner(Player player){
        owner = player;
        isOwned = true;
    }

    public Player getOwner () {return owner;}
    public String getOwnerName() {
        return owner.getName();
    }

    public void releaseOwnership(){
        isOwned = false;
        owner = null;
    }
}

