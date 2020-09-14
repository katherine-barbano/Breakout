package breakout;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import util.DukeApplicationTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Tests for Game class.
 *
 * @author Robert C Duvall
 */
public class MainTest extends DukeApplicationTest {

  private Game game;
  private Ball ball;
  private Paddle paddle;

  @Override
  public void start (Stage stage) {
    game = new Game(stage);

    ball = lookup("#ball").query();
    paddle = lookup("#paddle").query();
  }

  @Test
  public void verifyInitialPositionSizeVelocityOfBall() {
    assertEquals(300, ball.getCenterX());
    assertEquals(555, ball.getCenterY());

    assertEquals(15, ball.getRadius());

    assertEquals(0, ball.getVelocityX());
    assertEquals(150, ball.getVelocityY());
  }

  @Test
  public void verifyInitialPositionSizeOfPaddle() {
    assertEquals(265, paddle.getX());
    assertEquals(570, paddle.getY());

    assertEquals(70, paddle.getWidth());
    assertEquals(10, paddle.getHeight());
  }

  @Test
  public void testMovePaddle() {
    Scene myScene = game.getScene();

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

    assertEquals(570, paddle.getY());
  }

  @Test
  public void testBallCornerInteraction() {
    ball.setVelocityX(-150);
    ball.setVelocityY(-150);
    ball.setCenterX(15);
    ball.setCenterY(15);

    for(int numSteps = 0; numSteps < 2; numSteps ++) {
      game.step(Game.SECOND_DELAY);
    }

    assertEquals(150,ball.getVelocityX());
    assertEquals(150,ball.getVelocityY());
  }

}
