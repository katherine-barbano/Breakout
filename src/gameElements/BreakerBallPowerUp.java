package gameElements;

import javafx.scene.Group;

public class BreakerBallPowerUp extends PowerUp {

  public BreakerBallPowerUp(Group gameRoot, Paddle gamePaddle) {
    super(gameRoot, gamePaddle);
    setPowerUpType(PowerUpType.BREAKER_BALL);
  }

  public void givePowerUp() {
    if (getGameBall() != null) {
      getGameBall().setIsBreakerBall(true);
    }
  }

  @Override
  void setProperties() {
    super.setProperties();
    setFill(getPaddlePowerUpColor());
    setId("Breaker ball power up");
  }
}
