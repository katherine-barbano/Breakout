package breakout;

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
import org.testfx.osgi.service.TestFx;

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

  private Scene gameScene;
  private Ball gameBall; // TODO extension: List<Ball> myBalls, to accomodate multi-gameBall powerups
  private Paddle gamePaddle;
  private Group gameRoot;
  private boolean gameIsPaused;
  private Text gamePauseText;

  public Game(Stage stage) {
    gameScene = setupScene();
    stage.setScene(gameScene);
    stage.setTitle(TITLE);
    stage.show();

    gameIsPaused = true;
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
    initializeLivesText();
    initializeStartText();

    Scene scene = new Scene(gameRoot, SCENE_SIZE, SCENE_SIZE, BACKGROUND);
    scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
    return scene;
  }

  private void initializeStartText() {
    gamePauseText = new Text();
    gamePauseText.setText(START_TITLE);
    gamePauseText.setX(START_XPOSITION);
    gamePauseText.setY(START_YPOSITION);
    gamePauseText.setFont(new Font(TEXT_FONT, TEXT_SIZE));
    gamePauseText.setFill(TEXT_COLOR);

    gameRoot.getChildren().add(gamePauseText);
  }

  private void handleKeyInput(KeyCode code) {
    gamePaddle.handleKeyInput(code, gameIsPaused);
    handleSpaceBarInput(code);
    handleRKeyInput(code);
  }

  private void handleSpaceBarInput(KeyCode code) {
    if(code == KeyCode.SPACE && gameIsPaused) {
      gamePauseText.setText(PAUSE_TITLE);
      gameRoot.getChildren().remove(gamePauseText);
      gameIsPaused = false;
      gameBall.unpause();
    }
    else if(code == KeyCode.SPACE){
      gameRoot.getChildren().add(gamePauseText);
      gameIsPaused = true;
      gameBall.pause();
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
    if(!ballIsValid) {
      reset();
    }
  }

  //TODO: update lives from Level
  void reset() {
    gameIsPaused = true;

    gameRoot.getChildren().remove(gameBall);
    gameRoot.getChildren().remove(gamePaddle);
    gameRoot.getChildren().remove(gamePauseText);

    initializeNewBallAndPaddle();
    initializeStartText();
  }

  void initializeNewBallAndPaddle() {
    gamePaddle = new Paddle(SCENE_SIZE,SCENE_SIZE);
    gameBall = new Ball (SCENE_SIZE, gamePaddle);

    gameRoot.getChildren().add(gameBall);
    gameRoot.getChildren().add(gamePaddle);
  }

  private Text initializeLivesText() {
    Text text = new Text();
    updateLivesText(text);
    text.setX(LIVES_XPOSITION);
    text.setY(LIVES_YPOSITION);
    text.setFont(new Font(TEXT_FONT, TEXT_SIZE));
    text.setFill(TEXT_COLOR);

    gameRoot.getChildren().add(text);

    return text;
  }

  // TODO
  private void updateLivesText(Text text) {
    text.setText(LIVES_TITLE + 0);
  }

  private void endGameText(Text text) {
    text.setText(GAME_OVER_MESSAGE);
  }

  Scene getScene() {
    return gameScene;
  }
}
