package ru.job4j.tictactoy;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LogicTest {


    @Test
    public void isXWonHorisontal() {

        String [] table={"X","X","X","","","","","","",};
        Logic logic = new Logic(table);
        assertEquals(logic.checkTableForWin(null,"X"), true);
    }
    @Test
    public void isXWonVertical() {
        String [] table={"X","","","X","","","X","","",};
        Logic logic = new Logic(table);
        assertEquals(logic.checkTableForWin( null,"X"), true);
    }

    @Test
    public void isXWonDiagonal() {
        String  [] table={"X","","","","X","","","","X",};
        Logic logic = new Logic(table);
        assertEquals(logic.checkTableForWin( null,"X"), true);
    }

    @Test
    public void isNotGap() {
        String [] table={"X","X","X","X","X","X","X","X","X",};
        Logic logic = new Logic(table);
        assertEquals(logic.hasGap(), false);
    }
}
