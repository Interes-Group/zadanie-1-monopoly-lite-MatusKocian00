package sk.stuba.fei.uim.oop;

public class Board {

    public final int SQUARES_NUM = 20;

    public final int POS_START = 0;
    public final int POS_JAIL = 5;

    private Square[] squares = new Square[SQUARES_NUM];


     Board(Dice dice){
        squares[0] = new Square("GO");
        squares[1] = new Property("Homies", 50);
        squares[2] = new Chance();
        squares[3] = new Property("Western part", 100);
        squares[4] = new Property("LalaLand",100);
        squares[5] = new Property("Diary Land",150);
        squares[6] = new Square("JAIl");
        squares[7] = new Chance();
        squares[8] = new Property("Kellys",180);
        squares[9] = new Property("Shelerton", 250);
        squares[10] = new Property("Moonlight",270);
        squares[11] = new Property("Kikisway",300);
        squares[12] = new Tax("TAX",200);
        squares[13] = new Property("Uberhigh", 300);
        squares[14] = new Property("Luniezis",340);
        squares[15] = new Chance();
        squares[16] = new Property("Peper House",400);
        squares[17] = new Property("Woodston",450);
        squares[18] = new GoToJail();
        squares[19] = new Property("Barbie World",500);
        squares[20] = new Property("Mainstorm",500);
        squares[21] = new Chance();
        squares[22] = new Property("FunnyLand",600);
        squares[23] = new Property("Marvel Hard", 750);

     }
}
