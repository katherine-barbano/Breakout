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
  private BlockConfiguration blockConfiguration;
  private int velocityX;
  private int velocityY;
  private boolean isPaused;
  private int sceneWidth;

  public Ball(int sceneWidthArg, Paddle paddleArg, BlockConfiguration configuration) {
    sceneWidth = sceneWidthArg;
    paddle = paddleArg;
    blockConfiguration = configuration;
    resetBall();
  }

  public void resetBall() {
    setCenterX(sceneWidth / 2);
    setCenterY(paddle.getY() - BALL_RADIUS);
    setRadius(BALL_RADIUS);
    setFill(BALL_COLOR);
    setId("ball");
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

  private boolean isTouchingBlock() {
    for (int i = 0; i < blockConfiguration.getBlockRows().length; i++) {
      BlockRow row = blockConfiguration.getBlockRows()[i];
      for (int j = 0; j < row.getRowOfBlocks().length; j++) {
        Block tempBlock = row.getRowOfBlocks()[j];
        if (tempBlock.getBlockHardness() == 0) continue;
        if (isTouching(tempBlock)) return true;
      }
    }
    return false;
  }

  boolean isTouching(Block block) {
    double R = BALL_RADIUS;
    double Xcoord = getCenterX();
    double Ycoord = getCenterY();
    double width = block.getWidth();
    double height = block.getHeight();
    double xPos = block.getX();
    double yPos = block.getY();

    // from https://www.geeksforgeeks.org/check-if-any-point-overlaps-the-given-circle-and-rectangle/
    double Xn = Math.max(xPos, Math.min(Xcoord, (xPos+width)));
    double Yn = Math.max(yPos, Math.min(Ycoord, (yPos + height)));
    double Dx = Xn - Xcoord;
    double Dy = Yn - Ycoord;
    return (Dx * Dx + Dy * Dy) <= R*R;
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
    if (isTouchingBlock()) {
      velocityY *= -1;
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
