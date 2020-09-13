package breakout;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
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

  private Scene myScene;
  private Ball ball;
  private Paddle paddle;

  public Game(Stage stage) {
    myScene = setupScene(SCENE_SIZE, SCENE_SIZE, BACKGROUND);
    stage.setScene(myScene);
    stage.setTitle(TITLE);
    stage.show();

    KeyFrame frame = new KeyFrame(Duration.seconds(SECOND_DELAY), e -> step(SECOND_DELAY));
    Timeline animation = new Timeline();
    animation.setCycleCount(Timeline.INDEFINITE);
    animation.getKeyFrames().add(frame);
    animation.play();
  }

  private Scene setupScene (int sceneWidth, int sceneHeight, Paint sceneBackground) {
    Group root = new Group();

    paddle = new Paddle(sceneWidth, sceneHeight);
    ball = new Ball (sceneWidth, paddle);

    root.getChildren().add(paddle);
    root.getChildren().add(ball);

    Scene scene = new Scene(root, sceneWidth, sceneHeight, sceneBackground);
    return scene;
  }


  void step (double elapsedTime) {
    System.out.println("I am taking a step");

    ball.updateCoordinates(elapsedTime);
    //paddle.updatePaddleCoordinates();
  }
}
