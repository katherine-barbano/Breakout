package gameElements;

import breakout.Game;
import gameElements.Ball;
import gameElements.BlockConfiguration;
import gameElements.Paddle;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import util.DukeApplicationTest;

public class BlockConfigurationTest extends DukeApplicationTest {
  private BlockConfiguration testConfiguration;
  private Block block;
  private Game game;
  private Ball ball;
  private Paddle paddle;

  @Override
  public void start (Stage stage) {
    game = new Game(stage);
    testConfiguration = game.getCurrentGameLevel().getLevelConfiguration();

    ball = lookup("#ball").query();
    paddle = lookup("#paddle").query();
  }

  public void startAnimation() {
    Scene myScene = game.getScene();
    press(myScene, KeyCode.SPACE);
  }


  // FIXME: both of these tests are recording 72, which means the blockConfig
  // FIXME: is counting 0 hardness blocks which it should not do
  @Test
  void testNumBlocksRemaining() {
    assertEquals(60, testConfiguration.getNumberOfBlocksRemaining());
  }

  @Test
  void testNumBlocksRemainingWhenBlockIsRemoved() {
    startAnimation();

    for(int numSteps = 0; numSteps < 150; numSteps ++) {
      javafxRun(() -> game.step(Game.SECOND_DELAY));
    }
    assertEquals(59, testConfiguration.getNumberOfBlocksRemaining());
  }
}
