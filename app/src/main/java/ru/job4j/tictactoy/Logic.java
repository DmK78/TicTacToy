package ru.job4j.tictactoy;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Logic {
    private final Button[] table;
    private int fc = 0;
    String huPlayer;
    String aiPlayer;
    List<Move> moves = new ArrayList<>();
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

    public List<Integer> emptyIndexies(CharSequence[] table) {
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
            huPlayer = "O";
            aiPlayer = currentPlayer;
        } else {
            huPlayer = currentPlayer;
            aiPlayer = "X";
        }

        CharSequence[] origBoard = getTable();
        int bestSpot = minimax(origBoard, aiPlayer);
        table[bestSpot].setText(currentPlayer);

    }

    private int minimax(CharSequence[] newBoard, String player) {
        //available spots
        List<Integer> availSpots = emptyIndexies(newBoard);
        if (checkTableForWin(newBoard, huPlayer)) {
            score = -10;
        } else if (checkTableForWin(newBoard, aiPlayer)) {
            score = 10;
        } else if (availSpots.size() == 0) {
            score = 0;
        }
        // an array to collect all the objects
        // loop through available spots
        for (int i = 0; i < availSpots.size(); i++) {
            //create an object for each and store the index of that spot that was stored as a number in the object's index key
            // set the empty spot to the current player
            newBoard[availSpots.get(i)] = player;
            //if collect the score resulted from calling minimax on the opponent of the current player
            Move move = new Move();
            move.index = availSpots.get(i);
            move.score = score;
            if (player.equals(aiPlayer)) {
                minimax(newBoard, huPlayer);
            } else {
                minimax(newBoard, aiPlayer);
            }
            //reset the spot to empty
            //newBoard[availSpots.get(i)] = move.index;
            newBoard[availSpots.get(i)] = "";
            // push the object to the array
            moves.add(move);
        }

// if it is the computer's turn loop over the moves and choose the move with the highest score
        int bestMove = -1;
        if (player.equals(aiPlayer)) {
            int bestScore = -10000;
            for (int i = 0; i < moves.size(); i++) {
                if (moves.get(i).score > bestScore) {
                    bestScore = moves.get(i).score;
                    bestMove = i;
                }
            }
        } else {
// else loop over the moves and choose the move with the lowest score
            int bestScore = 10000;
            for (int i = 0; i < moves.size(); i++) {
                if (moves.get(i).score < bestScore) {
                    bestScore = moves.get(i).score;
                    bestMove = i;
                }
            }
        }
// return the chosen move (object) from the array to the higher depth
        return moves.get(bestMove).index;

    }

    public CharSequence[] getTable() {
        CharSequence[] result = new CharSequence[9];
        for (int i = 0; i < table.length; i++) {
            result[i] = table[i].getText();
        }
        return result;
    }


}
