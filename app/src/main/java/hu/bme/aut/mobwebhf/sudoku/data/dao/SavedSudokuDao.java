package hu.bme.aut.mobwebhf.sudoku.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import hu.bme.aut.mobwebhf.sudoku.data.entity.SavedSudoku;

@Dao
public interface SavedSudokuDao {
    @Query("SELECT * FROM savedsudokus")
    List<SavedSudoku> getAll();

    @Query("SELECT count(*) FROM savedsudokus")
    int numberOfSavedSudokus();

    @Query("DELETE FROM savedsudokus")
    void deleteAll();

    @Insert
    void insert(SavedSudoku savedSudoku);

    @Delete
    void delete(SavedSudoku savedSudoku);
}
