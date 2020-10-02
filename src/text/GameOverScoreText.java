package text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javafx.scene.Group;

public class GameOverScoreText extends GameOverText {

  private List<String> previousGameScores;
  private boolean hasLastScore;

  public GameOverScoreText(Group gameRootArg) {
    super(gameRootArg);
    setFill(getGameOverScoreColor());
    previousGameScores = new ArrayList<>();
    hasLastScore = false;
  }

  @Override
  public void initializeText(String words) {
    initializeProperties(getGameOverScoreTitle(), getGameOverScoreXPosition(),
        getGameOverScoreYPosition(), getGameOverScoreId());
    addText();
  }

  private String getUpdatedScoreBoard(int score) {
    previousGameScores.add(String.valueOf(score));
    String headerString = makeHeaderString(score);
    StringBuilder stringBuilder = new StringBuilder(headerString);

    String[] highestScores = getHighestScores();
    for (String highScore : highestScores) {
      stringBuilder.append(makeHighScore(highScore));
    }
    stringBuilder.append(getScoreBoardExitText());
    return stringBuilder.toString();
  }

  private String makeHeaderString(int score) {
    String yourScoreLine = getGameOverYourScoreTitle() + score;
    return yourScoreLine + getGameOverScoreTitle();
  }

  private String makeHighScore(String score) {
    return getGameOverScorePrefix() + score + getGameOverScoreSeparator();
  }

  private String[] getHighestScores() {
    Collections.sort(previousGameScores, Collections.reverseOrder());
    String[] highScores = new String[getGameOverScoreAmountShown()];
    for (int i = 0; i < highScores.length; i++) {
      highScores[i] =
          (i >= previousGameScores.size()) ? "TBD" : String.valueOf(previousGameScores.get(i));
    }
    return highScores;
  }

  public void addScoreToList(int score) {
    if (!hasLastScore) {
      String newScoreBoard = getUpdatedScoreBoard(score);
      updateText(newScoreBoard);
      hasLastScore = true;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GameOverScoreText that = (GameOverScoreText) o;
    return Objects.equals(previousGameScores, that.previousGameScores);
  }

  @Override
  public int hashCode() {
    return Objects.hash(previousGameScores);
  }

  public void setHasLastScore(boolean hasLastScore) {
    this.hasLastScore = hasLastScore;
  }
}
