package text;

import javafx.scene.Group;

public class TimerText extends StatusText {

  public TimerText(int startTime, Group gameRootArg) {
    super(gameRootArg, startTime);
    setStatusTextProperties(getTimerTitle(), getTimerXPosition(), getTimerYPosition(), getTimerId());
  }
}