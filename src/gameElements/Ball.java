package gameElements;

import breakout.Game;
import breakout.Level;
import gameElements.PowerUp.PowerUpType;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import javafx.scene.Group;
import javafx.scene.paint.Color;
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
  private Group gameRoot;
  private Properties properties;

  private final Paddle paddle;
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

  void initializeProperties() {
    properties = new Properties();
    FileInputStream ip = null;
    try {
      ip = new FileInputStream(Game.PROPERTY_FILE);
      properties.load(ip);
    }
    catch (FileNotFoundException e) {}
    catch (IOException e) {}
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

  public boolean updateCoordinatesAndContinue(double elapsedTime, boolean isPaused) {
    if (isTouchingBottomWall()) {
      return false;
    }
    if (!isPaused) {
      if (getBlockBallIsTouching() != null) {
        updateVelocityXForBlockHit();
        updateVelocityYForBlockHit();
      }
      else {
        updateVelocityX();
        updateVelocityY();
      }
      updatePositionX(elapsedTime);
      updatePositionY(elapsedTime);
    }
    return true;
  }

  private void updatePositionX(double elapsedTime) {
    double edgeOfBall = paddle.getWidth()/2 + getBallRadius();
    if(paddle.isTouchingPaddleLeftSide(this)) {
      setCenterX(paddle.getCenterX()-edgeOfBall);
    }
    else if(paddle.isTouchingPaddleRightSide(this)) {
      setCenterX(paddle.getCenterX()+edgeOfBall);
    }
    setCenterX(getCenterX() + velocityX * elapsedTime);
  }

  private void updateCenterXForPaddle(double edgeOfBall){
    if(paddle.isTouchingPaddleLeftSide(this)) {
      setCenterX(paddle.getCenterX()-edgeOfBall);
    }
    else if(paddle.isTouchingPaddleRightSide(this)) {
      setCenterX(paddle.getCenterX()+edgeOfBall);
    }
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

    if(!velocityCanBeUpdated) {
      return;
    }
    // FIXME: can we extract this into a helper method?
    else if (isTouchingPaddleTop) {
      velocityX = getVelocityXFromPaddleHit();
    }
    else if (isTouchingPaddleLeft && velocityX >=getInfoBarHeight()) {
      velocityX = velocityX + getVelocityChangeForPaddleSide();
    }
    else if (isTouchingPaddleRight && velocityX <getInfoBarHeight()) {
      velocityX = velocityX * -1 + getVelocityChangeForPaddleSide();
    }
    else if (isTouchingPaddleLeft && velocityX >getInfoBarHeight()) {
      velocityX = velocityX * -1 - getVelocityChangeForPaddleSide();
    }
    else if (isTouchingPaddleLeft && velocityX <=getInfoBarHeight()) {
      velocityX = velocityX - getVelocityChangeForPaddleSide();
    }
    velocityCanBeUpdated = false;
  }

  private void updateVelocityXForBlockHit() {
    Block touchedBlock = getBlockBallIsTouching();
    if  (touchedBlock.isTouchingLeftOrRight(this)) {
      velocityX = velocityX * -1;
    }
  }

  private void updateVelocityYForBlockHit() {
    Block touchedBlock = getBlockBallIsTouching();
    if (touchedBlock.isTouchingBlockTop(this)) {
      velocityY = velocityY * -1;
    }
  }

  private void updateVelocityCanBeUpdated() {
    boolean notTouchingPaddleSides = !paddle.isTouchingPaddleLeftSide(this) && !paddle.isTouchingPaddleRightSide(this);
    boolean notTouchingPaddleTop = !paddle.isTouchingPaddleTop(this);
    if (notTouchingPaddleTop && notTouchingPaddleSides) {
      velocityCanBeUpdated = true;
    }
  }

  private void updateVelocityY() {
    boolean notTouchingPaddleSides = !paddle.isTouchingPaddleRightSide(this) && !paddle.isTouchingPaddleLeftSide(this);
    if (paddle.isTouchingPaddleTop(this) && notTouchingPaddleSides) {
      velocityY = velocityY * -1;
    }
    if (isTouchingTopWall()) {
      velocityY = velocityY * -1;
    }
  }

  public Block getBlockBallIsTouching() {
    for (Block block : blockConfiguration.getBlocksAsList()) {
      if (block== null || block.getBlockHardness() == 0) continue;
      if (block.isTouchingCircle(this)) return block;
    }
    return null;
  }

  // TODO: move into higher level class
  public void handleBlockBehavior(Block block) {
    if (block.hasPowerUp()) {
      PowerUpType powerUpType = block.getPowerUp().getPowerUpType();
      if (powerUpType == PowerUpType.MOVING_BLOCK) {
        increaseScoreBy(getMovingBlockScoreValue());
      } else if (block.getBlockHardness() == getMinimumHardness()) {
        handleFoundPowerUpInBlock(block);
      }
    }
  }

  // TODO: move into higher level class
  private void handleFoundPowerUpInBlock(Block block) {
    PowerUpType powerUpType = block.getPowerUp().getPowerUpType();
    switch (powerUpType) {
      case SLOW_BALL :
      case BREAKER_BALL: {
        block.getPowerUp().setGameBall(this);
        break;
      } case PADDLE: {
        paddle.setWidth(paddle.getWidth());
        break;
      } case MOVING_BLOCK: {
        increaseScoreBy(getMovingBlockScoreValue());
        break;
      } default: {
        throw new IllegalStateException("This shouldn't be reached");
      }
    }
    if (powerUpType != PowerUpType.MOVING_BLOCK) {
      block.releasePowerUp();
      block.setHasReleasedPowerUp(true);
    }
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

  public void setScore(int i) { this.ballScore = i; }
  public void increaseScoreBy(int i) { this.ballScore+= i; }
  public int getScore() { return this.ballScore; }
  public void setIsBreakerBall(boolean isBreakerBall) { this.isBreakerBall = isBreakerBall; }
  public boolean isBreakerBall() { return this.isBreakerBall; }

  private double getPlayableArea() {return Double.parseDouble(properties.getProperty("playable_area_size")); }
  private double getBallRadius() { return Double.parseDouble(properties.getProperty("ball_radius"));}
  private Paint getBallColor() { return Paint.valueOf(properties.getProperty("ball_color")); }
  private int getNormalBallSpeed() {return Integer.parseInt(properties.getProperty("normal_ball_speed")); }
  private double getRightAngle() { return Double.parseDouble(properties.getProperty("right_angle"));}
  private int getVelocityChangeForPaddleSide() { return Integer
      .parseInt(properties.getProperty("velocity_change_for_paddle_side")); }

  private int getInfoBarHeight() { return Integer
      .parseInt(properties.getProperty("info_bar_height")); }
  private int getMinimumHardness() { return Integer.parseInt(properties.getProperty("minimum_hardness")); }
  int getMovingBlockScoreValue() { return Integer.parseInt(properties.getProperty("moving_block_score_value"));}
}
