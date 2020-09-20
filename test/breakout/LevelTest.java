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

  void startAnimation() {
    Scene myScene = game.getScene();
    press(myScene,KeyCode.SPACE);
  }

  @Test
  void testBallPaddleInteraction() {
    double startingYBall = ball.getCenterY();

    startAnimation();

    for(int numSteps = 0; numSteps < 3; numSteps ++) {
      game.step(Game.SECOND_DELAY);
    }

    assertEquals(300,ball.getCenterX());
    assertTrue(ball.getCenterY()<startingYBall);
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

  @Test
  void paddleMissesBallCheckPositionAndLifeReset() {
    startAnimation();
    ballTouchesGround();
    ball = lookup("#ball").query();
    assertEquals(2,game.getGameLevel().getLives());

    assertEquals(0,ball.getVelocityX());
    assertEquals(150,ball.getVelocityY());

    assertEquals(300, ball.getCenterX());
    assertEquals(555, ball.getCenterY());
  }

  private void ballTouchesGround() {
    ball = lookup("#ball").query();
    ball.setCenterX(20);
    ball.setCenterY(585);
    ball.setVelocityX(0);
    ball.setVelocityY(150);

    for(int numSteps = 0; numSteps < 3; numSteps ++) {
      javafxRun(() -> game.step(Game.SECOND_DELAY));
    }
  }
}
