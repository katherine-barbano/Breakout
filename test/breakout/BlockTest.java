package breakout;

import gameElements.Ball;
import gameElements.Block;
import gameElements.Paddle;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import util.DukeApplicationTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Block (potentially BlockRow and BlockConfiguration
 * as well)
 */
public class BlockTest extends DukeApplicationTest {

  private Block testBlock;
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
  void assertBlockIsMade() {
    Scene testScene = game.getScene();
    int width = (int) testScene.getWidth();
    int height = (int) testScene.getHeight();
    testBlock = new Block(width, height, 1);
    assertEquals(testBlock.getBlockColor(), Color.PALETURQUOISE);
  }

  @Test
  void assertBlockColorUpdates() {
    Scene testScene = game.getScene();
    int width = (int) testScene.getWidth();
    int height = (int) testScene.getHeight();
    testBlock = new Block(width, height, 1);
    testBlock.setBlockHardness(2);
    assertEquals(testBlock.getBlockColor(), Color.PALEVIOLETRED);
  }

}
