package hu.bme.aut.mobwebhf.sudoku.fragments.game;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import hu.bme.aut.mobwebhf.sudoku.MainActivity;
import hu.bme.aut.mobwebhf.sudoku.R;
import hu.bme.aut.mobwebhf.sudoku.data.database.AppDatabase;
import hu.bme.aut.mobwebhf.sudoku.data.entity.SavedSudoku;
import hu.bme.aut.mobwebhf.sudoku.model.Difficulty;

public class PauseFragment extends Fragment {
    private AppDatabase database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_pause, container, false);
        ImageButton btnResume = view.findViewById(R.id.btnResume);
        ImageButton btnCancel = view.findViewById(R.id.btnCancel);
        database = AppDatabase.getInstance(getActivity().getApplicationContext());

        btnResume.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, SavedSudoku>() {
                    @Override
                    protected SavedSudoku doInBackground(Void... voids) {
                        SavedSudoku savedSudoku = database.savedSudokuDao().getAll().get(0);
                        database.savedSudokuDao().deleteAll();

                        return savedSudoku;
                    }

                    @Override
                    protected void onPostExecute(SavedSudoku savedSudoku) {
                        MainActivity activity = (MainActivity) getActivity();
                        activity.startGameFromBoard(savedSudoku.board, true, savedSudoku.time, Difficulty.MEDIUM);
                    }
                }.execute();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        database.savedSudokuDao().deleteAll();
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        MainActivity activity = (MainActivity) getActivity();
                        activity.navigateHomeScreen();
                    }
                }.execute();
            }
        });

        return view;
    }
}
