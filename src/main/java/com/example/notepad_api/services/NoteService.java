package com.example.notepad_api.services;

import com.example.notepad_api.models.Note;
import com.example.notepad_api.repositories.NoteRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {
    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository){
        this.noteRepository = noteRepository;
    }

    public Note addNote(Note note){
        return noteRepository.save(note);
    }

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public Page<Note> getAllNotes(Pageable pageable){
        return noteRepository.findAll(pageable);
    }

    public Optional<Note> findNoteById(Long id){
        return noteRepository.findById(id);
    }

    @Transactional
    public Note updateNote(Long id, String title, String content){
        Optional<Note> existingNote = noteRepository.findById(id);

        if(existingNote.isEmpty()){
            return null;
        }

        Note note = existingNote.get();
        note.setTitle(title);
        note.setContent(content);

        return noteRepository.save(note);
    }

    @Transactional
    public Note patchNote(Long id, String title, String content){
        Optional<Note> existingNote = noteRepository.findById(id);

        if (existingNote.isEmpty()){
            return null;
        }

        Note note = existingNote.get();

        if (title != null){
            note.setTitle(title);
        }

        if (content != null){
            note.setContent(content);
        }

        return noteRepository.save(note);
    }

    public boolean deleteNote(Long id){
        if (!noteRepository.existsById(id)){
            return false;
        }
        noteRepository.deleteById(id);
        return true;
    }
}