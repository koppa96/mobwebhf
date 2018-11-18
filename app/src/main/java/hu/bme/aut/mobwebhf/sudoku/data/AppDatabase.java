package hu.bme.aut.mobwebhf.sudoku.data;

import android.annotation.SuppressLint;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import hu.bme.aut.mobwebhf.sudoku.R;

@Database(
        entities = { Sudoku.class, Highscore.class, SavedSudoku.class },
        version = 3
)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;
    public abstract SudokuDao sudokuDao();
    public abstract HighscoreDao highscoreDao();
    public abstract SavedSudokuDao savedSudokuDao();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, "sudoku-db")
                           .addCallback(new AppDatabaseCallback(context))
                           .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                           .build();
        }

        return instance;
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS 'savedsudokus' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'board' TEXT)");
        }
    };

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE 'savedsudokus' ADD COLUMN 'time' INTEGER");
        }
    };

    private static class AppDatabaseCallback extends RoomDatabase.Callback {
        private Context context;

        AppDatabaseCallback(Context context) {
            this.context = context;
        }

        @SuppressLint("StaticFieldLeak")
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    InputStream inputStream = context.getResources().openRawResource(R.raw.sudokus);
                    Scanner scanner = new Scanner(inputStream);

                    ArrayList<Sudoku> sudokus = new ArrayList<>();
                    while (scanner.hasNextLine()) {
                        Sudoku sudoku = new Sudoku();
                        sudoku.board = scanner.nextLine();
                        sudokus.add(sudoku);
                    }

                    Sudoku[] sudokuArray = sudokus.toArray(new Sudoku[sudokus.size()]);
                    getInstance(context).sudokuDao().insertAll(sudokuArray);
                    scanner.close();

                    return null;
                }
            }.execute();
        }
    }
}
