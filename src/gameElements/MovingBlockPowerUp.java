package gameElements;

import javafx.scene.Group;

public class MovingBlockPowerUp extends PowerUp{

  public MovingBlockPowerUp(Group gameRootArg, Paddle paddleArg, Block blockArg) {
    super(gameRootArg, paddleArg, blockArg);
    setPowerUpType(PowerUpType.MOVING_BLOCK);
  }

  public MovingBlockPowerUp(Group gameRoot, Paddle gamePaddle) {
    super(gameRoot, gamePaddle);
    setPowerUpType(PowerUpType.MOVING_BLOCK);
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
