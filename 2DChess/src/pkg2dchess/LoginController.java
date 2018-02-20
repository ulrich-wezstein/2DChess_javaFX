/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2dchess;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author wezsteul
 */
public class LoginController implements Initializable {

    @FXML
    private Button playNow;
    @FXML
    private Label loginErrorLabel;
    @FXML
    private TextField textfield_player1name;
    @FXML
    private TextField textfield_player2name;

    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        Stage stage;
        Parent root;
        //wenn spielen button gedrückt wird
        GameController.setPlayerNames(textfield_player1name.getText(), textfield_player2name.getText());
        if (event.getSource() == playNow) {
            if (!textfield_player1name.getText().isEmpty() && !textfield_player2name.getText().isEmpty()) {
                if (Integer.parseInt(System.getProperty("java.runtime.version").substring(2, 3)) == 8 && Integer.parseInt(System.getProperty("java.runtime.version").substring(6, 8))==16) {
                    stage = (Stage) playNow.getScene().getWindow();
                    //andere Scene wird geladen
                    root = FXMLLoader.load(getClass().getResource("Game.fxml"));
                    //scene wird gesetzt
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    //scene wird angezeigt
                    stage.show();
                } else if (Integer.parseInt(System.getProperty("java.runtime.version").substring(0, 1)) >= 9) {
                    stage = (Stage) playNow.getScene().getWindow();
                    root = FXMLLoader.load(getClass().getResource("Game.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } else {
                    loginErrorLabel.setText("Fehler: Java Version ist veraltet");
                }
            } else {
                //wenn textfelder leer sind
                loginErrorLabel.setText("Fehler: Bitte 2 Namen eingeben");
            }
        }

    }

    // Programm beenden
    @FXML
    private void exitClick(ActionEvent event) {
        Platform.exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        textfield_player1name.setText("Spieler1");
        textfield_player2name.setText("Spieler2");
    }

    // Spielernamen aus Textfeld übergeben
    public String getPlayer1Name() {
        return textfield_player1name.getText();
    }

    // Spielernamen aus Textfeld übergeben
    public String getPlayer2Name() {
        return textfield_player2name.getText();
    }
}
