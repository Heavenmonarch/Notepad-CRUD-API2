package com.example.notepad_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "Fields to update on an existing note")
public class PatchNoteRequest {
    @Size(max = 100, message="Title cannot exceed 100 characters")
    @Schema(
            description = "New title, if the title should change",
            example = "A revised title",
            nullable = true
    )
    private String title;

    @Size(max = 20000, message = "Content cannot exceed 20000 characters")
    @Schema(
            description = "New content, if the content should change",
            example = "A revised content",
            nullable = true
    )
    private String content;

    public PatchNoteRequest(){

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
