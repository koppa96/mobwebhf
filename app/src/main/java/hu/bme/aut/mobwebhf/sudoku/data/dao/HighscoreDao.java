package hu.bme.aut.mobwebhf.sudoku.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import hu.bme.aut.mobwebhf.sudoku.data.entity.Highscore;

@Dao
public interface HighscoreDao {
    @Query("SELECT * FROM highscores")
    List<Highscore> getAll();

    @Query("SELECT * FROM highscores WHERE difficulty = :diff ORDER BY seconds")
    List<Highscore> getHighscoresOfDifficulty(String diff);

    @Insert
    void insert(Highscore highscore);

    @Delete
    void delete(Highscore highscore);
}
