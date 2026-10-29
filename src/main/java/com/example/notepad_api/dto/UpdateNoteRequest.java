
package com.example.notepad_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Data used to replace a note's editable fields")
public class UpdateNoteRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    @Schema(description = "Replacement title", example = "Updated note title")
    private String title;

    @NotBlank(message = "Content is required")
    @Size(max = 2000, message = "Content cannot exceed 2000 characters")
    @Schema(description = "Replacement content", example = "Updated note content")
    private String content;

    public UpdateNoteRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}