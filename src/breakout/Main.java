package breakout;

import javafx.application.Application;
import javafx.stage.Stage;


/***
 * Calls launch(args) to start the program. Instantiates a new Game object to setup
 * the stage.
 *
 * @author Katherine Barbano
 */
public class Main extends Application {

    /**
     * Start of the program.
     */
    public static void main (String[] args) {
        launch(args);
    }

    /***
     * Instantiates a new Game object to create the stage.
     * @param stage Stage to run Breakout
     */
    @Override
    public void start (Stage stage) {
        Game game = new Game(stage);
        game.beginInfiniteLoop();
    }
}
