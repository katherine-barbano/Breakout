package text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javafx.scene.Group;

public class GameOverScoreText extends GameOverText {

  private List<String> previousGameScores;

  public GameOverScoreText(Group gameRootArg) {
    super(gameRootArg);
    setFill(getGameOverScoreColor());
    previousGameScores = new ArrayList<>();
  }

  @Override
  public void initializeText(String words) {
    initializeProperties(getGameOverScoreTitle(), getGameOverScoreXPosition(), getGameOverScoreYPosition(), getGameOverScoreId());
    addText();
  }

  private void updateScoreBoard() {
    StringBuilder stringBuilder = new StringBuilder(getGameOverScoreTitle());
    String[] highestScores = getHighestScores();
    for (String score : highestScores) {
      stringBuilder.append(makeHighScore(score));
    }
    stringBuilder.append(getScoreBoardExitText());
    updateText(stringBuilder.toString());
  }

  private String makeHighScore(String score) {
    return getGameOverScorePrefix() + score + getGameOverScoreSeparator();
  }

  private String[] getHighestScores() {
    Collections.sort(previousGameScores);
    String[] highScores = new String[getGameOverScoreAmountShown()];
    for (int i = 0; i < highScores.length; i++) {
      highScores[i] = (i >= previousGameScores.size()) ?  "TBD" : String.valueOf(previousGameScores.get(i));
    }
    return highScores;
  }

  public void addScoreToList(int score) {
    this.previousGameScores.add(String.valueOf(score));
    updateScoreBoard();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) { return true; }
    else if (o == null || getClass() != o.getClass()) { return false; }
    GameOverScoreText that = (GameOverScoreText) o;
    return Objects.equals(previousGameScores, that.previousGameScores);
  }

  @Override
  public int hashCode() {
    return Objects.hash(previousGameScores);
  }
}
