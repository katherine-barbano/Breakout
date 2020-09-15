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
  public static final int NORMAL_BALL_SPEED = 150;

  private final Paddle paddle;
  private int velocityX;
  private int velocityY;
  private boolean isPaused;

  public Ball(int sceneWidthArg, Paddle paddleArg) {
    super(sceneWidthArg / 2, paddleArg.getY() - BALL_RADIUS, BALL_RADIUS);
    setFill(BALL_COLOR);
    setId("ball");
    paddle = paddleArg;
    velocityX = 0;
    velocityY = NORMAL_BALL_SPEED;
    isPaused = true;
  }

  boolean updateCoordinatesAndContinue(double elapsedTime) {
    if (isTouchingBottomWall()) {
      return false;
    }
    if (!isPaused) {
      updateVelocityX();
      updateVelocityY();
      setCenterX(getCenterX() + velocityX * elapsedTime);
      setCenterY(getCenterY() + velocityY * elapsedTime);
    }
    return true;
  }

  int getVelocityX() {
    return velocityX;
  }

  void setVelocityX(int velocityXArg) {
    velocityX = velocityXArg;
  }

  int getVelocityY() {
    return velocityY;
  }

  void setVelocityY(int velocityYArg) {
    velocityY = velocityYArg;
  }

  void pause() {
    isPaused = true;
  }

  void unpause() {
    isPaused = false;
  }

  // TODO: update once have blocks
  private boolean isTouchingBlock() {
    return false;
  }

  private void updateVelocityX() {
    if (isTouchingSideWall()) {
      velocityX = velocityX * -1;
    }
    if (isTouchingPaddleSide()) {
      velocityX = velocityX * -1;
    }
    if (isTouchingPaddleTop()) {
      velocityX = getVelocityXFromPaddleHit();
    }
  }

  private void updateVelocityY() {
    if (isTouchingPaddleTop()) {
      velocityY = velocityY * -1;
    }
    if (isTouchingTopWall()) {
      velocityY = velocityY * -1;
    }
  }

  //TODO: modify the physics
  private int getVelocityXFromPaddleHit() {
    double distanceFromPaddleCenter = getCenterX() - paddle.getCenterX();
    double angleRatio = distanceFromPaddleCenter / paddle.getWidth();
    double angleRadians = Math.toRadians(angleRatio * 90);
    return (int) (velocityY * Math.sin(angleRadians));
  }

  //TODO: fix for edges of ball that are not in center
  private boolean isTouchingPaddleTop() {
    boolean hitsInCorrectXCoordinates = paddle.getX() < getCenterX() &&
        getCenterX() < paddle.getX() + paddle.getWidth();
    boolean hitsInCorrectYCoordinate = paddle.getY() < getCenterY() + BALL_RADIUS;
    return hitsInCorrectYCoordinate && hitsInCorrectXCoordinates;
  }

  private boolean isTouchingPaddleSide() {
    boolean hitsLeftSideOfPaddleXCoordinate = getCenterX() + BALL_RADIUS > paddle.getX()
        && getCenterX() < paddle.getX();
    boolean hitsRightSideOfPaddleXCoordinate = getCenterX() - BALL_RADIUS < paddle.getX() +
        paddle.getWidth() && getCenterX() > paddle.getX() + paddle.getWidth();

    boolean hitsWithinTopOfPaddle = paddle.getY() < getCenterY() + BALL_RADIUS;
    boolean hitsWithinBottomOfPaddle =
        paddle.getY() + paddle.getHeight() > getCenterY() - BALL_RADIUS;

    boolean hitsInCorrectXCoordinate =
        hitsLeftSideOfPaddleXCoordinate || hitsRightSideOfPaddleXCoordinate;
    boolean hitsWithinYCoordinate = hitsWithinTopOfPaddle && hitsWithinBottomOfPaddle;

    return hitsWithinYCoordinate && hitsInCorrectXCoordinate;
  }

  private boolean isTouchingTopWall() {
    return getCenterY() - BALL_RADIUS < 0;
  }

  //assumes Scene has already been instantiated in Game so that it can use the getScene method
  private boolean isTouchingSideWall() {
    double leftWallBounce = getCenterX() - BALL_RADIUS;
    double rightWallBounce = getCenterX() + BALL_RADIUS;
    return leftWallBounce < 0 || rightWallBounce > getScene().getWidth();
  }

  //assumes Scene has already been instantiated in Game so that it can use the getScene method
  private boolean isTouchingBottomWall() {
    return getCenterY() + BALL_RADIUS > getScene().getHeight();
  }
}
