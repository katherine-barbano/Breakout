package gameElements;

import breakout.Game;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
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

  private Group gameRoot;

  public Paddle(Group gameRootArg) {
    gameRoot = gameRootArg;
    setPaddleProperties();
    gameRoot.getChildren().add(this);
  }

  public void removePaddle() {
    gameRoot.getChildren().remove(this);
  }

  void extendPaddleWidth() { setWidth(NORMAL_PADDLE_WIDTH * 2); }
  void setNormalPaddleWidth() {
    setWidth(NORMAL_PADDLE_WIDTH);
  }

  public void setPaddleProperties() {
    setX(Game.SCENE_SIZE / 2 - NORMAL_PADDLE_WIDTH/2);
    setY(Game.SCENE_SIZE - VERTICAL_PADDLE_OFFSET_FROM_BOTTOM);
    setWidth(NORMAL_PADDLE_WIDTH);
    setHeight(PADDLE_HEIGHT);
    setFill(PADDLE_COLOR);
    setId("paddle");
  }

  public void handleKeyInput(KeyCode code, boolean isPaused) {
    if(code == KeyCode.LEFT && !isPaused) {
      moveLeft();
    }
    else if(code== KeyCode.RIGHT && !isPaused) {
      moveRight();
    }
  }

  void moveLeft() {
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

  double getCenterX() {
    return getX() + getWidth()/2;
  }

  //TODO: fix for edges of ball that are not in center
  boolean isTouchingPaddleTop(Circle collisionCircle) {
    double R = collisionCircle.getRadius();
    double Xcoord = collisionCircle.getCenterX();
    double Ycoord = collisionCircle.getCenterY();
    double xPos = this.getX();
    double yPos = this.getY();

    // from https://www.geeksforgeeks.org/check-if-any-point-overlaps-the-given-circle-and-rectangle/
    double Xn = Math.max(xPos, Math.min(Xcoord, (xPos + this.getWidth())));
    double Yn = Math.max(yPos, Math.min(Ycoord, (yPos + this.getHeight())));
    double Dx = Xn - Xcoord;
    double Dy = Yn - Ycoord;

    return (Dx * Dx + Dy * Dy) <= R*R;
  }

  boolean isTouchingPaddleLeftSide(Circle collisionCircle) {
    double xPos = this.getX();
    double xCoord = collisionCircle.getCenterX();
    double Xn = calculateXn(xPos, xCoord);
    boolean touchingLeftSide = Xn==this.getCenterX()-this.getWidth()/2;
    return isTouchingPaddleTop(collisionCircle) && touchingLeftSide;
  }

  boolean isTouchingPaddleRightSide(Circle collisionCircle) {
    double xPos = this.getX();
    double xCoord = collisionCircle.getCenterX();
    double Xn = calculateXn(xPos, xCoord);
    boolean touchingRightSide = Xn==this.getCenterX()+this.getWidth()/2;
    return isTouchingPaddleTop(collisionCircle) && touchingRightSide;
  }

  private double calculateXn(double xPos, double xCoord) {
    return Math.max(xPos, Math.min(xCoord, (xPos + this.getWidth())));
  }
}
