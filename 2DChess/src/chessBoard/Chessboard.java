package chessBoard;

import java.util.ArrayList;

abstract public class Chessboard {
	public static ArrayList<ArrayList<Field>> create() {
		ArrayList<ArrayList<Field>> chessboard = new ArrayList<ArrayList<Field>>();
		for (int i = 0; i <= 9; i++) {
			ArrayList<Field> row = new ArrayList<>();
			for (int j = 0; j <= 9; j++) {
				row.add(new Field(j,i));
			}
			chessboard.add(i, row);
		}
		return chessboard;
	}
}
