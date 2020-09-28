package text;

import static org.junit.jupiter.api.Assertions.assertEquals;

import breakout.Game;
import gameElements.Ball;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

public class LevelTextTest extends DukeApplicationTest {
  private Game game;
  private Ball ball;
  private GameText levelText;
  private Scene scene;

  @Override
  public void start (Stage stage) {
    game = new Game(stage);
    levelText = game.getCurrentGameLevel().getInfoBar().getLevelText();
  }

  public void startAnimation() {
    scene = game.getScene();
    press(scene, KeyCode.SPACE);
  }

  @Test
  void verifyInitialLevelText() {
    assertEquals("Level: 1",levelText.getText());
  }

  @Test
  void levelTextAfterLosingLife() {
    startAnimation();
    ballTouchesGround();
    assertEquals("Level: 1",levelText.getText());
  }

  private void ballTouchesGround() {
    ball = lookup("#ball").query();
    ball.setCenterX(20);
    ball.setCenterY(585);
    ball.setVelocityX(0);
    ball.setVelocityY(150);

    for(int numSteps = 0; numSteps < 3; numSteps ++) {
      javafxRun(() -> game.step(game.getSecondDelay()));
    }
  }

  @Test
  void levelTextOnNextLevel() {
    startAnimation();
    //The following line was written by Robert Duvall in DukeApplicationTest.
    //Had to use this directly in order to test right clicks with a different MouseButton argument, since the click method only tests for left clicks.
    javafxRun(() -> scene.getOnMouseClicked().handle(new MouseEvent(MouseEvent.MOUSE_CLICKED, 10, 10, 10, 10, MouseButton.SECONDARY, 1,
        false, false, false, false, true, false, false, true, false, false, null)));
    levelText = game.getCurrentGameLevel().getInfoBar().getLevelText();
    assertEquals("Level: 2", levelText.getText());
  }

  @Test
  void levelTextOnPreviousLevel() {
    levelTextOnNextLevel();
    click(game.getScene(), 200, 200);
    levelText = game.getCurrentGameLevel().getInfoBar().getLevelText();
    assertEquals("Level: 1",levelText.getText());
  }
}
