package breakout;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/***
 *  A small rectangle at the bottom of the screen that a Ball
 *  can bounce off of. Moved horizontally using the arrow keys.
 *
 *  Method: collision, setSize, move
 */
public class Paddle extends Rectangle{

  public static final Paint PADDLE_COLOR = Color.DARKBLUE;
  public static final int NORMAL_PADDLE_WIDTH = 70;
  public static final int PADDLE_HEIGHT = 10;
  public static final int VERTICAL_PADDLE_OFFSET_FROM_BOTTOM = 30;

  private int paddleWidth;

  public Paddle(int sceneWidth, int sceneHeight) {
    super(sceneWidth / 2 - NORMAL_PADDLE_WIDTH/2, sceneHeight - VERTICAL_PADDLE_OFFSET_FROM_BOTTOM, NORMAL_PADDLE_WIDTH, PADDLE_HEIGHT);
    setFill(PADDLE_COLOR);
    setId("paddle");
    paddleWidth = NORMAL_PADDLE_WIDTH;
  }

  //for power up later
  public void extendPaddleWidth() {
    paddleWidth = NORMAL_PADDLE_WIDTH * 2;
  }

  //for power up later
  public void setNormalPaddleWidth() {
    paddleWidth = NORMAL_PADDLE_WIDTH;
  }
}
