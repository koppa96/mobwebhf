package hu.bme.aut.mobwebhf.sudoku.fragments.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

import hu.bme.aut.mobwebhf.sudoku.MainActivity;
import hu.bme.aut.mobwebhf.sudoku.R;
import hu.bme.aut.mobwebhf.sudoku.data.database.AppDatabase;

public class SettingsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        RadioButton rBtnLight = view.findViewById(R.id.rBtnLight);
        final RadioButton rBtnDark = view.findViewById(R.id.rBtnDark);
        Button btnSet = view.findViewById(R.id.btnSet);
        final CheckBox cbDeleteHighscores = view.findViewById(R.id.cbDeleteHighscores);

        final SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        if (sharedPref.getBoolean("dark", false)) {
            rBtnDark.setChecked(true);
        } else {
            rBtnLight.setChecked(true);
        }

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbDeleteHighscores.isChecked()) {
                    AlertDialog dialog = new AlertDialog.Builder(getContext())
                            .setTitle(R.string.sure)
                            .setMessage(R.string.sure_delete_highscores)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteHighscores();
                                }
                            })
                            .setNegativeButton(R.string.no, null)
                            .create();
                    dialog.show();
                }

                if (sharedPref.getBoolean("dark", false) != rBtnDark.isChecked()) {
                    Editor edit = sharedPref.edit();
                    edit.putBoolean("dark", rBtnDark.isChecked());
                    edit.apply();

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });

        return view;
    }

    @SuppressLint("StaticFieldLeak")
    private void deleteHighscores() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                AppDatabase database = AppDatabase.getInstance(getContext());
                database.highscoreDao().deleteAll();

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Toast.makeText(getContext(), R.string.highscores_deleted, Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }
}
