package text;

import static org.junit.jupiter.api.Assertions.assertEquals;

import breakout.Game;
import gameElements.Ball;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

public class LivesTextTest extends DukeApplicationTest {
  private Game game;
  private Ball ball;
  private GameText livesText;
  private Scene myScene;

  @Override
  public void start (Stage stage) {
    game = new Game(stage);
    livesText = game.getCurrentGameLevel().getInfoBar().getLivesText();
  }

  public void startAnimation() {
    myScene = game.getScene();
    press(myScene, KeyCode.SPACE);
  }

  @Test
  void verifyInitialLives() {
    assertEquals("Lives: 3",livesText.getText());
  }

  @Test
  void loseLife() {
    startAnimation();
    ballTouchesGround();
    assertEquals("Lives: 2",livesText.getText());
  }

  private void ballTouchesGround() {
    ball = lookup("#ball").query();
    ball.setCenterX(20);
    ball.setCenterY(665);
    ball.setVelocityX(0);
    ball.setVelocityY(150);

    for(int numSteps = 0; numSteps < 3; numSteps ++) {
      javafxRun(() -> game.step(game.getSecondDelay()));
    }
  }
}
