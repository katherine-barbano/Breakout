package breakout;

import gameElements.Ball;
import gameElements.Block;
import gameElements.Paddle;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import util.DukeApplicationTest;

public class GameTest extends DukeApplicationTest{
  private Game game;

  @Override
  public void start (Stage stage) {
    game = new Game(stage);
  }

  @Test
  void resetGameToLevelOne() {
    javafxRun(() -> {
      game.gameOver();
      game.resetGameToLevel(0);
      assertEquals(game.getCurrentGameLevel().getLevelNumber(), 1);
    });
  }

  @Test
  void scrollThroughNextLevelsCheatKey() {
    Scene scene = game.getScene();
    for (int levelIndex = 1; levelIndex < 4; levelIndex++) {
      assertEquals(levelIndex, game.getCurrentGameLevel().getLevelNumber());
      //The following line was written by Robert Duvall in DukeApplicationTest.
      //Had to use this directly in order to test right clicks with a different MouseButton argument, since the click method only tests for left clicks.
      javafxRun(() -> scene.getOnMouseClicked().handle(new MouseEvent(MouseEvent.MOUSE_CLICKED, 10, 10, 10, 10, MouseButton.SECONDARY, 1,
          false, false, false, false, true, false, false, true, false, false, null)));
    }
    assertEquals(game.getCurrentGameLevel().getLevelNumber(), 3);
  }

  @Test
  void scrollThroughPreviousLevelsCheatKey() {
    scrollThroughNextLevelsCheatKey();
    for (int levelIndex = 3; levelIndex > 0; levelIndex--) {
      assertEquals(levelIndex, game.getCurrentGameLevel().getLevelNumber());
      click(game.getScene(), 200, 200);
    }
    assertEquals(game.getCurrentGameLevel().getLevelNumber(), 1);
  }

  @Test
  void jumpToLevelsWithNumericCheatKey() {
    press(game.getScene(), KeyCode.DIGIT2);
    assertEquals(2, game.getCurrentGameLevel().getLevelNumber());
    press(game.getScene(), KeyCode.DIGIT1);
    assertEquals(1, game.getCurrentGameLevel().getLevelNumber());
    press(game.getScene(), KeyCode.DIGIT0);
    assertEquals(1, game.getCurrentGameLevel().getLevelNumber());
    press(game.getScene(), KeyCode.DIGIT9);
    assertEquals(1, game.getCurrentGameLevel().getLevelNumber());
  }


  @Test
  void verifyInitialLevelsList() {
    List<Level> actualLevelList = game.getGameLevelsList();
    assertEquals(3, actualLevelList.size());
    for (int levelIndex = 0; levelIndex < 3; levelIndex++) {
      Level levelAtIndex = actualLevelList.get(levelIndex);
      assertEquals(levelIndex+1, levelAtIndex.getLevelNumber());
    }
  }

  @Test
  void timeIsUpTargetScoreSurpassed() {
    game.setScore(110);
    startAnimation();
    for(int numSteps = 0; numSteps < 10; numSteps ++) {
      javafxRun(() -> game.step(Game.SECOND_DELAY));
    }

    List<Node> children = game.getRoot().getChildren();
    assertTrue(children.size()>63);
  }

  @Test
  void directoryNotFoundWhenInitializingLevels() {
    fail();
  }

  @Test
  void levelOneScoreToWinWinningCondition() {
    Level level = game.getGameLevels().get(0);
    assertEquals(100, level.getScoreToWinLevel());
  }

  @Test
  void levelTwoScoreToWinWinningCondition() {
    Level level = game.getGameLevels().get(1);
    assertEquals(500, level.getScoreToWinLevel());
  }

  @Test
  void levelThreeScoreToWinWinningCondition() {
    Level level = game.getGameLevels().get(2);
    assertEquals(600, level.getScoreToWinLevel());
  }

  @Test
  void levelOneTimeLimit() {
    Level level = game.getGameLevels().get(0);
    assertEquals(60, level.getLevelTimeLimit());
  }

  @Test
  void levelTwoTimeLimit() {
    Level level = game.getGameLevels().get(1);
    assertEquals(100, level.getLevelTimeLimit());
  }

  @Test
  void levelThreeTimeLimit() {
    Level level = game.getGameLevels().get(2);
    assertEquals(20, level.getLevelTimeLimit());
  }

  void startAnimation() {
    Scene myScene = game.getScene();
    press(myScene,KeyCode.SPACE);
  }

  @Test
  void levelOneScoreDoesNotSurpassWinningCondition() {
    Level level = game.getGameLevels().get(0);
    level.setLevelTimeLimit(1);
    startAnimation();
    for(int numSteps = 0; numSteps < 10; numSteps ++) {
      javafxRun(() -> game.step(Game.SECOND_DELAY));
    }

    List<Node> children = game.getRoot().getChildren();
    assertTrue(children.size()<=63);
  }

  @Test
  void levelTwoScoreDoesNotSurpassWinningCondition() {
    Level level = game.getGameLevels().get(1);
    level.setLevelTimeLimit(1);
    startAnimation();
    for(int numSteps = 0; numSteps < 10; numSteps ++) {
      javafxRun(() -> game.step(Game.SECOND_DELAY));
    }

    List<Node> children = game.getRoot().getChildren();
    assertTrue(children.size()<=63);
  }

  @Test
  void levelThreeScoreDoesNotSurpassWinningCondition() {
    Level level = game.getGameLevels().get(2);
    level.setLevelTimeLimit(1);
    startAnimation();
    for(int numSteps = 0; numSteps < 10; numSteps ++) {
      javafxRun(() -> game.step(Game.SECOND_DELAY));
    }

    List<Node> children = game.getRoot().getChildren();
    assertTrue(children.size()<=63);
  }
}
