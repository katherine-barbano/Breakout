package gameElements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import breakout.Game;
import breakout.Level;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

public class InfoBarTest extends DukeApplicationTest {
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
  void removeLevelSpecificText() {
    InfoBar infoBar = game.getInfoBar();
    javafxRun(() -> infoBar.removeAllLevelSpecificText());
    List<Node> children = game.getRoot().getChildren();
    assertTrue(children.size()==65);
  }
}
