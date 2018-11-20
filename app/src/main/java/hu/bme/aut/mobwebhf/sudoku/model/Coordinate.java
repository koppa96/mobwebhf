package hu.bme.aut.mobwebhf.sudoku.model;

import android.support.annotation.Nullable;

public class Coordinate {
    public int row, col;

    public Coordinate() {
        this(0, 0);
    }

    public Coordinate(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }

        Coordinate other = (Coordinate) obj;
        return row == other.row && col == other.col;
    }
}
