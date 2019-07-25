package ru.job4j.tictactoy;

public class Move {
    int index;
    int score;

    public Move(int index, int score) {
        this.index = index;
        this.score = score;
    }

    public Move() {

    }

    @Override
    public String toString() {
        return "Move{" +
                "index=" + index +
                ", score=" + score +
                '}';
    }
}
