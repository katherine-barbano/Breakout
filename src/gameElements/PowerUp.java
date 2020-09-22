package gameElements;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public abstract class PowerUp extends Circle {
  public static final int BALL_RADIUS = 15;
  public static final int NORMAL_BALL_SPEED = 150;

  private Group gameRoot;
  private Paddle gamePaddle;
  private Block ownerBlock;
  private int velocityY;

  public PowerUp(Group gameRootArg, Paddle paddleArg, Block blockArg) {
    gameRoot = gameRootArg;
    gamePaddle = paddleArg;
    ownerBlock = blockArg;
    setProperties();
  }

  public void showInScene() {
    velocityY = NORMAL_BALL_SPEED;
    if (! gameRoot.getChildren().contains(this))
      gameRoot.getChildren().add(this);
  }
  private void updatePositionY(double elapsedTime) {
    setCenterY(getCenterY() + velocityY * elapsedTime);
  }
  public void removeFromScene() { gameRoot.getChildren().remove(this); }
  abstract void givePowerUp();
  abstract void setProperties();


  protected Paddle getPaddle() { return gamePaddle; }

  public Block getOwnerBlock() { return ownerBlock; }

  public void setOwnerBlock(Block ownerBlock) { this.ownerBlock = ownerBlock; }
}
