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
  public static final int START_XPOSITION = 20;
  public static final int START_YPOSITION = LIVES_YPOSITION;

  private Scene myScene;
  private Ball ball; // TODO extension: List<Ball> myBalls, to accomodate multi-ball powerups
  private Paddle paddle;
  private Group root;
  private boolean isPaused;
  private Text pauseText;

  public Game(Stage stage) {
    myScene = setupScene();
    stage.setScene(myScene);
    stage.setTitle(TITLE);
    stage.show();

    isPaused = true;
  }

  void beginInfiniteLoop() {
    KeyFrame frame = new KeyFrame(Duration.seconds(SECOND_DELAY), e -> step(SECOND_DELAY));
    Timeline animation = new Timeline();
    animation.setCycleCount(Timeline.INDEFINITE);
    animation.getKeyFrames().add(frame);
    animation.play();
  }

  private Scene setupScene () {
    root = new Group();

    initializeNewBallAndPaddle();
    initializeLivesText();
    initializeStartText();

    Scene scene = new Scene(root, SCENE_SIZE, SCENE_SIZE, BACKGROUND);
    scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
    return scene;
  }

  private void initializeStartText() {
    pauseText = new Text();
    pauseText.setText(START_TITLE);
    pauseText.setX(START_XPOSITION);
    pauseText.setY(START_YPOSITION);
    pauseText.setFont(new Font(TEXT_FONT, TEXT_SIZE));
    pauseText.setFill(TEXT_COLOR);

    root.getChildren().add(pauseText);
  }

  private void handleKeyInput(KeyCode code) {
    paddle.handleKeyInput(code, isPaused);
    handleSpaceBarInput(code);
    handleRKeyInput(code);
  }

  private void handleSpaceBarInput(KeyCode code) {
    if(code == KeyCode.SPACE && isPaused) {
      pauseText.setText(PAUSE_TITLE);
      root.getChildren().remove(pauseText);
      isPaused = false;
      ball.unpause();
    }
    else if(code == KeyCode.SPACE){
      root.getChildren().add(pauseText);
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

    boolean ballIsValid = ball.updateCoordinatesAndContinue(elapsedTime);
    if(!ballIsValid) {
      // TODO numLives --;
      reset();
    }
  }

  //TODO: update lives from Level
  void reset() {
    isPaused = true;

    root.getChildren().remove(ball);
    root.getChildren().remove(paddle);
    root.getChildren().remove(pauseText);

    initializeNewBallAndPaddle();
    initializeStartText();
  }

  void initializeNewBallAndPaddle() {
    paddle = new Paddle(SCENE_SIZE,SCENE_SIZE);
    ball = new Ball (SCENE_SIZE, paddle);

    root.getChildren().add(ball);
    root.getChildren().add(paddle);
  }

  private Text initializeLivesText() {
    Text text = new Text();
    updateLivesText(text);
    text.setX(LIVES_XPOSITION);
    text.setY(LIVES_YPOSITION);
    text.setFont(new Font(TEXT_FONT, TEXT_SIZE));
    text.setFill(TEXT_COLOR);

    root.getChildren().add(text);

    return text;
  }

  //TODO: replace 0 with lives from Level
  private void updateLivesText(Text text) {
    text.setText(LIVES_TITLE + 0);
  }

  Scene getScene() {
    return myScene;
  }
}
