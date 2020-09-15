package breakout;

import java.util.ArrayList;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import util.DukeApplicationTest;

public class BlockConfigurationTest extends DukeApplicationTest {
  private BlockConfiguration testConfiguration;
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
    press(myScene, KeyCode.SPACE);
  }

  @Test
  void testNumBlocksRemaining() {
    testConfiguration = new BlockConfiguration("level_1");
    assertEquals(testConfiguration.getNumberOfBlocksRemaining(), 60);
  }
}
