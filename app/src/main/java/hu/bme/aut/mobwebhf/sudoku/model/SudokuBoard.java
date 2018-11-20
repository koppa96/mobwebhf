package hu.bme.aut.mobwebhf.sudoku.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class SudokuBoard {
    private SudokuField[][] board;
    private Difficulty difficulty;
    private SudokuBoard() {
        board = new SudokuField[9][9];
    }

    public SudokuBoard(String serializedSudoku, Difficulty difficulty) throws IllegalArgumentException {
        if (serializedSudoku.length() != 81) {
            throw new IllegalArgumentException();
        }

        board = new SudokuField[9][9];
        this.difficulty = difficulty;

        Random random = new Random();
        ArrayList<Integer> keptIndexes = new ArrayList<>();
        for (int i = 0; i < 81; i++) {
            keptIndexes.add(i);
        }

        for (int i = 0; i < itemsToRemove(difficulty); i++) {
            keptIndexes.remove(random.nextInt(keptIndexes.size()));
        }

        for (int i = 0; i < 81; i++) {
            if (!keptIndexes.contains(i)) {
                board[i / 9][i % 9] = new SudokuField(0, true);
            } else {
                board[i / 9][i % 9] = new SudokuField(Character.getNumericValue(serializedSudoku.charAt(i)), false);
            }
        }
    }

    public boolean isFilled() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j].getValue() == 0) {
                    return false;
                }
            }
        }

        return true;
    }

    private int itemsToRemove(Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return 35;
            case MEDIUM:
                return 45;
            case HARD:
                return 55;
        }

        return 45;
    }

    public boolean checkIfWon() {
        return rowsOk() && colsOk() && groupsOk();
    }

    private boolean rowsOk() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 8; j++) {
                for (int k = j + 1; k < 9; k++) {
                    if (board[i][j].getValue() == board[i][k].getValue()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private boolean colsOk() {
        for (int j = 0; j < 9; j++) {
            for (int i = 0; i < 8; i++) {
                for (int k = i + 1; k < 9; k++) {
                    if (board[i][j].getValue() == board[k][j].getValue()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private boolean groupsOk() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                LinkedList<Integer> numbers = new LinkedList<>();
                for (int k = i * 3; k < i * 3 + 3; k++) {
                    for (int l = j * 3; l < j * 3 + 3; l++) {
                        if (numbers.contains(board[k][l].getValue())) {
                            return false;
                        }

                        numbers.add(board[k][l].getValue());
                    }
                }
            }
        }

        return true;
    }

    public Set<Coordinate> conflictingFields() {
        Set<Coordinate> fields = new HashSet<>();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 8; j++) {
                for (int k = j + 1; k < 9; k++) {
                    if (board[i][j].getValue() != 0 && board[i][j].getValue() == board[i][k].getValue()) {
                        fields.add(new Coordinate(i, j));
                        fields.add(new Coordinate(i, k));
                    }
                }
            }
        }

        for (int j = 0; j < 9; j++) {
            for (int i = 0; i < 8; i++) {
                for (int k = i + 1; k < 9; k++) {
                    if (board[i][j].getValue() != 0 && board[i][j].getValue() == board[k][j].getValue()) {
                        fields.add(new Coordinate(i, j));
                        fields.add(new Coordinate(k, j));
                    }
                }
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                ArrayList<Integer> numbers = new ArrayList<>();
                for (int k = i * 3; k < i * 3 + 3; k++) {
                    for (int l = j * 3; l < j * 3 + 3; l++) {
                        numbers.add(board[k][l].getValue());
                    }
                }

                for (int k = 0; k < 8; k++) {
                    for (int l = k + 1; l < 9; l++) {
                        if (numbers.get(k) != 0 && numbers.get(k).equals(numbers.get(l))) {
                            fields.add(new Coordinate(i * 3 + k / 3, j * 3 + k % 3));
                            fields.add(new Coordinate(i * 3 + l / 3, j * 3 + l % 3));
                        }
                    }
                }
            }
        }

        return fields;
    }

    public SudokuField[][] getBoard() {
        return board;
    }

    @NonNull
    public static SudokuBoard parseBoard(String lastGame) {
        String[] boardElements = lastGame.split(" ");
        SudokuBoard newBoard = new SudokuBoard();

        newBoard.difficulty = Difficulty.valueOf(boardElements[0]);
        for (int i = 0; i < 81; i++) {
            int value = Integer.parseInt(boardElements[i + 1].substring(0, 1));
            boolean variable = boardElements[i + 1].length() == 1;

            newBoard.board[i / 9][i % 9] = new SudokuField(value, variable);
        }

        return newBoard;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(difficulty.toString() + " ");
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                builder.append(board[i][j].toString() + " ");
            }
        }

        return builder.toString();
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }
}
