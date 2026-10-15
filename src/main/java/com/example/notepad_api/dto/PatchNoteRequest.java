package com.example.notepad_api.dto;

import jakarta.validation.constraints.Size;

public class PatchNoteRequest {
    @Size(max = 100, message="Title cannot exceed 100 characters")
    private String title;

    @Size(max = 20000, message = "Content cannot exceed 20000 characters")
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
