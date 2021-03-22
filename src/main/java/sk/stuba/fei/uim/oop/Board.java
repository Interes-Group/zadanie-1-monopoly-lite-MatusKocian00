package sk.stuba.fei.uim.oop;

public class Board {

    public final int SQUARES_NUM = 24;

    public final int POS_START = 0;
    public final int POS_JAIL = 5;

    private Square[] squares = new Square[SQUARES_NUM];


     Board(Dice dice){
        squares[0] = new Square("GO");
        squares[1] = new Property("Homies", 50,4);
        squares[2] = new Chance();
        squares[3] = new Property("Western part", 100,6);
        squares[4] = new Property("LalaLand",100,6);
        squares[5] = new Property("Diary Land",150,8);
        squares[6] = new Square("JAIL/Just visit");
        squares[7] = new Chance();
        squares[8] = new Property("Kellys",180,14);
        squares[9] = new Property("Shelerton", 180,14);
        squares[10] = new Property("Moonlight",180,16);
        squares[11] = new Property("Kikisway",200,16);
        squares[12] = new Tax("TAX",200);
        squares[13] = new Property("Uberhigh", 240,18);
        squares[14] = new Property("Luniezis",260,20);
        squares[15] = new Chance();
        squares[16] = new Property("Peper House",260,20);
        squares[17] = new Property("Woodston",280,24);
        squares[18] = new GoToJail();
        squares[19] = new Property("Barbie World",300,26);
        squares[20] = new Property("Mainstorm",320,26);
        squares[21] = new Chance();
        squares[22] = new Property("FunnyLand",350,28);
        squares[23] = new Property("Marvel Hard", 400,50);

     }
    public Square getSquare (int index) {
        return squares[index];
    }

}
