package text;

import javafx.scene.Group;
import javafx.scene.paint.Color;

public class TimerText extends StatusText {

  public static final String TIMER_TITLE = "Time: ";
  public static final int TIMER_XPOSITION = LevelText.LEVEL_XPOSITION;
  public static final int TIMER_YPOSITION = LevelText.LEVEL_YPOSITION+20;
  public static final String TIMER_ID = "#timerText";

  public TimerText(int startTime, Group gameRootArg) {
    super(gameRootArg, startTime, TIMER_TITLE, TIMER_XPOSITION, TIMER_YPOSITION, TIMER_ID);
  }
}