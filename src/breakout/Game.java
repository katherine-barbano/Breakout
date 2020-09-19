package breakout;

import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

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
  public static final String START_TITLE = "Click the space bar to start!";
  public static final String PAUSE_TITLE="Paused. Resume with space bar";
  public static final String GAME_OVER_MESSAGE = "Game Over! Click the space bar to play again.";
  public static final int START_XPOSITION = 20;
  public static final int START_YPOSITION = LIVES_YPOSITION;
  public static final int NUMBER_OF_LEVELS = 1;

  private Scene gameScene;
  private Level gameLevel;
  private Ball gameBall; // TODO extension: List<Ball> myBalls, to accomodate multi-gameBall powerups
  private Paddle gamePaddle;
  private Group gameRoot;
  private boolean gameIsPaused;
  private Text gamePauseText;
  private Text gameOverText;
  private Text gameLivesText;

  public Game(Stage stage) {
    gameIsPaused = true;
    gamePauseText = new Text();
    gameOverText = new Text();
    gameLivesText = new Text();

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

  private Scene setupScene () {
    gameRoot = new Group();

    startGameAtLevelOne();
    initializeNewBallAndPaddle();
    initializeLivesText();
    initializeStartText();
    addFieldsToRoot();

    Scene scene = new Scene(gameRoot, SCENE_SIZE, SCENE_SIZE, BACKGROUND);
    scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
    return scene;
  }

  private void handleKeyInput(KeyCode code) {
    gamePaddle.handleKeyInput(code, gameIsPaused);
    handleSpaceBarInput(code);
    handleRKeyInput(code);
  }

  private void handleSpaceBarInput(KeyCode code) {
    if(code == KeyCode.SPACE && gameIsPaused) {
      gamePauseText.setText("");
      gameIsPaused = false;
      gameBall.unpause();
    }
    else if(code == KeyCode.SPACE){
      gamePauseText.setText(PAUSE_TITLE);
      gameIsPaused = true;
      gameBall.pause();
    }
  }

  private void handleRKeyInput(KeyCode code) {
    if(code == KeyCode.R) {
      resetPosition();
    }
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
      endGameText();
      resetPosition();
      return;
    }
    gameLevel.decreaseLivesByOne();

    resetEntireLevel();
    updateLivesText();
  }

  void resetPosition() {
    gameIsPaused = true;
    gamePauseText.setText(START_TITLE);

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

  private void initializeLivesText() {
    gameLivesText.setText(LIVES_TITLE + gameLevel.getLives());
    gameLivesText.setX(LIVES_XPOSITION);
    gameLivesText.setY(LIVES_YPOSITION);
    gameLivesText.setFont(new Font(TEXT_FONT, TEXT_SIZE));
    gameLivesText.setFill(TEXT_COLOR);
  }

  // TODO
  private void updateLivesText() {
    gameLivesText.setText(LIVES_TITLE + gameLevel.getLives());
    gameLivesText.setX(LIVES_XPOSITION);
    gameLivesText.setY(LIVES_YPOSITION);
    gameLivesText.setFont(new Font(TEXT_FONT, TEXT_SIZE));
    gameLivesText.setFill(TEXT_COLOR);
  }

  private void initializeStartText() {
    if (gameRoot.getChildren().contains(gameOverText)) {
      gameOverText.setText("");
    }
    gamePauseText = writeStartText(gamePauseText, START_TITLE);
  }

  private void endGameText() {
    if (gameRoot.getChildren().contains(gamePauseText)) {
      gamePauseText.setText("");
    }
    writeStartText(gameOverText, GAME_OVER_MESSAGE);
  }

  private Text writeStartText(Text text, String message) {
    text = new Text();
    text.setText(message);
    text.setX(START_XPOSITION);
    text.setY(START_YPOSITION);
    text.setFont(new Font(TEXT_FONT, TEXT_SIZE));
    text.setFill(TEXT_COLOR);

    return text;
  }

  private void addFieldsToRoot() {
    gameRoot.getChildren().add(gameLivesText);
    gameRoot.getChildren().add(gameBall);
    gameRoot.getChildren().add(gamePaddle);
    gameRoot.getChildren().add(gamePauseText);
  }

  Scene getScene() { return gameScene; }
  void setLevel(int levelNumber) { this.gameLevel = new Level(levelNumber); }
  Level getGameLevel() { return gameLevel; }
}
