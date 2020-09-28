package text;

import breakout.Game;
import gameElements.Ball;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import util.DukeApplicationTest;

public class GameOverTextTest extends DukeApplicationTest{
  private Game game;
  private Ball ball;
  private GameText gameOverText;
  private Scene myScene;

  @Override
  public void start (Stage stage) {
    game = new Game(stage);
    gameOverText = game.getGameOverText();
  }

  public void startAnimation() {
    myScene = game.getScene();
    press(myScene, KeyCode.SPACE);
  }

  @Test
  //TODO: do this once blocks can be broken
  void wonText() {
    fail();
  }

  @Test
  void lostText() {
    startAnimation();
    for(int livesLost = 0; livesLost<2; livesLost++) {
      ballTouchesGround();
      press(myScene, KeyCode.SPACE);
    }
    ballTouchesGround();
    gameOverText = lookup("#gameOverText").query();
    assertEquals("Game Over! Tap the space bar to restart from Level 1.",gameOverText.getText());
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
