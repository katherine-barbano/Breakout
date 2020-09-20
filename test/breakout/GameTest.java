package breakout;

import gameElements.Ball;
import gameElements.Block;
import gameElements.Paddle;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import util.DukeApplicationTest;

public class GameTest extends DukeApplicationTest{
  private Game game;

  @Override
  public void start (Stage stage) {
    game = new Game(stage);
  }

  public void startAnimation() {
    Scene myScene = game.getScene();
    press(myScene, KeyCode.SPACE);
  }

  @Test
  void resetGameToLevelOne() {
    javafxRun(() -> {
      game.gameOver();
      game.resetGameToLevel(0);
      assertEquals(game.getCurrentGameLevel().getLevelNumber(), 1);
    });
  }
}
