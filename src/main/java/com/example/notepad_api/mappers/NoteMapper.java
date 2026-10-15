package com.example.notepad_api.mappers;

import com.example.notepad_api.dto.NoteResponse;
import com.example.notepad_api.models.Note;

public class NoteMapper {
    private NoteMapper() {

    }
    public static NoteResponse toResponse(Note note){
        return new NoteResponse(
                note.getId(),
                note.getTitle(),
                note.getContent()
        );
    }
}
