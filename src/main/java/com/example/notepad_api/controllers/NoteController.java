package com.example.notepad_api.controllers;


import com.example.notepad_api.NoteManager;
import com.example.notepad_api.exceptions.ApiErrorResponse;
import com.example.notepad_api.models.Note;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<Note>> getAllNotes(){
        return ResponseEntity.ok(noteManager.getAllNotes());
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getNoteById(@PathVariable Long id){
        Optional<Note> note = noteManager.findNoteById(id);

        if (note.isPresent()){
            return ResponseEntity.ok(note.get());
        }
        ApiErrorResponse error = new ApiErrorResponse("Note with ID " + id + " was not found", HttpStatus.NOT_FOUND.value(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @PostMapping
    public ResponseEntity<Note> createNote(@Valid @RequestBody Note note){
        Note savedNote = noteManager.addNote(note);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedNote);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Long id, @Valid @RequestBody Note note){
        Note updatedNote = noteManager.updateNote(id, note.getTitle(), note.getContent());
        if (updatedNote == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedNote);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id){
        boolean deleted = noteManager.deleteNote(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

}
