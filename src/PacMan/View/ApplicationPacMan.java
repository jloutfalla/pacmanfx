package PacMan.View;

import PacMan.Model.Direction;
import PacMan.View.Pane.Menu;
import PacMan.View.Pane.Plateau;
import PacMan.Model.Jeu;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Observable;
import java.util.Observer;

public class ApplicationPacMan extends Application {
    private Stage stage;
    private AudioClip acBeginning;

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Importing the font that will be used in the game
        Font.loadFont(System.class.getResource("/fonts/alterebro-pixel-font.ttf").toExternalForm(), 20);

        stage = primaryStage;

        acBeginning = new AudioClip(System.class.getResource("/sound/pacman_beginning.wav").toExternalForm());
        acBeginning.play();
        acBeginning.setCycleCount(AudioClip.INDEFINITE);

        setMenuOnStage();

        primaryStage.setTitle("PacMan FX");
        primaryStage.setMinWidth(500);
        primaryStage.setMinHeight(500);
        primaryStage.getIcons().add(new Image(System.class.getResourceAsStream("/icons/logo.png")));
        primaryStage.show();
    }

    private void setMenuOnStage() {
        Menu menu = new Menu();

        menu.playButton.setOnMouseClicked((click) -> {
            acBeginning.stop();
            setPlateauOnStage();

        });

        menu.exitButton.setOnMouseClicked((click) -> {
            Platform.exit();
        });

        Scene scene = new Scene(menu);

        stage.setScene(scene);
    }

    private void setPlateauOnStage() {
        Plateau plateau = new Plateau();
        Scene scene = new Scene(plateau);
        Jeu jeu = plateau.getJeu();

        stage.setScene(scene);

        scene.setOnKeyPressed(key -> {
            Direction direction;
            switch(key.getCode()) {
                case UP:
                case Z:
                    direction = Direction.UP;
                    break;

                case DOWN:
                case S:
                    direction = Direction.DOWN;
                    break;

                case LEFT:
                case Q:
                    direction = Direction.LEFT;
                    break;

                case RIGHT:
                case D:
                    direction = Direction.RIGHT;
                    break;

                default:
                    direction = Direction.NOT_A_DIRECTION;
                    break;
            }

            jeu.deplacer(direction);

        });

        jeu.addObserver(new Observer() {
            @Override
            public void update(Observable observable, Object o) {
                System.out.println("UPDATE !!!!");
                plateau.draw();

                if (plateau.getJeu().finPartie()) {
                     setMenuOnStage();
                }
            }
        });

        /**
         * Stop all thread to properly close the game
         */
        stage.setOnCloseRequest((e) -> {
            jeu.pacmanThread.stop();
            for(Thread t : jeu.ghostThreads)
                t.stop();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}