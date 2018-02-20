package figureClasses;

import chessBoard.Field;
import java.util.ArrayList;

public class Tower extends Figur {

    public Tower(boolean color, int startpos_x, int startpos_y, int checkpos_x, int checkpos_y) {
        super(color, startpos_x, startpos_y, checkpos_x, checkpos_y);
    }

    {
        this.name = "T";
        this.counter = 0;
        String white = "/pictures/tower_white.png";
        String black = "/pictures/tower_black.png";
        this.figur_icon_url = setImageURL(this.color, white, black);
    }

    public ArrayList<Field> getPossibleHitFields(ArrayList<ArrayList<Field>> Game_Field) {
        ArrayList<Field> possible_moves = getPossibleFields(Game_Field);
        return possible_moves;
    }

    public ArrayList<Field> getPossibleFields(ArrayList<ArrayList<Field>> Game_Field) {
        ArrayList<Field> possible_moves = new ArrayList<>();

        check_verticalplus(Game_Field, possible_moves, this.startposition_x, this.startposition_y);
        check_verticalminus(Game_Field, possible_moves, this.startposition_x, this.startposition_y);
        check_horizontalplus(Game_Field, possible_moves, this.startposition_x, this.startposition_y);
        check_horizontalminus(Game_Field, possible_moves, this.startposition_x, this.startposition_y);

        return possible_moves;
    }

    private void check_verticalplus(ArrayList<ArrayList<Field>> Game_Field, ArrayList<Field> possible_moves, int x,
            int y) {
        int max_y = 8;

        int x_axis = x;
        int y_axis = y;

        boolean found_figure = false;
        // solange bis figur gefunden ist oder spielfeldende erreicht wurde
        while (!found_figure && y_axis <= max_y) {
            if (x_axis == x & y_axis == y) {
            } else {
                if (Game_Field.get(y_axis).get(x_axis).field_figur == null) {
                    possible_moves.add(new Field(x_axis, y_axis));
                } else {
                    found_figure = true;
                    if (Game_Field.get(y_axis).get(x_axis).field_figur.color == this.color) {
                    } else {
                        possible_moves.add(new Field(x_axis, y_axis));
                    }
                }
            }
            y_axis++;
        }
    }

    private void check_verticalminus(ArrayList<ArrayList<Field>> Game_Field, ArrayList<Field> possible_moves, int x,
            int y) {
        int min_y = 1;

        int x_axis = x;
        int y_axis = y;

        boolean found_figure = false;
        while (!found_figure && y_axis >= min_y) {
            if (x_axis == x & y_axis == y) {
            } else {
                if (Game_Field.get(y_axis).get(x_axis).field_figur == null) {
                    possible_moves.add(new Field(x_axis, y_axis));
                } else {
                    found_figure = true;
                    if (Game_Field.get(y_axis).get(x_axis).field_figur.color == this.color) {
                    } else {
                        possible_moves.add(new Field(x_axis, y_axis));
                    }
                }
            }
            y_axis--;
        }
    }

    private void check_horizontalplus(ArrayList<ArrayList<Field>> Game_Field, ArrayList<Field> possible_moves, int x,
            int y) {
        int max_x = 8;

        int x_axis = x;
        int y_axis = y;

        boolean found_figure = false;
        while (!found_figure && x_axis <= max_x) {
            if (x_axis == x & y_axis == y) {
            } else {
                if (Game_Field.get(y_axis).get(x_axis).field_figur == null) {
                    possible_moves.add(new Field(x_axis, y_axis));
                } else {
                    found_figure = true;
                    if (Game_Field.get(y_axis).get(x_axis).field_figur.color == this.color) {
                    } else {
                        possible_moves.add(new Field(x_axis, y_axis));
                    }
                }
            }
            x_axis++;
        }
    }

    private void check_horizontalminus(ArrayList<ArrayList<Field>> Game_Field, ArrayList<Field> possible_moves, int x,
            int y) {
        int min_x = 1;

        int x_axis = x;
        int y_axis = y;

        boolean found_figure = false;
        while (!found_figure && x_axis >= min_x) {
            if (x_axis == x & y_axis == y) {
            } else {
                if (Game_Field.get(y_axis).get(x_axis).field_figur == null) {
                    possible_moves.add(new Field(x_axis, y_axis));
                } else {
                    found_figure = true;
                    if (Game_Field.get(y_axis).get(x_axis).field_figur.color == this.color) {
                    } else {
                        possible_moves.add(new Field(x_axis, y_axis));
                    }
                }
            }
            x_axis--;
        }
    }
}
