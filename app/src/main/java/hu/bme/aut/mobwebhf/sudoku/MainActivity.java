package hu.bme.aut.mobwebhf.sudoku;

import android.annotation.SuppressLint;
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


import hu.bme.aut.mobwebhf.sudoku.data.AppDatabase;
import hu.bme.aut.mobwebhf.sudoku.fragments.GameFragment;
import hu.bme.aut.mobwebhf.sudoku.fragments.HighscoreFragment;
import hu.bme.aut.mobwebhf.sudoku.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity
                            implements NavigationView.OnNavigationItemSelectedListener {
    private AppDatabase database;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = AppDatabase.getInstance(getApplicationContext());
        loadSudokuCount();

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

        getSupportFragmentManager().beginTransaction()
                                   .replace(R.id.fragment_container, new GameFragment())
                                   .commit();
        navigationView.setCheckedItem(R.id.nav_playgame);
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
    private void loadSudokuCount() {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                return database.sudokuDao().getSudokuCount();
            }
        }.execute();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        FragmentManager manager = getSupportFragmentManager();
        switch (menuItem.getItemId()) {
            case R.id.nav_playgame:
                FragmentTransaction transaction = manager.beginTransaction();

                transaction.replace(R.id.fragment_container, new GameFragment());
                transaction.commit();
                break;
            case R.id.nav_highscores:
                transaction = manager.beginTransaction();

                transaction.replace(R.id.fragment_container, new HighscoreFragment());
                transaction.commit();
                break;
            case R.id.nav_settings:
                transaction = manager.beginTransaction();

                transaction.replace(R.id.fragment_container, new SettingsFragment());
                transaction.commit();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
