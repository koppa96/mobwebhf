package hu.bme.aut.mobwebhf.sudoku;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


import hu.bme.aut.mobwebhf.sudoku.data.database.AppDatabase;
import hu.bme.aut.mobwebhf.sudoku.fragments.game.GameFragment;
import hu.bme.aut.mobwebhf.sudoku.fragments.highscore.HighscoreFragment;
import hu.bme.aut.mobwebhf.sudoku.fragments.game.HomeFragment;
import hu.bme.aut.mobwebhf.sudoku.fragments.game.PauseFragment;
import hu.bme.aut.mobwebhf.sudoku.fragments.settings.SettingsFragment;
import hu.bme.aut.mobwebhf.sudoku.model.Difficulty;

public class MainActivity extends AppCompatActivity
                            implements NavigationView.OnNavigationItemSelectedListener {
    private AppDatabase database;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        if (preferences.getBoolean("dark", false)) {
            setTheme(R.style.AppThemeDark);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = AppDatabase.getInstance(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                                                                    R.string.navigation_drawer_open,
                                                                    R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_playgame);

        loadSavedSudokuCount();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void loadSavedSudokuCount() {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                return database.savedSudokuDao().numberOfSavedSudokus();
            }

            @Override
            protected void onPostExecute(Integer count) {
                if (count > 0) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.animator.fade_in, R.animator.fade_out)
                            .replace(R.id.fragment_container, new PauseFragment())
                            .commit();
                } else {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.animator.fade_in, R.animator.fade_out)
                            .replace(R.id.fragment_container, new HomeFragment())
                            .commit();
                }
            }
        }.execute();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        FragmentManager manager = getSupportFragmentManager();
        switch (menuItem.getItemId()) {
            case R.id.nav_playgame:
                loadSavedSudokuCount();
                break;
            case R.id.nav_highscores:
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                transaction.replace(R.id.fragment_container, new HighscoreFragment());
                transaction.commit();
                break;
            case R.id.nav_settings:
                transaction = manager.beginTransaction();
                transaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                transaction.replace(R.id.fragment_container, new SettingsFragment());
                transaction.commit();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void startGameFromBoard(String board, boolean isSavedGame, int seconds, Difficulty difficulty) {
        Bundle bundle = new Bundle();
        bundle.putString("board", board);
        bundle.putBoolean("issaved", isSavedGame);
        bundle.putInt("seconds", seconds);
        bundle.putString("difficulty", difficulty.toString());
        GameFragment gameFragment = new GameFragment();
        gameFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.fade_in, R.animator.fade_out)
                .replace(R.id.fragment_container, gameFragment)
                .commit();
    }

    public void navigateHomeScreen() {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.fade_in, R.animator.fade_out)
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();
    }
}
