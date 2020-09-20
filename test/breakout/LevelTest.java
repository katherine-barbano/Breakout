package breakout;

import gameElements.Ball;
import gameElements.Block;
import gameElements.Paddle;
import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import util.DukeApplicationTest;

public class LevelTest extends DukeApplicationTest {
  private Level level;
  private Game game;

  private Ball ball;
  private Paddle paddle;

  @Override
  public void start (Stage stage) {
    game = new Game(stage);

    ball = lookup("#ball").query();
    paddle = lookup("#paddle").query();
  }

  public void startAnimation() {
    Scene myScene = game.getScene();
    press(myScene,KeyCode.SPACE);
  }


  @Test
  public void verifyInitialPositionSizeVelocityOfBall() {
    assertEquals(300, ball.getCenterX());
    assertEquals(555, ball.getCenterY());

    assertEquals(15, ball.getRadius());

    assertEquals(0, ball.getVelocityX());
    assertEquals(150, ball.getVelocityY());
  }

  @Test
  public void verifyInitialPositionSizeOfPaddle() {
    assertEquals(265, paddle.getX());
    assertEquals(570, paddle.getY());

    assertEquals(70, paddle.getWidth());
    assertEquals(10, paddle.getHeight());
  }

  @Test
  public void testMovePaddle() {
    Scene myScene = game.getScene();

    startAnimation();

    for(int expectedXCoordinate = 255; expectedXCoordinate>=0; expectedXCoordinate=expectedXCoordinate-10) {
      press(myScene, KeyCode.LEFT);
      assertEquals(expectedXCoordinate,paddle.getX());
    }

    press(myScene, KeyCode.LEFT);
    assertTrue(paddle.getX()>=0);

    for(double expectedXCoordinate = paddle.getX()+10; expectedXCoordinate<=myScene.getWidth()-paddle.getWidth(); expectedXCoordinate=expectedXCoordinate+10) {
      press(myScene, KeyCode.RIGHT);
      assertEquals(expectedXCoordinate,paddle.getX());
    }

    press(myScene, KeyCode.RIGHT);
    assertTrue(paddle.getX()<=game.getScene().getWidth());

    assertEquals(570, paddle.getY());
  }

  @Test
  public void testBallCornerInteraction() {
    ball.setVelocityX(-150);
    ball.setVelocityY(-150);
    ball.setCenterX(15);
    ball.setCenterY(15);

    startAnimation();

    for(int numSteps = 0; numSteps < 2; numSteps ++) {
      game.step(Game.SECOND_DELAY);
    }

    assertEquals(150,ball.getVelocityX());
    assertEquals(150,ball.getVelocityY());

    assertEquals(15, ball.getCenterX());
    assertEquals(15, ball.getCenterY());
  }

  @Test
  public void testBallPaddleInteraction() {
    double startingYBall = ball.getCenterY();

    startAnimation();

    for(int numSteps = 0; numSteps < 3; numSteps ++) {
      game.step(Game.SECOND_DELAY);
    }

    assertEquals(300,ball.getCenterX());
    assertTrue(ball.getCenterY()<startingYBall);
  }

  @Test
  public void resetBallPosition() {
    ball.setCenterX(20);
    ball.setCenterY(585);
    ball.setVelocityX(0);
    ball.setVelocityY(150);

    startAnimation();

    for(int numSteps = 0; numSteps < 5; numSteps ++) {
      javafxRun(() -> game.step(Game.SECOND_DELAY));
    }

    ball = lookup("#ball").query();
    assertEquals(300,ball.getCenterX());
    assertEquals(555,ball.getCenterY());
  }

  @Test
  void testAllBlocks() {
    Group testGroup = new Group();
    level = new Level(testGroup,"testOneInput");
    int width = (int) game.getScene().getWidth();
    int height = (int) game.getScene().getHeight();
    ArrayList<Block> oneBlock = level.getAllBlocks(width, height);
    assertTrue(oneBlock.size() == 1);
    Block block = oneBlock.get(0);
    assertEquals(block.getBlockColor(), Color.PALETURQUOISE);
    assertEquals(block.getX(), block.getWidth());
  }

}
