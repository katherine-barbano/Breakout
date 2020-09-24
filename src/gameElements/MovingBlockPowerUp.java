package gameElements;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class MovingBlockPowerUp extends PowerUp{

  public static final int MOVING_BLOCK_SCORE_VALUE = 25;

  public MovingBlockPowerUp(Group gameRootArg, Paddle paddleArg, Block blockArg) {
    super(gameRootArg, paddleArg, blockArg);
  }

  @Override
  public void givePowerUp() {
    // TODO: Increase score by MOVING_BLOCK_SCORE_VALUE instead of the normal BLOCK_VALUE
  }

  // this power up shouldn't show in the scene, so this is overridden to do nothing.
  @Override
  public void showInScene() { }

  @Override
  void setProperties() {
    super.setProperties();
    setId("Moving block power up");
  }

}
