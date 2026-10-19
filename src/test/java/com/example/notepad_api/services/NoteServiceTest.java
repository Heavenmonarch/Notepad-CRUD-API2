package com.example.notepad_api.services;


import com.example.notepad_api.repositories.NoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest{
    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteService noteService;

    @Test
    void deleteNoteReturnsFalseWhenNoteDoesNotExist() {
        when(noteRepository.existsById(10L)).thenReturn(false);

        boolean result = noteService.deleteNote(10L);

        assertFalse(result);

        verify(noteRepository, never()).deleteById(10L);
    }
}
