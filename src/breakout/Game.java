package breakout;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;
import text.GameOverText;

/***
 * Handles the game flow over multiple Levels. Creates and initializes the Scene, Group, and
 * Nodes, and handles key input as well as losing and winning. Creates and runs the game through all Levels
 * within a directory in the data folder.
 */
public class Game {

  public static final String TITLE = "Breakout";
  public static final int SCENE_SIZE = 600;
  public static final int FRAMES_PER_SECOND = 60;
  public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
  public static final Paint BACKGROUND = Color.AZURE;
  public static final int NUMBER_OF_LEVELS = 1;

  private Scene gameScene;
  private Group gameRoot;

  private Level gameLevel;//TODO: make this a list of levels
  private GameOverText gameOverText;

  /***
   * Constructor initializes gameScene and gameRoot, including key inputs,
   * and shows the Scene on the screen.
   * @param stage Stage to set the JavaFX game on
   */
  public Game(Stage stage) {
    gameScene = setupScene();
    stage.setScene(gameScene);
    stage.setTitle(TITLE);
    stage.show();
  }

  /***
   * Used in Main.java to start running the Game in an infinite loop.
   * Should not be used for JUnit tests.
   */
  void beginInfiniteLoop() {
    KeyFrame frame = new KeyFrame(Duration.seconds(SECOND_DELAY), e -> step(SECOND_DELAY));
    Timeline animation = new Timeline();
    animation.setCycleCount(Timeline.INDEFINITE);
    animation.getKeyFrames().add(frame);
    animation.play();
  }

  /***
   * Handles the actions of the game for a single time step for a given second delay.
   * Handles resetting the game if the player loses the entire game, and handles resetting the
   * current level if the ball touches the ground but there are still lives left.
   * @param elapsedTime second delay
   */
  public void step (double elapsedTime) {
    if(gameLevel.gameIsLost()) {
      gameOver();
    }
    else {
      boolean ballIsValid = gameLevel.isBallValid(elapsedTime);
      if (!ballIsValid) {
        gameLevel.resetCurrentLevel();
      }
    }
  }

  /***
   * Sets up the gameRoot, starts the game at the first Level, and sets up event handler for key inputs
   * @return Scene to be set as the gameScene
   */
  private Scene setupScene () {
    gameRoot = new Group();

    startGameAtLevelOne();
    gameOverText = new GameOverText(gameRoot);

    Scene scene = new Scene(gameRoot, SCENE_SIZE, SCENE_SIZE, BACKGROUND);
    scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
    return scene;
  }

  private void handleKeyInput(KeyCode code) {
    if(code == KeyCode.SPACE && gameLevel.gameIsLost()) {
      startGameAtLevelOne();
    }
    else {
      gameLevel.handleKeyInput(code);
    }
  }

  private void gameOver() {
    gameOverText.gameOverUpdate(gameLevel.levelIsWon());
    gameLevel.removeLevel();
  }

  /***
   * Initiates and runs the first Level in the Game.
   * Assumes the first level has the number "1" in its file name, and that no other levels have
   * the number 1 in the file name.
   */
  void startGameAtLevelOne() {
    gameLevel = new Level(gameRoot,1);
  }

  /***
   *
   * @param levelNumber
   */
  void setLevel(int levelNumber) { this.gameLevel = new Level(gameRoot,levelNumber); }

  /***
   *
   * @return
   */
  public Level getGameLevel() { return gameLevel; }

  /***
   *
   * @return
   */
  public Scene getScene() { return gameScene; }

  /***
   *
   * @return
   */
  public GameOverText getGameOverText() {
    return gameOverText;
  }

  /***
   *
   * @return
   */
  public Group getRoot() {
    return gameRoot;
  }
}
