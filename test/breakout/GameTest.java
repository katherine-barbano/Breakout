package breakout;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import util.DukeApplicationTest;

public class GameTest extends DukeApplicationTest{
  private Level level;
  private Game game;
  private Ball ball;
  private Paddle paddle;
  private Block block;

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
  void test() {
    game.startGameAtLevelOne();
    Group testGroup = new Group();
    level = new Level(testGroup, 1);
    assertEquals(game.getGameLevel(), level);
  }
}
