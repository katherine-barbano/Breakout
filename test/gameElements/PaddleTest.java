package gameElements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import breakout.Game;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

public class PaddleTest extends DukeApplicationTest {
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
  void verifyInitialPositionSizeOfPaddle() {
    assertEquals(265, paddle.getX());
    assertEquals(620, paddle.getY());

    assertEquals(70, paddle.getWidth());
    assertEquals(10, paddle.getHeight());
  }

  @Test
  void testMovePaddle() {
    Scene myScene = game.getScene();

    startAnimation();

    for(int expectedXCoordinate = 255; expectedXCoordinate>=0; expectedXCoordinate=expectedXCoordinate-10) {
      press(myScene, KeyCode.LEFT);
      assertEquals(expectedXCoordinate,paddle.getX());
    }

    press(myScene, KeyCode.LEFT);
    assertTrue(paddle.getX()>=0);

    for(double expectedXCoordinate = paddle.getX()+10; expectedXCoordinate<=myScene.getWidth()-paddle.getWidth(); expectedXCoordinate=expectedXCoordinate+10) {
      press(myScene, KeyCode.RIGHT);
      assertEquals(expectedXCoordinate,paddle.getX());
    }

    press(myScene, KeyCode.RIGHT);
    assertTrue(paddle.getX()<=game.getScene().getWidth());

    assertEquals(620, paddle.getY());
  }

  @Test
  void testPaddlePowerUpWorks() {

  }
}
