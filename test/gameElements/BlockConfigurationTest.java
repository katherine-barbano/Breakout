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
  public static final String GAME_TYPE = "sample_game";
  public static final int LEVEL_ONE_INDEX = 0;

  private Game game;
  private Ball ball;
  private Paddle paddle;
  private BlockConfiguration testConfiguration;

  @Override
  public void start (Stage stage) {
    game = new Game(stage);
    game.setLevelNumber(LEVEL_ONE_INDEX);
    ball = lookup("#ball").query();
    paddle = lookup("#paddle").query();
  }

  public void startAnimation() {
    Scene myScene = game.getScene();
    press(myScene, KeyCode.SPACE);
  }

  @Test
  void testNumBlocksRemaining() {
    testConfiguration = game.getCurrentGameLevel().getLevelConfiguration();
    assertEquals(60, testConfiguration.getNumberOfBlocksRemaining());
  }

  @Test
  void testNumBlocksRemainingWhenBlockIsRemoved() {
    testConfiguration = game.getCurrentGameLevel().getLevelConfiguration();
    startAnimation();

    for(int numSteps = 0; numSteps < 75; numSteps ++) {
      javafxRun(() -> game.step(game.getSecondDelay()));
    }
    assertEquals(59, testConfiguration.getNumberOfBlocksRemaining());
  }

  @Test
  void testPowerUpsExist() {
    testConfiguration = game.getCurrentGameLevel().getLevelConfiguration();
    startAnimation();

    for(int numSteps = 0; numSteps < 75; numSteps ++) {
      javafxRun(() -> game.step(game.getSecondDelay()));
    }

    assertEquals(0, testConfiguration.getNumberOfPowerUps());
  }

  @Test
  void testPowerUpsLevelTwo() {
    game.setLevelNumber(LEVEL_ONE_INDEX + 1);
    testConfiguration = game.getCurrentGameLevel().getLevelConfiguration();
    assertEquals(4, testConfiguration.getNumberOfPowerUps());
  }
}
