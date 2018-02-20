package figureClasses;

import chessBoard.Field;
import java.util.ArrayList;
import javafx.scene.image.Image;
import pkg2dchess.GameController;

abstract public class Figur {
    // allgemeine Eigenschaften f�r alle Klassen

    public String name = "";
    public String figur_icon_url;
    public int counter = 0;
    public boolean color = true;
    public boolean checked = false;
    public int checkedposition_x = 0;
    public int checkedposition_y = 0;
    public int startposition_x = 0;
    public int startposition_y = 0;
    public boolean castling = false;
    public boolean enPassant = false;
    public int drawcounter = 0;

    public Figur(boolean color, int startpos_x, int startpos_y, int checkpos_x, int checkpos_y) {
        this.color = color;
        this.startposition_x = startpos_x;
        this.startposition_y = startpos_y;
        this.checkedposition_x = checkpos_x;
        this.checkedposition_y = checkpos_y;
    }

    // image wird für die jeweilige Farbe abgerufen
    public String setImageURL(boolean color, String whitefigure, String blackfigure) {
        String figurpicture;
        if (color) {
            figurpicture = whitefigure;
        } else {
            figurpicture = blackfigure;
        }
        return figurpicture;
    }

    public String getName() {
        return ((this.color) ? "w" : "s") + this.name;
    }

    public boolean getCastling() {
        return this.castling;
    }

    public void setCastling(boolean castling) {
        this.castling = castling;
    }

    public boolean getColor() {
        return this.color;
    }

    public void setDrawCounter() {
        this.drawcounter = GameController.getCounter();
    }

    abstract public ArrayList<Field> getPossibleFields(ArrayList<ArrayList<Field>> Game_Field);

    abstract public ArrayList<Field> getPossibleHitFields(ArrayList<ArrayList<Field>> Game_Field);

    public void increment_counter(int newcounter) {
        this.counter = newcounter;
    }

}
