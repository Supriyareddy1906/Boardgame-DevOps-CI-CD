
package com.javaproject.beans;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class BoardGameTest {

    @Test
    void testGettersAndSetters() {
        BoardGame game = new BoardGame();

        game.setId(1L);
        game.setName("Chess");
        game.setLevel(3);
        game.setMinPlayers(2);
        game.setMaxPlayers("2");
        game.setGameType("Strategy");

        List<Review> reviews = new ArrayList<>();
        game.setReviews(reviews);

        assertEquals(1L, game.getId());
        assertEquals("Chess", game.getName());
        assertEquals(3, game.getLevel());
        assertEquals(2, game.getMinPlayers());
        assertEquals("2", game.getMaxPlayers());
        assertEquals("Strategy", game.getGameType());
        assertSame(reviews, game.getReviews());
    }

    @Test
    void testEqualsAndHashCode() {
        BoardGame first = createGame();
        BoardGame second = createGame();

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());

        second.setName("Different game");
        assertNotEquals(first, second);
    }

    @Test
    void testToString() {
        BoardGame game = createGame();

        assertTrue(game.toString().contains("Chess"));
        assertTrue(game.toString().contains("Strategy"));
    }

    @Test
    void testDefaultValues() {
        BoardGame game = new BoardGame();

        assertEquals(0, game.getLevel());
        assertEquals(0, game.getMinPlayers());
        assertNull(game.getId());
        assertNull(game.getName());
        assertNull(game.getReviews());
    }

    private BoardGame createGame() {
        BoardGame game = new BoardGame();
        game.setId(1L);
        game.setName("Chess");
        game.setLevel(3);
        game.setMinPlayers(2);
        game.setMaxPlayers("2");
        game.setGameType("Strategy");
        return game;
    }
}
