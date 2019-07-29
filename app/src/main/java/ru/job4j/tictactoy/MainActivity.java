package ru.job4j.tictactoy;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Logic logic;
    private final String SAVED_TABLE = "saved_table";
    private boolean enemyHuman = true;
    private Button[] cells;
    private Switch switcher;
    private String currentPlayer = "X";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button11 = findViewById(R.id.button11);
        Button button12 = findViewById(R.id.button12);
        Button button13 = findViewById(R.id.button13);
        Button button21 = findViewById(R.id.button21);
        Button button22 = findViewById(R.id.button22);
        Button button23 = findViewById(R.id.button23);
        Button button31 = findViewById(R.id.button31);
        Button button32 = findViewById(R.id.button32);
        Button button33 = findViewById(R.id.button33);
        switcher = findViewById(R.id.switchEnemy);
        switcher.setChecked(enemyHuman);
        cells = new Button[]{button11, button12, button13, button21, button22, button23, button31, button32, button33};
        if (savedInstanceState != null) {
            CharSequence[] symbols = savedInstanceState.getCharSequenceArray(SAVED_TABLE);
            for (int i = 0; i < symbols.length; i++) {
                cells[i].setText(symbols[i]);
            }
        }
        logic = new Logic(cells);
    }

    private void showAlert(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequenceArray(SAVED_TABLE, logic.getSymbols());
    }

    private boolean checkState() {
        boolean gap = this.logic.hasGap();
        if (!gap) {
            this.showAlert("Все поля запонены! Начните новую Игру!");
        }
        return gap;
    }

    private boolean checkWinner() {
        boolean result = false;
        if (logic.checkTableForWin(logic.getTable(),"X")) {
            this.showAlert("Победили Крестики! Начните новую Игру!");
            result = true;
        } else if (logic.checkTableForWin(logic.getTable(),"O")) {
            this.showAlert("Победили Нолики! Начните новую Игру!");
            result = true;
        }
        return result;
    }


    public void answer(View view) {
        Button button = (Button) view;
        if (this.checkState() && !button.getText().equals("O") && !button.getText().equals("X") && !checkWinner()) {
                button.setText(currentPlayer);
                changePlayer();
            if (!checkWinner()) {
                checkState();
            }
            if (!enemyHuman && !checkWinner() && checkState()) {
                pcTurn();
                changePlayer();
            }
        }
    }

    private void pcTurn() {
        logic.pcTurn(currentPlayer);
        if (!checkWinner() && checkState()) {
            checkState();
        }


    }

    private void changePlayer() {
        if (currentPlayer.equals("X")) {
            currentPlayer = "O";
        } else {
            currentPlayer = "X";
        }
    }

    public void newGame(View view) {
        logic.clearTable();
        currentPlayer = "X";
    }

    public void switchEnemy(View view) {
        if (enemyHuman) {
            enemyHuman = false;
            switcher.setChecked(false);
        } else {
            enemyHuman = true;
            switcher.setChecked(true);

        }


    }
}
