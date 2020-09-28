package text;

import breakout.Game;
import gameElements.Ball;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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
  void wonText() {
    startAnimation();
    for (int levelNext = 0; levelNext<3; levelNext++) {
      javafxRun(() -> myScene.getOnMouseClicked().handle(new MouseEvent(MouseEvent.MOUSE_CLICKED, 10, 10, 10, 10, MouseButton.SECONDARY, 1,
          false, false, false, false, true, false, false, true, false, false, null)));
    }
    for(int presses =0; presses<59; presses++) {
      press(myScene, KeyCode.S);
    }
    press(myScene,KeyCode.SPACE);
    for(int steps=0;steps<350;steps++) {
      javafxRun(() -> game.step(game.getSecondDelay()));
    }
    gameOverText = lookup("#gameOverText").query();
    assertNotEquals("You lost! Tap the space bar to restart from Level 1.",gameOverText.getText());
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
    assertEquals("You lost! Tap the space bar to restart from Level 1.",gameOverText.getText());
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
