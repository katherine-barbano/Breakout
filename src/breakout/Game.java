package breakout;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
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
  public static final int SIZE = 400;
  public static final int FRAMES_PER_SECOND = 60;
  public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
  public static final Paint BACKGROUND = Color.AZURE;
  public static final Paint HIGHLIGHT = Color.OLIVEDRAB;
  public static final Paint RACER_COLOR = Color.HOTPINK;
  public static final int RACER_SIZE = 30;
  public static final int RACER_SPEED = 40;
  public static final Paint MOVER_COLOR = Color.PLUM;
  public static final int MOVER_SIZE = 50;
  public static final int MOVER_ROUNDING = 15;
  public static final int MOVER_SPEED = 5;
  public static final Paint GROWER_COLOR = Color.BISQUE;
  public static final double GROWER_RATE = 1.1;
  public static final int GROWER_SIZE = 50;
  public static final int VERTICAL_OFFSET = 80;

  // some things needed to remember during game
  private Scene myScene;
  private Rectangle myMover;
  private Rectangle myGrower;
  private Circle myRacer;


  public Game(Stage stage) {
    myScene = setupScene(SIZE, SIZE, BACKGROUND);
    stage.setScene(myScene);
    stage.setTitle(TITLE);
    stage.show();

    KeyFrame frame = new KeyFrame(Duration.seconds(SECOND_DELAY), e -> step(SECOND_DELAY));
    Timeline animation = new Timeline();
    animation.setCycleCount(Timeline.INDEFINITE);
    animation.getKeyFrames().add(frame);
    animation.play();
  }

  // Create the game's "scene": what shapes will be in the game and their starting properties
  Scene setupScene (int width, int height, Paint background) {
    // create one top level collection to organize the things in the scene
    Group root = new Group();
    // make some shapes and set their properties
    myRacer = new Circle(width / 2, height / 2, RACER_SIZE / 2);
    myRacer.setFill(RACER_COLOR);
    myRacer.setId("racer");
    // x and y represent the top left corner, so center it in window
    myMover = new Rectangle(width / 2 - MOVER_SIZE / 2, height / 2 - VERTICAL_OFFSET, MOVER_SIZE, MOVER_SIZE);
    myMover.setArcWidth(MOVER_ROUNDING);
    myMover.setArcHeight(MOVER_ROUNDING);
    myMover.setFill(MOVER_COLOR);
    myMover.setId("mover");
    myGrower = new Rectangle(width / 2 - GROWER_SIZE / 2, height / 2 + VERTICAL_OFFSET, GROWER_SIZE, GROWER_SIZE);
    myGrower.setFill(GROWER_COLOR);
    myGrower.setId("grower");
    // order added to the group is the order in which they are drawn (so last one is on top)
    root.getChildren().add(myMover);
    root.getChildren().add(myGrower);
    root.getChildren().add(myRacer);
    // create a place to see the shapes
    Scene scene = new Scene(root, width, height, background);
    return scene;
  }


  void step (double elapsedTime) {
    System.out.println("I am taking a step");
  }
}
