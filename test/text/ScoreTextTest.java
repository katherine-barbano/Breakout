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
  private Ball ball;

  @Override
  public void start (Stage stage) {
    game = new Game(stage);
    ball = lookup("#ball").query();
    scoreText = game.getInfoBar().getScoreText();
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
    ball.setCenterX(180);
    ball.setCenterY(460);
    ball.setVelocityX(0);
    ball.setVelocityY(-150);
    sleep(2000);

    startAnimation();
    for(int numSteps = 0; numSteps < 5; numSteps ++) {
      javafxRun(() -> game.step(Game.SECOND_DELAY));
    }
    assertEquals("Score: 5", scoreText.getText());
  }
}

