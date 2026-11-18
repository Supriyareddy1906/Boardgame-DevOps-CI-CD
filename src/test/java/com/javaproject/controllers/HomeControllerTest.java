
package com.javaproject.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import com.javaproject.beans.BoardGame;
import com.javaproject.beans.Review;
import com.javaproject.database.DatabaseAccess;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DatabaseAccess databaseAccess;

    @Test
    void testNewUserPage() throws Exception {
        mockMvc.perform(get("/newUser"))
                .andExpect(status().isOk())
                .andExpect(view().name("new-user"))
                .andExpect(model().attributeExists("authorities"));
    }

    @Test
    void testHomePage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("boardgames"));
    }

    @Test
    void testBoardGameDetail() throws Exception {
        mockMvc.perform(get("/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("boardgame"))
                .andExpect(model().attributeExists("boardgame"));
    }

    @Test
    void testReviewsPage() throws Exception {
        mockMvc.perform(get("/1/reviews"))
                .andExpect(status().isOk())
                .andExpect(view().name("review"))
                .andExpect(model().attributeExists("boardgame"))
                .andExpect(model().attributeExists("reviews"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testAddReviewPage() throws Exception {
        mockMvc.perform(get("/secured/addReview/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("secured/addReview"))
                .andExpect(model().attributeExists("boardgame"))
                .andExpect(model().attributeExists("review"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testEditReviewPage() throws Exception {
        List<Review> reviews = databaseAccess.getReviews(1L);
        Long reviewId = reviews.get(0).getId();

        mockMvc.perform(get("/1/reviews/" + reviewId))
                .andExpect(status().isOk())
                .andExpect(view().name("secured/addReview"))
                .andExpect(model().attributeExists("review"))
                .andExpect(model().attributeExists("boardgame"));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void testAddBoardGamePage() throws Exception {
        mockMvc.perform(get("/secured/addBoardGame"))
                .andExpect(status().isOk())
                .andExpect(view().name("secured/addBoardGame"))
                .andExpect(model().attributeExists("boardgame"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testUserSecuredPage() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(view().name("secured/user/index"));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void testManagerSecuredPage() throws Exception {
        mockMvc.perform(get("/manager"))
                .andExpect(status().isOk())
                .andExpect(view().name("secured/manager/index"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testSecuredGatewayPage() throws Exception {
        mockMvc.perform(get("/secured"))
                .andExpect(status().isOk())
                .andExpect(view().name("secured/gateway"));
    }

    @Test
    void testLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void testPermissionDeniedPage() throws Exception {
        mockMvc.perform(get("/permission-denied"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/permission-denied"));
    }
}
