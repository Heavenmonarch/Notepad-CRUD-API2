package com.example.notepad_api.controllers;


import com.example.notepad_api.dto.CreateNoteRequest;
import com.example.notepad_api.dto.NoteResponse;
import com.example.notepad_api.services.NoteService;
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
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    public ResponseEntity<List<NoteResponse>> getAllNotes(){
        List<NoteResponse> responses = noteService.getAllNotes().stream().map(NoteResponse::fromNote).toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getNoteById(@PathVariable Long id){
        Optional<Note> note = noteService.findNoteById(id);

        if (note.isPresent()){
            NoteResponse response = NoteResponse.fromNote(note.get());
            return ResponseEntity.ok(response);
        }
        ApiErrorResponse error = new ApiErrorResponse("Note with ID " + id + " was not found", HttpStatus.NOT_FOUND.value(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @PostMapping
    public ResponseEntity<NoteResponse> createNote(@Valid @RequestBody CreateNoteRequest request){
        Note note = new Note(request.getTitle(), request.getContent());
        Note savedNote = noteService.addNote(note);
        NoteResponse response = NoteResponse.fromNote(savedNote);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Long id, @Valid @RequestBody CreateNoteRequest request){
        Note updatedNote = noteService.updateNote(id, request.getTitle(), request.getContent());
        if (updatedNote == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedNote);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id){
        boolean deleted = noteService.deleteNote(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

}
