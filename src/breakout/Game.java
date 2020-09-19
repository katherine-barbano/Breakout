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
  private Ball gameBall; // TODO extension: List<Ball> myBalls, to accomodate multi-gameBall powerups
  private Paddle gamePaddle;
  private boolean gameIsPaused;

  private PauseText gamePauseText;
  private GameOverText gameOverText;
  private LivesText gameLivesText;

  public Game(Stage stage) {
    gameIsPaused = true;

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
    boolean ballIsValid = gameBall.updateCoordinatesAndContinue(elapsedTime, gameIsPaused);
    if(!ballIsValid && gameLevel.getLives() <= 0) {
      gameOver();
    }
    else if (!ballIsValid) {
      resetCurrentLevel();
    }
  }

  private Scene setupScene () {
    gameRoot = new Group();

    startGameAtLevelOne();

    gamePauseText = new PauseText(gameRoot);
    gameOverText = new GameOverText(gameRoot);
    gameLivesText = new LivesText(gameLevel.getLives(),gameRoot);

    initializeNewBallAndPaddle();

    Scene scene = new Scene(gameRoot, SCENE_SIZE, SCENE_SIZE, BACKGROUND);
    scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
    return scene;
  }

  void initializeNewBallAndPaddle() {
    gamePaddle = new Paddle(gameRoot);
    gameBall = new Ball (gameRoot, gamePaddle, gameLevel.getLevelConfiguration());
  }

  void resetBallAndPaddle() {
    gamePaddle.removePaddle();
    gameBall.removeBall();
    initializeNewBallAndPaddle();
  }

  private void handleKeyInput(KeyCode code) {
    gamePaddle.handleKeyInput(code, gameIsPaused);
    if(code == KeyCode.SPACE) {
      handleSpaceBarInput();
    }
    else if(code == KeyCode.R) {
      resetPosition();
    }
  }

  private void handleSpaceBarInput() {
    if(gameIsPaused) {
      unpauseGame();
    }
    else {
      pauseGame();
    }
  }

  private void pauseGame() {
    gamePauseText.startPause();
    gameIsPaused = true;
  }

  private void unpauseGame() {
    gamePauseText.endPause();
    gameIsPaused = false;
  }

  private void gameOver() {
    gameOverText.gameOverUpdate();
    resetBallAndPaddle();
  }

  private void resetCurrentLevel() {
    resetPosition();
    gameLevel.decreaseLivesByOne();
    gameLivesText.updateLives(gameLevel.getLives());
  }

  private void resetPosition() {
    gameIsPaused = true;
    gamePauseText.removeText();
    gamePauseText = new PauseText(gameRoot);

    resetBallAndPaddle();
  }

  //TODO: @Anna can we remove this method? Because of refactoring for gameOver
  private void removeBlocks() {
    ArrayList<Block> allBlocks = gameLevel.getAllBlocks(SCENE_SIZE, SCENE_SIZE);
    for (Block block : allBlocks) gameRoot.getChildren().remove(block);
  }

  void startGameAtLevelOne() {
    gameLevel = new Level(1);
    gameLevel.setLives(Level.INITIAL_NUMBER_LIVES);
    gameLevel.updateBlocks(SCENE_SIZE, SCENE_SIZE);
    ArrayList<Block> allBlocks = gameLevel.getAllBlocks(SCENE_SIZE, SCENE_SIZE);
    gameRoot.getChildren().addAll(allBlocks);
  }

  void setLevel(int levelNumber) { this.gameLevel = new Level(levelNumber); }
  Level getGameLevel() { return gameLevel; }

  Scene getScene() { return gameScene; }
}
