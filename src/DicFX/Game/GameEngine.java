package DicFX.Game;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;


public class GameEngine {

    private final int MAX_COL = 5;
    private final int MAX_ROW = 6;
    private int CUR_ROW = 1;
    private int CUR_COLUMN = 1;
    private String randomWord;

    public static final ArrayList<String> loadedWordList = new ArrayList<>();

    public void initGameWord() {
        // Data lay tu https://gist.github.com/scholtes/94f3c0303ba6a7768b47583aff36654d
        InputStream wordList = getClass().getResourceAsStream("game_Data.txt");

        if (wordList != null) {
            try (BufferedReader winningWordsReader = new BufferedReader(new InputStreamReader(wordList))) {
                winningWordsReader.lines().forEach(loadedWordList::add);
            } catch (IOException e) {
                System.out.println("Error reading winning words.");
            }
        } else {
            System.out.println("Error loading winning words file.");
        }
    }

    public void createGameUI(GridPane gameMain) {
        for (int i = 1; i <= MAX_ROW; i++) {
            for (int j = 1; j <= MAX_COL; j++) {
                Label label = new Label();
                label.getStyleClass().add("label-default");
                gameMain.add(label, j, i);
            }
        }
    }


    public void generateRandomWord() {
        randomWord = loadedWordList.get(new Random().nextInt(loadedWordList.size()));
    }

    // Xu ly event

    public void onKeyPressed(GridPane gameMain, KeyEvent keyEvent) {
        Platform.runLater(() -> {
            KeyCode keyCode = keyEvent.getCode();
            if (keyCode == KeyCode.BACK_SPACE) {
                onBackSpacePressed(gameMain);
            } else if (keyCode.isLetterKey()) {
                onLetterPressed(gameMain, keyEvent);
            } else if (keyCode == KeyCode.ENTER) {
                onEnterPressed(gameMain);
            }
        });
    }

    public void resetGame(GridPane gameMain) {
        generateRandomWord();
        Label label;
        for (Node child : gameMain.getChildren())
            if (child instanceof Label) {
                label = (Label) child;
                label.getStyleClass().clear();
                label.setText("");
                label.getStyleClass().add("label-default");
            }
        CUR_COLUMN = 1;
        CUR_ROW = 1;
    }

    private void onBackSpacePressed(GridPane gameMain) {
        if ((CUR_COLUMN == MAX_COL || CUR_COLUMN == 1)
                && !Objects.equals(getLabelText(gameMain, CUR_ROW, CUR_COLUMN), "")) {
            clearTile(gameMain);
        } else if ((CUR_COLUMN > 1 && CUR_COLUMN < MAX_COL)
                || (CUR_COLUMN == MAX_COL && Objects.equals(getLabelText(gameMain, CUR_ROW, CUR_COLUMN), ""))) {
            CUR_COLUMN--;
            clearTile(gameMain);
        }
    }

    private void clearTile(GridPane gameMain) {
        setLabelText(gameMain, CUR_ROW, CUR_COLUMN, "");
        clearLabelStyleClass(gameMain, CUR_ROW, CUR_COLUMN);
        setLabelStyleClass(gameMain, CUR_ROW, CUR_COLUMN, "label-default");
    }

    private void onLetterPressed(GridPane gameMain, KeyEvent keyEvent) {
        if (Objects.equals(getLabelText(gameMain, CUR_ROW, CUR_COLUMN), "")) {
            setLabelText(gameMain, CUR_ROW, CUR_COLUMN, keyEvent.getText());
            animateLabel(gameMain);
            if (CUR_COLUMN < MAX_COL) {
                CUR_COLUMN++;
            }
        }
    }

    private void animateLabel(GridPane gameMain) {

        Label label = getLabel(gameMain, CUR_ROW, CUR_COLUMN);
        ScaleTransition firstTrans = new ScaleTransition(Duration.millis(100), label);
        firstTrans.setFromX(1);
        firstTrans.setToX(1.1);
        firstTrans.setFromY(1);
        firstTrans.setToY(1.1);
        ScaleTransition secondTrans = new ScaleTransition(Duration.millis(100), label);
        secondTrans.setFromX(1.1);
        secondTrans.setToX(1);
        secondTrans.setFromY(1.1);
        secondTrans.setToY(1);
        new SequentialTransition(firstTrans, secondTrans).play();
        setLabelStyleClass(gameMain, CUR_ROW, CUR_COLUMN, "label-with-letter");
    }

    private void onEnterPressed(GridPane gameMain) {
        if (CUR_ROW <= MAX_ROW && CUR_COLUMN == MAX_COL) {
            String ans = getWordFromCurrentRow(gameMain).toLowerCase();
            if (ans.equals(randomWord)) {
                updateRowColors(gameMain, CUR_ROW);
                EndWindow.display(true, randomWord);
            } else if (AnsInList(ans)) {
                handleValidAns(gameMain);
            } else {
                Alert.newAlert();
            }
            handleGameReset(gameMain);
        }
    }

    private void handleValidAns(GridPane gameMain) {
        updateRowColors(gameMain, CUR_ROW);
        if (CUR_ROW == MAX_ROW) {
            EndWindow.display(false, randomWord);
        }
        CUR_ROW++;
        CUR_COLUMN = 1;
    }


    private void handleGameReset(GridPane gameMain) {
        if (EndWindow.resetGame) {
            resetGame(gameMain);
            EndWindow.resetGame = false;
        }
    }


    private void updateRowColors(GridPane gameMain, int row) {
        for (int i = 1; i <= MAX_COL; i++) {
            Label label = getLabel(gameMain, row, i);
            if (label != null) {
                String styleClass = getStyleClass(label.getText().charAt(0), i);
                changeColorWithFade(label, styleClass);
            }
        }
    }

    private String getStyleClass(char currentChar, int i) {
        currentChar = Character.toLowerCase(currentChar);
        if (currentChar == randomWord.charAt(i - 1)) {
            return "correct-label";
        } else if (randomWord.contains(String.valueOf(currentChar))) {
            return "contain-label";
        } else {
            return "wrong-label";
        }
    }

    private void changeColorWithFade(Label label, String styleClass) {
        FadeTransition firstTrans = new FadeTransition(Duration.millis(300), label);
        firstTrans.setFromValue(1);
        firstTrans.setToValue(0.2);

        firstTrans.setOnFinished(e -> {
            label.getStyleClass().clear();
            label.getStyleClass().setAll(styleClass);
        });

        FadeTransition secondTrans = new FadeTransition(Duration.millis(300), label);
        secondTrans.setFromValue(0.2);
        secondTrans.setToValue(1);

        new SequentialTransition(firstTrans, secondTrans).play();
    }

    private String getWordFromCurrentRow(GridPane gameMain) {
        StringBuilder input = new StringBuilder();
        for (int j = 1; j <= MAX_COL; j++)
            input.append(getLabelText(gameMain, CUR_ROW, j));
        return input.toString();
    }


    private boolean AnsInList(String guess) {
        return binarySearch(guess);
    }


    private boolean binarySearch(String string) {
        int i = 0;
        int j = GameEngine.loadedWordList.size() - 1;

        while (i <= j) {
            int mid = i + (j - i) / 2;
            int cmp = string.compareTo(GameEngine.loadedWordList.get(mid));

            if (cmp == 0) {
                return true;
            } else if (cmp < 0) {
                j = mid - 1;
            } else {
                i = mid + 1;
            }
        }

        return false;
    }

    private void setLabelText(GridPane gameMain, int row, int col, String input) {
        Label label = getLabel(gameMain, row, col);
        if (label != null) {
            label.setText(input.toUpperCase());
        }
    }

    private Label getLabel(GridPane gameMain, int searchRow, int searchColumn) {
        for (Node child : gameMain.getChildren()) {
            Integer r = GridPane.getRowIndex(child);
            Integer c = GridPane.getColumnIndex(child);
            int row = 0;
            int column = 0;
            if (r != null) {
                row = r;
            }
            if (c != null) {
                column = c;
            }
            if (row == searchRow && column == searchColumn && child instanceof Label) {
                return (Label) child;
            }
        }
        return null;
    }

    private String getLabelText(GridPane gameMain, int row, int col) {
        Label label = getLabel(gameMain, row, col);
        if (label != null) {
            return label.getText().toLowerCase();
        }
        return null;
    }

    private void setLabelStyleClass(GridPane gameMain, int row, int col, String styleClass) {
        Label label = getLabel(gameMain, row, col);
        if (label != null) {
            label.getStyleClass().add(styleClass);
        }
    }

    private void clearLabelStyleClass(GridPane gameMain, int row, int col) {
        Label label = getLabel(gameMain, row, col);
        if (label != null) {
            label.getStyleClass().clear();
        }
    }
}