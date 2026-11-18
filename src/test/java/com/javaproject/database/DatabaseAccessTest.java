package com.javaproject.database;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.javaproject.beans.BoardGame;
import com.javaproject.beans.Review;

@SpringBootTest
class DatabaseAccessTest {

    @Autowired
    private DatabaseAccess databaseAccess;

    @Test
    void testGetAuthorities() {
        List<String> authorities = databaseAccess.getAuthorities();

        assertNotNull(authorities);
        assertTrue(authorities.contains("ROLE_USER"));
        assertTrue(authorities.contains("ROLE_MANAGER"));
    }

    @Test
    void testGetBoardGames() {
        List<BoardGame> games = databaseAccess.getBoardGames();

        assertNotNull(games);
        assertFalse(games.isEmpty());
    }

    @Test
    void testGetBoardGame() {
        BoardGame game = databaseAccess.getBoardGame(1L);

        assertNotNull(game);
        assertEquals("Splendor", game.getName());
    }

    @Test
    void testGetBoardGameNotFound() {
        BoardGame game = databaseAccess.getBoardGame(999L);

        assertNull(game);
    }

    @Test
    void testGetReviews() {
        List<Review> reviews = databaseAccess.getReviews(1L);

        assertNotNull(reviews);
        assertFalse(reviews.isEmpty());
    }

    @Test
    void testGetReviewsNotFound() {
        List<Review> reviews = databaseAccess.getReviews(999L);

        assertNull(reviews);
    }

    @Test
    void testAddReview() {
        Review review = new Review();
        review.setGameId(1L);
        review.setText("DatabaseAccessTest unique review");

        int result = databaseAccess.addReview(review);

        assertEquals(1, result);
    }

    @Test
    void testDeleteReview() {
        Review review = new Review();
        review.setGameId(1L);
        review.setText("Review to delete in DatabaseAccessTest");

        databaseAccess.addReview(review);

        List<Review> reviews = databaseAccess.getReviews(1L);
        Review addedReview = reviews.stream()
                .filter(r -> r.getText().equals("Review to delete in DatabaseAccessTest"))
                .findFirst()
                .orElseThrow();

        int result = databaseAccess.deleteReview(addedReview.getId());

        assertEquals(1, result);
        assertNull(databaseAccess.getReview(addedReview.getId()));
    }

    @Test
    void testGetReview() {
        Review review = databaseAccess.getReview(1L);

        assertNotNull(review);
        assertEquals(1L, review.getGameId());
    }

    @Test
    void testGetReviewNotFound() {
        Review review = databaseAccess.getReview(999L);

        assertNull(review);
    }

    @Test
    void testEditReview() {
        Review review = new Review();
        review.setGameId(1L);
        review.setText("Review to edit in DatabaseAccessTest");

        databaseAccess.addReview(review);

        List<Review> reviews = databaseAccess.getReviews(1L);
        Review addedReview = reviews.stream()
                .filter(r -> r.getText().equals("Review to edit in DatabaseAccessTest"))
                .findFirst()
                .orElseThrow();

        addedReview.setText("Updated review in DatabaseAccessTest");

        int result = databaseAccess.editReview(addedReview);

        assertEquals(1, result);
        assertEquals(
                "Updated review in DatabaseAccessTest",
                databaseAccess.getReview(addedReview.getId()).getText()
        );
    }
}