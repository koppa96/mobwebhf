package hu.bme.aut.mobwebhf.sudoku.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "highscores")
public class Highscore {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String player;
    public String difficulty;
    public int seconds;
}
