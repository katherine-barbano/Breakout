package gameElements;

import breakout.Level;
import javafx.scene.Group;
import javafx.scene.shape.Circle;

public abstract class PowerUp extends Circle {
  public static final int BALL_RADIUS = 15;
  public static final int POWER_UP_DROP_SPEED = 100;

  private Group gameRoot;
  protected Paddle gamePaddle;
  private Ball gameBall;
  private Block ownerBlock;
  private PowerUpType powerUpType;
  private int velocityY;
  private boolean isReleased;

  public enum PowerUpType {
    BREAKER_BALL,
    PADDLE,
    MOVING_BLOCK,
    SLOW_BALL,
    P_LAST // shouldn't be reached
  }

  public PowerUp(Group gameRootArg, Paddle paddleArg, Block blockArg) {
    gameRoot = gameRootArg;
    gamePaddle = paddleArg;
    ownerBlock = blockArg;
    setProperties();
  }

  public PowerUp(Level level, Block blockArg) {
    gameRoot = level.getGameRoot();
    gamePaddle = level.getGamePaddle();
    ownerBlock = blockArg;
    setProperties();
  }

  public abstract void givePowerUp();

  void setProperties() {
    double centerX = GetXCenterOfBlock(getOwnerBlock());
    double centerY = GetYCenterOfBlock(getOwnerBlock());
    setCenterX(centerX);
    setCenterY(centerY);

    setIsReleased(false);
    setRadius(BALL_RADIUS);
  }

  public void showInScene() {
    velocityY = POWER_UP_DROP_SPEED;
    setIsReleased(true);
    addToScene();
  }

  public void updateLocation(double elapsedTime, boolean isPaused) {
    if (!isPaused) {
      updatePositionY(elapsedTime);
      if (isTouchingPaddle()) {
        givePowerUp();
        removeFromScene();
      } else if (isTouchingBottomWall()) removeFromScene();
    }
  }

  private boolean isTouchingPaddle() {
    if (gamePaddle == null) return false;
    return gamePaddle.isTouchingPaddleTop(this) ||
        gamePaddle.isTouchingPaddleLeftSide(this) ||
        gamePaddle.isTouchingPaddleRightSide(this);
  }

  private boolean isTouchingBottomWall() {
    if (gamePaddle == null) return false;
    return getCenterY() < gamePaddle.getY();
  }

  private double GetXCenterOfBlock(Block block) {
    double x = block.getX();
    double width = block.getWidth();
    return x + ((width) / 2);
  }

  private double GetYCenterOfBlock(Block block) {
    double y = block.getY();
    double height = block.getHeight();
    return y + ((height) / 2);
  }

  private void addToScene() {
    assert (this != null); // FIXME for testing
    assert (gameRoot != null);
    assert (gameRoot.getChildren() != null);
    if (! gameRoot.getChildren().contains(this)) {
      gameRoot.getChildren().add(this);
    }
  }
  public void removeFromScene() {
    gameRoot.getChildren().remove(this);
  }

  private void updatePositionY(double elapsedTime) { setCenterY(getCenterY() + velocityY * elapsedTime); }

  public Block getOwnerBlock() { return ownerBlock; }
  public void setOwnerBlock(Block ownerBlock) { this.ownerBlock = ownerBlock; }

  public boolean isReleased() { return isReleased; }
  public void setIsReleased(boolean released) { isReleased = released; }

  public Group getGameRoot() { return gameRoot; }
  public void setGameRoot(Group gameRoot) { this.gameRoot = gameRoot; }

  public Paddle getGamePaddle() { return gamePaddle; }
  public void setPaddle(Paddle gamePaddle) { this.gamePaddle = gamePaddle; }

  public int getVelocityY() { return velocityY; }
  public void setVelocityY(int velocityY) { this.velocityY = velocityY; }

  public void setPowerUpType(PowerUpType type) { this.powerUpType = type; }
  public PowerUpType getPowerUpType() { return this.powerUpType; }

  public Ball getGameBall() { return this.gameBall; }
  public void setGameBall(Ball ball) { this.gameBall = ball; }
}
