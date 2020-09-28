package text;

import javafx.scene.Group;

public class LivesText extends StatusText {

  public LivesText(int numberOfLives, Group gameRootArg) {
    super(gameRootArg, numberOfLives);
    setStatusTextProperties(getLivesTitle(), getLivesXPosition(), getLivesYPosition(), getLivesId());
    addText();
  }
}
