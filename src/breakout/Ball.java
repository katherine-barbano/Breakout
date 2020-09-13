package breakout;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/***
 * Purpose: A circle that breaks Blocks, and ends the game if it crosses the
 * bottom of the screen. Bounces off all other walls of the screen, as well as
 * the Paddle and other Blocks.
 *
 * Method: isOnScreen, breakBlock, isTouchingBlock, isTouchingPaddle
 */
public class Ball extends Circle {

  public static final Paint BALL_COLOR = Color.RED;
  public static final int BALL_RADIUS = 15;

  public Ball(int sceneWidth, Paddle paddle) {
    super(sceneWidth/2, paddle.getY() - BALL_RADIUS, BALL_RADIUS);
    setFill(BALL_COLOR);
    setId("ball");
  }
}
