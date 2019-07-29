package ru.job4j.tictactoy;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Logic {
    private final Button[] table;
    private int fc = 0;
    private String huPlayer;
    private String aiPlayer;
    private static List<Move> moves = new ArrayList<>();
    private int score;

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


    @RequiresApi(api = Build.VERSION_CODES.N)
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

    private String switchPlayer(String player) {
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
        int firstMoveIndex = 0;
        List<Integer> movesLeft = getEmptyIndexies(newBoard);
        // активация защиты, если следующий ход приведет к победе человека, то нужно занять эту клетку
        for (int def = 0; def < movesLeft.size(); def++) {
            newBoard[movesLeft.get(def)] = huPlayer;
            if (checkTableForWin(newBoard, huPlayer)) {
                return movesLeft.get(def);
            }
            newBoard[movesLeft.get(def)] = "";
        }
        //занимаем середину, если она пустая.
        if (newBoard[4].equals("")) {
            return 4;
        }
        // берем по одной свободной ячкйке outerLoop, затем углубляемся с innerLoop
        for (int outerLoop = 0; outerLoop < movesLeft.size(); outerLoop++) {
            count++;
            newBoard[movesLeft.get(outerLoop)] = player;
            firstMoveIndex = movesLeft.get(outerLoop);
            // проверяем, если хорд приводит к результату, то записываем его в moves
            if (checkTableForWin(newBoard, huPlayer)) {
                moves.add(new Move(firstMoveIndex, -10, count));
                newBoard[movesLeft.get(outerLoop)] = "";
                continue;
            } else if (checkTableForWin(newBoard, aiPlayer)) {
                moves.add(new Move(firstMoveIndex, 10, count));
                newBoard[movesLeft.get(outerLoop)] = "";
                continue;
                // если первый ход привел к заполнению поля, записываем его
            } else if (getEmptyIndexies(newBoard).size() == 0) {
                moves.add(new Move(firstMoveIndex, 0, count));
                newBoard[movesLeft.get(outerLoop)] = "";
            } else {

                player = switchPlayer(player);
                // создаем лист вложенных ходов, после добавления первого хода
                List<Integer> innerMoves = getEmptyIndexies(newBoard);
                for (int innerLoop = 0; innerLoop < innerMoves.size(); innerLoop++) {
                    count++;
                    newBoard[innerMoves.get(innerLoop)] = player;
                    if (checkTableForWin(newBoard, huPlayer)) {
                        moves.add(new Move(firstMoveIndex, -10, count));
                        newBoard[movesLeft.get(outerLoop)] = "";
                        player = switchPlayer(player);
                        break;
                    } else if (checkTableForWin(newBoard, aiPlayer)) {
                        moves.add(new Move(firstMoveIndex, 10, count));
                        newBoard[movesLeft.get(outerLoop)] = "";
                        player = switchPlayer(player);
                        break;
                    } else if (getEmptyIndexies(newBoard).size() == 0) {
                        moves.add(new Move(firstMoveIndex, 0, count));
                    } else {
                        player = switchPlayer(player);
                    }
                }
                //зачищаем сделанные ходы
                count = 0;
                for (int o : innerMoves) {
                    newBoard[o] = "";
                }
                newBoard[movesLeft.get(outerLoop)] = "";
                player = switchPlayer(player);
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
