package breakout;

import gameElements.Ball;
import gameElements.Block;
import gameElements.Paddle;
import java.util.List;
import javafx.scene.Group;
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
  void verifyInitialLevelsList() {
    List<Level> actualLevelList = game.getGameLevelsList();
    assertEquals(3, actualLevelList.size());
    for (int levelIndex = 0; levelIndex < 3; levelIndex++) {
      Level levelAtIndex = actualLevelList.get(levelIndex);
      assertEquals(levelIndex+1, levelAtIndex.getLevelNumber());
    }
  }
}
