package breakout;

import javafx.scene.input.KeyCode;
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
  public static final int PADDLE_SPEED = 10;

  private int paddleWidth;
  private int sceneWidth;
  private int sceneHeight;

  public Paddle(int sceneWidthArg, int sceneHeightArg) {
    sceneWidth = sceneWidthArg;
    sceneHeight = sceneHeightArg;
    resetPaddle();
  }

  //for power up later
  public void extendPaddleWidth() {
    paddleWidth = NORMAL_PADDLE_WIDTH * 2;
  }

  //for power up later
  public void setNormalPaddleWidth() {
    paddleWidth = NORMAL_PADDLE_WIDTH;
  }

  public void resetPaddle() {
    setX(sceneWidth / 2 - NORMAL_PADDLE_WIDTH/2);
    setY(sceneHeight - VERTICAL_PADDLE_OFFSET_FROM_BOTTOM);
    setWidth(NORMAL_PADDLE_WIDTH);
    setHeight(PADDLE_HEIGHT);
    setFill(PADDLE_COLOR);
    setId("paddle");
    paddleWidth = NORMAL_PADDLE_WIDTH;
  }

  public void handleKeyInput(KeyCode code, boolean isPaused) {
    if(code == KeyCode.LEFT && !isPaused) {
      moveLeft();
    }
    else if(code== KeyCode.RIGHT && !isPaused) {
      moveRight();
    }
  }

  private void moveLeft() {
    double newXPosition = getX() - PADDLE_SPEED;
    if(newXPosition>0) {
      setX(newXPosition);
    }
  }

  //assumes Scene has already been instantiated in Game so that it can use getScene
  private void moveRight() {
    double newXPosition = getX() + PADDLE_SPEED;
    if(newXPosition + getWidth() <getScene().getWidth()) {
      setX(newXPosition);
    }
  }

  public double getCenterX() {
    return getX() + getWidth()/2;
  }
}
