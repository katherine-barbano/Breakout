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
    assertEquals(ball.getCenterX(), 300);
    assertEquals(ball.getCenterY(), 555);

    assertEquals(ball.getRadius(), 15);

    assertEquals(ball.getVelocityX(), 0);
    assertEquals(ball.getVelocityY(),150);
  }
}
