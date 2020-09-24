package gameElements;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class BreakerBallPowerUp extends PowerUp {

  public static final Paint BREAKER_BALL_COLOR = Color.FUCHSIA;

  public BreakerBallPowerUp(Group gameRootArg, Paddle paddleArg, Block blockArg) {
    super(gameRootArg, paddleArg, blockArg);
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
    setFill(BREAKER_BALL_COLOR);
    setId("Breaker ball power up");
  }
}
