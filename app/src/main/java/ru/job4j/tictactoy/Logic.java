package ru.job4j.tictactoy;

import android.os.Build;
import android.support.annotation.RequiresApi;
import java.util.ArrayList;
import java.util.List;

public class Logic {
    private String[] table = new String[9];
    private String huPlayer;
    private String aiPlayer;

    public Logic(String[] table) {
        this.table = table;
    }

    public CharSequence[] getSymbols() {
        CharSequence[] result = new CharSequence[table.length];
        for (int i = 0; i < table.length; i++) {
            result[i] = table[i];
        }
        return result;

    }

    public boolean hasGap() {
        boolean result = false;
        for (int i = 0; i < table.length; i++) {
            if (table[i].equals("")) {
                result = true;
                break;
            }
        }
        return result;
    }

    public boolean checkTableForWin(String[] table, String player) {
        boolean result = false;
        if(table==null){
            table=this.table;
        }
        if (table[0].equals(player) && table[1].equals(player) && table[2].equals(player)
                || table[3].equals(player) && table[4].equals(player) && table[5].equals(player)
                || table[6].equals(player) && table[7].equals(player) && table[8].equals(player)
                || table[0].equals(player) && table[3].equals(player) && table[6].equals(player)
                || table[1].equals(player) && table[4].equals(player) && table[7].equals(player)
                || table[2].equals(player) && table[5].equals(player) && table[8].equals(player)
                || table[0].equals(player) && table[4].equals(player) && table[8].equals(player)
                || table[2].equals(player) && table[4].equals(player) && table[6].equals(player)) {
            result = true;
        }

        return result;
    }

    public void clearTable() {
        for (int i = 0; i < table.length; i++) {
            table[i] = "";
        }
    }

    private List<Integer> getEmptyIndexies(String[] table) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < table.length; i++) {
            if (table[i].equals("")) {
                result.add(i);
            }
        }
        return result;
    }

    public int pcTurn(String currentPlayer, boolean pcSmart) {
        int result=-1;
        if(!pcSmart){
            //random PC
            List<Integer> emptyTable = getEmptyIndexies(table);
            if(emptyTable.size()>0){
                int turn = (int) (Math.random() * emptyTable.size());
                result = emptyTable.get(turn);
            }
        } else
        {
            if (currentPlayer.equals("X")) {
                aiPlayer = "X";
                huPlayer = "O";
            } else {
                aiPlayer = "O";
                huPlayer = "X";
            }
            result = getBestTurn(table, aiPlayer);
        }
return result;


    }

    private String switchPlayer(String player) {
        if (player.equals('X')) {
            player = "O";
        } else {
            player = "X";
        }
        return player;
    }

    private int getBestTurn(String[] newBoard, String player) {
        List<Integer> movesLeft = getEmptyIndexies(newBoard);
        // акттвация атаки или защиты, если следующий ход привдеет к победе компьютера, то знамаем эту клетку. Если же нет, то проверяем , не победит ли человек
        for (int def = 0; def < movesLeft.size(); def++) {
            newBoard[movesLeft.get(def)] = aiPlayer;
            if (checkTableForWin(newBoard, aiPlayer)) {
                return movesLeft.get(def);
            }
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
        return getEmptyIndexies(newBoard).get(0);
    }


    public void putSymbol(int indexOf, String currentPlayer) {
        table[indexOf] = currentPlayer;
    }
}
