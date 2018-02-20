package figureClasses;

import chessBoard.Field;
import java.util.ArrayList;
import pkg2dchess.GameController;

public class Pawn extends Figur {

    public Pawn(boolean color, int startpos_x, int startpos_y, int checkpos_x, int checkpos_y) {
        super(color, startpos_x, startpos_y, checkpos_x, checkpos_y);
    }

    {
        this.name = "B";
        this.counter = 0;
        String white = "/pictures/pawn_white.png";
        String black = "/pictures/pawn_black.png";
        this.figur_icon_url = setImageURL(this.color, white, black);
    }

    public ArrayList<Field> getPossibleHitFields(ArrayList<ArrayList<Field>> Game_Field) {
        ArrayList<Field> possible_moves = new ArrayList<>();

        int y = this.startposition_y;
        int x = this.startposition_x;

        int min_x = calc_min_x(this.startposition_x);
        int max_x = calc_max_x(this.startposition_x);

        for (int x_axis = min_x; x_axis <= max_x; x_axis++) {
            if (this.color == true) {
                if (Game_Field.get(y + 1).get(x_axis).field_figur == null
                        || Game_Field.get(y + 1).get(x_axis).field_figur.color != this.color) {
                    if (Math.abs(x_axis - x) == 1) {
                        possible_moves.add(new Field(x_axis, y + 1));
                    }
                }
            } else if (this.color == false) {
                if (Game_Field.get(y - 1).get(x_axis).field_figur == null
                        || Game_Field.get(y - 1).get(x_axis).field_figur.color != this.color) {
                    if (Math.abs(x_axis - x) == 1) {
                        possible_moves.add(new Field(x_axis, y - 1));
                    }
                }
            }
        }
        check_enpassant(Game_Field, x, y, possible_moves);
        return possible_moves;
    }

    public ArrayList<Field> getPossibleFields(ArrayList<ArrayList<Field>> Game_Field) {
        ArrayList<Field> possible_moves = new ArrayList<>();

        int y = this.startposition_y;
        int x = this.startposition_x;

        int min_x = calc_min_x(this.startposition_x);
        int max_x = calc_max_x(this.startposition_x);

        for (int x_axis = min_x; x_axis <= max_x; x_axis++) {
            if (this.color == true) {
                if (Game_Field.get(y + 1).get(x).field_figur == null) {
                    possible_moves.add(new Field(x, y + 1));
                    if (this.counter == 0 && Game_Field.get(y + 2).get(x).field_figur == null) {
                        possible_moves.add(new Field(x, y + 2));
                    }
                }
                if (Game_Field.get(y + 1).get(x_axis).field_figur != null
                        && Game_Field.get(y + 1).get(x_axis).field_figur.color != this.color) {
                    if (Math.abs(x_axis - x) == 1) {
                        possible_moves.add(new Field(x_axis, y + 1));
                    }
                }
            } else if (this.color == false) {
                if (Game_Field.get(y - 1).get(x).field_figur == null) {
                    possible_moves.add(new Field(x, y - 1));
                    if (this.counter == 0 && Game_Field.get(y - 2).get(x).field_figur == null) {
                        possible_moves.add(new Field(x, y - 2));
                    }
                }
                if (Game_Field.get(y - 1).get(x_axis).field_figur != null
                        && Game_Field.get(y - 1).get(x_axis).field_figur.color != this.color) {
                    if (Math.abs(x_axis - x) == 1) {
                        possible_moves.add(new Field(x_axis, y - 1));
                    }
                }
            }
        }
        check_enpassant(Game_Field, x, y, possible_moves);
        return possible_moves;
    }

    private void check_enpassant(ArrayList<ArrayList<Field>> Game_Field, int x, int y, ArrayList<Field> possible_moves) {
        if (this.color == true) {
            if (Game_Field.get(y).get(x - 1).field_figur != null) {
                if ("B".equals(Game_Field.get(y).get(x - 1).field_figur.name) && Game_Field.get(y).get(x - 1).field_figur.enPassant && Game_Field.get(y).get(x - 1).field_figur.drawcounter == GameController.getCounter()) {
                    possible_moves.add(new Field(x - 1, y + 1));
                }
            }
            if (Game_Field.get(y).get(x + 1).field_figur != null) {
                if ("B".equals(Game_Field.get(y).get(x + 1).field_figur.name) && Game_Field.get(y).get(x + 1).field_figur.enPassant && Game_Field.get(y).get(x + 1).field_figur.drawcounter == GameController.getCounter()) {
                    possible_moves.add(new Field(x + 1, y + 1));
                }
            }
        } else if (this.color == false) {
            if (Game_Field.get(y).get(x - 1).field_figur != null) {
                if ("B".equals(Game_Field.get(y).get(x - 1).field_figur.name) && Game_Field.get(y).get(x - 1).field_figur.enPassant && Game_Field.get(y).get(x - 1).field_figur.drawcounter == GameController.getCounter()) {
                    possible_moves.add(new Field(x - 1, y - 1));
                }
            }
            if (Game_Field.get(y).get(x + 1).field_figur != null) {
                if ("B".equals(Game_Field.get(y).get(x + 1).field_figur.name) && Game_Field.get(y).get(x + 1).field_figur.enPassant && Game_Field.get(y).get(x + 1).field_figur.drawcounter == GameController.getCounter()) {
                    possible_moves.add(new Field(x + 1, y - 1));
                }
            }
        }
    }

    private int calc_min_x(int startx) {
        int min_x;
        if (startx == 1) {
            min_x = startx;
        } else {
            min_x = startx - 1;
        }
        return min_x;
    }

    private int calc_max_x(int startx) {
        int max_x;
        if (startx == 8) {
            max_x = startx;
        } else {
            max_x = startx + 1;
        }
        return max_x;
    }

}
