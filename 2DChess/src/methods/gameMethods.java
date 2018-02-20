/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package methods;

/**
 *
 * @author wezsteul
 */
import java.util.Random;

public class gameMethods {

    public static Boolean setPlayerColors() {
        Boolean player1color;
        Random rand = new Random();
        int random_number = rand.nextInt(10000);
        if (random_number >= 5000) {
            player1color = true;
        } else {
            player1color = false;
        }
        return player1color;
    }
}
