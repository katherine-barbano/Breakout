package text;

import static org.junit.jupiter.api.Assertions.*;

import breakout.Game;
import gameElements.Ball;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

public class PauseTextTest extends DukeApplicationTest {
  private Game game;
  private Ball ball;
  private GameText pauseText;
  private Scene myScene;

  @Override
  public void start (Stage stage) {
    game = new Game(stage);
    pauseText = game.getCurrentGameLevel().getPauseText();
  }

  public void startAnimation() {
    myScene = game.getScene();
    press(myScene, KeyCode.SPACE);
  }

  @Test
  void verifyInitialPauseText() {
    assertEquals("Click the space bar to start!",pauseText.getText());
    startAnimation();
  }

  @Test
  void cheatKeyPauseAndUnpause() {
    startAnimation();
    press(myScene, KeyCode.SPACE);
    assertEquals("Paused. Resume with space bar",pauseText.getText());
    press(myScene, KeyCode.SPACE);
    Group root = game.getRoot();
    assertFalse(root.getChildren().contains(pauseText));
  }

  @Test
  void loseALifePauseText() {
    startAnimation();
    ballTouchesGround();
    assertEquals("Click the space bar to start!", pauseText.getText());
  }

  private void ballTouchesGround() {
    ball = lookup("#ball").query();
    ball.setCenterX(20);
    ball.setCenterY(585);
    ball.setVelocityX(0);
    ball.setVelocityY(150);

    for(int numSteps = 0; numSteps < 3; numSteps ++) {
      javafxRun(() -> game.step(Game.SECOND_DELAY));
    }
  }
}
