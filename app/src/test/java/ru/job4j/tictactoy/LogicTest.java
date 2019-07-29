package ru.job4j.tictactoy;

import android.content.Context;
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
        Button button11=findViewById(R.id.button11);
        Button button12=findViewById(R.id.button12);
        Button button13=findViewById(R.id.button13);
        Button button21=findViewById(R.id.button21);
        Button button22=findViewById(R.id.button22);
        Button button23=findViewById(R.id.button23);
        Button button31=findViewById(R.id.button31);
        Button button32=findViewById(R.id.button32);
        Button button33=findViewById(R.id.button33);
        Button[] table = {button11,button12,button13,button21,button22,button23,button31,button32,button33};

        Logic logic = new Logic(table);
        CharSequence [] result={"X","X","X","X","X","X","X","X","X",};
        assertEquals(logic.hasGap(), false);
    }
}
