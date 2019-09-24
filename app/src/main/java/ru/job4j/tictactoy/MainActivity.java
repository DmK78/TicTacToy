package ru.job4j.tictactoy;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private Logic logic;
    private final String SAVED_TABLE = "saved_table";
    private boolean enemyHuman = true;
    private List<Button> cells = new ArrayList<>();
    private String[] characters = new String[]{"", "", "", "", "", "", "", "", ""};
    private Switch switcherPcHuman;
    private String currentPlayer = "X";
    private Switch switcherPcSmart;
    private boolean pcTurn;
    final int MAX_STREAMS = 5;
    SoundPool sp;
    int soundIdClick;
    int soundIdPcWin;
    int soundIdHumanWin;
    LinearLayout linearLayout;
    Button buttonNewGame;
    CheckBox checkBoxMusic;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        buttonNewGame = findViewById(R.id.buttonNewGame);
        linearLayout=findViewById(R.id.linearLayout);
        checkBoxMusic=findViewById(R.id.checkBoxMusic);

        sp = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        buttonNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logic.clearTable();
                linearLayout.animate().scaleX(0).scaleY(0).setDuration(200).withEndAction(() -> linearLayout.animate().scaleX(1).scaleY(1).setDuration(200));
                for (Button button : cells) {
                    button.setText("");
                    button.setClickable(true);
                    button.setBackground(null);
                }
                currentPlayer = "X";
            }
        });
        soundIdClick = sp.load(this, R.raw.click, 1);
        soundIdPcWin = sp.load(this, R.raw.sigh, 1);
        soundIdHumanWin = sp.load(this, R.raw.applause, 1);


        //Log.d(LOG_TAG, "soundIdShot = " + soundIdShot);


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
        startService(new Intent(this, MusicService.class));

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
        //playSound(R.raw.click);

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
        if (result) {
            for (Button button : cells) {
                button.setClickable(false);






            }
            if (pcTurn) {
                sp.play(soundIdPcWin, 1, 1, 0, 0, 1);

            } else {
                sp.play(soundIdHumanWin, 1, 1, 0, 0, 1);

            }
        }
        return result;
    }

    public void answer(View view) {
        Button button = (Button) view;
        if (this.checkState() && !checkWinner() && button.getText() != null && "".equals(button.getText())) {
            button.setText(currentPlayer);
            setFigure(button, currentPlayer);
            logic.putSymbol(cells.indexOf(button), currentPlayer);
            sp.play(soundIdClick, 1, 1, 0, 0, 1);

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
        pcTurn = true;
        Button button = cells.get((logic.pcTurn(currentPlayer, switcherPcSmart.isChecked())));
        button.setText(currentPlayer);
        setFigure(button, currentPlayer);
        logic.putSymbol(cells.indexOf(button), currentPlayer);
        if (!checkWinner() && checkState()) {
            checkState();
        }
        sp.play(soundIdClick, 1, 1, 0, 0, 1);
        pcTurn = false;

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
            button.setClickable(true);
            button.setBackground(null);

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
        stopService(new Intent(this, MusicService.class)); // остановить песню
    }

    // развернули приложение
    @Override
    public void onResume() {
        super.onResume();
        startService(new Intent(this, MusicService.class)); // запустить песню
    }


    void setFigure(Button button, String currentPlayer) {
        button.setVisibility(View.VISIBLE);
        if (currentPlayer.equals("X")) {
            button.setAlpha(0);
            //button.animate().scaleY(0).scaleX(0).setDuration(1);
            button.setBackground(this.getResources().getDrawable(R.drawable.x));
            button.animate().alpha(1).scaleY(1).scaleX(1).setDuration(1000);
        } else {
            button.setAlpha(0);
            //button.animate().scaleY(0).scaleX(0).setDuration(1);
            button.setBackground(this.getResources().getDrawable(R.drawable.o));
            button.animate().alpha(1).scaleY(1).scaleX(1).setDuration(1000);
        }
    }


    public void switchMusic(View view) {
        CheckBox checkBox = (CheckBox)view;
        if(checkBox.isChecked()){
            startService(new Intent(this, MusicService.class)); // запустить песню
        }else {
            stopService(new Intent(this, MusicService.class)); // остановить песню
        }
    }
}
