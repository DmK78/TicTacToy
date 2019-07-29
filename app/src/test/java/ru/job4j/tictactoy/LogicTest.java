package ru.job4j.tictactoy;

import android.widget.Button;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class LogicTest {


    @Test
    public void isXWonHorisontal() {
        Button[] table = new Button[9];
        Logic logic = new Logic(table);
        CharSequence [] result={"X","X","X","","","","","","",};
        assertEquals(logic.checkTableForWin(result, "X"), true);
    }
    @Test
    public void isXWonVertical() {
        Button[] table = new Button[9];
        Logic logic = new Logic(table);
        CharSequence [] result={"X","","","X","","","X","","",};
        assertEquals(logic.checkTableForWin(result, "X"), true);
    }

    @Test
    public void isXWonDiagonal() {
        Button[] table = new Button[9];
        Logic logic = new Logic(table);
        CharSequence [] result={"X","","","","X","","","","X",};
        assertEquals(logic.checkTableForWin(result, "X"), true);
    }

    @Test
    public void isNotGap() {
        Button[] table = new Button[9];
        Button button = new Button(this);
        Logic logic = new Logic(table);
        CharSequence [] result={"X","X","X","X","X","X","X","X","X",};
        assertEquals(logic.hasGap(), false);
    }
}
