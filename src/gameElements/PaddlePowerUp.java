package gameElements;

import javafx.scene.Group;

public class PaddlePowerUp extends PowerUp{

  public PaddlePowerUp(Group gameRootArg, Paddle paddleArg, Block blockArg) {
    super(gameRootArg, paddleArg, blockArg);
    setPowerUpType(PowerUpType.PADDLE);
  }

  @Override
  public void givePowerUp() {
    gamePaddle.extendPaddleWidth();
  }

  @Override
  void setProperties() {
    super.setProperties();
    setFill(getPaddlePowerUpColor());
    setId("Paddle power up");
  }

}
