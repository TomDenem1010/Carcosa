package com.home.carcosa.boardgame.constans;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class BoardgameStatusTest {

    @Test
    void values_containsExactlyFourConstants() {
        assertEquals(4, BoardgameStatus.values().length);
    }

    @ParameterizedTest
    @ValueSource(strings = { "OWN", "SOLD", "THINKINGTOSELL", "THINKINGTOBUY" })
    void valueOf_returnsConstantForValidName(String name) {
        assertNotNull(BoardgameStatus.valueOf(name));
    }

    @ParameterizedTest
    @ValueSource(strings = { "OWN", "SOLD", "THINKINGTOSELL", "THINKINGTOBUY" })
    void name_matchesExpectedString(String expected) {
        assertEquals(expected, BoardgameStatus.valueOf(expected).name());
    }

    @ParameterizedTest
    @ValueSource(strings = { "own", "Sold", "UNKNOWN", "" })
    void valueOf_throwsForInvalidName(String invalid) {
        assertThrows(IllegalArgumentException.class, () -> BoardgameStatus.valueOf(invalid));
    }

    @Test
    void ordinal_ownIsZero() {
        assertEquals(0, BoardgameStatus.OWN.ordinal());
    }

    @Test
    void ordinal_soldIsOne() {
        assertEquals(1, BoardgameStatus.SOLD.ordinal());
    }

    @Test
    void ordinal_thinkingToSellIsTwo() {
        assertEquals(2, BoardgameStatus.THINKINGTOSELL.ordinal());
    }

    @Test
    void ordinal_thinkingToBuyIsThree() {
        assertEquals(3, BoardgameStatus.THINKINGTOBUY.ordinal());
    }
}
