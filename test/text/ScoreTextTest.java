package text;

import static org.junit.jupiter.api.Assertions.assertEquals;

import breakout.Game;
import gameElements.Ball;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

public class ScoreTextTest extends DukeApplicationTest {
  private Game game;
  private GameText scoreText;
  private Scene myScene;

  @Override
  public void start (Stage stage) {
    game = new Game(stage);
    scoreText = game.getScoreText();
  }

  public void startAnimation() {
    myScene = game.getScene();
    press(myScene, KeyCode.SPACE);
  }

  @Test
  void testInitialScore() {
    assertEquals("Score: 0", scoreText.getText());
  }

  @Test
  void testGainPoint() {
    startAnimation();
    for(int numSteps = 0; numSteps < 100; numSteps ++) {
      javafxRun(() -> game.step(Game.SECOND_DELAY));
    }
    assertEquals("Score: 10", scoreText.getText());
  }
}

