package ru.job4j.tictactoy;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Logic logic;
    private final String SAVED_TABLE = "saved_table";
    private boolean enemyHuman = true;
    private List<Button> cells = new ArrayList<>();
    private String[] characters = new String[]{"", "", "", "", "", "", "", "", ""};
    private Switch switcherPcHuman;
    private String currentPlayer = "X";
    private Switch switcherPcSmart;
    private boolean mIsBound = false;



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
        switcherPcSmart = findViewById(R.id.switchPcSmart);
        switcherPcHuman = findViewById(R.id.switchEnemy);
        switcherPcHuman.setChecked(enemyHuman);
        cells.add(button11);
        cells.add(button12);
        cells.add(button13);
        cells.add(button21);
        cells.add(button22);
        cells.add(button23);
        cells.add(button31);
        cells.add(button32);
        cells.add(button33);
        if (savedInstanceState != null) {
            CharSequence[] symbols = savedInstanceState.getCharSequenceArray(SAVED_TABLE);
        }
        logic = new Logic(characters);
        startService(new Intent(this, MyService.class));
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
     playSound(R.raw.click);
        boolean gap = this.logic.hasGap();
        if (!gap) {
            this.showAlert("Все поля запонены! Начните новую Игру!");
        }
        return gap;
    }

    private boolean checkWinner() {
        boolean result = false;
        if (logic.checkTableForWin(null, "X")) {
            this.showAlert("Победили Крестики! Начните новую Игру!");
            result = true;
        } else if (logic.checkTableForWin(null, "O")) {
            this.showAlert("Победили Нолики! Начните новую Игру!");
            result = true;
        }
        if(result){
            playSound(R.raw.applause);
        }
        return result;
    }

    public void answer(View view) {
        Button button = (Button) view;
        if (this.checkState() && !checkWinner() && button.getText() != null && "".equals(button.getText())) {
            button.setText(currentPlayer);
            logic.putSymbol(cells.indexOf(button), currentPlayer);

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
        Button button = cells.get((logic.pcTurn(currentPlayer, switcherPcSmart.isChecked())));
        button.setText(currentPlayer);
        logic.putSymbol(cells.indexOf(button), currentPlayer);
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
        for (Button button : cells) {
            button.setText("");
        }
        currentPlayer = "X";
    }

    public void switchEnemy(View view) {
        if (enemyHuman) {
            enemyHuman = false;
            switcherPcHuman.setChecked(false);
        } else {
            enemyHuman = true;
            switcherPcHuman.setChecked(true);
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        stopService(new Intent(this, MyService.class)); // остановить песню
    }

    // развернули приложение
    @Override
    public void onResume() {
        super.onResume();
        startService(new Intent(this, MyService.class)); // запустить песню
    }

    private void  playSound(int uri){
        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();

    }


}
