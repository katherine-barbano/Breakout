package gameElements;

import breakout.Game;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javafx.scene.Group;
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

  private Group gameRoot;
  private Properties properties;

  public Paddle(Group gameRootArg) {
    gameRoot = gameRootArg;
    initializeProperties();
    setPaddleProperties();
    gameRoot.getChildren().add(this);
  }

  void initializeProperties() {
    properties = new Properties();
    FileInputStream ip = null;
    try {
      ip = new FileInputStream(Game.PROPERTY_FILE);
      properties.load(ip);
    }
    catch (FileNotFoundException e) {}
    catch (IOException e) {}
  }

  public void removePaddle() {
    gameRoot.getChildren().remove(this);
  }

  public void setPaddleProperties() {
    setX(getPlayableAreaSize() / 2 - getNormalPaddleWidth()/2);
    setY(getSceneSize() - getVerticalPaddleOffset());
    setWidth(getNormalPaddleWidth());
    setHeight(getPaddleHeight());
    setFill(getPaddleColor());
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
    double newXPosition = getX() - getPaddleSpeed();
    if(newXPosition>0) {
      setX(newXPosition);
    }
  }

  //assumes Scene has already been instantiated in Game so that it can use getScene
  private void moveRight() {
    double newXPosition = getX() + getPaddleSpeed();
    if(newXPosition + getWidth() <getScene().getWidth()) {
      setX(newXPosition);
    }
  }

  double getCenterX() {
    return getX() + getWidth()/2;
  } // TODO: should this have brackets around it?
  double getCenterY() { return (getY() + getHeight())/2; }

  //TODO: fix for edges of ball that are not in center
  public boolean isTouchingPaddleTop(Circle collisionCircle) {
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

  public void setPowerUpWidth() {
    int newWidth = getPowerUpPaddleWidth();
    setWidth(newWidth);
  }


  Paint getPaddleColor() { return Paint.valueOf(properties.getProperty("paddle_color"));}
  int getNormalPaddleWidth() { return Integer.parseInt(properties.getProperty("normal_paddle_width"));}
  int getPowerUpPaddleWidth() { return Integer.parseInt(properties.getProperty("power_up_paddle_width"));}
  int getPaddleHeight() { return Integer.parseInt(properties.getProperty("paddle_height"));}
  int getVerticalPaddleOffset() { return Integer.parseInt(properties.getProperty("vertical_paddle_offset"));}
  int getPaddleSpeed() { return Integer.parseInt(properties.getProperty("paddle_speed"));}
  int getSceneSize() { return Integer.parseInt(properties.getProperty("scene_size"));}
  int getPlayableAreaSize() { return Integer.parseInt(properties.getProperty("playable_area_size"));}
}
