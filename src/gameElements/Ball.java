package gameElements;

import breakout.Game;
import breakout.Level;
import gameElements.PowerUp.PowerUpType;
import java.util.List;
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
  public static final int VELOCITY_CHANGE_FOR_PADDLE_SIDE = 100;
  public static final int PLAYABLE_AREA_TOP_BOUND = InfoBar.INFO_BAR_HEIGHT;
  public static final int RIGHT_ANGLE=90;

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
    setCenterX(Game.PLAYABLE_AREA_SIZE / 2);
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
      if (isTouchingBlockInBlockConfiguration()) {
        updateVelocityXForBlockHit();
        updateVelocityYForBlockHit();
        respondToBlockCollision();
      }
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
    else if (isTouchingPaddleLeft && velocityX >=PLAYABLE_AREA_TOP_BOUND) {
      velocityX = velocityX + VELOCITY_CHANGE_FOR_PADDLE_SIDE;
    }
    else if (isTouchingPaddleRight && velocityX <PLAYABLE_AREA_TOP_BOUND ) {
      velocityX = velocityX * -1 + VELOCITY_CHANGE_FOR_PADDLE_SIDE;
    }
    else if (isTouchingPaddleLeft && velocityX >PLAYABLE_AREA_TOP_BOUND) {
      velocityX = velocityX * -1 - VELOCITY_CHANGE_FOR_PADDLE_SIDE;
    }
    else if (isTouchingPaddleLeft && velocityX <=PLAYABLE_AREA_TOP_BOUND) {
      velocityX = velocityX - VELOCITY_CHANGE_FOR_PADDLE_SIDE;
    }
    velocityCanBeUpdated = false;
  }

  private void updateVelocityXForBlockHit() {
    Block touchedBlock = getBlockBallIsTouching();
    if (touchedBlock == null) return;
    if (touchedBlock.isTouchingLeftOrRight(this)) {
        velocityX *= -1;
    }
  }

  private void updateVelocityYForBlockHit() {
    Block touchedBlock = getBlockBallIsTouching();
    if (touchedBlock == null) return;
    if (touchedBlock.isTouchingBottomOrTop(this)) {
      velocityY *= -1;
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

  private boolean isTouchingBlockInBlockConfiguration() {
    List<Block> blockList = blockConfiguration.getBlocksAsList();
    for (Block block : blockList) {
      if (block== null || block.getBlockHardness() == 0) continue;
      if (block.isTouchingCircle(this)) return true;
    }
    return false;
  }

  private Block getBlockBallIsTouching() {
    for (Block block : blockConfiguration.getBlocksAsList()) {
      if (block== null || block.getBlockHardness() == 0) continue;
      if (block.isTouchingCircle(this)) return block;
    }
    return null;
  }

  // TODO move into BlockConfiguration
  private void respondToBlockCollision() {
    Block block = getBlockBallIsTouching();
    if (block == null) return;
    blockConfiguration.findAndDecrementBlock(block, this);
    handleBlockBehavior(block);
  }

  // TODO: move into higher level class
  private void handleBlockBehavior(Block block) {
    if (block.hasPowerUp()) {
      PowerUpType powerUpType = block.getPowerUp().getPowerUpType();
      if (powerUpType == PowerUpType.MOVING_BLOCK) {
        increaseScoreBy(MovingBlockPowerUp.MOVING_BLOCK_SCORE_VALUE);
      } else if (block.getBlockHardness() == Block.MINIMUM_HARDNESS) {
        handleFoundPowerUpInBlock(block);
      }
    }
  }

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
        increaseScoreBy(MovingBlockPowerUp.MOVING_BLOCK_SCORE_VALUE);
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
    double angleRadians = Math.toRadians(angleRatio * RIGHT_ANGLE);
    return (int) (velocityY * Math.sin(angleRadians));
  }

  private boolean isTouchingTopWall() {
    return getCenterY() - BALL_RADIUS < PLAYABLE_AREA_TOP_BOUND;
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
