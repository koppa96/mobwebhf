package hu.bme.aut.mobwebhf.sudoku.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface SudokuDao {
    @Query("SELECT * FROM sudokus")
    List<Sudoku> getAll();

    @Query("SELECT * FROM sudokus WHERE id = :id LIMIT 1")
    Sudoku getSudokuById(long id);

    @Query("SELECT count(*) FROM sudokus")
    int getSudokuCount();

    @Insert
    void insertAll(Sudoku... sudokus);

    @Insert
    void insert(Sudoku sudoku);

    @Delete
    void delete(Sudoku sudoku);
}
