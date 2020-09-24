package gameElements;

/**
 *
 */
public class MovingBlock extends Block {

  public static final int MOVING_BLOCK_VELOCITY = 75;

  private boolean isMovingLeft;
  private int velocity;

  public MovingBlock(){
    super();
    setVelocity(MOVING_BLOCK_VELOCITY);
  }

  @Override
  public void updateLocationAndVelocity(double elapsedTime, boolean gameIsPaused) {
    if (!gameIsPaused) {
      updateLocation(elapsedTime);
      updateVelocity();
    }
  }

  private void updateLocation(double elapsedTime) {
    double oldXPosition = getX();
    setX(oldXPosition + elapsedTime * velocity);
  }

  private void updateVelocity() {
    if (isTouchingLeftWall() || isTouchingRightWall()) {
      negateVelocity();
    }
  }

  private void negateVelocity() {
    if (Math.abs(velocity) != MOVING_BLOCK_VELOCITY) velocity = MOVING_BLOCK_VELOCITY;
    velocity *= -1;
  }

  private boolean isTouchingRightWall() {
    double rightWallBounce = getX() + getWidth();
    return rightWallBounce > getScene().getWidth();
  }

  private boolean isTouchingLeftWall() {
    double leftWallBounce = getX();
    return leftWallBounce <= 0;
  }

  @Override
  public int getVelocity() { return this.velocity; }
  public void setVelocity(int velocity) { this.velocity = velocity; }
}
