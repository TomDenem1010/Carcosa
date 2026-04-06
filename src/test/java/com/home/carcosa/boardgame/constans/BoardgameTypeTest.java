package com.home.carcosa.boardgame.constans;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class BoardgameTypeTest {

    @Test
    void values_containsExactlyTwoConstants() {
        assertEquals(2, BoardgameType.values().length);
    }

    @ParameterizedTest
    @ValueSource(strings = { "BASEGAME", "EXPANSION" })
    void valueOf_returnsConstantForValidName(String name) {
        assertNotNull(BoardgameType.valueOf(name));
    }

    @ParameterizedTest
    @ValueSource(strings = { "BASEGAME", "EXPANSION" })
    void name_matchesExpectedString(String expected) {
        assertEquals(expected, BoardgameType.valueOf(expected).name());
    }

    @ParameterizedTest
    @ValueSource(strings = { "basegame", "Expansion", "UNKNOWN", "" })
    void valueOf_throwsForInvalidName(String invalid) {
        assertThrows(IllegalArgumentException.class, () -> BoardgameType.valueOf(invalid));
    }

    @Test
    void ordinal_basegameIsZero() {
        assertEquals(0, BoardgameType.BASEGAME.ordinal());
    }

    @Test
    void ordinal_expansionIsOne() {
        assertEquals(1, BoardgameType.EXPANSION.ordinal());
    }
}
