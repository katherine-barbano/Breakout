package gameElements;

import javafx.scene.Group;

public class SlowBallPowerUp extends PowerUp {

  public SlowBallPowerUp(Group gameRootArg, Paddle paddleArg, Block blockArg) {
    super(gameRootArg, paddleArg, blockArg);
    setPowerUpType(PowerUpType.SLOW_BALL);
  }

  public SlowBallPowerUp(Group gameRoot, Paddle gamePaddle) {
    super(gameRoot, gamePaddle);
    setPowerUpType(PowerUpType.SLOW_BALL);
  }

  @Override
  public void givePowerUp(Ball gameBall, Paddle gamePaddle) {
    if (gameBall != null) {
      gameBall.setVelocityY(getSlowBallSpeed());
    }
  }

  @Override
  void setProperties() {
    super.setProperties();
    setFill(getSlowBallPowerUpColor());
    setId("Slow Ball Power Up");
  }
}
