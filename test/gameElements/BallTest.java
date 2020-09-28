package gameElements;

import static org.junit.jupiter.api.Assertions.assertEquals;

import breakout.Game;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

public class BallTest extends DukeApplicationTest {
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
    press(myScene, KeyCode.SPACE);
  }

  @Test
  void testBallCornerInteraction() {
    ball.setVelocityX(-150);
    ball.setVelocityY(-150);
    ball.setCenterX(15);
    ball.setCenterY(65);

    startAnimation();

    for(int numSteps = 0; numSteps < 2; numSteps ++) {
      game.step(game.getSecondDelay());
    }

    assertEquals(150,ball.getVelocityX());
    assertEquals(150,ball.getVelocityY());

    assertEquals(15, ball.getCenterX());
    assertEquals(65, ball.getCenterY());
  }

  @Test
  void verifyInitialPositionSizeVelocityOfBall() {
    assertEquals(300, ball.getCenterX());
    assertEquals(605, ball.getCenterY());

    assertEquals(15, ball.getRadius());

    assertEquals(0, ball.getVelocityX());
    assertEquals(150, ball.getVelocityY());
  }

  @Test
  void resetBallPosition() {
    ball.setCenterX(20);
    ball.setCenterY(625);
    ball.setVelocityX(0);
    ball.setVelocityY(150);

    startAnimation();

    for(int numSteps = 0; numSteps < 5; numSteps ++) {
      javafxRun(() -> game.step(game.getSecondDelay()));
    }

    ball = lookup("#ball").query();
    assertEquals(300,ball.getCenterX());
    assertEquals(605,ball.getCenterY());
  }
}
