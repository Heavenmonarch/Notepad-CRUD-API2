
package com.example.notepad_api;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class NoteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void completeNoteCrudLifecycle() throws Exception {

        String createRequest = """
                {
                    "title": "Integration Test Note",
                    "content": "Original content"
                }
                """;

        MvcResult createResult = mockMvc.perform(
                        post("/api/notes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createRequest)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title")
                        .value("Integration Test Note"))
                .andReturn();

        String responseBody = createResult
                .getResponse()
                .getContentAsString();

        Number idValue = JsonPath.read(responseBody, "$.id");
        Long noteId = idValue.longValue();


        mockMvc.perform(get("/api/notes/" + noteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(noteId))
                .andExpect(jsonPath("$.title")
                        .value("Integration Test Note"))
                .andExpect(jsonPath("$.content")
                        .value("Original content"));


        String updateRequest = """
                {
                    "title": "Updated Integration Note",
                    "content": "Updated content"
                }
                """;

        mockMvc.perform(
                        put("/api/notes/" + noteId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateRequest)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title")
                        .value("Updated Integration Note"))
                .andExpect(jsonPath("$.content")
                        .value("Updated content"));


        String patchRequest = """
                {
                    "content": "Patched content"
                }
                """;

        mockMvc.perform(
                        patch("/api/notes/" + noteId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(patchRequest)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title")
                        .value("Updated Integration Note"))
                .andExpect(jsonPath("$.content")
                        .value("Patched content"));


        mockMvc.perform(delete("/api/notes/" + noteId))
                .andExpect(status().isNoContent());


        mockMvc.perform(get("/api/notes/" + noteId))
                .andExpect(status().isNotFound());
    }


    @Test
    void getNonexistentNoteReturnsNotFound() throws Exception {

        mockMvc.perform(get("/api/notes/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteNonexistentNoteReturnsNotFound() throws Exception {

        mockMvc.perform(delete("/api/notes/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void patchWithEmptyBodyReturnsBadRequest() throws Exception {

        String requestBody = "{}";

        mockMvc.perform(
                        patch("/api/notes/999999")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest());
    }

}
