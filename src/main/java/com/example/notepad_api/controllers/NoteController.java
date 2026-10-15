package com.example.notepad_api.controllers;


import com.example.notepad_api.dto.CreateNoteRequest;
import com.example.notepad_api.dto.NoteResponse;
import com.example.notepad_api.dto.PatchNoteRequest;
import com.example.notepad_api.dto.UpdateNoteRequest;
import com.example.notepad_api.mappers.NoteMapper;
import com.example.notepad_api.services.NoteService;
import com.example.notepad_api.exceptions.ApiErrorResponse;
import com.example.notepad_api.models.Note;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<Page<NoteResponse>> getAllNotes(
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                noteService.getAllNotes(pageable)
                        .map(NoteMapper::toResponse)
        );
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getNoteById(@PathVariable Long id){
        Optional<Note> note = noteService.findNoteById(id);

        if (note.isPresent()){
            NoteResponse response = NoteMapper.toResponse(note.get());
            return ResponseEntity.ok(response);
        }
        ApiErrorResponse error = new ApiErrorResponse("Note with ID " + id + " was not found", HttpStatus.NOT_FOUND.value(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @PostMapping
    public ResponseEntity<NoteResponse> createNote(@Valid @RequestBody CreateNoteRequest request){
        Note note = new Note(request.getTitle(), request.getContent());
        Note savedNote = noteService.addNote(note);
        NoteResponse response = NoteMapper.toResponse(savedNote);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

   @PutMapping("/{id}")
   public ResponseEntity<?> updateNote(@PathVariable Long id, @Valid @RequestBody UpdateNoteRequest request){
        Note updatedNote = noteService.updateNote(id, request.getTitle(), request.getContent());
        if (updatedNote == null) {
           ApiErrorResponse error = new ApiErrorResponse(
                   "Note with ID " + id + " was not found",
                   HttpStatus.NOT_FOUND.value(),
                   null
           );

           return ResponseEntity
                   .status(HttpStatus.NOT_FOUND)
                   .body(error);
        }

       NoteResponse response = NoteMapper.toResponse(updatedNote);

       return ResponseEntity.ok(response);
   }


    @PatchMapping("/{id}")
    public ResponseEntity<?> patchNote(
            @PathVariable Long id,
            @Valid @RequestBody PatchNoteRequest request
    ) {

        if (request.getTitle() == null && request.getContent() == null) {
            ApiErrorResponse error = new ApiErrorResponse(
                    "At least one field must be provided",
                    HttpStatus.BAD_REQUEST.value(),
                    null
            );

            return ResponseEntity
                    .badRequest()
                    .body(error);
        }

        if (request.getTitle() != null && request.getTitle().isBlank()) {
            ApiErrorResponse error = new ApiErrorResponse(
                    "Title cannot be blank",
                    HttpStatus.BAD_REQUEST.value(),
                    null
            );

            return ResponseEntity
                    .badRequest()
                    .body(error);
        }

        if (request.getContent() != null && request.getContent().isBlank()) {
            ApiErrorResponse error = new ApiErrorResponse(
                    "Content cannot be blank",
                    HttpStatus.BAD_REQUEST.value(),
                    null
            );

            return ResponseEntity
                    .badRequest()
                    .body(error);
        }

        Note updatedNote = noteService.patchNote(
                id,
                request.getTitle(),
                request.getContent()
        );

        if (updatedNote == null) {
            ApiErrorResponse error = new ApiErrorResponse(
                    "Note with ID " + id + " was not found",
                    HttpStatus.NOT_FOUND.value(),
                    null
            );

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(error);
        }

        return ResponseEntity.ok(NoteMapper.toResponse(updatedNote));
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
