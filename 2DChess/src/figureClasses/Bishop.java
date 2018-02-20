package figureClasses;

import chessBoard.Field;
import java.util.ArrayList;

public class Bishop extends Figur {

    public Bishop(boolean color, int startpos_x, int startpos_y, int checkpos_x, int checkpos_y) {
        super(color, startpos_x, startpos_y, checkpos_x, checkpos_y);
    }

    {
        this.name = "L";
        this.counter = 0;
        String white = "/pictures/bishop_white.png";
        String black = "/pictures/bishop_black.png";
        this.figur_icon_url = setImageURL(this.color, white, black);
    }

    public ArrayList<Field> getPossibleHitFields(ArrayList<ArrayList<Field>> Game_Field) {
        ArrayList<Field> possible_moves = getPossibleFields(Game_Field);
        return possible_moves;
    }

    public ArrayList<Field> getPossibleFields(ArrayList<ArrayList<Field>> Game_Field) {
        ArrayList<Field> possible_moves = new ArrayList<>();

        check_Q1(possible_moves, Game_Field, this.startposition_x, this.startposition_y);
        check_Q2(possible_moves, Game_Field, this.startposition_x, this.startposition_y);
        check_Q3(possible_moves, Game_Field, this.startposition_x, this.startposition_y);
        check_Q4(possible_moves, Game_Field, this.startposition_x, this.startposition_y);

        return possible_moves;
    }



    private void check_Q1(ArrayList<Field> possible_moves, ArrayList<ArrayList<Field>> Game_Field, int x, int y) {
        x = this.startposition_x;
        y = this.startposition_y;

        int max_x = 8;
        int max_y = 8;

        int x_axis;
        int y_axis;

        boolean found_figure = false;
        while (!found_figure) {
            for (x_axis = x; x_axis <= max_x; x_axis++) {
                for (y_axis = y; y_axis <= max_y; y_axis++) {
                    if (x_axis == x & y_axis == y) {
                    } else {
                        // wenn keine figur gefunden wurde und feld der Gangart
                        // entspricht
                        if (!found_figure && Math.abs(x_axis - x) == Math.abs(y_axis - y)) {
                            if (Game_Field.get(y_axis).get(x_axis).field_figur == null) {
                                possible_moves.add(new Field(x_axis, y_axis));
                            } else {
                                found_figure = true;
                                // wenn figur gleiche farbe hat
                                if (Game_Field.get(y_axis).get(x_axis).field_figur.color == this.color) {
                                } else {
                                    // wenn figur schlagbar ist, m�gliches feld
                                    // hinzuf�gen
                                    possible_moves.add(new Field(x_axis, y_axis));
                                }
                            }
                        }
                    }
                }
            }
            found_figure = true;
        }
    }

    private void check_Q2(ArrayList<Field> possible_moves, ArrayList<ArrayList<Field>> Game_Field, int x, int y) {
        x = this.startposition_x;
        y = this.startposition_y;

        int max_x = 8;
        int min_y = 1;

        int x_axis;
        int y_axis;

        boolean found_figure = false;
        while (!found_figure) {
            for (x_axis = x; x_axis <= max_x; x_axis++) {
                for (y_axis = y; y_axis >= min_y; y_axis--) {
                    if (x_axis == x & y_axis == y) {
                    } else {
                        if (!found_figure && Math.abs(x_axis - x) == Math.abs(y_axis - y)) {
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

                    }
                }
            }
            found_figure = true;
        }
    }

    private void check_Q3(ArrayList<Field> possible_moves, ArrayList<ArrayList<Field>> Game_Field, int x, int y) {
        x = this.startposition_x;
        y = this.startposition_y;

        int min_x = 1;
        int max_y = 8;

        int x_axis;
        int y_axis;

        boolean found_figure = false;
        while (!found_figure) {
            for (x_axis = x; x_axis >= min_x; x_axis--) {
                for (y_axis = y; y_axis <= max_y; y_axis++) {
                    if (x_axis == x & y_axis == y) {
                    } else {
                        if (!found_figure && Math.abs(x_axis - x) == Math.abs(y_axis - y)) {
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

                    }
                }
            }
            found_figure = true;
        }
    }

    private void check_Q4(ArrayList<Field> possible_moves, ArrayList<ArrayList<Field>> Game_Field, int x, int y) {
        x = this.startposition_x;
        y = this.startposition_y;

        int min_x = 1;
        int min_y = 1;

        int x_axis;
        int y_axis;

        boolean found_figure = false;
        while (!found_figure) {
            for (x_axis = x; x_axis >= min_x; x_axis--) {
                for (y_axis = y; y_axis >= min_y; y_axis--) {
                    if (x_axis == x & y_axis == y) {
                    } else {
                        if (!found_figure && Math.abs(x_axis - x) == Math.abs(y_axis - y)) {
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

                    }
                }
            }
            found_figure = true;
        }
    }
}
