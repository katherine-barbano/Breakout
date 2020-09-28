package gameElements;

import javafx.scene.Group;

public class SlowBallPowerUp extends PowerUp {

  public SlowBallPowerUp(Group gameRootArg, Paddle paddleArg, Block blockArg) {
    super(gameRootArg, paddleArg, blockArg);
    setPowerUpType(PowerUpType.SLOW_BALL);
  }

  @Override
  public void givePowerUp() {
    if (getGameBall() != null)
      getGameBall().setVelocityY(getSlowBallSpeed());
  }

  @Override
  void setProperties() {
    super.setProperties();
    setFill(getSlowBallPowerUpColor());
    setId("Slow Ball Power Up");
  }
}
