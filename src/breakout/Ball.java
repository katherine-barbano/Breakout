package breakout;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;


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
  public static final int NORMAL_BALL_SPEED = 150;

  private Paddle paddle;
  private int velocityX;
  private int velocityY;
  private int sceneWidth;

  public Ball(int sceneWidthArg, Paddle paddleArg) {
    super(sceneWidthArg/2, paddleArg.getY() - BALL_RADIUS, BALL_RADIUS);
    setFill(BALL_COLOR);
    setId("ball");
    paddle = paddleArg;
    sceneWidth = sceneWidthArg;
    velocityX = -1* NORMAL_BALL_SPEED;
    velocityY = NORMAL_BALL_SPEED;
  }

  public void updateCoordinates(double elapsedTime) {
    //updateVelocityX();
    updateVelocityY();
    setCenterX(getCenterX() + velocityX * elapsedTime);
    setCenterY(getCenterY() + velocityY * elapsedTime);
  }

  // TODO: update once have blocks
  private boolean isTouchingBlock() {
    return false;
  }

  private void updateVelocityY() {
    if(isTouchingPaddle()) {
      velocityY = velocityY * -1;
    }
    if(isTouchingTopWall()) {
      velocityY = velocityY * -1;
    }
    if(isTouchingSideWall()) {
      velocityX = velocityX * -1;
    }
  }

  private boolean isTouchingPaddle() {
    return getBoundsInParent().intersects(paddle.getBoundsInParent());
  }

  private boolean isTouchingTopWall() {
    return getCenterY()-BALL_RADIUS<0;
  }

  private boolean isTouchingSideWall() {
    double leftWallBounce = getCenterX() - BALL_RADIUS;
    double rightWallBounce = getCenterX() + BALL_RADIUS;
    return leftWallBounce<0 || rightWallBounce>sceneWidth;
  }

  private boolean topOfBallIsTouchingBottomWall() {
    return false;
  }
}
