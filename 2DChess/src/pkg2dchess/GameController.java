/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2dchess;

import chessBoard.Field;
import chessBoard.Player;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;

/**
 *
 * @author wezsteul
 */
public class GameController implements Initializable {

    //FXML elemente deklarieren
    @FXML
    private GridPane chessBoardpane;
    @FXML
    private GridPane whiteCheckedFiguresPane;
    @FXML
    private GridPane blackCheckedFiguresPane;
    @FXML
    private GridPane whiteCheckedPawnsPane;
    @FXML
    private GridPane blackCheckedPawnsPane;
    @FXML
    private Button playerBlackExit;
    @FXML
    private Button playerWhiteExit;
    @FXML
    private Label gameErrorLabel;
    @FXML
    private Label drawLabel;
    @FXML
    private Label statusLabel;
    //sonstige Elemente deklarieren und/oder initialisieren
    private ArrayList<ArrayList<Field>> chess_board;
    private Image figureimage;
    public int startField[] = new int[2];
    public int targetField[] = new int[2];
    private Player player1;
    private Player player2;
    private boolean draw = true;
    private boolean is_check = false;
    private static int drawnumbers;
    private static ArrayList<String> players = new ArrayList<>();
    private ArrayList<Field> possibleFields = new ArrayList<>();
    private InnerShadow nodeInnerShadow = new InnerShadow(20, 0, 0, Color.GREEN);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chess_board = chessBoard.Chessboard.create();
        chess_board = methods.populateBoard.create();
        statusLabel.setText("hallo");
        try {
            displayBoard();
        } catch (IOException ex) {
            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
        }
        startGame();
    }

    //FXML Methode bei Button Click
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        //Alert Box zum Bestätigen der Aufgabe        
        Alert alertboxconfirm = new Alert(Alert.AlertType.WARNING, null, ButtonType.YES, ButtonType.NO);
        Stage stage = (Stage) alertboxconfirm.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/pictures/chessboard.png"));
        alertboxconfirm.setTitle("Aufgabe");
        alertboxconfirm.setHeaderText("Wirklich aufgeben?");
        // Zentrieren im Spielfeld
        setAlertCenter(alertboxconfirm);
        alertboxconfirm.showAndWait();

        // Bei Ja -> neue Alertbox zur Ausgabe des Gewinners
        if (alertboxconfirm.getResult() == ButtonType.YES) {
            alertboxconfirm.close();
            onGiveUpButton("");
            if (event.getSource() == playerWhiteExit) {
                if (player1.color == true) {
                    player1.checked = true;
                } else {
                    player2.checked = true;
                }
            } else if (event.getSource() == playerBlackExit) {
                if (player1.color == false) {
                    player1.checked = true;
                } else {
                    player2.checked = true;
                }
            }
        } else //Falls aus Versehen auf den Knopf gedrückt wird -> Spiel weiterführen
        {
            alertboxconfirm.close();
        }
    }

    // Tatsächliche Aufgabe mit extra Alert
    private void onGiveUpButton(String status) throws IOException {
        Alert alertbox = new Alert(Alert.AlertType.CONFIRMATION, "Wollen Sie ein neues Spiel?", ButtonType.YES, ButtonType.NO);
        // Zentrieren im Spielfeld
        setAlertCenter(alertbox);
        // Dialogfenster Icon ändern
        Stage iconstage = (Stage) alertbox.getDialogPane().getScene().getWindow();
        iconstage.getIcons().add(new Image("/pictures/chessboard.png"));
        alertbox.setTitle("Partie beendet");

        // Verlierer und Gewinner in Status ausgeben, sowie im AlertHeader
        if (player1.checked == true) {
            alertbox.setHeaderText(status + "Spieler " + player2.name + " hat gewonnen");
            gameErrorLabel.setText(status + player1.name + " hat verloren");
        } else {
            alertbox.setHeaderText(status + "Spieler " + player1.name + " hat gewonnen");
            gameErrorLabel.setText(status + player2.name + " hat verloren");
        }

        alertbox.showAndWait();
        Stage stage;
        Parent root;
        // Bei "Nein" zurück zu Login, bei "Ja" Schachbrett neu laden
        if (alertbox.getResult() == ButtonType.NO) {
            stage = (Stage) playerWhiteExit.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        } else {
            stage = (Stage) playerWhiteExit.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("Game.fxml"));
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Alert Fenster in die Mitte vom Schachbrettfenster setzen, Height erkennt er nicht...
    private void setAlertCenter(Alert alert) {
        double w_x = chessBoardpane.getScene().getWindow().getX();
        double w_y = chessBoardpane.getScene().getWindow().getY();
        double w_width = chessBoardpane.getScene().getWindow().getWidth();
        double w_height = chessBoardpane.getScene().getWindow().getHeight();
        double a_width = alert.getDialogPane().getWidth();
//        double a_height = alert.getDialogPane().getHeight();
        alert.setX(w_x + (w_width / 2 - a_width / 2));
        alert.setY(w_y + (w_height / 2 - 100));
    }

    //Spielernamen von Logincontroller bekommen
    public static void setPlayerNames(String name1, String name2) {
        if (!players.isEmpty()) {
            players.clear();
        }
        players.add(name1);
        players.add(name2);
    }

    public static String getPlayer1Name() {
        return players.get(0);
    }

    public static String getPlayer2Name() {
        return players.get(1);
    }

    public static int getCounter() {
        return drawnumbers;
    }

    // Spiellogik starten
    private void startGame() {
        boolean player1color = methods.gameMethods.setPlayerColors();
        // Spieler erstellen
        player1 = new Player(players.get(0), player1color);
        player2 = new Player(players.get(1), (player1color == true ? false : true));
        player1.checked = false;
        player2.checked = false;
        drawnumbers = 0;
        // Aufgeben Knopf 
        if (draw == true) {
            playerWhiteExit.setDisable(false);
            playerBlackExit.setDisable(true);
        } else {
            playerWhiteExit.setDisable(true);
            playerBlackExit.setDisable(false);
        }
        gameErrorLabel.setText("Spiel ist gestartet, " + ((player1.color == true) ? player1.name : player2.name) + " ist Weiß und ist am Zug");
    }

    // Schachbrett vom Backend aufrufen und Bilder in GridPanes schreiben
    private void displayBoard() throws IOException {
        // bisheriges leer machen
        startField[0] = 0;
        startField[1] = 0;
        targetField[0] = 0;
        targetField[1] = 0;
        chessBoardpane.getChildren().clear();

        // Schachbrett Bild neu setzen
        Image chessboardimage = new Image("/pictures/chessboard.png");
        ImageView imvchessboard = new ImageView(chessboardimage);
        imvchessboard.setFitHeight(733);
        imvchessboard.setFitWidth(736);
        chessBoardpane.add(imvchessboard, 0, 0);
        GridPane.setHalignment(imvchessboard, HPos.LEFT);
        GridPane.setValignment(imvchessboard, VPos.TOP);

        whiteCheckedFiguresPane.getChildren().clear();
        // Aufgeben Knopf neu setzen
        whiteCheckedFiguresPane.add(playerBlackExit, 9, 0);

        blackCheckedFiguresPane.getChildren().clear();
        // Aufgeben Knopf neu setzen
        blackCheckedFiguresPane.add(playerWhiteExit, 9, 0);

        whiteCheckedPawnsPane.getChildren().clear();
        blackCheckedPawnsPane.getChildren().clear();
        // neu auffuellen
        for (int x = 1; x <= 8; x++) {
            for (int y = 1; y <= 8; y++) {
                //für jedes Gitternetzfeld wird ein Bild vom Arraylist abgerufen und im ImageView dargestellt
                displayFigures(chessBoardpane, x, y);
            }
        }
        // Schachmatt Status abfragen
        check_checkmate(chess_board, draw);

        for (int i = 1; i <= 8; i++) {
            displayCheckedFigures(whiteCheckedFiguresPane, i, 0);
            displayCheckedFigures(blackCheckedFiguresPane, i, 9);
            displayCheckedPawns(whiteCheckedPawnsPane, 0, i);
            displayCheckedPawns(blackCheckedPawnsPane, 9, i);
        }  
        // Statusmeldung, wer am Zug ist
        gameErrorLabel.setTextFill(Color.BLACK);
        drawLabel.setText("Es ist am Zug:" + ((draw == true) ? " (Weiß)" : " (Schwarz)"));
    }

    //Schachfiguren in eigentliches Schachbrett GridPane setzen
    private void displayFigures(GridPane newpane, int x, int y) {
        ImageView imv = setImages(x, y, true);
        newpane.add(imv, x, 9 - y);
    }

    // GridPanes mit geschlagenen Figuren auffuellen
    private void displayCheckedFigures(GridPane newpane, int x, int y) {
        ImageView imv = setImages(x, y, false);
        newpane.add(imv, x, 0);
    }

    // GridPanes mit geschlagenen Bauern auffuellen
    private void displayCheckedPawns(GridPane newpane, int x, int y) {
        ImageView imv = setImages(x, y, false);
        newpane.add(imv, 0, y);
    }

    // Bilder setzen
    private ImageView setImages(int x, int y, Boolean setupDragAndDrop) {
        ImageView imv = new ImageView();
        // Bild von Backend holen (von den einzelnen Klassen)
        figureimage = new Image(chess_board.get(y).get(x).getImageURL());
        imv.setImage(figureimage);
        // falls Bild Platzhalter fuer leeres Feld ist, Deckkraft auf 0, ansonsten 1
        if ("/pictures/empty.png".equals(chess_board.get(y).get(x).getImageURL())) {
            imv.setOpacity(0);
        } else {
            imv.setOpacity(1);
        }
        // Bildgroesse anpassen
        imv.setFitHeight(80);
        imv.setFitWidth(80);
        // Events für Felder initialisieren
        setImvEvents(imv, setupDragAndDrop, x, y);

        return imv;
    }

    // wenn Maus Drag auf Feld erkannt wird
    private void mouseDragDetected(MouseEvent event, ImageView imv) {
        // dragboard (Ablage) initialisieren
        Dragboard db = imv.startDragAndDrop(TransferMode.ANY);
        // Eltern Ablage initialisieren, damit das Feldbild am Mauszeiger hängt
        ClipboardContent content = new ClipboardContent();
        content.putImage(imv.getImage());
        db.setContent(content);
        Node node = (Node) event.getTarget();
        // Startkoordinaten des Feldes bekommen
        startField[0] = GridPane.getColumnIndex(node);
        startField[1] = 9 - GridPane.getRowIndex(node);

        possibleFields.clear();
        // moegliche Zielfelder von Startfigur halten
        possibleFields = chess_board.get(startField[1]).get(startField[0])
                .getPossibleMoves(chess_board);
        for (int i = 0; i < possibleFields.size(); i++) {
            ImageView tempimv = (ImageView) getNode(chessBoardpane, possibleFields.get(i).pos_x, 9 - possibleFields.get(i).pos_y);
            tempimv.setOpacity(1);
            tempimv.setEffect(nodeInnerShadow);
        }

        chessBoardpane.setCursor(Cursor.CLOSED_HAND);
        event.consume();

    }

    // wenn weiter gezogen wird, erkenn ob Dragboard ein Bild hat, wenn nicht, dann nicht akzeptieren
    private void mouseDragOver(DragEvent event, ImageView imv) {
        if (event.getGestureSource() != imv && event.getDragboard().hasImage()) {
            Node node = (Node) event.getTarget();
            // Zielkoordinaten des Feldes erkennen
            targetField[0] = GridPane.getColumnIndex(node);
            targetField[1] = 9 - GridPane.getRowIndex(node);
            for (int j = 0; j < possibleFields.size(); j++) {
                ImageView tempimv = (ImageView) getNode(chessBoardpane, possibleFields.get(j).pos_x, 9 - possibleFields.get(j).pos_y);
                tempimv.setOpacity(1);
                tempimv.setEffect(nodeInnerShadow);
            }
            gameErrorLabel.setTextFill(Color.RED);
            gameErrorLabel.setText("Falsches Zielfeld!");

            for (int i = 0; i < possibleFields.size(); i++) {
                // Wenn Feld eines moeglichen Zielfeldes entspricht
                if (targetField[0] == possibleFields.get(i).pos_x && targetField[1] == possibleFields.get(i).pos_y) {
                    event.acceptTransferModes(TransferMode.MOVE);

                    gameErrorLabel.setTextFill(Color.BLACK);
                    gameErrorLabel.setText("Mögliches Zielfeld");

                }
            }
        }
    }

    // wenn Figur abgelegt wird
    private void mouseDragDropped(DragEvent event, ImageView imv) throws IOException {
        boolean castlingmove = false;
        int temptargetTowerField[] = new int[2];
        boolean success = false;
        for (int i = 0; i < possibleFields.size(); i++) {
            // wenn Eventfeld einer der moeglichen Zugfelder entspricht
            if (targetField[0] == possibleFields.get(i).pos_x && targetField[1] == possibleFields.get(i).pos_y) {
                Dragboard db = event.getDragboard();
                // wenn dragboard ein bild hat
                if (db.hasImage()) {
                    success = true;
                    // bild übergeben
                    imv.setImage(db.getImage());
                    // deckkraft wieder auf 1 setzen (da ja keine empty bild uebergeben wird)
                    imv.setOpacity(1);
                    imv.setFitHeight(80);
                    imv.setFitWidth(80);
                    // Mauszeiger normalisieren
                    chessBoardpane.setCursor(Cursor.DEFAULT);

                    // wenn Rochade gemacht wurde
                    if (chess_board.get(startField[1]).get(startField[0]).field_figur.castling && Math.abs(startField[0] - targetField[0]) > 1) {
                        int temptargetKingField[] = new int[2];
                        // tatsächliche Zielfelder für Koenig und Turm in seperate Array schreiben
                        setupCastling(temptargetTowerField, temptargetKingField);
                        move_figure(chess_board, temptargetKingField, startField);
                        castlingmove = true;
                    } else if ("B".equals(chess_board.get(startField[1]).get(startField[0]).field_figur.name) && Math.abs(targetField[1] - startField[1]) == 1 && Math.abs(targetField[0] - startField[0]) == 1 && chess_board.get(targetField[1]).get(targetField[0]).field_figur == null) {
                        int targetPawnfield[] = new int[2];
                        if (targetField[0] > startField[0]) {
                            targetPawnfield[0] = startField[0] + 1;
                        } else {
                            targetPawnfield[0] = startField[0] - 1;
                        }
                        targetPawnfield[1] = startField[1];
                        check_figure(chess_board, targetPawnfield, startField, draw);
                        move_figure(chess_board, targetField, startField);
                    } else {
                        // wenn zielfeld eine gegnerische Figur hat
                        check_figure(chess_board, targetField, startField, draw);
                        // eigentlicher Zugausfuehren
                        move_figure(chess_board, targetField, startField);
                    }

                    // Anzahl der Zuege erhoehen (gesamt und der einzelnen Spieler)                    
                    drawnumbers++;

                    // Zuganzahl des Spielers erhöhen
                    incrementPlayerCounter();

                    // Zug dem Gegner "geben"
                    draw = (draw == true) ? false : true;

                    // Aufgeben Knopf inaktiv/aktiv setzen, je nach Zugfarbe
                    setButtons();

                }

                event.setDropCompleted(success);
                event.consume();

                // ueberpruefen, ob Koenig geschlagen wurde
                check_kingstatus(chess_board, (draw == player1.color) ? player1 : player2);

                // Schachstatus ueberpruefen
                check_check(chess_board, targetField);

                // Bauer austauschen (derzeit nur vom Feld nehmen)
                pawn_auto_check(chess_board);

                // wenn Rochade gespielt wurde, Doppelzug beenden
                if (castlingmove) {
                    move_figure(chess_board, temptargetTowerField, targetField);
                }

                // Spielfeld aktualisieren
                displayBoard();
            }

        }
    }

    // Gridpane Zelle ansteuern
    private Node getNode(GridPane grid, int column, int row) {
        Node result = null;
        for (Node node : grid.getChildren()) {
            if (GridPane.getColumnIndex(node) == column
                    && GridPane.getRowIndex(node) == row) {
                result = node;
            }
        }
        return result;
    }

    // Anzahl Zuege des Spielers erhoehen
    private void incrementPlayerCounter() {
        if (draw == player1.color) {
            player1.draws++;
        } else {
            player2.draws++;
        }
    }

    // Aufgeben Knopf für Spieler aktivieren und für Gegenspieler deaktivieren
    private void setButtons() {
        if (draw == true) {
            playerWhiteExit.setDisable(false);
            playerBlackExit.setDisable(true);
        } else {
            playerWhiteExit.setDisable(true);
            playerBlackExit.setDisable(false);
        }
    }

    // Rochade Zielfelder fuer Koenig und Turm in seperate Array schreiben
    private void setupCastling(int[] temptargetTowerField, int[] temptargetKingField) {

        if (targetField[0] > startField[0]) {

            temptargetKingField[0] = startField[0] + 2;
            temptargetKingField[1] = targetField[1];

            temptargetTowerField[0] = startField[0] + 1;
            temptargetTowerField[1] = targetField[1];

        } else if (targetField[0] < startField[0]) {

            temptargetKingField[0] = startField[0] - 2;
            temptargetKingField[1] = targetField[1];

            temptargetTowerField[0] = startField[0] - 1;
            temptargetTowerField[1] = targetField[1];

        }

    }

    //Events bei Mausinteraktion werden gesetzt
    private void setImvEvents(ImageView imv, Boolean setupDragAndDrop, int x, int y) {
        if (setupDragAndDrop) {
            if (chess_board.get(y).get(x).field_figur != null && chess_board.get(y).get(x).field_figur.color == draw) {
                // wenn die figur der eigenen Farbe entspricht
                imv.setOnMouseEntered(event -> {
                    Node eventnode = (Node) event.getTarget();
                    int eventField[] = new int[2];
                    eventField[0] = GridPane.getColumnIndex(eventnode);
                    eventField[1] = 9 - GridPane.getRowIndex(eventnode);
                    if (!chess_board.get(eventField[1]).get(eventField[0]).getPossibleMoves(chess_board).isEmpty()) {
                        imv.setCursor(Cursor.HAND);
                        gameErrorLabel.setTextFill(Color.BLACK);
                        gameErrorLabel.setText("Diese Figur kann gezogen werden.");
                    } else {
                        Image falsefigurimage = new Image("/pictures/falsefigur.png");
                        ImageCursor falsefigur = new ImageCursor(falsefigurimage, falsefigurimage.getWidth() / 2, falsefigurimage.getHeight() / 2);
                        imv.setCursor(falsefigur);
                        gameErrorLabel.setTextFill(Color.RED);
                        gameErrorLabel.setText("Diese Figur kann nicht gezogen werden!");
                    }

                });
                imv.setOnMousePressed(event -> {
                    if (!"/pictures/empty.png".equals(chess_board.get(y).get(x).getImageURL())) {
                        imv.setCursor(Cursor.CLOSED_HAND);
                    }
                });
                imv.setOnDragDetected((MouseEvent event) -> {
                    if (!"/pictures/empty.png".equals(chess_board.get(y).get(x).getImageURL())) {
                        mouseDragDetected(event, imv);
                    }
                });
                imv.setOnDragExited((DragEvent event) -> {
                    for (int i = 0; i < possibleFields.size(); i++) {
                        ImageView tempimv = (ImageView) getNode(chessBoardpane, possibleFields.get(i).pos_x, 9 - possibleFields.get(i).pos_y);
                        if (chess_board.get(possibleFields.get(i).pos_y).get(possibleFields.get(i).pos_x).field_figur == null) {
                            tempimv.setOpacity(0);
                        }
                        tempimv.setEffect(null);
                    }
                });
            } else {
                // wenn es ein Leeres Feld oder nicht die Eigene Figur ist
                imv.setOnMouseEntered(event -> {
                    Image falsefigurimage = new Image("/pictures/falsefigur.png");
                    ImageCursor falsefigur = new ImageCursor(falsefigurimage, falsefigurimage.getWidth() / 2, falsefigurimage.getHeight() / 2);
                    imv.setCursor(falsefigur);
                });
                imv.setOnMousePressed(event -> {
                    Image falsefigurimage = new Image("/pictures/falsefigur.png");
                    ImageCursor falsefigur = new ImageCursor(falsefigurimage, falsefigurimage.getWidth() / 2, falsefigurimage.getHeight() / 2);
                    imv.setCursor(falsefigur);
                    Node eventnode = (Node) event.getTarget();
                    int eventField[] = new int[2];
                    eventField[0] = GridPane.getColumnIndex(eventnode);
                    eventField[1] = 9 - GridPane.getRowIndex(eventnode);
                    if (chess_board.get(eventField[1]).get(eventField[0]).field_figur == null) {
                        gameErrorLabel.setTextFill(Color.RED);
                        gameErrorLabel.setText("Dieses Feld ist leer");
                    } else {
                        gameErrorLabel.setTextFill(Color.RED);
                        gameErrorLabel.setText("Dies ist eine Gegnerische Figur!");
                    }
                });

            }
            imv.setOnDragOver((DragEvent event) -> {
                mouseDragOver(event, imv);
            });
            imv.setOnDragDropped((DragEvent event) -> {
                try {
                    mouseDragDropped(event, imv);
                } catch (IOException ex) {
                    Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        } else {
            // wenn drop noch nicht beendet ist
            imv.setOnMouseEntered(event -> {
                Image falsefigurimage = new Image("/pictures/falsefigur.png");
                ImageCursor falsefigur = new ImageCursor(falsefigurimage, falsefigurimage.getWidth() / 2, falsefigurimage.getHeight() / 2);
                imv.setCursor(falsefigur);
            });
        }
        imv.setOnMouseExited(event -> {
            imv.setCursor(Cursor.DEFAULT);
        });
        imv.setOnMouseReleased(event -> {
            for (int i = 0; i < possibleFields.size(); i++) {
                ImageView tempimv = (ImageView) getNode(chessBoardpane, possibleFields.get(i).pos_x, 9 - possibleFields.get(i).pos_y);
                if (chess_board.get(possibleFields.get(i).pos_y).get(possibleFields.get(i).pos_x).field_figur == null) {
                    tempimv.setOpacity(0);
                }
                tempimv.setEffect(null);
            }
            gameErrorLabel.setTextFill(Color.BLACK);
            gameErrorLabel.setText("");
            imv.setCursor(Cursor.DEFAULT);
        });
    }

    // Methode zum Uebergeben der Figur ins Zielfeld
    private void move_figure(ArrayList<ArrayList<Field>> Game_Field, int target_field[], int start_field[]) {
        if ("B".equals(Game_Field.get(start_field[1]).get(start_field[0]).field_figur.name) && Math.abs(target_field[1] - start_field[1]) == 2) {
            Game_Field.get(start_field[1]).get(start_field[0]).field_figur.enPassant = true;
            Game_Field.get(start_field[1]).get(start_field[0]).field_figur.drawcounter = drawnumbers + 1;
        }
        // Zielfeld erhaelt ausgewaehlte figur
        Game_Field.get(target_field[1]).get(target_field[0]).deliver(
                Game_Field.get(start_field[1]).get(start_field[0]).field_figur, target_field[0], target_field[1]);
        // Figurzaehler wird erhoeht
        Game_Field.get(target_field[1]).get(target_field[0]).field_figur.counter++;
        // startfeld wird leer
        Game_Field.get(start_field[1]).get(start_field[0]).deliver_empty();
    }

    // Methode zum ueberpruefen und schlagen von gegnerischen Figuren auf dem Zielfeld
    private void check_figure(ArrayList<ArrayList<Field>> Game_Field, int target_field[], int start_field[], boolean playercolor) {
        // wenn gegnerische figur, dann stell diese Figur auf dessen Checked
        // Position
        int x_axis = target_field[0];
        int y_axis = target_field[1];

        if (Game_Field.get(y_axis).get(x_axis).field_figur != null) {
            if (Game_Field.get(y_axis).get(x_axis).field_figur.color == draw) {
                if (x_axis < start_field[0]) {
                    Game_Field.get(start_field[1]).get(start_field[0] - 1).deliver(Game_Field.get(y_axis).get(x_axis).field_figur, start_field[0] - 1,
                            start_field[1]);
                    Game_Field.get(target_field[1]).get(target_field[0]).field_figur.counter++;
                } else {
                    Game_Field.get(start_field[1]).get(start_field[0] + 1).deliver(Game_Field.get(y_axis).get(x_axis).field_figur, start_field[0] + 1,
                            start_field[1]);
                    Game_Field.get(target_field[1]).get(target_field[0]).field_figur.counter++;
                }
            } else {
                int check_x = Game_Field.get(y_axis).get(x_axis).field_figur.checkedposition_x;
                int check_y = Game_Field.get(y_axis).get(x_axis).field_figur.checkedposition_y;
                if (Game_Field.get(y_axis).get(x_axis).field_figur.color != playercolor) {

                    Game_Field.get(y_axis).get(x_axis).field_figur.checked = true;

                    Game_Field.get(check_y).get(check_x).deliver(Game_Field.get(y_axis).get(x_axis).field_figur, check_x,
                            check_y);
                }

            }
        }
        // bisheriges feld leer machen
        Game_Field.get(y_axis).get(x_axis).deliver_empty();
    }

    // Methode zur Ueberpruefung, ob Schachmatt vorliegt
    private void check_checkmate(ArrayList<ArrayList<Field>> game_Field, Boolean draw) throws IOException {
        int x_axis;
        int y_axis;
        int kingfield[] = new int[2];
        // Position des eigenen Koenigs ermitteln
        for (x_axis = 1; x_axis <= 8; x_axis++) {
            for (y_axis = 1; y_axis <= 8; y_axis++) {
                if (game_Field.get(y_axis).get(x_axis).field_figur != null
                        && "K".equals(game_Field.get(y_axis).get(x_axis).field_figur.name)
                        && game_Field.get(y_axis).get(x_axis).field_figur.color == draw) {
                    kingfield[0] = x_axis;
                    kingfield[1] = y_axis;
                }
            }
        }
        // moegliche Zugfelder des Koenigs ermitteln
        ArrayList<Field> possible_king_fields = game_Field.get(kingfield[1]).get(kingfield[0]).field_figur
                .getPossibleFields(game_Field);
        boolean field_found = false;
        int fields_found = 0;
        // fuer jedes einzelne zugfeld des Koenigs
        for (int i = 0; i < possible_king_fields.size(); i++) {
            field_found = false;
            x_axis = 1;
            // ueber ganzes Spielfeld schauen
            while (!field_found && x_axis <= 8) {
                y_axis = 1;
                while (!field_found && y_axis <= 8) {
                    // wenn noch kein moegliches Zugfeld abgedeckt wurde, die
                    // position leer ist oder eine gegnerische figur drauf steht
                    if (!field_found && game_Field.get(y_axis).get(x_axis).field_figur != null
                            && (game_Field.get(y_axis).get(x_axis).field_figur.color != draw)) {
                        // moegliche Zugfelder der gegnerischen Figur ermitteln
                        ArrayList<Field> field_figur_possible_fields = game_Field.get(y_axis).get(x_axis).field_figur
                                .getPossibleHitFields(game_Field);
                        int figurfieldcounter = 0;

                        while (!field_found && figurfieldcounter < field_figur_possible_fields.size()) {
                            // wenn moegliches Zugfeld des Koenigs auch moegliches
                            // Zugfeld einer gegnerischen Figur ist, Zaehler
                            // erhoehen
                            if (possible_king_fields.get(i).pos_x == field_figur_possible_fields
                                    .get(figurfieldcounter).pos_x
                                    && possible_king_fields.get(i).pos_y == field_figur_possible_fields
                                    .get(figurfieldcounter).pos_y) {
                                field_found = true;
                                fields_found++;
                            }
                            figurfieldcounter++;

                        }
                    }
                    y_axis++;
                }
                x_axis++;
            }
        }
        // wenn jetzt die Anzahl der Zugfelder des Koenigs und die abgedeckten
        // Felder durch gegnerische Figuren uebereinstimmt
        if (possible_king_fields.size() == fields_found) {
            // wenn das einzige feld abgedeckt werden kann von einer eigenen figur,
            // oder es nur eine einzelne gegnerische figur gibt, die aber geschlagen werden kann
            if (possible_king_fields.size() == 1) {
                if (is_check) {
                    if (draw == player1.color) {
                        player1.checked = true;
                    } else {
                        player2.checked = true;
                    }
                    statusLabel.setText(statusLabel.getText() + "Schachmatt!.");
                    onGiveUpButton("Schachmatt. ");
                }
            } else // Falls der Koenig keine Zugmoeglichkeit per se hat
            if (possible_king_fields.size() >= 2) {
                if (is_check) {

                    if (draw == player1.color) {
                        player1.checked = true;
                    } else {
                        player2.checked = true;
                    }
                    statusLabel.setText(statusLabel.getText() + "Schachmatt!.");
                    onGiveUpButton("Schachmatt. ");
                }
            } else {

                int x = kingfield[0];
                int y = kingfield[1];
                int min_x = (x == 1) ? x : (x - 1);
                int max_x = (x == 8) ? x : (x + 1);
                int min_y = (y == 1) ? y : (y - 1);
                int max_y = (y == 8) ? y : (y + 1);
                // schauen, ob eine eigene Figur neben dem Koenig steht, sonst
                // haette der Koenig eine Zugmoeglichkeit, andernfalls Schachmatt.
                for (x_axis = min_x; x_axis <= max_x; x_axis++) {
                    for (y_axis = min_y; y_axis <= max_y; y_axis++) {
                        if (x_axis == x & y_axis == y) {
                        } else {
                            if (game_Field.get(y_axis).get(x_axis).field_figur == null) {
                                if (draw == player1.color) {
                                    player1.checked = true;
                                } else {
                                    player2.checked = true;
                                }
                                statusLabel.setText(statusLabel.getText() + "Schachmatt!.");
                                onGiveUpButton("Schachmatt. ");
                            } else if (game_Field.get(y_axis).get(x_axis).field_figur != null) {
                                if (game_Field.get(y_axis).get(x_axis).field_figur.color != draw) {
                                    if (draw == player1.color) {
                                        player1.checked = true;
                                    } else {
                                        player2.checked = true;
                                    }
                                    statusLabel.setText(statusLabel.getText() + "Schachmatt!.");
                                    onGiveUpButton("Schachmatt. ");
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    // Methode zur Ueberpruefung von Status "Schach"
    private void check_check(ArrayList<ArrayList<Field>> game_Field, int target_field[]) {
        is_check = false;
        statusLabel.setText("");
        ArrayList<Field> nextPossibleFields = new ArrayList<>();
        nextPossibleFields = chess_board.get(target_field[1]).get(target_field[0])
                .getPossibleMoves(game_Field);
        for (int i = 0; i < nextPossibleFields.size(); i++) {
            int x = nextPossibleFields.get(i).pos_x;
            int y = nextPossibleFields.get(i).pos_y;
            if (game_Field.get(y).get(x).field_figur != null) {
                if (game_Field.get(y).get(x).field_figur.name.equals("K")) {
                    statusLabel.setText("Schach!");
                    is_check = true;
                }
            }
        }
//        }
    }

    // Ueberpruefen, ob Koenig geschlagen ist
    private void check_kingstatus(ArrayList<ArrayList<Field>> Game_Field, Player player) throws IOException {
        // Wenn ein Koenig auf der Checked Position steht und die Spielerfarbe
        // hat, wird Spieler als geschlagen gesetzt und im naechsten Schritt als
        // Verlierer ausgegeben
        if (Game_Field.get(0).get(5).field_figur != null && (Game_Field.get(0).get(5).field_figur.checked == true
                & Game_Field.get(0).get(5).field_figur.color == player.color)) {
            player.checked = true;
            onGiveUpButton("König wurde geschlagen. ");
        }
        if (Game_Field.get(9).get(5).field_figur != null && (Game_Field.get(9).get(5).field_figur.checked == true
                & Game_Field.get(9).get(5).field_figur.color == player.color)) {
            player.checked = true;
            onGiveUpButton("König wurde geschlagen. ");
        }

    }

    // Wenn Bauer zur Grundlinie durchgezogen ist
    private void pawn_auto_check(ArrayList<ArrayList<Field>> Game_Field) {
        for (int x_axis = 1; x_axis <= 8; x_axis++) {
            if (Game_Field.get(1).get(x_axis).field_figur != null) {
                if ("B".equals(Game_Field.get(1).get(x_axis).field_figur.name)) {
                    int check_x = Game_Field.get(1).get(x_axis).field_figur.checkedposition_x;
                    int check_y = Game_Field.get(1).get(x_axis).field_figur.checkedposition_y;
                    Game_Field.get(1).get(x_axis).field_figur.checked = true;
                    Game_Field.get(check_y).get(check_x).deliver(Game_Field.get(1).get(x_axis).field_figur, check_x,
                            check_y);
                    Game_Field.get(1).get(x_axis).deliver_empty();
                    gameErrorLabel.setText("Ihr durchgezogener Bube wurde automatisch vom Spielfeld genommen");
                    // Aufwerten
                    promotePawn(chess_board, x_axis, 9);
                }
            }
        }
        for (int x_axis = 1; x_axis <= 8; x_axis++) {
            if (Game_Field.get(8).get(x_axis).field_figur != null) {
                if ("B".equals(Game_Field.get(8).get(x_axis).field_figur.name)) {
                    int check_x = Game_Field.get(8).get(x_axis).field_figur.checkedposition_x;
                    int check_y = Game_Field.get(8).get(x_axis).field_figur.checkedposition_y;
                    Game_Field.get(8).get(x_axis).field_figur.checked = true;
                    Game_Field.get(check_y).get(check_x).deliver(Game_Field.get(8).get(x_axis).field_figur, check_x,
                            check_y);
                    Game_Field.get(8).get(x_axis).deliver_empty();
                    gameErrorLabel.setText("Ihr durchgezogener Bube wurde automatisch vom Spielfeld genommen");
                    // Aufwerten
                    promotePawn(chess_board, x_axis, 0);
                }
            }
        }
    }

    // Bauer aufwerten, solange eine geschlagene Figur vorhanden ist.
    private void promotePawn(ArrayList<ArrayList<Field>> chess_board, int x_axis, int y_axis) {
        List<String> choices = new ArrayList<>();
        Map<String, ArrayList<Integer>> promotionmap = new HashMap<>();
        int bishopcounter = 1;
        int knightcounter = 1;
        int towercounter = 1;
        for (int n = 1; n <= 8; n++) {
            if (chess_board.get(y_axis).get(n).field_figur != null) {
                // Je nach Figur in Liste hinzufügen mit Namen, dann in Hashmap den Namen als Key, 
                // x und y Werte in seperate ArrayList
                switch (chess_board.get(y_axis).get(n).field_figur.name) {
                    case "D":
                        choices.add("Dame");
                        ArrayList<Integer> fields = new ArrayList<>();
                        fields.add(n);
                        fields.add(y_axis);
                        promotionmap.put("Dame", fields);
                        break;
                    case "L":
                        choices.add("Läufer" + bishopcounter);
                        ArrayList<Integer> bishopfields = new ArrayList<>();
                        bishopfields.add(n);
                        bishopfields.add(y_axis);
                        promotionmap.put("Läufer" + bishopcounter, bishopfields);
                        bishopcounter++;
                        break;
                    case "S":
                        choices.add("Springer" + knightcounter);
                        ArrayList<Integer> knightfields = new ArrayList<>();
                        knightfields.add(n);
                        knightfields.add(y_axis);
                        promotionmap.put("Springer" + knightcounter, knightfields);
                        knightcounter++;
                        break;
                    case "T":
                        choices.add("Turm" + towercounter);
                        ArrayList<Integer> towerfields = new ArrayList<>();
                        towerfields.add(n);
                        towerfields.add(y_axis);
                        promotionmap.put("Turm" + towercounter, towerfields);
                        towercounter++;
                        break;
                    default:
                        break;
                }
            }
        }
        if (!choices.isEmpty()) {
            // Dialog Fenster: Icon, Position, Text
            ChoiceDialog<String> promotionDialog = new ChoiceDialog<>(choices.get(0), choices);
            promotionDialog.setTitle("Bauer aufwerten");
            promotionDialog.setHeaderText(null);
            Stage iconstage = (Stage) promotionDialog.getDialogPane().getScene().getWindow();
            iconstage.getIcons().add(new Image("/pictures/chessboard.png"));
            promotionDialog.setContentText("Wählen Sie die Figur aus, zu dem \nder Bauer aufgewertet werden soll:");
            promotionDialog.getDialogPane().lookupButton(ButtonType.CANCEL).setDisable(true);
            double w_x = chessBoardpane.getScene().getWindow().getX();
            double w_y = chessBoardpane.getScene().getWindow().getY();
            double w_width = chessBoardpane.getScene().getWindow().getWidth();
            double w_height = chessBoardpane.getScene().getWindow().getHeight();
            double a_width = promotionDialog.getDialogPane().getWidth();
            promotionDialog.setX(w_x + (w_width / 2 - a_width / 2));
            promotionDialog.setY(w_y + (w_height / 2 - 100));

            Optional<String> result = promotionDialog.showAndWait();
            // wenn eine Figur ausgewählt wurde
            if (result.isPresent()) {
                int checkFigurField[] = new int[2];
                checkFigurField[0] = promotionmap.get(result.get()).get(0);
                checkFigurField[1] = promotionmap.get(result.get()).get(1);
                move_figure(chess_board, targetField, checkFigurField);
            }
        }
    }

}
