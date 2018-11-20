package hu.bme.aut.mobwebhf.sudoku.fragments.highscore;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hu.bme.aut.mobwebhf.sudoku.R;
import hu.bme.aut.mobwebhf.sudoku.fragments.highscore.adapter.ViewPagerAdapter;
import hu.bme.aut.mobwebhf.sudoku.model.Difficulty;

public class HighscoreFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_highscores, container, false);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        HighscoreListFragment highscoreListFragment = new HighscoreListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("difficulty", Difficulty.EASY.toString());
        highscoreListFragment.setArguments(bundle);
        adapter.addFragment(highscoreListFragment, getString(R.string.easy));

        highscoreListFragment = new HighscoreListFragment();
        bundle = new Bundle();
        bundle.putString("difficulty", Difficulty.MEDIUM.toString());
        highscoreListFragment.setArguments(bundle);
        adapter.addFragment(highscoreListFragment, getString(R.string.medium));

        highscoreListFragment = new HighscoreListFragment();
        bundle = new Bundle();
        bundle.putString("difficulty", Difficulty.HARD.toString());
        highscoreListFragment.setArguments(bundle);
        adapter.addFragment(highscoreListFragment, getString(R.string.hard));

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}
