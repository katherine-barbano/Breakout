package gameElements;

import breakout.Game;
import breakout.Level;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class PaddlePowerUp extends PowerUp{

  public static final Paint PADDLE_POWER_UP_COLOR = Color.LIGHTGREEN;
  public static final int BIGGER_PADDLE_WIDTH = 100;

  public PaddlePowerUp(Group gameRootArg, Paddle paddleArg, Block blockArg) {
    super(gameRootArg, paddleArg, blockArg);
  }

  @Override
  void givePowerUp() {
    Paddle gamePaddle = super.getPaddle();
    gamePaddle.extendPaddleWidth();
  }

  @Override
  void setProperties() {
    setCenterX(getOwnerBlock().getX());
    setCenterY(getOwnerBlock().getY());
    setRadius(BALL_RADIUS);
    setFill(PADDLE_POWER_UP_COLOR);
    setId("paddle power up");
  }
}
