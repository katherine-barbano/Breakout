package gameElements;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class SlowBallPowerUp extends PowerUp {

  public static final Paint SLOW_BALL_POWER_UP_COLOR = Color.PERU;

  public SlowBallPowerUp(Group gameRootArg, Paddle paddleArg, Block blockArg) {
    super(gameRootArg, paddleArg, blockArg);
    setPowerUpType(PowerUpType.SLOW_BALL);
  }

  @Override
  public void givePowerUp() {
    if (getGameBall() != null) {
      getGameBall().setVelocityY(Ball.SLOW_BALL_SPEED);
    }
  }

  @Override
  void setProperties() {
    super.setProperties();
    setFill(SLOW_BALL_POWER_UP_COLOR);
    setId("Slow Ball Power Up");
  }
}
