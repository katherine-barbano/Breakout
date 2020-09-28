package gameElements;

import breakout.Game;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
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

  private Properties properties;
  private int blockHardness;
  private Paint blockColor;
  private PowerUp randomPowerUp;
  private boolean hasPowerUp;
  private boolean hasReleasedPowerUp;

  public Block() {}

  public Block(PowerUp powerUp) {
    this.randomPowerUp = powerUp;
    this.hasPowerUp = true;
    powerUp.assignToBlock(this);
    initializeProperties();
    setRandomHardness();
    colorBlock();
  }

  public Block(int sceneWidth, int sceneHeight, int blockHardness) {
    initializeProperties();
    setBlockHardness(blockHardness);
    colorBlock();
    setDimensions(sceneWidth,sceneHeight);
  }

  public Block(int blockHardness) {
    initializeProperties();
    setBlockHardness(blockHardness);
    colorBlock();
  }

  void colorBlock() {
    updateBlockColor();
    setFill(blockColor);
    setId("block");
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

  public void updateBlockColor() {
    Paint newBlockColor = getBlockColor(this.blockHardness);
    this.blockColor = newBlockColor;
    setFill(this.blockColor);
  }

  void setDimensions(int sceneWidth, int sceneHeight) {
    if (properties == null) initializeProperties();
    int blockWidth = getBlockWidth(sceneWidth);
    int blockHeight = getBlockHeight(sceneHeight);
    setWidth(blockWidth);
    setHeight(blockHeight);
  }

  int getBlockWidth(int sceneWidth) {
    return sceneWidth / getBlocksPerRow();
  }

  int getBlockHeight(int sceneHeight) {
    return (sceneHeight-getInfoBarHeight()-getBlockConfigurationOffsetFromPaddle()) / getNumberOfBlockRows();
  }

  Paint getBlockColor(int blockHardness) {
    switch (blockHardness) {
      case 0 : return getBackgroundColor();
      case 1 : return getBlockColorOne();
      case 2: return getBlockColorTwo();
      case 3: return getBlockColorThree();
      default : throw new IllegalStateException("Unexpected value: " + blockHardness);
    }
  }

  public boolean isTouchingCircle(Circle collisionCircle) {
    double R = getBallRadius();
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

  private double getBallRadius() { return Double.parseDouble(properties.getProperty("ball_radius"));}

  public int getBlockHardness() {
    return blockHardness;
  }
  public void setBlockHardness(int blockHardness) {
    this.blockHardness = blockHardness;
  }
  // using Math.random()
  // used https://stackoverflow.com/questions/363681/how-do-i-generate-random-integers-within-a-specific-range-in-java
  public void setRandomHardness() {
    if (properties == null) initializeProperties();
    int inclusiveRange = getMaximumHardness() - getMinimumHardness() + 1;
    int randomHardness = getMinimumHardness() + (int)(Math.random() * inclusiveRange);
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
  public void releasePowerUp() {
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

  public boolean isTouchingBlockTop(Circle collisionCircle) {
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

  boolean isTouchingBlockRightSide(Circle collisionCircle) {
    double xPos = this.getX();
    double xCoord = collisionCircle.getCenterX();
    double Xn = calculateXn(xPos, xCoord);
    boolean touchingRightSide = Xn==this.getCenterX()+this.getWidth()/2;
    return isTouchingBlockTop(collisionCircle) && touchingRightSide;
  }

  boolean isTouchingBlockLeftSide(Circle collisionCircle) {
    double xPos = this.getX();
    double xCoord = collisionCircle.getCenterX();
    double Xn = calculateXn(xPos, xCoord);
    boolean touchingLeftSide = Xn==this.getCenterX()-this.getWidth()/2;
    return isTouchingBlockTop(collisionCircle) && touchingLeftSide;
  }

  public boolean isTouchingLeftOrRight(Circle circle) {
    return isTouchingBlockLeftSide(circle) || isTouchingBlockRightSide(circle);
  }

  private double getCenterX() {
    return (getX() + getWidth())/2.0;
  }

  private double calculateXn(double xPos, double xCoord) {
    return Math.max(xPos, Math.min(xCoord, (xPos + this.getWidth())));
  }

  void updateAttributes(double elapsedTime, boolean gameIsPaused) {}

  private int getInfoBarHeight() { return Integer.parseInt(properties.getProperty("info_bar_height")); }
  private Paint getBackgroundColor() { return Paint.valueOf(properties.getProperty("background_color")); }
  private Paint getBlockColorOne() { return Paint.valueOf(properties.getProperty("block_strength_one")); }
  private Paint getBlockColorTwo() { return Paint.valueOf(properties.getProperty("block_strength_two")); }
  private Paint getBlockColorThree() { return Paint.valueOf(properties.getProperty("block_strength_three")); }
  private int getBlocksPerRow() { return Integer.parseInt(properties.getProperty("blocks_per_row")); }
  private int getNumberOfBlockRows() { return Integer.parseInt(properties.getProperty("number_of_block_rows")); }
  public int getMinimumHardness() { return Integer.parseInt(properties.getProperty("minimum_hardness")); }
  private int getMaximumHardness() { return Integer.parseInt(properties.getProperty("maximum_hardness")); }
  private int getBlockConfigurationOffsetFromPaddle() { return Integer.parseInt(properties.getProperty("block_configuration_offset_from_paddle"));}
  public int getMovingBlockVelocity() {
    if (properties == null) initializeProperties();
    return Integer.parseInt(properties.getProperty("moving_block_speed"));}
    public double getVaryingBlockDownTime() { return Double.parseDouble(properties.getProperty("varying_block_down_time"));}
}
