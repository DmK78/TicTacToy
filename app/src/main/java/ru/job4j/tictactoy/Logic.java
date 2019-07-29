package ru.job4j.tictactoy;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Logic {
    private final Button[] table;
    private int fc = 0;
    String huPlayer;
    String aiPlayer;
    static List<Move> moves = new ArrayList<>();
    int score;

    public Logic(Button[] table) {
        this.table = table;
    }

    public CharSequence[] getSymbols() {
        CharSequence[] result = new CharSequence[table.length];
        for (int i = 0; i < table.length; i++) {
            result[i] = table[i].getText();
        }
        return result;
    }

    public boolean hasGap() {
        boolean result = false;
        for (int i = 0; i < table.length; i++) {
            if (table[i].getText().equals("")) {
                result = true;
                break;
            }
        }
        return result;
    }

    public boolean checkTableForWin(CharSequence[] selectedTable, CharSequence player) {
        boolean result = false;
        if (selectedTable[0].equals(player) && selectedTable[1].equals(player) && selectedTable[2].equals(player)
                || selectedTable[3].equals(player) && selectedTable[4].equals(player) && selectedTable[5].equals(player)
                || selectedTable[6].equals(player) && selectedTable[7].equals(player) && selectedTable[8].equals(player)
                || selectedTable[0].equals(player) && selectedTable[3].equals(player) && selectedTable[6].equals(player)
                || selectedTable[1].equals(player) && selectedTable[4].equals(player) && selectedTable[7].equals(player)
                || selectedTable[2].equals(player) && selectedTable[5].equals(player) && selectedTable[8].equals(player)
                || selectedTable[0].equals(player) && selectedTable[4].equals(player) && selectedTable[8].equals(player)
                || selectedTable[2].equals(player) && selectedTable[4].equals(player) && selectedTable[6].equals(player))
            result = true;
        return result;
    }

    public void clearTable() {
        for (Button button : table) {
            button.setText("");
        }
    }

    public List<Integer> getEmptyIndexies(CharSequence[] table) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < table.length; i++) {
            if (table[i].equals("")) {
                result.add(i);
            }
        }
        return result;
    }


    public void pcTurn(String currentPlayer) {
        //random PC
        /*List<Integer> emptyTable = emptyIndexies(getTable());
        if(emptyTable.size()>0){
            int turn = (int) (Math.random() * emptyTable.size());
            table[emptyTable.get(turn)].setText(currentPlayer);
        }*/

        moves.clear();
        if (currentPlayer.equals("X")) {
            aiPlayer = "X";
            huPlayer = "O";
        } else {
            aiPlayer = "O";
            huPlayer = "X";
        }
        CharSequence[] origBoard = getTable();
        int bestSpot = minimax(origBoard, aiPlayer);
        table[bestSpot].setText(currentPlayer);

    }

    private String changePlayer(String player) {
        if (player.equals("X")) {
            player = "O";
        } else {
            player = "X";
        }
        return player;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private int minimax(CharSequence[] newBoard, String player) {
        int result = -1;
//счетчик глубины ходов
        int count = 0;

        int index = 0;
        //занимаем середину, если она пустая.
        if (newBoard[4].equals("")) {
            return 4;
        }
        List<Integer> movesLeft = getEmptyIndexies(newBoard);
        for (int outerLoop = 0; outerLoop < movesLeft.size(); outerLoop++) {
            // активация защиты, если следующий ход приведет к победе человека, то нужно занять эту клетку
            List<Integer> arrayAttack = getEmptyIndexies(newBoard);
            for (int def = 0; def < arrayAttack.size(); def++) {
                newBoard[arrayAttack.get(def)] = huPlayer;
                if (checkTableForWin(newBoard, huPlayer)) {
                    return arrayAttack.get(def);
                }
                newBoard[arrayAttack.get(def)] = "";
            }
            count++;
            newBoard[movesLeft.get(outerLoop)] = player;
            index = movesLeft.get(outerLoop);
            if (checkTableForWin(newBoard, huPlayer)) {
                score = -10;
                moves.add(new Move(index, score, count));
                newBoard[movesLeft.get(outerLoop)] = "";
            } else if (checkTableForWin(newBoard, aiPlayer)) {
                score = 10;
                moves.add(new Move(index, score, count));
                newBoard[movesLeft.get(outerLoop)] = "";
            } else if (getEmptyIndexies(newBoard).size() == 0) {
                score = 0;
                moves.add(new Move(index, score, count));
                newBoard[movesLeft.get(outerLoop)] = "";
            } else {
                player = changePlayer(player);

                List<Integer> innerMoves = getEmptyIndexies(newBoard);
                Log.i("mini - innerMoves", innerMoves.toString());
                for (int innerLoop = 0; innerLoop < innerMoves.size(); innerLoop++) {
                    count++;
                    newBoard[innerMoves.get(innerLoop)] = player;
                    if (checkTableForWin(newBoard, huPlayer)) {
                        score = -10;
                        moves.add(new Move(index, score, count));
                        newBoard[movesLeft.get(outerLoop)] = "";
                        player = changePlayer(player);
                        break;
                    } else if (checkTableForWin(newBoard, aiPlayer)) {
                        score = 10;
                        moves.add(new Move(index, score, count));
                        newBoard[movesLeft.get(outerLoop)] = "";
                        player = changePlayer(player);
                        break;
                    } else if (getEmptyIndexies(newBoard).size() == 0) {
                        score = 0;
                        moves.add(new Move(index, score, count));
                    } else {
                        player = changePlayer(player);
                    }
                }
                count = 0;
                for (int o : innerMoves) {
                    newBoard[o] = "";
                }
            }
        }
        int min = 10000;
        Move resultMovie = new Move();
        List<Move> newMoves = moves.stream().filter(v -> v.score == 10).collect(Collectors.toList());
        // если не найдены победные ходы, то берет первый ход из доступных
        if (newMoves.size() == 0) {
            resultMovie.index = getEmptyIndexies(newBoard).get(0);
        } else {
            for (Move move : newMoves) {
                if (move.moves < min) {
                    min = move.moves;
                    resultMovie = move;
                }
            }

        }

        return resultMovie.index;
    }



    public CharSequence[] getTable() {
        CharSequence[] result = new CharSequence[9];
        for (int i = 0; i < table.length; i++) {
            result[i] = table[i].getText();
        }
        return result;
    }


}
