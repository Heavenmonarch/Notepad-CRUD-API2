package com.example.notepad_api.controllers;


import com.example.notepad_api.dto.CreateNoteRequest;
import com.example.notepad_api.dto.NoteResponse;
import com.example.notepad_api.dto.PatchNoteRequest;
import com.example.notepad_api.dto.UpdateNoteRequest;
import com.example.notepad_api.mappers.NoteMapper;
import com.example.notepad_api.services.NoteService;
import com.example.notepad_api.exceptions.ApiErrorResponse;
import com.example.notepad_api.models.Note;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Tag(
        name = "Notes",
        description = "Operations for creating, retrieving, updating and deleting notes"
)
@RestController
@RequestMapping("/api/notes")
public class NoteController
{
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }
    @Operation(
            summary = "Get all notes",
            description = "Get all the notes that have been created"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "notes retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<Page<NoteResponse>> getAllNotes(
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                noteService.getAllNotes(pageable)
                        .map(NoteMapper::toResponse)
        );
    }

    @Operation(
            summary = "Get one note",
            description = "Retrieve one note using the id of the note"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "note retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No note exists with that ID"
            )
    })
    @Parameter(description = "unique ID of the note", example = "1", required = true)
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

    @Operation(
            summary = "Create a note",
            description = "Creates a new note and returns the saved note"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Note created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping
    public ResponseEntity<NoteResponse> createNote(@Valid @RequestBody CreateNoteRequest request){
        Note note = new Note(request.getTitle(), request.getContent());
        Note savedNote = noteService.addNote(note);
        NoteResponse response = NoteMapper.toResponse(savedNote);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

   @Operation(
           summary = "Replace a note",
           description = "updates the title and content of na existing note"
   )
   @ApiResponses({
           @ApiResponse(responseCode = "200", description = "Note updated successfully"),
           @ApiResponse(responseCode = "400", description = "Invalid data request"),
           @ApiResponse(responseCode = "404", description = "Note not found")
   })
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

   @Operation(
           summary = "Partially update a note",
           description = "Updates only the supplied fields of an existing note"
   )
   @ApiResponses({
           @ApiResponse(responseCode = "200", description = "Note updated successfully"),
           @ApiResponse(responseCode = "400", description = "Empty or invalid patch request"),
           @ApiResponse(responseCode = "404", description = "Note not found")
   }
   )
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
    @Operation(
            summary = "Delete a note",
            description = "Permanently deletes a note identified by its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Note deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Note not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id){
        boolean deleted = noteService.deleteNote(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

}
