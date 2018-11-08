package hu.bme.aut.mobwebhf.sudoku.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "sudokus")
public class Sudoku {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String board;
}
