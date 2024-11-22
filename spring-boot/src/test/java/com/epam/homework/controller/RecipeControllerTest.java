package com.epam.homework.controller;

import com.epam.homework.model.Recipe;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RecipeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void createRecipe() throws Exception {
        Recipe recipe = new Recipe(null, "Mac n cheese", "Mac, cheese", "Easy");
        mockMvc.perform(post("/recipes").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(recipe)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void updateRecipe() throws Exception {
        Recipe recipe = new Recipe(1L, "Carbonara", "Pasta, Bacon", "Medium");
        mockMvc.perform(post("/recipes").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(recipe)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void deleteRecipe() throws Exception {
        mockMvc.perform(delete("/recipes/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getAll() throws Exception {
        mockMvc.perform(get("/recipes")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getById() throws Exception {
        mockMvc.perform(get("/recipes/{id}", 2)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
