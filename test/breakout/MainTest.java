package breakout;

import java.util.concurrent.TimeUnit;
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

  private Ball ball;
  private Paddle paddle;

  @Override
  public void start (Stage stage) {
    Game game = new Game(stage);

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
  public void movePaddle() {

  }

}
