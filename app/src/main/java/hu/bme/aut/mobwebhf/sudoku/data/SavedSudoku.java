package hu.bme.aut.mobwebhf.sudoku.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "savedsudokus")
public class SavedSudoku {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String board;

    public int time;
}
