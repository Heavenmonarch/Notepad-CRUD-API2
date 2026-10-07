package com.example.notepad_api.controllers;


import com.example.notepad_api.NoteManager;
import com.example.notepad_api.models.Note;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController
{
    private final NoteManager noteManager;

    public NoteController(NoteManager noteManager) {
        this.noteManager = noteManager;
    }

    @GetMapping
    public List<Note> getAllNotes(){
        return noteManager.getAllNotes();
    }

}
