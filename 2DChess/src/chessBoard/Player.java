/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessBoard;

/**
 *
 * @author wezsteul
 */
public class Player {

    public String name = "";
    public int draws = 0;
    public boolean color = true;
    public boolean checked = false;
    public boolean castlingRight = true;

    public Player(String name, Boolean color) {
        this.name = name;
        this.color = color;
        this.checked = false;
    }
}
