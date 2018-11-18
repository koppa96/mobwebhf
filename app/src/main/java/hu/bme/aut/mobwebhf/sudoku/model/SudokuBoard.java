package hu.bme.aut.mobwebhf.sudoku.model;

import android.support.annotation.NonNull;

import java.util.LinkedList;
import java.util.Random;

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
        for (int i = 0; i < 81; i++) {
            int value = Character.getNumericValue(serializedSudoku.charAt(i));

            int removePercent;
            switch (difficulty) {
                case EASY:
                    removePercent = 40;
                    break;
                case HARD:
                    removePercent = 60;
                    break;
                case MEDIUM:
                    removePercent = 50;
                    break;
                default:
                    removePercent = 50;
            }

            if (random.nextInt(100) < removePercent) {
                board[i / 9][i % 9] = new SudokuField(0, true);
            } else {
                board[i / 9][i % 9] = new SudokuField(value, false);
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

    public SudokuField[][] getBoard() {
        return board;
    }

    @NonNull
    public static SudokuBoard parseBoard(String lastGame) {
        String[] boardElements = lastGame.split(" ");
        SudokuBoard newBoard = new SudokuBoard();

        newBoard.difficulty = Difficulty.valueOf(boardElements[0]);
        for (int i = 0; i < 81; i++) {
            int value = Integer.parseInt(boardElements[i]);
            boolean fixed = boardElements[i].charAt(1) == 'f';

            newBoard.board[i / 9][i % 9] = new SudokuField(value, fixed);
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
}
