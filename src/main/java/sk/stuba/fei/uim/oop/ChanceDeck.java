package sk.stuba.fei.uim.oop;

public class ChanceDeck extends ChanceCards {


    ChanceDeck(){
        // FORWARD
        cards.add(new Card ("ADVANCE BY 2", GO_FORWARD,2));
        cards.add(new Card ("ADVANCE BY 3", GO_FORWARD,3));

        // BACK
        cards.add(new Card ("BACK BY 2",GO_BACKWARD,2));
        cards.add(new Card ("BACK BY 3",GO_BACKWARD,3));

        // JAIL
        cards.add(new Card ("JEALOUS GIRLFRIEND REPORTED YOU, GO TO JAIL " ,GO_TO_JAIL));
        cards.add(new Card ("SUGAR DADDY PAID BAIL, U ARE FREE", GET_OF_JAIL));

        // GAIN
        cards.add(new Card ("U FOUND 200$",GET_MONEY,200));
        cards.add(new Card ("SUGAR DADDY GAVE U 500$",GET_MONEY,500));

        //PAY
        cards.add(new Card ("KID STOLE U 500$",PAY_FINE,500));
        cards.add(new Card ("WIFE BORROWED YOUR CREDIT CARD FOR HER NEW 200$ HANDBAG",PAY_FINE,200));

        shuffle();

    }
}
