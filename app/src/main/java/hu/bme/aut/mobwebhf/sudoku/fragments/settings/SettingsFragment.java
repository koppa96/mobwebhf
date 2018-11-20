package hu.bme.aut.mobwebhf.sudoku.fragments.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import hu.bme.aut.mobwebhf.sudoku.MainActivity;
import hu.bme.aut.mobwebhf.sudoku.R;

public class SettingsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        RadioButton rBtnLight = view.findViewById(R.id.rBtnLight);
        final RadioButton rBtnDark = view.findViewById(R.id.rBtnDark);
        Button btnSet = view.findViewById(R.id.btnSet);

        final SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        if (sharedPref.getBoolean("dark", false)) {
            rBtnDark.setChecked(true);
        } else {
            rBtnLight.setChecked(true);
        }

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editor edit = sharedPref.edit();
                edit.putBoolean("dark", rBtnDark.isChecked());
                edit.apply();

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }
}
