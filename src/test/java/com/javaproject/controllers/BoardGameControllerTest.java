
package com.javaproject.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaproject.beans.BoardGame;
import com.javaproject.database.DatabaseAccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class BoardGameControllerTest {

    @Mock
    private DatabaseAccess databaseAccess;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        BoardGameController controller =
                new BoardGameController(databaseAccess);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetAllBoardGames() throws Exception {
        BoardGame game = createGame();

        when(databaseAccess.getBoardGames())
                .thenReturn(List.of(game));

        mockMvc.perform(get("/boardgames"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Chess"));
    }

    @Test
    void testGetBoardGameFound() throws Exception {
        BoardGame game = createGame();

        when(databaseAccess.getBoardGame(1L))
                .thenReturn(game);

        mockMvc.perform(get("/boardgames/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Chess"));
    }

    @Test
    void testGetBoardGameNotFound() throws Exception {
        when(databaseAccess.getBoardGame(999L))
                .thenReturn(null);

        mockMvc.perform(get("/boardgames/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No such record"));
    }

    @Test
    void testPostBoardGameSuccess() throws Exception {
        BoardGame game = createGame();

        when(databaseAccess.addBoardGame(any(BoardGame.class)))
                .thenReturn(10L);

        mockMvc.perform(post("/boardgames")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(game)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        org.hamcrest.Matchers.endsWith("/boardgames/10")))
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void testPostBoardGameConflict() throws Exception {
        BoardGame game = createGame();

        when(databaseAccess.addBoardGame(any(BoardGame.class)))
                .thenThrow(new RuntimeException("Duplicate game"));

        mockMvc.perform(post("/boardgames")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(game)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message")
                        .value("Name already exists."));
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
