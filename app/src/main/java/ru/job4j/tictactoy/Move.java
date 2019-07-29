package ru.job4j.tictactoy;

public class Move {
    int index;
    int score;
    int moves;

    public Move() {

    }

    public Move(int index, int score, int moves) {
        this.index = index;
        this.score = score;
        this.moves = moves;
    }

    @Override
    public String toString() {
        return "Move{" +
                "index=" + index +
                ", score=" + score +
                '}';
    }
}
