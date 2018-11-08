package hu.bme.aut.mobwebhf.sudoku.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Random;

import hu.bme.aut.mobwebhf.sudoku.R;
import hu.bme.aut.mobwebhf.sudoku.data.AppDatabase;
import hu.bme.aut.mobwebhf.sudoku.data.Sudoku;
import hu.bme.aut.mobwebhf.sudoku.model.Difficulty;
import hu.bme.aut.mobwebhf.sudoku.model.SudokuBoard;
import hu.bme.aut.mobwebhf.sudoku.model.SudokuField;

public class GameFragment extends Fragment {
    private TextView[][] boardGUI;
    private TextView selectedItem;
    private AppDatabase database;
    private SudokuBoard boardModel;
    private HashMap<TextView, SudokuField> fieldMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_game, container, false);

        database = AppDatabase.getInstance(getActivity().getApplicationContext());

        boardGUI = new TextView[9][9];
        fieldMap = new HashMap<>();
        int[] rowIds = new int[] { R.id.row1, R.id.row2, R.id.row3, R.id.row4, R.id.row5, R.id.row6,
                                   R.id.row7, R.id.row8, R.id.row9 };
        int[] colIds = new int[] { R.id.column1, R.id.column2, R.id.column3, R.id.column4, R.id.column5,
                                   R.id.column6, R.id.column7, R.id.column8, R.id.column9 };
        int[] buttonIds = new int[] { R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6,
                                      R.id.btn7, R.id.btn8, R.id.btn9 };

        for (int i = 0; i < 9; i++) {
            View row = view.findViewById(rowIds[i]);
            for (int j = 0; j < 9; j++) {
                boardGUI[i][j] = row.findViewById(colIds[j]);
                boardGUI[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectedItem != null) {
                            selectedItem.setBackgroundColor(Color.WHITE);
                        }

                        selectedItem = (TextView)v;
                        selectedItem.setBackgroundColor(Color.YELLOW);
                    }
                });
            }
        }

        for (int i = 0; i < buttonIds.length; i++) {
            final Button button = view.findViewById(buttonIds[i]);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedItem == null) {
                        Toast.makeText(view.getContext(), "Nope", Toast.LENGTH_SHORT).show();
                    } else {
                        SudokuField field = fieldMap.get(selectedItem);
                        if (!field.isVariable()) {
                            Toast.makeText(view.getContext(), "Nope", Toast.LENGTH_SHORT).show();
                        } else {
                            field.setValue(Integer.parseInt(button.getText().toString()));
                            selectedItem.setText(button.getText());

                            if (boardModel.isFilled() && boardModel.checkIfWon()) {
                                Toast.makeText(view.getContext(), "YOU WIN :::))))))))))))", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            });
        }

        final Button btnDel = view.findViewById(R.id.btnDel);
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedItem == null) {
                    Toast.makeText(view.getContext(), "Nope", Toast.LENGTH_SHORT).show();
                } else {
                    SudokuField field = fieldMap.get(selectedItem);
                    if (field.isVariable()) {
                        field.setValue(0);
                        selectedItem.setText("");
                    } else {
                        Toast.makeText(view.getContext(), "Nope", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        loadRandomSudoku();

        return view;
    }

    @SuppressLint("StaticFieldLeak")
    private void loadRandomSudoku() {
        new AsyncTask<Void, Void, Sudoku>() {
            @Override
            protected Sudoku doInBackground(Void... voids) {
                int sudokuCount = database.sudokuDao().getSudokuCount();
                Random random = new Random();
                return database.sudokuDao().getSudokuById(random.nextInt(sudokuCount));
            }

            @Override
            protected void onPostExecute(Sudoku sudoku) {
                boardModel = new SudokuBoard(sudoku.board, Difficulty.EASY);
                SudokuField[][] fields = boardModel.getBoard();
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        fieldMap.put(boardGUI[i][j], fields[i][j]);
                    }
                }
                updateViewFromModel();
            }
        }.execute();
    }

    private void updateViewFromModel() {
        for (TextView tv : fieldMap.keySet()) {
            SudokuField field = fieldMap.get(tv);

            String content = tv.getText().toString();
            if (!(content.equals("") && field.getValue() == 0) && !content.equals(Integer.toString(field.getValue()))) {
                if (!field.isVariable()) {
                    tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
                }
                tv.setText(Integer.toString(field.getValue()));
            }
        }
    }
}
