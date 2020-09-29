package gameElements;

import javafx.scene.Group;

public class BreakerBallPowerUp extends PowerUp {

  public BreakerBallPowerUp(Group gameRoot, Paddle gamePaddle) {
    super(gameRoot, gamePaddle);
    setPowerUpType(PowerUpType.BREAKER_BALL);
  }

  public void givePowerUp(Ball gameBall, Paddle gamePaddle) {
    if (gameBall != null) {
      gameBall.setIsBreakerBall(true);
    }
  }

  @Override
  void setProperties() {
    super.setProperties();
    setFill(getBreakerBallPowerUpColor());
    setId("Breaker ball power up");
  }
}
