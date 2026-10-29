package com.example.notepad_api.dto;


import io.swagger.v3.oas.annotations.media.Schema;

//import com.example.notepad_api.models.Note;
@Schema(description = "A note returned by the API")
public class NoteResponse {
    @Schema(description = "Unique database ID", example = "1")
    private Long id;

    @Schema(description = "Title of the note", example = "My first note")
    private String title;

    @Schema(
            description = "Content of the note",
            example = "I am learning Spring Boot"
    )
    private String content;
    public NoteResponse(Long id, String title, String content){
        this.id = id;
        this.title = title;
        this.content = content;
    }

//    public static NoteResponse fromNote(Note note){
//        return new NoteResponse(note.getId(), note.getTitle(), note.getContent());
//    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
