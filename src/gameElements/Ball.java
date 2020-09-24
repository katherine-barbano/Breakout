package gameElements;

import breakout.Game;
import breakout.Level;
import gameElements.PowerUp.PowerUpType;
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

  public static final Paint BALL_COLOR = Color.RED;
  public static final int BALL_RADIUS = 15;
  public static final int NORMAL_BALL_SPEED = 150;
  public static final int SLOW_BALL_SPEED = 75;

  private final Paddle paddle;
  private BlockConfiguration blockConfiguration;
  private int ballScore;
  private int velocityX;
  private int velocityY;
  private Group gameRoot;
  private boolean velocityCanBeUpdated = true;
  private boolean isBreakerBall = false;

  public Ball(Group gameRootArg, Paddle paddleArg, Level levelArg) {
    gameRoot = gameRootArg;
    paddle = paddleArg;
    blockConfiguration = levelArg.getLevelConfiguration();

    setBallProperties();

    gameRoot.getChildren().add(this);
  }

  public void setBallProperties() {
    setScore(0);
    setCenterX(Game.SCENE_SIZE / 2);
    setCenterY(paddle.getY() - BALL_RADIUS);
    setRadius(BALL_RADIUS);
    setFill(BALL_COLOR);
    setId("ball");
    velocityX = 0;
    velocityY = NORMAL_BALL_SPEED;
  }

  public void removeBall() {
    gameRoot.getChildren().remove(this);
  }

  public boolean updateCoordinatesAndContinue(double elapsedTime, boolean isPaused) {
    if (isTouchingBottomWall()) {
      return false;
    }
    if (!isPaused) {
      updateVelocityX();
      updateVelocityY();
      updatePositionX(elapsedTime);
      updatePositionY(elapsedTime);
    }
    return true;
  }

  private void updatePositionX(double elapsedTime) {
    double edgeOfBall = paddle.getWidth()/2 + BALL_RADIUS;
    if(paddle.isTouchingPaddleLeftSide(this)) {
      setCenterX(paddle.getCenterX()-edgeOfBall);
    }
    else if(paddle.isTouchingPaddleRightSide(this)) {
      setCenterX(paddle.getCenterX()+edgeOfBall);
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

    if(!velocityCanBeUpdated) {
      return;
    }
    else if (isTouchingPaddleTop && velocityCanBeUpdated) {
      velocityX = getVelocityXFromPaddleHit();
    }
    else if (isTouchingPaddleLeft && velocityX >=0 && velocityCanBeUpdated) {
      velocityX = velocityX + 100;
    }
    else if (isTouchingPaddleRight && velocityX <0 && velocityCanBeUpdated) {
      velocityX = velocityX * -1 + 100;
    }
    else if (isTouchingPaddleLeft && velocityX >0 && velocityCanBeUpdated) {
      velocityX = velocityX * -1 - 100;
    }
    else if (isTouchingPaddleLeft && velocityX <=0 && velocityCanBeUpdated) {
      velocityX = velocityX -100;
    }
    velocityCanBeUpdated = false;
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
    if (isTouchingBlockInBlockConfiguration() && !isBreakerBall) {
      velocityY *= -1;
    }
  }

  private boolean isTouchingBlockInBlockConfiguration() {
    for (int i = 0; i < blockConfiguration.getBlockRows().length; i++) {
      BlockRow blockRow = blockConfiguration.getBlockRows()[i];
      if (blockRow == null) continue;
      for (int j = 0; j < blockRow.getRowOfBlocks().length; j++) {
        Block tempBlock = blockRow.getRowOfBlocks()[j];
        if (tempBlock== null || tempBlock.getBlockHardness() == 0) continue;
        if (tempBlock.isTouchingCircle(this)){
          respondToBlockCollision(tempBlock);
          return true;
        }
      }
    }
    return false;
  }

  private void respondToBlockCollision(Block block) {
    increaseScoreBy(10);

    if (block.hasPowerUp()) {
      PowerUpType powerUpType = block.getPowerUp().getPowerUpType();
      if (powerUpType == PowerUpType.MOVING_BLOCK) {
        increaseScoreBy(MovingBlockPowerUp.MOVING_BLOCK_SCORE_VALUE);
      } else if (block.getBlockHardness() == Block.MINIMUM_HARDNESS) {
        handleFoundPowerUpInBlock(block);
      }
    }
    blockConfiguration.decrementBlock(block, this);
  }

  private void handleFoundPowerUpInBlock(Block block) {
    PowerUpType powerUpType = block.getPowerUp().getPowerUpType();
      switch (powerUpType) {
        case SLOW_BALL -> block.getPowerUp().setGameBall(this); // FIXME
        case BREAKER_BALL -> block.getPowerUp().setGameBall(this);
        case MOVING_BLOCK -> increaseScoreBy(MovingBlockPowerUp.MOVING_BLOCK_SCORE_VALUE);
        case PADDLE -> paddle.setWidth(paddle.getWidth());
        case P_LAST -> throw new IllegalStateException("This shouldn't be reached");
      }
      if (powerUpType != PowerUpType.MOVING_BLOCK) {
        block.releasePowerUp();
        block.setHasReleasedPowerUp(true);
      }
  }

  private int getVelocityXFromPaddleHit() {
    double distanceFromPaddleCenter = getCenterX() - paddle.getCenterX();
    double angleRatio = distanceFromPaddleCenter / paddle.getWidth();
    double angleRadians = Math.toRadians(angleRatio * 90);
    return (int) (velocityY * Math.sin(angleRadians));
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
    return getCenterY() + BALL_RADIUS >= getScene().getHeight();
  }

  public void setScore(int i) { this.ballScore = i; }
  public void increaseScoreBy(int i) { this.ballScore+= i; }
  public int getScore() { return this.ballScore; }
  public void setIsBreakerBall(boolean isBreakerBall) { this.isBreakerBall = isBreakerBall; }
  public boolean isBreakerBall() { return this.isBreakerBall; }
}
