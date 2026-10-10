package com.example.notepad_api.controllers;


import com.example.notepad_api.NoteManager;
import com.example.notepad_api.models.Note;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("{id}")
    public Optional<Note> getNoteById(@PathVariable Long id) {
        return noteManager.findNoteById(id);
    }

    @PostMapping
    public Note createNote(@RequestBody Note note) {
        noteManager.addNote(note);
        return note;
    }

    @PutMapping("/{id}")
    public Note updateNote(@RequestBody Note note, @PathVariable Long id) {
        return noteManager.updateNote(id, note.getTitle(), note.getContent());
    }

    @DeleteMapping("/{id}")
    public boolean deleteNote(@PathVariable Long id) {
        return noteManager.deleteNote(id);
    }

}
