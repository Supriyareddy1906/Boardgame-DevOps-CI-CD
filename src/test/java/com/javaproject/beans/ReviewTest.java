
package com.javaproject.beans;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ReviewTest {

    @Test
    void testGettersAndSetters() {
        Review review = new Review();

        review.setId(1L);
        review.setGameId(2L);
        review.setText("Excellent game");

        assertEquals(1L, review.getId());
        assertEquals(2L, review.getGameId());
        assertEquals("Excellent game", review.getText());
    }

    @Test
    void testEqualsAndHashCode() {
        Review first = createReview();
        Review second = createReview();

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());

        second.setText("Different review");
        assertNotEquals(first, second);
    }

    @Test
    void testToString() {
        Review review = createReview();

        assertTrue(review.toString().contains("Excellent game"));
    }

    @Test
    void testDefaultValues() {
        Review review = new Review();

        assertNull(review.getId());
        assertNull(review.getGameId());
        assertNull(review.getText());
    }

    private Review createReview() {
        Review review = new Review();
        review.setId(1L);
        review.setGameId(2L);
        review.setText("Excellent game");
        return review;
    }
}
