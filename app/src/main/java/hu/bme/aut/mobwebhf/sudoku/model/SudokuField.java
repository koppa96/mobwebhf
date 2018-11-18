package hu.bme.aut.mobwebhf.sudoku.model;

import android.support.annotation.NonNull;

public class SudokuField {
    private boolean variable;
    private int value;

    public SudokuField(int value, boolean variable) {
        this.value = value;
        this.variable = variable;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isVariable() {
        return variable;
    }

    @NonNull
    @Override
    public String toString() {
        return Integer.toString(value) + (variable ? "" : "f");
    }
}
