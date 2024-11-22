package com.epam.homework.integration;

import com.epam.homework.repository.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
public class RecipesIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RecipeRepository recipeRepository;

    @Test
    public void createRecipe() throws Exception {
        var token = login("admin", "password");
        System.out.println("TOKEN: " + token);
        var recipeJson = "{\"title\":\"bagel\",\"ingredients\":\"flour, eggs, milk\",\"difficulty\":\"hard\"}";
        var count = recipeRepository.count();
        mockMvc.perform(post("/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(recipeJson)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
        assertEquals(count+1, recipeRepository.count());
    }

    private String login(String username, String password) throws Exception {
        String asJson = String.format("{\"user\":\"%s\",\"password\":\"%s\"}", username, password);
        return mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson)).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }
}
