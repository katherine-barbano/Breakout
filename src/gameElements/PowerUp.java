package gameElements;

import javafx.scene.Group;
import javafx.scene.shape.Circle;

public abstract class PowerUp extends Circle {
  public static final int BALL_RADIUS = 15;
  public static final int POWER_UP_DROP_SPEED = 100;

  private Group gameRoot;
  protected Paddle gamePaddle;
  private Block ownerBlock;
  private int velocityY;
  private boolean isReleased;

  public abstract void givePowerUp();
  abstract void setProperties();

  public PowerUp(Group gameRootArg, Paddle paddleArg, Block blockArg) {
    gameRoot = gameRootArg;
    gamePaddle = paddleArg;
    ownerBlock = blockArg;
    setProperties();
  }

  public void showInScene() {
    velocityY = POWER_UP_DROP_SPEED;
    setIsReleased(true);
    addToScene();
  }

  public void updateLocation(double elapsedTime, boolean isPaused) {
    if (isTouchingPaddle()) {
      givePowerUp();
      removeFromScene();
    }
    else if (isTouchingBottomWall()) removeFromScene();
    else if (!isPaused) updatePositionY(elapsedTime);
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

  private void addToScene() { if (! gameRoot.getChildren().contains(this)) gameRoot.getChildren().add(this); }
  public void removeFromScene() { gameRoot.getChildren().remove(this); }

  public void setPaddle(Paddle paddle) { gamePaddle = paddle; }

  private void updatePositionY(double elapsedTime) { setCenterY(getCenterY() + velocityY * elapsedTime); }

  public Block getOwnerBlock() { return ownerBlock; }

  public boolean isReleased() { return isReleased; }
  public void setIsReleased(boolean released) { isReleased = released; }
}
