package gameElements;

import static org.junit.jupiter.api.Assertions.assertEquals;

import breakout.Game;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

public class PowerUpTest extends DukeApplicationTest {
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
  void testPowerUpsOnFirstRow(){
    startAnimation();
    for(int numSteps = 0; numSteps < 150; numSteps ++) {
      javafxRun(() -> game.step(Game.SECOND_DELAY));
    }
    assertEquals(140, paddle.getWidth());
  }

  // assumes two paddlePowerUps are hit; that is,
  // shows the paddle will only extend its width once.
  @Test
  void testPaddleOnlyExtendsOnce() {
    startAnimation();
    for(int numSteps = 0; numSteps < 300; numSteps ++) {
      javafxRun(() -> game.step(Game.SECOND_DELAY));
    }
    assertEquals(140, paddle.getWidth());
  }

  @Test
  void testPaddleWidthResets() {
    startAnimation();
    for(int numSteps = 0; numSteps < 150; numSteps ++) {
      javafxRun(() -> game.step(Game.SECOND_DELAY));
    }
    game.getCurrentGameLevel().decreaseLivesByOne();
    assertEquals(70, paddle.getWidth());
  }

  @Test
  void testBallSlowsDown() {
    // TODO
  }

  @Test
  void testBallSlowsDownOnce() {
    // TODO
  }

  @Test
  void testBallBreakerWorks() {
    // TODO
  }

  @Test
  void testBallBreakerAppliesOnce() {
    // TODO
  }
}
