package breakout;

import gameElements.Ball;
import gameElements.Block;
import gameElements.BlockConfiguration;
import gameElements.InfoBar;
import gameElements.Paddle;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import text.GameOverText;
import text.ScoreText;
import util.DukeApplicationTest;

public class LevelTest extends DukeApplicationTest {
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
    press(myScene,KeyCode.SPACE);
  }

  @Test
  void testBallPaddleInteraction() {
    double startingYBall = ball.getCenterY();

    startAnimation();

    for(int numSteps = 0; numSteps < 3; numSteps ++) {
      sleep(2000);
      game.step(Game.SECOND_DELAY);
    }

    assertEquals(300,ball.getCenterX());
    assertTrue(ball.getCenterY()<startingYBall);
  }

  @Test
  void testAllBlocks() {
    Group testGroup = new Group();
    InfoBar infoBar = new InfoBar(new ScoreText(0,testGroup),testGroup);
    Level level = new Level(testGroup,"game_for_testing","testOneInput", infoBar);
    int width = (int) game.getScene().getWidth();
    int height = (int) game.getScene().getHeight();
    List<Block> oneBlock = level.getLevelConfiguration().getBlocksAsList();
    assertTrue(oneBlock.size() == 1);
    Block block = oneBlock.get(0);
    assertEquals(block.getBlockColor(), Color.PALETURQUOISE);
    assertEquals(block.getX(), block.getWidth());
  }

  @Test
  void paddleMissesBallCheckPositionAndLifeReset() {
    startAnimation();
    ballTouchesGround();
    ball = lookup("#ball").query();
    assertEquals(2,game.getCurrentGameLevel().getLives());

    assertEquals(0,ball.getVelocityX());
    assertEquals(150,ball.getVelocityY());

    assertEquals(300, ball.getCenterX());
    assertEquals(625, ball.getCenterY());
  }

  private void ballTouchesGround() {
    ball = lookup("#ball").query();
    ball.setCenterX(20);
    ball.setCenterY(665);
    ball.setVelocityX(0);
    ball.setVelocityY(150);

    for(int numSteps = 0; numSteps < 3; numSteps ++) {
      javafxRun(() -> game.step(Game.SECOND_DELAY));
    }
  }

  @Test
  void addLivesWithLCheatKey() {
    startAnimation();
    Scene gameScene = game.getScene();
    Level level = game.getCurrentGameLevel();
    press(gameScene, KeyCode.L);
    assertEquals(4,level.getLives());
    for(int livesLost = 0; livesLost<3; livesLost++) {
      ballTouchesGround();
      press(gameScene, KeyCode.SPACE);
    }
    assertEquals(1,level.getLives());
    ballTouchesGround();
    GameOverText gameOverText = lookup("#gameOverText").query();
    assertEquals("Game Over! Tap the space bar to restart from Level 1.",gameOverText.getText());
  }

  @Test
  void deleteBlockWithDCheatKey() {
    startAnimation();
    Scene gameScene = game.getScene();
    Level level = game.getCurrentGameLevel();
    for(int blockNumber=59;blockNumber>0;blockNumber--) {
      press(gameScene, KeyCode.D);
      assertEquals(blockNumber,level.getLevelConfiguration().getNumberOfBlocksRemaining());
    }
  }

  @Test
  void releasePowerUpWithPCheatKey() {
    startAnimation();
    Scene gameScene = game.getScene();
    press(gameScene, KeyCode.DIGIT2);
    press(gameScene,KeyCode.SPACE);
    Block firstPowerUpBlock = game.getCurrentGameLevel().getFirstBlockWithPowerUp();
    press(gameScene, KeyCode.P);
    javafxRun(() -> game.step(Game.SECOND_DELAY));
    assertFalse(firstPowerUpBlock.hasPowerUp());
  }

  @Test
  void releaseAllPowerUpWithKCheatKey() {
    startAnimation();
    Scene gameScene = game.getScene();
    press(gameScene, KeyCode.DIGIT2);
    press(gameScene,KeyCode.SPACE);
    press(gameScene, KeyCode.K);
    javafxRun(() -> game.step(Game.SECOND_DELAY));
    Block firstPowerUpBlock = game.getCurrentGameLevel().getFirstBlockWithPowerUp();
    assertTrue(firstPowerUpBlock==null);
  }

  @Test
  void addTimeWithTCheatKey() {
    Scene gameScene = game.getScene();
    Level level = game.getCurrentGameLevel();
    press(gameScene, KeyCode.T);
    assertEquals(70,level.getInfoBar().getTimeRemaining());
  }

  @Test
  void decreaseScoreToWinWithSCheatKey() {
    Scene gameScene = game.getScene();
    Level level = game.getCurrentGameLevel();
    press(gameScene, KeyCode.S);
    assertEquals(90,level.getScoreToWinLevel());
  }


  @Test
  void scoreAndTimeFileNotFound() {
    Group testGroup = new Group();
    InfoBar infoBar = new InfoBar(new ScoreText(0,testGroup),testGroup);
    assertThrows(IllegalArgumentException.class, () -> new Level(testGroup,"game_without_score_time_file",1, infoBar));
  }

  @Test
  void scoreAndTimeFileFormattedIncorrectly() {
    Group testGroup = new Group();
    InfoBar infoBar = new InfoBar(new ScoreText(0,testGroup),testGroup);
    assertThrows(IllegalArgumentException.class, () -> new Level(testGroup,"game_score_time_file_format_wrong",3, infoBar));
  }
}
