package chessBoard;

import figureClasses.Figur;
import java.util.ArrayList;

public class Field {

    public Figur field_figur;
    private final String name = "  ";
    public int pos_x;
    public int pos_y;

    public Field(int x, int y) {
        this.pos_x = x;
        this.pos_y = y;
    }

    // Methode um Spielfigur dem Feld zu �bergeben
    public Figur deliver(Figur new_figur, int x, int y) {
        field_figur = new_figur;
        field_figur.startposition_x = x;
        field_figur.startposition_y = y;
        return field_figur;
    }

    // delivered mit leerer figur
    public void deliver_empty() {
        field_figur = null;
    }

    //falls feldfigur leer ist empty.png ausgeben, ansonsten die respektive
    //Spielfigur ausgeben
    public String getImageURL() {
        if (this.field_figur != null) {
            return this.field_figur.figur_icon_url;
        } else {           
            return "/pictures/empty.png";
        }
    }
    // Methode zur Namensgebung im Spielfeld
    // Falls field ein Objekt hat, dann Objektnamen ermitteln und �bergeben,
    // ansonsten " "

    public String getName() {
        return ((field_figur != null) ? field_figur.getName() : this.name);
    }

    public boolean getColor(boolean playercolor) {
        boolean newcolor;
        if (playercolor) {
            newcolor = false;
        } else {
            newcolor = true;
        }

        return ((field_figur != null) ? field_figur.getColor() : newcolor);
    }

    public ArrayList<Field> getPossibleMoves(ArrayList<ArrayList<Field>> Game_Field) {
        return field_figur.getPossibleFields(Game_Field);
    }

    @Override
    public String toString() {
        return "Field [pos_x=" + pos_x + ", pos_y=" + pos_y + "]";
    }

    public int getCounter() {
        if (field_figur != null) {
            return field_figur.counter;
        } else {
            return 0;
        }
    }

    public void increment_counter(int newcounter) {
        if (field_figur != null) {
            field_figur.increment_counter(newcounter);
        }
    }

}
