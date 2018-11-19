package hu.bme.aut.mobwebhf.sudoku.fragments.game;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import hu.bme.aut.mobwebhf.sudoku.R;
import hu.bme.aut.mobwebhf.sudoku.data.database.AppDatabase;
import hu.bme.aut.mobwebhf.sudoku.data.entity.SavedSudoku;
import hu.bme.aut.mobwebhf.sudoku.fragments.highscore.HighscoreInputDialogFragment;
import hu.bme.aut.mobwebhf.sudoku.model.Difficulty;
import hu.bme.aut.mobwebhf.sudoku.model.SudokuBoard;
import hu.bme.aut.mobwebhf.sudoku.model.SudokuField;
import hu.bme.aut.mobwebhf.sudoku.model.Timer;

public class GameFragment extends Fragment {
    private TextView[][] boardGUI;
    private TextView selectedItem;
    private TextView tvTime;
    private SudokuBoard boardModel;
    private HashMap<TextView, SudokuField> fieldMap;
    private AppDatabase database;
    private Timer timer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_game, container, false);
        database = AppDatabase.getInstance(getActivity().getApplicationContext());

        Bundle args = getArguments();
        if (args.getBoolean("issaved")) {
            boardModel = SudokuBoard.parseBoard(args.getString("board"));
            timer = new Timer((TextView) view.findViewById(R.id.tvTime), getActivity(), args.getInt("seconds"));
        } else {
            boardModel = new SudokuBoard(args.getString("board"), Difficulty.valueOf(args.getString("difficulty")));
            timer = new Timer((TextView) view.findViewById(R.id.tvTime), getActivity());
        }

        boardGUI = new TextView[9][9];
        fieldMap = new HashMap<>();
        int[] rowIds = new int[]{R.id.row1, R.id.row2, R.id.row3, R.id.row4, R.id.row5, R.id.row6,
                R.id.row7, R.id.row8, R.id.row9};
        int[] colIds = new int[]{R.id.column1, R.id.column2, R.id.column3, R.id.column4, R.id.column5,
                R.id.column6, R.id.column7, R.id.column8, R.id.column9};
        int[] buttonIds = new int[]{R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6,
                R.id.btn7, R.id.btn8, R.id.btn9};

        for (int i = 0; i < 9; i++) {
            View row = view.findViewById(rowIds[i]);
            for (int j = 0; j < 9; j++) {
                boardGUI[i][j] = row.findViewById(colIds[j]);
                fieldMap.put(boardGUI[i][j], boardModel.getBoard()[i][j]);

                if (!boardModel.getBoard()[i][j].isVariable()) {
                    boardGUI[i][j].setBackgroundColor(Color.argb(255, 240, 240, 240));
                }

                boardGUI[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!fieldMap.get((TextView) v).isVariable()) {
                            Toast.makeText(view.getContext(), getString(R.string.fixed_tile), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (selectedItem != null) {
                            selectedItem.setBackgroundColor(Color.WHITE);
                        }
                        selectedItem = (TextView) v;
                        selectedItem.setBackgroundColor(Color.argb(255, 200, 200, 255));
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
                        Toast.makeText(view.getContext(), getString(R.string.select_var_tile), Toast.LENGTH_SHORT).show();
                    } else {
                        SudokuField field = fieldMap.get(selectedItem);

                        field.setValue(Integer.parseInt(button.getText().toString()));
                        selectedItem.setText(button.getText());

                        if (boardModel.isFilled() && boardModel.checkIfWon()) {
                            timer.stopTimer();
                            Bundle bundle = new Bundle();
                            bundle.putString("difficulty", boardModel.getDifficulty().toString());
                            bundle.putInt("time", timer.getValue());

                            HighscoreInputDialogFragment dialogFragment = new HighscoreInputDialogFragment();
                            dialogFragment.setArguments(bundle);

                            dialogFragment.show(getActivity().getSupportFragmentManager(), dialogFragment.TAG);
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
                    Toast.makeText(view.getContext(), R.string.select_var_tile, Toast.LENGTH_SHORT).show();
                } else {
                    SudokuField field = fieldMap.get(selectedItem);
                    field.setValue(0);
                    selectedItem.setText("");
                }
            }
        });

        updateViewFromModel();
        timer.start();

        return view;
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

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onStop() {
        super.onStop();

        if (boardModel.isFilled() && boardModel.checkIfWon()) {
            return;
        }

        timer.stopTimer();
        new AsyncTask<SudokuBoard, Void, Void>() {
            @Override
            protected Void doInBackground(SudokuBoard... sudokuBoards) {
                SavedSudoku savedSudoku = new SavedSudoku();
                savedSudoku.board = sudokuBoards[0].toString();
                savedSudoku.time = timer.getValue();

                database.savedSudokuDao().insert(savedSudoku);
                return null;
            }
        }.execute(boardModel);
    }
}
