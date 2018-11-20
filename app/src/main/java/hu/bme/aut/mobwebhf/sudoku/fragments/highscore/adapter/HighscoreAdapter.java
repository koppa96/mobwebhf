package hu.bme.aut.mobwebhf.sudoku.fragments.highscore.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hu.bme.aut.mobwebhf.sudoku.R;
import hu.bme.aut.mobwebhf.sudoku.data.entity.Highscore;

public class HighscoreAdapter extends RecyclerView.Adapter<HighscoreAdapter.HighscoreViewHolder> {
    private List<Highscore> highscores;

    public HighscoreAdapter() {
        highscores = new ArrayList<>();
    }

    @NonNull
    @Override
    public HighscoreViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                                      .inflate(R.layout.layout_highscore_item, viewGroup, false);
        return new HighscoreViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HighscoreViewHolder highscoreViewHolder, int i) {
        Highscore highscore = highscores.get(i);
        highscoreViewHolder.tvPlace.setText(String.format("%1$d.", i + 1));
        highscoreViewHolder.tvName.setText(highscore.player);
        highscoreViewHolder.tvTime.setText(String.format("%1$d:%2$d", highscore.seconds / 60, highscore.seconds % 60));
        highscoreViewHolder.highscore = highscore;
    }

    @Override
    public int getItemCount() {
        return highscores.size();
    }

    public class HighscoreViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlace, tvName, tvTime;
        Highscore highscore;

        public HighscoreViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlace = itemView.findViewById(R.id.tvPlace);
            tvName = itemView.findViewById(R.id.tvName);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }

    public void addHighscore(Highscore highscore) {
        highscores.add(highscore);
        notifyItemInserted(highscores.size() - 1);
    }

    public void update(List<Highscore> highscores) {
        this.highscores.clear();
        this.highscores.addAll(highscores);
        notifyDataSetChanged();
    }
}
