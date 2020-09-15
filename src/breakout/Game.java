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
  public static final String LIVES_TITLE = "Lives: ";
  public static final int LIVES_XPOSITION = 500;
  public static final int LIVES_YPOSITION = 40;
  public static final String TEXT_FONT = "ARIAL";
  public static final int TEXT_SIZE = 20;
  public static final Paint TEXT_COLOR = Color.MEDIUMVIOLETRED;
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
    gameScene = setupScene();
    stage.setScene(gameScene);
    stage.setTitle(TITLE);
    stage.show();

    gameIsPaused = true;
    gamePauseText = new Text();
    gameOverText = new Text();
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

    initializeNewBallAndPaddle();
    startGameAtLevelOne();
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
    if(code == KeyCode.SPACE && isPaused) {
      pauseText.setText("");
      isPaused = false;
      ball.unpause();
    }
    else if(code == KeyCode.SPACE){
      pauseText.setText(PAUSE_TITLE);
      isPaused = true;
      ball.pause();
    }
  }

  private void handleRKeyInput(KeyCode code) {
    if(code == KeyCode.R) {
      reset();
    }
  }


  void step (double elapsedTime) {
    // System.out.println("I am taking a step");

    boolean ballIsValid = gameBall.updateCoordinatesAndContinue(elapsedTime);
    if(!ballIsValid && gameLevel.getLives() > 0) {
      retryLevel();
    }
    else if (!ballIsValid) {
      reset();
    }
  }

  void retryLevel() {

    if (gameLevel.getLives() == 0) {
      endGameText();
      reset();
      return;
    }
    gameIsPaused = true;
    gameLevel.decreaseLivesByOne();

    gameRoot.getChildren().remove(gameBall);
    gameRoot.getChildren().remove(gamePaddle);
    gameRoot.getChildren().remove(gamePauseText);

    initializeNewBallAndPaddle();
    updateLivesText();
  }

  void reset() {
    gameIsPaused = true;

    gameRoot.getChildren().remove(gameBall);
    gameRoot.getChildren().remove(gamePaddle);
    gameRoot.getChildren().remove(gamePauseText);
    removeBlocks();

    initializeNewBallAndPaddle();
    initializeStartText();
    startGameAtLevelOne();
  }

  private void removeBlocks() {
    ArrayList<Block> allBlocks = gameLevel.getAllBlocks(SCENE_SIZE, SCENE_SIZE);
    for (Block block : allBlocks) gameRoot.getChildren().remove(block);
  }

  void initializeNewBallAndPaddle() {
    gamePaddle = new Paddle(SCENE_SIZE,SCENE_SIZE);
    gameBall = new Ball (SCENE_SIZE, gamePaddle);

    gameRoot.getChildren().add(gameBall);
    gameRoot.getChildren().add(gamePaddle);
  }

  void startGameAtLevelOne() {
    gameLevel = new Level(1);
    gameLevel.setLives(Level.INITIAL_NUMBER_LIVES);
    gameLevel.updateBlocks(SCENE_SIZE, SCENE_SIZE);
    ArrayList<Block> allBlocks = gameLevel.getAllBlocks(SCENE_SIZE, SCENE_SIZE);
    gameRoot.getChildren().addAll(allBlocks);
  }

  private void initializeLivesText() {
    gameLivesText = new Text();
    gameLivesText.setText(LIVES_TITLE + gameLevel.getLives());
    gameLivesText.setX(LIVES_XPOSITION);
    gameLivesText.setY(LIVES_YPOSITION);
    gameLivesText.setFont(new Font(TEXT_FONT, TEXT_SIZE));
    gameLivesText.setFill(TEXT_COLOR);

    gameRoot.getChildren().add(gameLivesText);
  }

  // TODO
  private void updateLivesText() {
    gameRoot.getChildren().remove(gameLivesText);
    gameLivesText.setText(LIVES_TITLE + gameLevel.getLives());
    gameLivesText.setX(LIVES_XPOSITION);
    gameLivesText.setY(LIVES_YPOSITION);
    gameLivesText.setFont(new Font(TEXT_FONT, TEXT_SIZE));
    gameLivesText.setFill(TEXT_COLOR);

    gameRoot.getChildren().add(gameLivesText);
  }

  private void initializeStartText() {
    if (gameRoot.getChildren().contains(gameOverText)) gameRoot.getChildren().remove(gameOverText);
    writeStartText(gamePauseText, START_TITLE);
  }

  private void endGameText() {
    if (gameRoot.getChildren().contains(gamePauseText)) gameRoot.getChildren().remove(gamePauseText);
    writeStartText(gameOverText, GAME_OVER_MESSAGE);
  }

  private void writeStartText(Text text, String message) {
    text = new Text();
    text.setText(message);
    text.setX(START_XPOSITION);
    text.setY(START_YPOSITION);
    text.setFont(new Font(TEXT_FONT, TEXT_SIZE));
    text.setFill(TEXT_COLOR);

    gameRoot.getChildren().add(text);
  }

  Scene getScene() { return gameScene; }
  void setLevel(int levelNumber) { this.gameLevel = new Level(levelNumber); }
  Level getGameLevel() { return gameLevel; }
}
