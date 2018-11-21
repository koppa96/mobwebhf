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
import android.widget.RadioButton;

import java.util.Random;

import hu.bme.aut.mobwebhf.sudoku.MainActivity;
import hu.bme.aut.mobwebhf.sudoku.R;
import hu.bme.aut.mobwebhf.sudoku.data.database.AppDatabase;
import hu.bme.aut.mobwebhf.sudoku.data.entity.Sudoku;
import hu.bme.aut.mobwebhf.sudoku.data.database.AppDatabase;
import hu.bme.aut.mobwebhf.sudoku.fragments.settings.SettingsFragment;
import hu.bme.aut.mobwebhf.sudoku.model.Difficulty;

public class HomeFragment extends Fragment {
    private AppDatabase database;
    private RadioButton rBtnEasy, rBtnMedium, rBtnHard;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ImageButton imgBtnSettings = view.findViewById(R.id.imgBtnSettings);
        ImageButton imgBtnPlay = view.findViewById(R.id.imgBtnStart);
        database = AppDatabase.getInstance(getActivity().getApplicationContext());

        rBtnEasy = view.findViewById(R.id.rBtnEasy);
        rBtnMedium = view.findViewById(R.id.rBtnMedium);
        rBtnHard = view.findViewById(R.id.rBtnHard);

        imgBtnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.animator.fade_in, R.animator.fade_out)
                        .replace(R.id.fragment_container, new SettingsFragment())
                        .commit();
            }
        });

        imgBtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRandomSudoku();
            }
        });

        return view;
    }

    @SuppressLint("StaticFieldLeak")
    void loadRandomSudoku() {
        new AsyncTask<Void, Void, Sudoku>() {
            @Override
            protected Sudoku doInBackground(Void... voids) {
                int count = database.sudokuDao().getSudokuCount();
                Random random = new Random();

                return database.sudokuDao().getSudokuById(random.nextInt(count));
            }

            @Override
            protected void onPostExecute(Sudoku sudoku) {
                MainActivity activity = (MainActivity) getActivity();
                activity.startGameFromBoard(sudoku.board, false, 0, getSelectedDifficulty());
            }
        }.execute();
    }

    Difficulty getSelectedDifficulty() {
        return rBtnEasy.isChecked() ? Difficulty.EASY : rBtnMedium.isChecked() ? Difficulty.MEDIUM : Difficulty.HARD;
    }
}