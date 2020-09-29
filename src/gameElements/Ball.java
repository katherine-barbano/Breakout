package gameElements;

import breakout.Game;
import breakout.Level;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javafx.scene.Group;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;


/***
 * Purpose: A circle that breaks Blocks, and ends the game if it crosses the
 * bottom of the screen. Bounces off all other walls of the screen, as well as
 * the Paddle and other Blocks.
 *
 * Method: isOnScreen, breakBlock, isTouchingBlockInBlockConfiguration, isTouchingPaddle
 */
public class Ball extends Circle {

  private final Paddle paddle;
  private Group gameRoot;
  private Properties properties;
  private BlockConfiguration blockConfiguration;

  private int velocityX;
  private int velocityY;
  private boolean velocityCanBeUpdated = true;

  private boolean isBreakerBall = false;
  private int ballScore;

  public Ball(Group gameRootArg, Paddle paddleArg, Level levelArg) {
    gameRoot = gameRootArg;
    paddle = paddleArg;
    blockConfiguration = levelArg.getLevelConfiguration();

    initializeProperties();
    setBallProperties();

    gameRoot.getChildren().add(this);
  }

  /**
   * load properties from config.properties
   */
  void initializeProperties() {
    properties = new Properties();
    FileInputStream ip = null;
    try {
      ip = new FileInputStream(Game.PROPERTY_FILE);
      properties.load(ip);
    } catch (FileNotFoundException e) {
    } catch (IOException e) {
    }
  }

  public void setBallProperties() {
    setScore(0);
    setCenterX(getPlayableArea() / 2);
    setCenterY(paddle.getY() - getBallRadius());
    setRadius(getBallRadius());
    setFill(getBallColor());
    setId("ball");
    velocityX = 0;
    velocityY = getNormalBallSpeed();
  }

  public void removeBall() {
    gameRoot.getChildren().remove(this);
  }

  /***
   * Returns whether the ball's position is not touching the ground, meaning
   * it is in a position that allows the game to continue. Also updates
   * the coordinates of the ball.
   *
   * Should be called by step in Level.
   * @param elapsedTime time elapsed in a single step
   * @return true if ball is valid and Level should continue
   */
  public boolean updateCoordinatesAndContinue(double elapsedTime, boolean isPaused) {
    if (isTouchingBottomWall()) {
      return false;
    }
    if (!isPaused) {
      updateVelocity();
      updatePositionX(elapsedTime);
      updatePositionY(elapsedTime);
    }
    return true;
  }

  private void updateVelocity() {
    if (getBlockBallIsTouching() != null) {
      updateVelocityXForBlockHit();
      updateVelocityYForBlockHit();
    } else {
      updateVelocityX();
      updateVelocityY();
    }
  }

  private void updatePositionX(double elapsedTime) {
    double edgeOfBall = paddle.getWidth() / 2 + getBallRadius();
    if (paddle.isTouchingPaddleLeftSide(this)) {
      setCenterX(paddle.getCenterX() - edgeOfBall);
    } else if (paddle.isTouchingPaddleRightSide(this)) {
      setCenterX(paddle.getCenterX() + edgeOfBall);
    }
    setCenterX(getCenterX() + velocityX * elapsedTime);
  }

  private void updatePositionY(double elapsedTime) {
    setCenterY(getCenterY() + velocityY * elapsedTime);
  }

  public int getVelocityX() {
    return velocityX;
  }

  public void setVelocityX(int velocityXArg) {
    velocityX = velocityXArg;
  }

  public int getVelocityY() {
    return velocityY;
  }

  public void setVelocityY(int velocityYArg) {
    velocityY = velocityYArg;
  }

  private void updateVelocityX() {
    updateVelocityXForWallTouch();
    updateVelocityXForPaddleHits();
    updateVelocityCanBeUpdated();
  }

  private void updateVelocityXForWallTouch() {
    if (isTouchingSideWall()) {
      velocityX = velocityX * -1;
    }
  }

  private void updateVelocityXForPaddleHits() {
    boolean isTouchingPaddleTop = paddle.isTouchingPaddleTop(this);
    boolean isTouchingPaddleRight = paddle.isTouchingPaddleRightSide(this);
    boolean isTouchingPaddleLeft = paddle.isTouchingPaddleLeftSide(this);

    if (!velocityCanBeUpdated) {
      return;
    }
    else if (isTouchingPaddleTop) {
      velocityX = getVelocityXFromPaddleHit();
    } else if (isTouchingPaddleLeft && velocityX >= getInfoBarHeight()) {
      velocityX = velocityX + getVelocityChangeForPaddleSide();
    } else if (isTouchingPaddleRight && velocityX < getInfoBarHeight()) {
      velocityX = velocityX * -1 + getVelocityChangeForPaddleSide();
    } else if (isTouchingPaddleLeft && velocityX > getInfoBarHeight()) {
      velocityX = velocityX * -1 - getVelocityChangeForPaddleSide();
    } else if (isTouchingPaddleLeft && velocityX <= getInfoBarHeight()) {
      velocityX = velocityX - getVelocityChangeForPaddleSide();
    }
    velocityCanBeUpdated = false;
  }

  private void updateVelocityXForBlockHit() {
    Block touchedBlock = getBlockBallIsTouching();
    if (touchedBlock.isTouchingLeftOrRight(this)) {
      velocityX = velocityX * -1;
    }
  }

  private void updateVelocityYForBlockHit() {
    velocityY = velocityY * -1;
  }

  private void updateVelocityCanBeUpdated() {
    boolean notTouchingPaddleSides =
        !paddle.isTouchingPaddleLeftSide(this) && !paddle.isTouchingPaddleRightSide(this);
    boolean notTouchingPaddleTop = !paddle.isTouchingPaddleTop(this);
    if (notTouchingPaddleTop && notTouchingPaddleSides) {
      velocityCanBeUpdated = true;
    }
  }

  private void updateVelocityY() {
    boolean notTouchingPaddleSides =
        !paddle.isTouchingPaddleRightSide(this) && !paddle.isTouchingPaddleLeftSide(this);
    if (paddle.isTouchingPaddleTop(this) && notTouchingPaddleSides) {
      velocityY = velocityY * -1;
    }
    if (isTouchingTopWall()) {
      velocityY = velocityY * -1;
    }
  }

  public Block getBlockBallIsTouching() {
    for (Block block : blockConfiguration.getBlocksAsList()) {
      if (block == null) {
        continue;
      }
      if (block.isTouchingCircle(this)) {
        return block;
      }
    }
    return null;
  }

  private int getVelocityXFromPaddleHit() {
    double distanceFromPaddleCenter = getCenterX() - paddle.getCenterX();
    double angleRatio = distanceFromPaddleCenter / paddle.getWidth();
    double angleRadians = Math.toRadians(angleRatio * getRightAngle());
    return (int) (velocityY * Math.sin(angleRadians));
  }

  private boolean isTouchingTopWall() {
    return getCenterY() - getBallRadius() < getInfoBarHeight();
  }

  //assumes Scene has already been instantiated in Game so that it can use the getScene method
  private boolean isTouchingSideWall() {
    double leftWallBounce = getCenterX() - getBallRadius();
    double rightWallBounce = getCenterX() + getBallRadius();
    return leftWallBounce < 0 || rightWallBounce > getScene().getWidth();
  }

  //assumes Scene has already been instantiated in Game so that it can use the getScene method
  private boolean isTouchingBottomWall() {
    return getCenterY() + getBallRadius() >= getScene().getHeight();
  }

  public void increaseScoreBy(int i) {
    this.ballScore += i;
  }

  public int getScore() {
    return this.ballScore;
  }

  public void setScore(int i) {
    this.ballScore = i;
  }

  public void setIsBreakerBall(boolean isBreakerBall) {
    this.isBreakerBall = isBreakerBall;
  }

  public boolean isBreakerBall() {
    return this.isBreakerBall;
  }

  // properties accessors//
  private double getPlayableArea() {
    return Double.parseDouble(properties.getProperty("playable_area_size"));
  }

  private double getBallRadius() {
    return Double.parseDouble(properties.getProperty("ball_radius"));
  }

  private Paint getBallColor() {
    return Paint.valueOf(properties.getProperty("ball_color"));
  }

  private int getNormalBallSpeed() {
    return Integer.parseInt(properties.getProperty("normal_ball_speed"));
  }

  private double getRightAngle() {
    return Double.parseDouble(properties.getProperty("right_angle"));
  }

  private int getVelocityChangeForPaddleSide() {
    return Integer
        .parseInt(properties.getProperty("velocity_change_for_paddle_side"));
  }

  private int getInfoBarHeight() {
    return Integer
        .parseInt(properties.getProperty("info_bar_height"));
  }

  public int getMovingBlockScoreValue() {
    return Integer.parseInt(properties.getProperty("moving_block_score_value"));
  }
}
