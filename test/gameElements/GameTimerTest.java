package gameElements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import breakout.Game;
import breakout.Level;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

public class GameTimerTest extends DukeApplicationTest {
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
  void timerDecreasesOverTime() {
    Level level = game.getGameLevels().get(0);
    int beginningTime = level.getInfoBar().getGameTimer().getTimeRemaining();
    startAnimation();
    for(int numSteps = 0; numSteps < 100; numSteps ++) {
      javafxRun(() -> game.step(game.getSecondDelay()));
    }
    int endingTime = level.getInfoBar().getGameTimer().getTimeRemaining();
    assertTrue(endingTime<beginningTime);
  }

  @Test
  void timeDoesNotChangeWhenPaused() {
    Level level = game.getGameLevels().get(0);
    int beginningTime = level.getInfoBar().getGameTimer().getTimeRemaining();
    startAnimation();
    level.getInfoBar().getGameTimer().pauseTimer();
    for(int numSteps = 0; numSteps < 100; numSteps ++) {
      javafxRun(() -> game.step(game.getSecondDelay()));
    }
    int endingTime = level.getInfoBar().getGameTimer().getTimeRemaining();
    assertTrue(endingTime==beginningTime);
  }

  @Test
  void timeContinuesToDecreaseWhenPausedAndUnpaused() {
    Level level = game.getGameLevels().get(0);
    int beginningTime = level.getInfoBar().getGameTimer().getTimeRemaining();
    startAnimation();
    level.getInfoBar().getGameTimer().pauseTimer();
    level.getInfoBar().getGameTimer().unpauseTimer();
    for(int numSteps = 0; numSteps < 100; numSteps ++) {
      javafxRun(() -> game.step(game.getSecondDelay()));
    }
    int endingTime = level.getInfoBar().getGameTimer().getTimeRemaining();
    assertTrue(endingTime<beginningTime);
  }

  @Test
  void initialTimeValueFromTextFileForAllLevels() {
    Level level = game.getGameLevels().get(0);
    int beginningTime = level.getInfoBar().getGameTimer().getTimeRemaining();
    startAnimation();
    level.getInfoBar().getGameTimer().pauseTimer();
    level.getInfoBar().getGameTimer().unpauseTimer();
    for(int numSteps = 0; numSteps < 100; numSteps ++) {
      javafxRun(() -> game.step(game.getSecondDelay()));
    }
    int endingTime = level.getInfoBar().getGameTimer().getTimeRemaining();
    assertTrue(endingTime<beginningTime);
  }
}
