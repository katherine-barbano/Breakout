package gameElements;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class PaddlePowerUp extends PowerUp{

  public static final Paint PADDLE_POWER_UP_COLOR = Color.LIGHTGREEN;

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
    setFill(PADDLE_POWER_UP_COLOR);
    setId("Paddle power up");
  }

}
