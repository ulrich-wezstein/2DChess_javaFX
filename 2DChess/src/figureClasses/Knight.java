package figureClasses;

import chessBoard.Field;
import java.util.ArrayList;

public class Knight extends Figur {

    public Knight(boolean color, int startpos_x, int startpos_y, int checkpos_x, int checkpos_y) {
        super(color, startpos_x, startpos_y, checkpos_x, checkpos_y);
    }

    {
        this.name = "S";
        this.counter = 0;
        String white = "/pictures/knight_white.png";
        String black = "/pictures/knight_black.png";
        this.figur_icon_url = setImageURL(this.color, white, black);
    }

    public ArrayList<Field> getPossibleHitFields(ArrayList<ArrayList<Field>> Game_Field) {
        ArrayList<Field> possible_moves = getPossibleFields(Game_Field);
        return possible_moves;
    }

    public ArrayList<Field> getPossibleFields(ArrayList<ArrayList<Field>> Game_Field) {
        ArrayList<Field> possible_moves = new ArrayList<>();

        int x = this.startposition_x;
        int y = this.startposition_y;

        int min_x = calc_min_x(this.startposition_x);
        int max_x = calc_max_x(this.startposition_x);

        int min_y = calc_min_y(this.startposition_y);
        int max_y = calc_max_y(this.startposition_y);

        for (int x_axis = min_x; x_axis <= max_x; x_axis++) {
            for (int y_axis = min_y; y_axis <= max_y; y_axis++) {
                if (x_axis == x & y_axis == y) {
                } else {
                    if (Math.abs(x_axis - x) + Math.abs(y_axis - y) == 3) {
                        if (Game_Field.get(y_axis).get(x_axis).field_figur == null) {
                            possible_moves.add(new Field(x_axis, y_axis));
                        }
                        if (Game_Field.get(y_axis).get(x_axis).field_figur != null) {
                            if (Game_Field.get(y_axis).get(x_axis).field_figur.color != this.color) {
                                possible_moves.add(new Field(x_axis, y_axis));
                            }
                        }
                    }
                }
            }
        }
        return possible_moves;
    }

    private int calc_min_x(int startx) {
        int min_x;
        if (startx == 1) {
            min_x = startx;
        } else if (startx == 2) {
            min_x = startx - 1;
        } else {
            min_x = startx - 2;
        }
        return min_x;
    }

    private int calc_max_x(int startx) {
        int max_x;
        if (startx == 8) {
            max_x = startx;
        } else if (startx == 7) {
            max_x = startx + 1;
        } else {
            max_x = startx + 2;
        }
        return max_x;
    }

    private int calc_min_y(int starty) {
        int min_y;
        if (starty == 1) {
            min_y = starty;
        } else if (starty == 2) {
            min_y = starty - 1;
        } else {
            min_y = starty - 2;
        }
        return min_y;
    }

    private int calc_max_y(int starty) {
        int max_y;
        if (starty == 8) {
            max_y = starty;
        } else if (starty == 7) {
            max_y = starty + 1;
        } else {
            max_y = starty + 2;
        }
        return max_y;
    }
}
