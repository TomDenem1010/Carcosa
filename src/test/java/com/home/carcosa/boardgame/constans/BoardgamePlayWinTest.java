package com.home.carcosa.boardgame.constans;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class BoardgamePlayWinTest {

    @Test
    void values_containsExactlyTwoConstants() {
        assertEquals(2, BoardgamePlayWin.values().length);
    }

    @ParameterizedTest
    @ValueSource(strings = { "SUCCESS", "FAILURE" })
    void valueOf_returnsConstantForValidName(String name) {
        assertNotNull(BoardgamePlayWin.valueOf(name));
    }

    @ParameterizedTest
    @ValueSource(strings = { "SUCCESS", "FAILURE" })
    void name_matchesExpectedString(String expected) {
        assertEquals(expected, BoardgamePlayWin.valueOf(expected).name());
    }

    @ParameterizedTest
    @ValueSource(strings = { "success", "UNKNOWN", "" })
    void valueOf_throwsForInvalidName(String invalid) {
        assertThrows(IllegalArgumentException.class, () -> BoardgamePlayWin.valueOf(invalid));
    }

    @Test
    void ordinal_successIsZero() {
        assertEquals(0, BoardgamePlayWin.SUCCESS.ordinal());
    }

    @Test
    void ordinal_failureIsOne() {
        assertEquals(1, BoardgamePlayWin.FAILURE.ordinal());
    }
}
