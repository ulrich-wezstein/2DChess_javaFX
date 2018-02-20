package figureClasses;

import chessBoard.Field;
import java.util.ArrayList;

public class King extends Figur {

    public King(boolean color, int startpos_x, int startpos_y, int checkpos_x, int checkpos_y) {
        super(color, startpos_x, startpos_y, checkpos_x, checkpos_y);
    }

    {
        this.name = "K";
        this.counter = 0;
        this.castling = true;

        String white = "/pictures/king_white.png";
        String black = "/pictures/king_black.png";
        this.figur_icon_url = setImageURL(this.color, white, black);
    }

    public ArrayList<Field> getPossibleHitFields(ArrayList<ArrayList<Field>> Game_Field) {
        ArrayList<Field> possible_moves = new ArrayList<>();

        int x = this.startposition_x;
        int y = this.startposition_y;
        int min_x = (x == 1) ? x : (x - 1);
        int max_x = (x == 8) ? x : (x + 1);
        int min_y = (y == 1) ? y : (y - 1);
        int max_y = (y == 8) ? y : (y + 1);
        for (int x_axis = min_x; x_axis <= max_x; x_axis++) {
            for (int y_axis = min_y; y_axis <= max_y; y_axis++) {
                if (x_axis == x & y_axis == y) {

                } else {
                    if (Game_Field.get(y_axis).get(x_axis).field_figur == null) {
                        possible_moves.add(new Field(x_axis, y_axis));
                    } else if (Game_Field.get(y_axis).get(x_axis).field_figur != null) {
                        if (Game_Field.get(y_axis).get(x_axis).field_figur.getColor() != this.color) {
                            possible_moves.add(new Field(x_axis, y_axis));
                        }
                    }

                }
            }
        }
        return possible_moves;
    }

    public ArrayList<Field> getPossibleFields(ArrayList<ArrayList<Field>> Game_Field) {
        ArrayList<Field> possible_moves = new ArrayList<>();

        int x = this.startposition_x;
        int y = this.startposition_y;
        int min_x = (x == 1) ? x : (x - 1);
        int max_x = (x == 8) ? x : (x + 1);
        int min_y = (y == 1) ? y : (y - 1);
        int max_y = (y == 8) ? y : (y + 1);
        for (int x_axis = min_x; x_axis <= max_x; x_axis++) {
            for (int y_axis = min_y; y_axis <= max_y; y_axis++) {
                if (x_axis == x & y_axis == y) {

                } else {
                    if (Game_Field.get(y_axis).get(x_axis).field_figur == null) {
                        possible_moves.add(new Field(x_axis, y_axis));
                    } else if (Game_Field.get(y_axis).get(x_axis).field_figur != null) {
                        if (Game_Field.get(y_axis).get(x_axis).field_figur.getColor() != this.color) {
                            possible_moves.add(new Field(x_axis, y_axis));
                        }
                    }

                }
            }
        }
        check_castling(Game_Field, possible_moves);
        return possible_moves;
    }

    private void check_castling(ArrayList<ArrayList<Field>> Game_Field, ArrayList<Field> possible_moves) {
        if (this.counter > 0){
            setCastling(false);
        }
        boolean fieldsempty = true;
        if (this.castling) {
            for (int y_axis = 1; y_axis <= 8; y_axis++) {
                for (int x_axis = 1; x_axis <= 8; x_axis++) {
                    if (Game_Field.get(y_axis).get(x_axis).field_figur != null && "T".equals(Game_Field.get(y_axis).get(x_axis).field_figur.name) && Game_Field.get(y_axis).get(x_axis).field_figur.color == this.color && Game_Field.get(y_axis).get(x_axis).field_figur.counter == 0) {
                        if (this.startposition_x > x_axis) {
                            for (int i = this.startposition_x - 1; i > x_axis; i--) {
                                if (Game_Field.get(y_axis).get(i).field_figur != null) {
                                    fieldsempty = false;
                                }
                            }
                            boolean isattackable = false;
                            if (fieldsempty) {
                                ArrayList<Field> castlingfields = new ArrayList<>();
                                castlingfields.add(new Field(this.startposition_x - 1, this.startposition_y));
                                castlingfields.add(new Field(this.startposition_x - 2, this.startposition_y));
                                isattackable = getFieldIsAttackable(castlingfields, Game_Field, this.color);
                                if (!isattackable) {
                                    possible_moves.add(new Field(x_axis, y_axis));
                                }
                            }
                        } else {
                            for (int i = this.startposition_x + 1; i < x_axis; i++) {
                                if (Game_Field.get(y_axis).get(i).field_figur != null) {
                                    fieldsempty = false;
                                }
                            }
                            boolean isattackable = false;
                            if (fieldsempty) {
                                ArrayList<Field> castlingfields = new ArrayList<>();
                                castlingfields.add(new Field(this.startposition_x + 1, this.startposition_y));
                                castlingfields.add(new Field(this.startposition_x + 2, this.startposition_y));
                                isattackable = getFieldIsAttackable(castlingfields, Game_Field, this.color);
                                if (!isattackable) {
                                    possible_moves.add(new Field(x_axis, y_axis));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean getFieldIsAttackable(ArrayList<Field> castlingfields, ArrayList<ArrayList<Field>> Game_Field, boolean figurcolor) {
        boolean field_found = false;
        int tempx_axis;
        int tempy_axis;
        for (int i = 0; i < castlingfields.size(); i++) {
            field_found = false;
            tempx_axis = 1;
            while (!field_found && tempx_axis <= 8) {
                tempy_axis = 1;
                while (!field_found && tempy_axis <= 8) {
                    if (!field_found && Game_Field.get(tempy_axis).get(tempx_axis).field_figur != null
                            && (Game_Field.get(tempy_axis).get(tempx_axis).field_figur.color != figurcolor)) {
                        ArrayList<Field> field_figur_possible_fields = Game_Field.get(tempy_axis).get(tempx_axis).field_figur.getPossibleHitFields(Game_Field);

                        int figurfieldcounter = 0;
                        while (!field_found && figurfieldcounter < field_figur_possible_fields.size()) {// erhoehen
                            if (castlingfields.get(i).pos_x == field_figur_possible_fields
                                    .get(figurfieldcounter).pos_x
                                    && castlingfields.get(i).pos_y == field_figur_possible_fields
                                    .get(figurfieldcounter).pos_y) {
                                field_found = true;
                            }
                            figurfieldcounter++;

                        }
                    }

                    tempy_axis++;
                }

                tempx_axis++;
            }
        }
        return field_found;
    }

}
