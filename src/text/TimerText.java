package text;

import javafx.scene.Group;

public class TimerText extends StatusText {

  public TimerText(int startTime, Group gameRootArg) {
    super(gameRootArg, startTime);
  }

  @Override
  void setStatusTextProperties(){
    setTitle(getTimerTitle());
    setX(getTimerXPosition());
    setY(getTimerYPosition());
    setId(getTimerId());
  }
}