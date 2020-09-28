package gameElements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import breakout.Game;
import breakout.Level;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

public class VaryingBlockTest extends DukeApplicationTest {
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
  void testExtraVaryingBlocksWereAdded() {
    startAnimation();
    assertEquals(30, game.getCurrentGameLevel().getLevelConfiguration().getNumberOfBlocksRemaining());
  }

  @Test
  void testBlockHardnessChanges() {
    startAnimation();
    List<Block> varyingBlocks = game.getCurrentGameLevel().getLevelConfiguration().getVaryingBlocks();
    assertFalse(varyingBlocks.size() == 0);
    int firstHardnessFirstBlock = varyingBlocks.get(0).getBlockHardness();

    for(int numSteps = 0; numSteps < 75; numSteps ++) {
      javafxRun(() -> game.step(game.getSecondDelay()));
    }

    assertFalse(firstHardnessFirstBlock == varyingBlocks.get(0).getBlockHardness());
  }
}
