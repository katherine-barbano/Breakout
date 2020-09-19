package breakout;

import java.util.ArrayList;
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

  private Scene setupScene () {
    gameRoot = new Group();

    startGameAtLevelOne();

    gamePauseText = new PauseText(gameRoot);
    gameOverText = new GameOverText(gameRoot);
    gameLivesText = new LivesText(gameLevel.getLives(),gameRoot);

    initializeNewBallAndPaddle();
    addFieldsToRoot();

    Scene scene = new Scene(gameRoot, SCENE_SIZE, SCENE_SIZE, BACKGROUND);
    scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
    return scene;
  }

  void beginInfiniteLoop() {
    KeyFrame frame = new KeyFrame(Duration.seconds(SECOND_DELAY), e -> step(SECOND_DELAY));
    Timeline animation = new Timeline();
    animation.setCycleCount(Timeline.INDEFINITE);
    animation.getKeyFrames().add(frame);
    animation.play();
  }

  private void handleKeyInput(KeyCode code) {
    gamePaddle.handleKeyInput(code, gameIsPaused);
    if(code == KeyCode.SPACE) {
      handleSpaceBarInput();
    }
    else if(code == KeyCode.R) {
      handleRKeyInput();
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
    gameBall.pause();
  }

  private void unpauseGame() {
    gamePauseText.endPause();
    gameIsPaused = false;
    gameBall.unpause();
  }

  private void handleRKeyInput() {
    resetPosition();
  }


  void step (double elapsedTime) {
    boolean ballIsValid = gameBall.updateCoordinatesAndContinue(elapsedTime);
    if(!ballIsValid && gameLevel.getLives() > 0) {
      retryLevel();
    }
    else if (!ballIsValid) {
      resetPosition();
    }
  }

  void retryLevel() {
    if (gameLevel.getLives() == 0) {
      gameOverText.gameOverUpdate();
      resetPosition();
      return;
    }
    gameLevel.decreaseLivesByOne();

    resetEntireLevel();
    gameLivesText.updateLives(gameLevel.getLives());
  }

  void resetPosition() {
    gameIsPaused = true;
    gamePauseText = new PauseText(gameRoot);

    gamePaddle.resetPaddle();
    gameBall.resetBall();
  }

  void resetEntireLevel() {
    resetPosition();
    removeBlocks();

    startGameAtLevelOne();
  }

  private void removeBlocks() {
    ArrayList<Block> allBlocks = gameLevel.getAllBlocks(SCENE_SIZE, SCENE_SIZE);
    for (Block block : allBlocks) gameRoot.getChildren().remove(block);
  }

  void initializeNewBallAndPaddle() {
    gamePaddle = new Paddle(SCENE_SIZE,SCENE_SIZE);
    gameBall = new Ball (SCENE_SIZE, gamePaddle, gameLevel.getLevelConfiguration());
  }

  void startGameAtLevelOne() {
    gameLevel = new Level(1);
    gameLevel.setLives(Level.INITIAL_NUMBER_LIVES);
    gameLevel.updateBlocks(SCENE_SIZE, SCENE_SIZE);
    ArrayList<Block> allBlocks = gameLevel.getAllBlocks(SCENE_SIZE, SCENE_SIZE);
    gameRoot.getChildren().addAll(allBlocks);
  }

  private void addFieldsToRoot() {
    gameRoot.getChildren().add(gameBall);
    gameRoot.getChildren().add(gamePaddle);
  }

  Scene getScene() { return gameScene; }
  void setLevel(int levelNumber) { this.gameLevel = new Level(levelNumber); }
  Level getGameLevel() { return gameLevel; }
}
