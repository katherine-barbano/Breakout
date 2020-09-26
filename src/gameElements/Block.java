package gameElements;

import breakout.Game;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/***
 *    Purpose: A rectangle that is removed from obstructing the Ball's movement
 *    if the hardness becomes 0. Contains a hardness field from 0 to 3. The Block's
 *    color corresponds to the current hardness, which is updated if it is hit by a
 *    Ball.
 *
 *    Method: updateHardness
 */
public class Block extends Rectangle {

  public static final Paint BLOCK_COLOR_INVISIBLE = Game.BACKGROUND;
  public static final Paint BLOCK_COLOR_ONE = Color.PALETURQUOISE;
  public static final Paint BLOCK_COLOR_TWO = Color.PALEVIOLETRED;
  public static final Paint BLOCK_COLOR_THREE = Color.PURPLE;
  public static final int BLOCKS_PER_ROW = 12;
  public static final int NUMBER_OF_BLOCK_ROWS = 8;
  public static final int MINIMUM_HARDNESS = 1;
  public static final int MAXIMUM_HARDNESS = 3;

  private int blockHardness;
  private Paint blockColor;
  private PowerUp randomPowerUp;
  private boolean hasPowerUp;
  private boolean hasReleasedPowerUp;

  public Block() {}
  public Block(int sceneWidth, int sceneHeight, int blockHardness) {
    setDimensions(sceneWidth, sceneHeight);
    setBlockHardness(blockHardness);
    updateBlockColor();
    setFill(blockColor);
    setId("block");
  }

  public void updateBlockColor() {
    Paint newBlockColor = getBlockColor(this.blockHardness);
    this.blockColor = newBlockColor;
    setFill(this.blockColor);
  }

  void setDimensions(int sceneWidth, int sceneHeight) {
    int blockWidth = sceneWidth / BLOCKS_PER_ROW;
    int blockHeight = getBlockHeight(sceneHeight);
    setWidth(blockWidth);
    setHeight(blockHeight);
  }

  // multiplied by factor of 0.8 because we aren't filling entire scene with rows
  // TODO: optimize this scaling factor
  private int getBlockHeight(int sceneHeight) {
    return (sceneHeight * 8) / (10 * (NUMBER_OF_BLOCK_ROWS + 1));
  }

  Paint getBlockColor(int blockHardness) {
    switch (blockHardness) {
      case 0 : return BLOCK_COLOR_INVISIBLE;
      case 1 : return BLOCK_COLOR_ONE;
      case 2: return BLOCK_COLOR_TWO;
      case 3: return BLOCK_COLOR_THREE;
      default : throw new IllegalStateException("Unexpected value: " + blockHardness);
    }
  }

  public boolean isTouchingCircle(Circle collisionCircle) {
    double R = Ball.BALL_RADIUS;
    double Xcoord = collisionCircle.getCenterX();
    double Ycoord = collisionCircle.getCenterY();
    double width = getWidth();
    double height = getHeight();
    double xPos = getX();
    double yPos = getY();

    // from https://www.geeksforgeeks.org/check-if-any-point-overlaps-the-given-circle-and-rectangle/
    double Xn = Math.max(xPos, Math.min(Xcoord, (xPos+width)));
    double Yn = Math.max(yPos, Math.min(Ycoord, (yPos + height)));
    double Dx = Xn - Xcoord;
    double Dy = Yn - Ycoord;
    return (Dx * Dx + Dy * Dy) <= R*R;
  }

  public int getBlockHardness() {
    return blockHardness;
  }
  public void setBlockHardness(int blockHardness) {
    this.blockHardness = blockHardness;
  }
  // using Math.random()
  // used https://stackoverflow.com/questions/363681/how-do-i-generate-random-integers-within-a-specific-range-in-java
  public void setRandomHardness() {
    int inclusiveRange = Block.MAXIMUM_HARDNESS - Block.MINIMUM_HARDNESS + 1;
    int randomHardness = Block.MINIMUM_HARDNESS + (int)(Math.random() * inclusiveRange);
    setBlockHardness(randomHardness);
  }
  void decreaseHardnessByOne() { setBlockHardness(blockHardness-1); }

  public Paint getBlockColor() {
    return blockColor;
  }
  public void setBlockColor(Paint blockColor) {
    this.blockColor = blockColor;
  }

  public void removeFromScene() {
    setDimensions(0,0);
    setX(0.0);
    setY(0.0);
  }

  public boolean hasPowerUp() { return hasPowerUp; }
  public void setHasPowerUp(boolean powerUpBoolean) { hasPowerUp = powerUpBoolean; }

  public boolean hasReleasedPowerUp() { return (randomPowerUp!= null && hasPowerUp && hasReleasedPowerUp); }
  public void setHasReleasedPowerUp(boolean hasReleasedPowerUp) {
    this.hasReleasedPowerUp = hasReleasedPowerUp;
    randomPowerUp.setIsReleased(true);
  }
  public void setPowerUp(PowerUp powerUp) { randomPowerUp = powerUp; }
  public PowerUp getPowerUp() { return randomPowerUp; }
  void releasePowerUp() {
    randomPowerUp.showInScene();
  }

  // assumes that owner block is already calibrated
  public void setDimensionsPowerUp() {
    if (this.hasPowerUp()) {
      randomPowerUp.setOwnerBlock(this);
      randomPowerUp.setProperties();
    }
  }

  public void updateLocationAndVelocity(double elapsedTime, boolean gameIsPaused) { }
  public int getVelocity() { return 0; }

  public boolean isTouchingLeftOrRight(Ball ball) {
    double minCircleHeight = ball.getCenterY() - ball.getRadius();
    double maxCircleHeight = ball.getCenterY() - ball.getRadius();
    double maxBlockHeight = getHeight() + getY();
    if (minCircleHeight < maxBlockHeight && maxCircleHeight > getY()) {
      boolean touchingLeft = (ball.getCenterX() + ball.getRadius()) > getX();
      boolean touchingRight = (ball.getCenterX() - ball.getRadius()) < getX() + getWidth();
      return touchingLeft || touchingRight;
    }
    return false;
  }

  public boolean isTouchingBottomOrTop(Ball ball) {
    double minCircleWidth = ball.getCenterX() - ball.getRadius();
    double maxCircleWidth = ball.getCenterX() - ball.getRadius();
    double minCircleHeight = ball.getCenterY() - ball.getRadius();
    double maxCircleHeight = ball.getCenterY() - ball.getRadius();

    double maxBlockWidth = getWidth() + getX();
    double maxBlockHeight = getHeight() + getY();
    if (minCircleWidth < maxBlockWidth && maxCircleWidth > getX()) {
      boolean touchingTop = (maxCircleHeight > getY()) && (minCircleHeight < getY());
      boolean touchingBottom = (minCircleHeight < maxBlockHeight) && (maxBlockWidth > maxBlockHeight);
      return touchingTop || touchingBottom;
    }
    return false;
  }
}
