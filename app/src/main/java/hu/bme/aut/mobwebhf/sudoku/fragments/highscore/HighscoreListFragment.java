package hu.bme.aut.mobwebhf.sudoku.fragments.highscore;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import hu.bme.aut.mobwebhf.sudoku.R;
import hu.bme.aut.mobwebhf.sudoku.data.database.AppDatabase;
import hu.bme.aut.mobwebhf.sudoku.data.entity.Highscore;
import hu.bme.aut.mobwebhf.sudoku.fragments.highscore.adapter.HighscoreAdapter;
import hu.bme.aut.mobwebhf.sudoku.model.Difficulty;

public class HighscoreListFragment extends Fragment {
    private RecyclerView recyclerView;
    private HighscoreAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_highscore_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new HighscoreAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        loadHighscoresOfDifficulty(getArguments().getString("difficulty"));

        return view;
    }

    @SuppressLint("StaticFieldLeak")
    public void loadHighscoresOfDifficulty(String difficulty) {
        new AsyncTask<String, Void, List<Highscore>>() {
            @Override
            protected List<Highscore> doInBackground(String... strings) {
                AppDatabase database = AppDatabase.getInstance(getActivity().getApplicationContext());
                return database.highscoreDao().getHighscoresOfDifficulty(strings[0]);
            }

            @Override
            protected void onPostExecute(List<Highscore> highscores) {
                adapter.update(highscores);
            }
        }.execute(difficulty);
    }
}
