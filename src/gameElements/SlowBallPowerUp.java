package gameElements;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class SlowBallPowerUp extends PowerUp {

  public static final Paint SLOW_BALL_POWER_UP_COLOR = Color.PERU;

  public SlowBallPowerUp(Group gameRootArg, Paddle paddleArg, Block blockArg) {
    super(gameRootArg, paddleArg, blockArg);
  }

  @Override
  public void givePowerUp() {
    // TODO: I want to talk with you about velocity logic with Ball before implementing
  }

  @Override
  void setProperties() {
    super.setProperties();
    setFill(SLOW_BALL_POWER_UP_COLOR);
    setId("Slow Ball Power Up");
  }
}
