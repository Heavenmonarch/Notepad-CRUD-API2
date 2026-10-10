package com.example.notepad_api;

import com.example.notepad_api.models.Note;
import com.example.notepad_api.repositories.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteManager{
    private final NoteRepository noteRepository;

    public NoteManager(NoteRepository noteRepository){
        this.noteRepository = noteRepository;
    }

    public Note addNote(Note note){
        return noteRepository.save(note);
    }

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public Optional<Note> findNoteById(Long id){
        return noteRepository.findById(id);
    }

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

    public boolean deleteNote(Long id){
        if (!noteRepository.existsById(id)){
            return false;
        }
        noteRepository.deleteById(id);
        return true;
    }
}