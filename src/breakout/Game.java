package breakout;

import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;
import text.GameOverText;
import text.LivesText;
import text.PauseText;

/***
 * Purpose: Creates a new BlockConfiguration instance for each text file. Create a new
 * Level for each BlockConfiguration. Iterates through all Levels (or
 * at least until a Game Over) and displays a "You Won" screen at the end.
 *
 * Methods: start, createBlockConfiguration, createLevels, runLevels, gameOverDisplay, youWonDisplay
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

  private Level gameLevel;
  private GameOverText gameOverText;
  private boolean gameIsLost;

  public Game(Stage stage) {
    gameScene = setupScene();
    stage.setScene(gameScene);
    stage.setTitle(TITLE);
    stage.show();
  }

  void beginInfiniteLoop() {
    KeyFrame frame = new KeyFrame(Duration.seconds(SECOND_DELAY), e -> step(SECOND_DELAY));
    Timeline animation = new Timeline();
    animation.setCycleCount(Timeline.INDEFINITE);
    animation.getKeyFrames().add(frame);
    animation.play();
  }

  void step (double elapsedTime) {
    if(!gameIsLost) {
      boolean ballIsValid = gameLevel.isBallValid(elapsedTime);
      if(!ballIsValid && gameLevel.getLives() <= 1) {
        gameOver();
      }
      else if (!ballIsValid) {
        gameLevel.resetCurrentLevel();
      }
    }
  }

  private Scene setupScene () {
    gameRoot = new Group();

    startGameAtLevelOne();
    gameOverText = new GameOverText(gameRoot);

    Scene scene = new Scene(gameRoot, SCENE_SIZE, SCENE_SIZE, BACKGROUND);
    scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
    return scene;
  }

  private void handleKeyInput(KeyCode code) {
    if(code == KeyCode.SPACE && gameIsLost) {
      startGameAtLevelOne();
      System.out.println("level1");
    }
    gameLevel.handleKeyInput(code);
  }

  private void gameOver() {
    gameOverText.gameOverUpdate();
    gameLevel.removeLevel();
    gameIsLost = true;
  }

  void startGameAtLevelOne() {
    gameIsLost = false;
    gameLevel = new Level(gameRoot,1);
    gameLevel.setLives(Level.INITIAL_NUMBER_LIVES);
    gameLevel.updateBlocks(SCENE_SIZE, SCENE_SIZE);
    ArrayList<Block> allBlocks = gameLevel.getAllBlocks(SCENE_SIZE, SCENE_SIZE);
    gameRoot.getChildren().addAll(allBlocks);
  }

  void setLevel(int levelNumber) { this.gameLevel = new Level(gameRoot,levelNumber); }

  Level getGameLevel() { return gameLevel; }

  Scene getScene() { return gameScene; }
}
