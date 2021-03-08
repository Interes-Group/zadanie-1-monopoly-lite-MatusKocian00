package sk.stuba.fei.uim.oop;

import java.util.*;
import java.util.ArrayList;

public class ChanceCards {

    public final int GO_FORWARD = 1;
    public final int GO_BACKWARD = 2;
    public final int PAY_FINE = 3;
    public final int GET_MONEY = 4;
    public final int GO_TO_JAIL = 5;
    public final int GET_OF_JAIL = 6;

    ArrayList<Card> cards = new ArrayList<>();

    public void addCard(Card card){
        cards.add(card);
    }

    public Card get(){
        Card card = cards.get(0);
        cards.remove(0);
        return card;
    }

    public Card put(Card card){
        cards.add(card);
        return card;

    }

    public void shuffle(){
        Collections.shuffle(cards);
    }


}
