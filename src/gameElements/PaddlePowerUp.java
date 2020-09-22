package gameElements;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class PaddlePowerUp extends PowerUp{

  public static final Paint PADDLE_POWER_UP_COLOR = Color.LIGHTGREEN;

  public PaddlePowerUp(Group gameRootArg, Paddle paddleArg, Block blockArg) {
    super(gameRootArg, paddleArg, blockArg);
  }

  @Override
  public void givePowerUp() {
    gamePaddle.extendPaddleWidth();
  }

  @Override
  void setProperties() {
    double centerX = GetXCenterOfBlock(getOwnerBlock());
    double centerY = GetYCenterOfBlock(getOwnerBlock());
    setCenterX(centerX);
    setCenterY(centerY);

    setIsReleased(false);
    setRadius(BALL_RADIUS);
    setFill(PADDLE_POWER_UP_COLOR);
    setId("Paddle power up");
  }

  private double GetXCenterOfBlock(Block block) {
    double x = block.getX();
    double width = block.getWidth();
    return x + ((width) / 2);
  }

  private double GetYCenterOfBlock(Block block) {
    double y = block.getY();
    double height = block.getHeight();
    return y + ((height) / 2);
  }
}
