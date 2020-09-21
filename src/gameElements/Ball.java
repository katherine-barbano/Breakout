package gameElements;

import breakout.Game;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;


/***
 * Purpose: A circle that breaks Blocks, and ends the game if it crosses the
 * bottom of the screen. Bounces off all other walls of the screen, as well as
 * the Paddle and other Blocks.
 *
 * Method: isOnScreen, breakBlock, isTouchingBlockInBlockConfiguration, isTouchingPaddle
 */
public class Ball extends Circle {

  public static final Paint BALL_COLOR = Color.RED;
  public static final int BALL_RADIUS = 15;
  public static final int NORMAL_BALL_SPEED = 150;

  private final Paddle paddle;
  private BlockConfiguration blockConfiguration;
  private int velocityX;
  private int velocityY;
  private Group gameRoot;
  private boolean velocityCanBeUpdated = true;

  public Ball(Group gameRootArg, Paddle paddleArg, BlockConfiguration configuration) {
    gameRoot = gameRootArg;
    paddle = paddleArg;
    blockConfiguration = configuration;

    setBallProperties();

    gameRoot.getChildren().add(this);
  }

  public void setBallProperties() {
    setCenterX(Game.SCENE_SIZE / 2);
    setCenterY(paddle.getY() - BALL_RADIUS);
    setRadius(BALL_RADIUS);
    setFill(BALL_COLOR);
    setId("ball");
    velocityX = 0;
    velocityY = NORMAL_BALL_SPEED;
  }

  public void removeBall() {
    gameRoot.getChildren().remove(this);
  }

  public boolean updateCoordinatesAndContinue(double elapsedTime, boolean isPaused) {
    if (isTouchingBottomWall()) {
      return false;
    }
    if (!isPaused) {
      updateVelocityX();
      updateVelocityY();
      if(isTouchingPaddleLeftSide()) {
        setCenterX(paddle.getCenterX()-paddle.getWidth()/2-BALL_RADIUS);
      }
      else if(isTouchingPaddleRightSide()) {
        setCenterX(paddle.getCenterX()+paddle.getWidth()/2+BALL_RADIUS);
      }
      setCenterX(getCenterX() + velocityX * elapsedTime);
      setCenterY(getCenterY() + velocityY * elapsedTime);
    }
    return true;
  }

  public int getVelocityX() {
    return velocityX;
  }

  public void setVelocityX(int velocityXArg) {
    velocityX = velocityXArg;
  }

  public int getVelocityY() {
    return velocityY;
  }

  public void setVelocityY(int velocityYArg) {
    velocityY = velocityYArg;
  }

  private void updateVelocityX() {
    System.out.println("begin: "+velocityX);
    if (isTouchingSideWall()) {
      velocityX = velocityX * -1;
    }
    if (isTouchingPaddleTop() && velocityCanBeUpdated) {
      System.out.println("yo");
      velocityX = getVelocityXFromPaddleHit();
      System.out.println(velocityX);
      velocityCanBeUpdated=false;
    }
    else if (isTouchingPaddleRightSide() && velocityX >=0 && velocityCanBeUpdated) {
      System.out.println("gah");
      velocityX = velocityX + 100;
      System.out.println(velocityX);
      velocityCanBeUpdated = false;
    }
    else if (isTouchingPaddleRightSide() && velocityX <0 && velocityCanBeUpdated) {
      System.out.println("hello");
      velocityX = velocityX * -1 + 100;
      System.out.println(velocityX);
      velocityCanBeUpdated = false;
    }
    else if (isTouchingPaddleLeftSide() && velocityX >0 && velocityCanBeUpdated) {
      System.out.println("ok");
      velocityX = velocityX * -1 - 100;
      System.out.println(velocityX);
      velocityCanBeUpdated = false;
    }
    else if (isTouchingPaddleLeftSide() && velocityX <=0 && velocityCanBeUpdated) {
      System.out.println("bruh");
      velocityX = velocityX -100;
      System.out.println(velocityX);
      velocityCanBeUpdated = false;
    }
    if ((!isTouchingPaddleLeftSide() && !isTouchingPaddleRightSide()) && !isTouchingPaddleTop()) {
      System.out.println("here");
      velocityCanBeUpdated = true;
    }

  }

  private void updateVelocityY() {
    if (isTouchingPaddleTop() && !isTouchingPaddleRightSide() && !isTouchingPaddleLeftSide()) {
      velocityY = velocityY * -1;
    }
    if (isTouchingTopWall()) {
      velocityY = velocityY * -1;
    }
    if (isTouchingBlockInBlockConfiguration()) {
      velocityY *= -1;
    }
  }

  private boolean isTouchingBlockInBlockConfiguration() {
    for (int i = 0; i < blockConfiguration.getBlockRows().length; i++) {
      BlockRow row = blockConfiguration.getBlockRows()[i];
      for (int j = 0; j < row.getRowOfBlocks().length; j++) {
        Block tempBlock = row.getRowOfBlocks()[j];
        if (tempBlock.getBlockHardness() == 0) continue;
        if (isTouchingSingleBlock(tempBlock)) return true;
      }
    }
    return false;
  }

  private boolean isTouchingSingleBlock(Block block) {
    double R = BALL_RADIUS;
    double Xcoord = getCenterX();
    double Ycoord = getCenterY();
    double width = block.getWidth();
    double height = block.getHeight();
    double xPos = block.getX();
    double yPos = block.getY();

    // from https://www.geeksforgeeks.org/check-if-any-point-overlaps-the-given-circle-and-rectangle/
    double Xn = Math.max(xPos, Math.min(Xcoord, (xPos+width)));
    double Yn = Math.max(yPos, Math.min(Ycoord, (yPos + height)));
    double Dx = Xn - Xcoord;
    double Dy = Yn - Ycoord;
    return (Dx * Dx + Dy * Dy) <= R*R;
  }


  //TODO: modify the physics
  private int getVelocityXFromPaddleHit() {
    double distanceFromPaddleCenter = getCenterX() - paddle.getCenterX();
    double angleRatio = distanceFromPaddleCenter / paddle.getWidth();
    double angleRadians = Math.toRadians(angleRatio * 90);
    return (int) (velocityY * Math.sin(angleRadians));
  }

  //TODO: fix for edges of ball that are not in center
  private boolean isTouchingPaddleTop() {

    double R = BALL_RADIUS;
    double Xcoord = getCenterX();
    double Ycoord = getCenterY();
    double width = paddle.getWidth();
    double height = paddle.getHeight();
    double xPos = paddle.getX();
    double yPos = paddle.getY();

    // from https://www.geeksforgeeks.org/check-if-any-point-overlaps-the-given-circle-and-rectangle/
    double Xn = Math.max(xPos, Math.min(Xcoord, (xPos+width)));
    double Yn = Math.max(yPos, Math.min(Ycoord, (yPos + height)));
    double Dx = Xn - Xcoord;
    double Dy = Yn - Ycoord;
    return (Dx * Dx + Dy * Dy) <= R*R;


    /*boolean hitsInCorrectXCoordinates = paddle.getX() < getCenterX() &&
        getCenterX() < paddle.getX() + paddle.getWidth();
    boolean hitsInCorrectYCoordinate = paddle.getY() < getCenterY() + BALL_RADIUS;
    return hitsInCorrectYCoordinate && hitsInCorrectXCoordinates;*/
  }

  private boolean isTouchingPaddleLeftSide() {
    double R = BALL_RADIUS;
    double Xcoord = getCenterX();
    double Ycoord = getCenterY();
    double width = paddle.getWidth();
    double height = paddle.getHeight();
    double xPos = paddle.getX();
    double yPos = paddle.getY();

    // from https://www.geeksforgeeks.org/check-if-any-point-overlaps-the-given-circle-and-rectangle/
    double Xn = Math.max(xPos, Math.min(Xcoord, (xPos+width)));
    double Yn = Math.max(yPos, Math.min(Ycoord, (yPos + height)));
    double Dx = Xn - Xcoord;
    double Dy = Yn - Ycoord;
    //return (Dx * Dx + Dy * Dy) <= R*R;
    return (Dx * Dx + Dy * Dy) <= R*R && (Xn==paddle.getCenterX()-paddle.getWidth()/2);
  }

  private boolean isTouchingPaddleRightSide() {


    double R = BALL_RADIUS;
    double Xcoord = getCenterX();
    double Ycoord = getCenterY();
    double width = paddle.getWidth();
    double height = paddle.getHeight();
    double xPos = paddle.getX();
    double yPos = paddle.getY();

    // from https://www.geeksforgeeks.org/check-if-any-point-overlaps-the-given-circle-and-rectangle/
    double Xn = Math.max(xPos, Math.min(Xcoord, (xPos+width)));
    double Yn = Math.max(yPos, Math.min(Ycoord, (yPos + height)));
    double Dx = Xn - Xcoord;
    double Dy = Yn - Ycoord;
    //return (Dx * Dx + Dy * Dy) <= R*R;
    return (Dx * Dx + Dy * Dy) <= R*R && ( Xn==paddle.getCenterX()+paddle.getWidth()/2);




    /*boolean hitsLeftSideOfPaddleXCoordinate = getCenterX() + BALL_RADIUS > paddle.getX()
        && getCenterX() < paddle.getX();
    boolean hitsRightSideOfPaddleXCoordinate = getCenterX() - BALL_RADIUS < paddle.getX() +
        paddle.getWidth() && getCenterX() > paddle.getX() + paddle.getWidth();

    boolean hitsWithinTopOfPaddle = paddle.getY() < getCenterY() + BALL_RADIUS;
    boolean hitsWithinBottomOfPaddle =
        paddle.getY() + paddle.getHeight() > getCenterY() - BALL_RADIUS;

    boolean hitsInCorrectXCoordinate =
        hitsLeftSideOfPaddleXCoordinate || hitsRightSideOfPaddleXCoordinate;
    boolean hitsWithinYCoordinate = hitsWithinTopOfPaddle && hitsWithinBottomOfPaddle;

    return hitsWithinYCoordinate && hitsInCorrectXCoordinate;*/
  }

  private boolean isTouchingTopWall() {
    return getCenterY() - BALL_RADIUS < 0;
  }

  //assumes Scene has already been instantiated in Game so that it can use the getScene method
  private boolean isTouchingSideWall() {
    double leftWallBounce = getCenterX() - BALL_RADIUS;
    double rightWallBounce = getCenterX() + BALL_RADIUS;
    return leftWallBounce < 0 || rightWallBounce > getScene().getWidth();
  }

  //assumes Scene has already been instantiated in Game so that it can use the getScene method
  private boolean isTouchingBottomWall() {
    return getCenterY() + BALL_RADIUS >= getScene().getHeight();
  }
}
