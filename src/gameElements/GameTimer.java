package gameElements;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.util.Duration;
import text.GameText;
import text.TimerText;

public class GameTimer {
  private Timeline timeline;
  private int timeSeconds;
  private GameText timerText;
  private Group root;

  //referenced https://asgteach.com/2011/10/javafx-animation-and-binding-simple-countdown-timer-2/ for how to set up Timeline with javafx
  public GameTimer(Group gameRoot, int timeStart) {
    root = gameRoot;
    this.timeSeconds = timeStart;
    timerText = new TimerText(timeSeconds,root);
    initializeTimeline();
    pauseTimer();
  }

  private void initializeTimeline() {
    timeline = new Timeline();
    timeline.setCycleCount(timeSeconds);
    EventHandler timerUpdate = new EventHandler() {
      public void handle(Event event) {
        timeSeconds--;

        TimerText subclassTimerText = (TimerText) timerText;
        subclassTimerText.updateValue(timeSeconds);
        timerText = subclassTimerText;

        if(timeSeconds <=0) {
          timeline.stop();
        }
      }
    };
    KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), timerUpdate);
    timeline.getKeyFrames().add(keyFrame);

    timeline.playFromStart();
  }

  void removeTimerText() {
    timerText.removeText();
  }

  void pauseTimer() {
    timeline.pause();
  }

  void unpauseTimer() {
    timeline.play();
  }

  boolean timeIsUp() {
    return timeSeconds==0;
  }
}
